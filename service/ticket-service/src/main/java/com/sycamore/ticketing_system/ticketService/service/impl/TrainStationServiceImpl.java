package com.sycamore.ticketing_system.ticketService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sycamore.ticketing_system.common.toolkit.BeanUtil;
import com.sycamore.ticketing_system.ticketService.dao.entity.TrainStationDO;
import com.sycamore.ticketing_system.ticketService.dao.mapper.TrainStationMapper;
import com.sycamore.ticketing_system.ticketService.dto.resp.TrainStationQueryRespDTO;
import com.sycamore.ticketing_system.ticketService.service.TrainStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * THIS IS A CLASS
 *
 * @PROJECT_NAME: ticketing_system
 * @CLASS_NAME: TrainStationServiceImpl
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/20 22:08
 */
@RequiredArgsConstructor
@Service
public class TrainStationServiceImpl implements TrainStationService {
    private final TrainStationMapper trainStationMapper;

    @Override
    public List<TrainStationQueryRespDTO> listTrainStationQuery(String trainId) {
        LambdaQueryWrapper<TrainStationDO> queryWrapper = Wrappers.lambdaQuery(TrainStationDO.class)
                .eq(TrainStationDO::getTrainId, trainId);
        List<TrainStationDO> trainStationDOList = trainStationMapper.selectList(queryWrapper);
        return BeanUtil.convert(trainStationDOList, TrainStationQueryRespDTO.class);
    }
}

