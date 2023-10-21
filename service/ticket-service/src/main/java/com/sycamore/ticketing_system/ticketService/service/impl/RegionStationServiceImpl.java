package com.sycamore.ticketing_system.ticketService.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sycamore.ticketing_system.cache.core.CacheLoader;
import com.sycamore.ticketing_system.cache.service.DistributedCache;
import com.sycamore.ticketing_system.cache.toolkit.CacheUtil;
import com.sycamore.ticketing_system.common.enums.FlagEnum;
import com.sycamore.ticketing_system.common.toolkit.BeanUtil;
import com.sycamore.ticketing_system.convention.exception.ClientException;
import com.sycamore.ticketing_system.ticketService.common.enums.RegionStationQueryTypeEnum;
import com.sycamore.ticketing_system.ticketService.dao.entity.RegionDO;
import com.sycamore.ticketing_system.ticketService.dao.entity.StationDO;
import com.sycamore.ticketing_system.ticketService.dao.mapper.RegionMapper;
import com.sycamore.ticketing_system.ticketService.dao.mapper.StationMapper;
import com.sycamore.ticketing_system.ticketService.dto.req.RegionStationQueryReqDTO;
import com.sycamore.ticketing_system.ticketService.dto.resp.RegionStationQueryRespDTO;
import com.sycamore.ticketing_system.ticketService.dto.resp.StationQueryRespDTO;
import com.sycamore.ticketing_system.ticketService.service.RegionStationService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.sycamore.ticketing_system.ticketService.common.enums.RedisKeyConstant.*;
import static com.sycamore.ticketing_system.ticketService.common.enums.SystemConstant.ADVANCE_TICKET_DAY;

/**
 * THIS IS A CLASS
 *
 * @PROJECT_NAME: ticketing_system
 * @CLASS_NAME: RegionStationServiceImpl
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/20 20:46
 */
@Service
@RequiredArgsConstructor
public class RegionStationServiceImpl implements RegionStationService {
    private final RegionMapper regionMapper;
    private final RedissonClient redissonClient;
    private final StationMapper stationMapper;
    private final DistributedCache distributedCache;

    @Override
    public List<StationQueryRespDTO> listAllStation() {
        return distributedCache.safeGet(
                STATION_ALL,
                List.class,
                () -> BeanUtil.convert(stationMapper.selectList(Wrappers.emptyWrapper()), StationQueryRespDTO.class),
                ADVANCE_TICKET_DAY,
                TimeUnit.DAYS
        );
    }

    @Override
    public List<RegionStationQueryRespDTO> listRegionStation(RegionStationQueryReqDTO requestParam) {
        String key;
        if (StrUtil.isNotBlank(requestParam.getName())) {
            key = REGION_STATION + requestParam.getName();
            return safeGetRegionStation(
                    key,
                    () -> {
                        LambdaQueryWrapper<StationDO> queryWrapper = Wrappers.lambdaQuery(StationDO.class)
                                .likeRight(StationDO::getName, requestParam.getName())
                                .or()
                                .likeRight(StationDO::getSpell, requestParam.getName());
                        List<StationDO> stationDOList = stationMapper.selectList(queryWrapper);
                        return JSON.toJSONString(BeanUtil.convert(stationDOList, RegionStationQueryRespDTO.class));
                    },
                    requestParam.getName()
            );
        }
        key = REGION_STATION + requestParam.getQueryType();
        LambdaQueryWrapper<RegionDO> queryWrapper = switch (requestParam.getQueryType()) {
            case 0 -> Wrappers.lambdaQuery(RegionDO.class)
                    .eq(RegionDO::getPopularFlag, FlagEnum.TRUE.code());
            case 1 -> Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.A_E.getSpells());
            case 2 -> Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.F_J.getSpells());
            case 3 -> Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.K_O.getSpells());
            case 4 -> Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.P_T.getSpells());
            case 5 -> Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.U_Z.getSpells());
            default -> throw new ClientException("查询失败，请检查查询参数是否正确");
        };
        return safeGetRegionStation(
                key,
                () -> {
                    List<RegionDO> regionDOList = regionMapper.selectList(queryWrapper);
                    return JSON.toJSONString(BeanUtil.convert(regionDOList, RegionStationQueryRespDTO.class));
                },
                String.valueOf(requestParam.getQueryType())
        );
    }
    private List<RegionStationQueryRespDTO> safeGetRegionStation(final String key, CacheLoader<String> loader, String param) {
        List<RegionStationQueryRespDTO> result;
        if (CollUtil.isNotEmpty(result = JSON.parseArray(distributedCache.get(key, String.class), RegionStationQueryRespDTO.class))) {
            return result;
        }
        String lockKey = String.format(LOCK_QUERY_REGION_STATION_LIST, param);

        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        try {
            // 双重判定锁，减轻获得分布式锁后线程访问数据库压力
            if (CollUtil.isEmpty(result = JSON.parseArray(distributedCache.get(key, String.class), RegionStationQueryRespDTO.class))) {
                // 如果访问 cacheLoader 加载数据为空，执行后置函数操作
                if (CollUtil.isEmpty(result = loadAndSet(key, loader))) {
                    return Collections.emptyList();
                }
            }
        } finally {
            lock.unlock();
        }
        return result;
    }

    private List<RegionStationQueryRespDTO> loadAndSet(final String key, CacheLoader<String> loader) {
        String result = loader.load();
        if (CacheUtil.isNullOrBlank(result)) {
            return Collections.emptyList();
        }
        List<RegionStationQueryRespDTO> respDTOList = JSON.parseArray(result, RegionStationQueryRespDTO.class);
        distributedCache.put(
                key,
                result,
                ADVANCE_TICKET_DAY,
                TimeUnit.DAYS
        );
        return respDTOList;
    }
}
