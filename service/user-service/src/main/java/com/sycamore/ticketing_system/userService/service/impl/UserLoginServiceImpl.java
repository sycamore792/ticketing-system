package com.sycamore.ticketing_system.userService.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sycamore.ticketing_system.cache.service.DistributedCache;
import com.sycamore.ticketing_system.common.toolkit.BeanUtil;
import com.sycamore.ticketing_system.convention.exception.ClientException;
import com.sycamore.ticketing_system.convention.exception.ServiceException;
import com.sycamore.ticketing_system.design_pattern.chain.AbstractChainContext;
import com.sycamore.ticketing_system.user.core.UserContext;
import com.sycamore.ticketing_system.user.core.UserInfoDTO;
import com.sycamore.ticketing_system.user.toolkit.JWTUtil;
import com.sycamore.ticketing_system.userService.common.enums.UserChainMarkEnum;
import com.sycamore.ticketing_system.userService.dao.entity.*;
import com.sycamore.ticketing_system.userService.dao.mapper.*;
import com.sycamore.ticketing_system.userService.dto.req.UserDeletionReqDTO;
import com.sycamore.ticketing_system.userService.dto.req.UserLoginReqDTO;
import com.sycamore.ticketing_system.userService.dto.req.UserRegisterReqDTO;
import com.sycamore.ticketing_system.userService.dto.resp.UserLoginRespDTO;
import com.sycamore.ticketing_system.userService.dto.resp.UserQueryRespDTO;
import com.sycamore.ticketing_system.userService.dto.resp.UserRegisterRespDTO;
import com.sycamore.ticketing_system.userService.service.UserLoginService;
import com.sycamore.ticketing_system.userService.service.UserService;
import com.sycamore.ticketing_system.userService.toolkit.UsernameTypeSelector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.sycamore.ticketing_system.userService.common.constants.RedisKeyConstant.*;
import static com.sycamore.ticketing_system.userService.common.enums.UserRegisterErrorCodeEnum.*;
import static com.sycamore.ticketing_system.userService.toolkit.UserReuseUtil.hashShardingIdx;

/**
 * THIS IS A CLASS
 *
 * @PROJECT_NAME: ticketing_system
 * @CLASS_NAME: UserLoginServiceImpl
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/19 14:06
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {
    private final UserMailMapper userMailMapper;
    private final UserPhoneMapper userPhoneMapper;
    private final UserMapper userMapper;
    private final UserReuseMapper userReuseMapper;
    private final UserDeletionMapper userDeletionMapper;
    private final DistributedCache distributedCache;
    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final AbstractChainContext abstractChainContext;
    private final RedissonClient redissonClient;
    private final UserService userService;
    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        String usernameOrMailOrPhone = requestParam.getUsernameOrMailOrPhone();
        String username = null;
        switch (UsernameTypeSelector.selectUsernameType(usernameOrMailOrPhone)) {
            case EMAIL:
                LambdaQueryWrapper<UserMailDO> mailQueryWrapper = Wrappers.lambdaQuery(UserMailDO.class)
                        .eq(UserMailDO::getMail, usernameOrMailOrPhone);
                username = Optional.ofNullable(userMailMapper.selectOne(mailQueryWrapper))
                        .map(UserMailDO::getUsername)
                        .orElseThrow(() -> new ClientException("用户名/手机号/邮箱不存在"));
                break;
            case PHONE:
                LambdaQueryWrapper<UserPhoneDO> queryWrapper = Wrappers.lambdaQuery(UserPhoneDO.class)
                        .eq(UserPhoneDO::getPhone, usernameOrMailOrPhone);
                username = Optional.ofNullable(userPhoneMapper.selectOne(queryWrapper))
                        .map(UserPhoneDO::getUsername)
                        .orElse(null);
                break;
            case USERNAME:
                break;
        }
        username = Optional.ofNullable(username).orElse(requestParam.getUsernameOrMailOrPhone());
        //核对用户名与密码
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username)
                .eq(UserDO::getPassword, requestParam.getPassword())
                .select(UserDO::getId, UserDO::getUsername, UserDO::getRealName);
        UserDO userDO = userMapper.selectOne(queryWrapper);
        if (userDO != null) {
            UserInfoDTO userInfo = UserInfoDTO.builder()
                    .userId(String.valueOf(userDO.getId()))
                    .username(userDO.getUsername())
                    .realName(userDO.getRealName())
                    .build();
            String accessToken = JWTUtil.generateToken(userInfo);
            UserLoginRespDTO actual = new UserLoginRespDTO(userInfo.getUserId(), requestParam.getUsernameOrMailOrPhone(), userDO.getRealName(), accessToken);
            distributedCache.put(accessToken, JSON.toJSONString(actual), 30, TimeUnit.MINUTES);
            return actual;
        }
        throw new ServiceException("账号不存在或密码错误");

    }

    @Override
    public UserLoginRespDTO checkLogin(String accessToken) {
        return distributedCache.get(accessToken, UserLoginRespDTO.class);
    }

    @Override
    public void logout(String accessToken) {
        distributedCache.delete(accessToken);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserRegisterRespDTO register(UserRegisterReqDTO requestParam) {
        abstractChainContext.handler(UserChainMarkEnum.USER_REGISTER_FILTER.name(), requestParam);
        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER + requestParam.getUsername());
        boolean tryLock = lock.tryLock();
        if (!tryLock) {
            throw new ServiceException(HAS_USERNAME_NOTNULL);
        }
        try {
            //尝试插入User表
           try {
               int inserted = userMapper.insert(BeanUtil.convert(requestParam, UserDO.class));
               if (inserted < 1) {
                   throw new ServiceException(USER_REGISTER_FAIL);
               }
           }catch (DuplicateKeyException dke){
               log.error("用户名 [{}] 重复注册", requestParam.getUsername());
               throw new ServiceException(HAS_USERNAME_NOTNULL);
           }
           //插入成功，尝试插入Phone表
            UserPhoneDO userPhoneDO = UserPhoneDO.builder()
                    .phone(requestParam.getPhone())
                    .username(requestParam.getUsername())
                    .build();
            try {
                userPhoneMapper.insert(userPhoneDO);
            } catch (DuplicateKeyException dke) {
                log.error("用户 [{}] 注册手机号 [{}] 重复", requestParam.getUsername(), requestParam.getPhone());
                throw new ServiceException(PHONE_REGISTERED);
            }
            //插入成功，尝试插入Mail表(可选)
            if (StrUtil.isNotBlank(requestParam.getMail())) {
                UserMailDO userMailDO = UserMailDO.builder()
                        .mail(requestParam.getMail())
                        .username(requestParam.getUsername())
                        .build();
                try {
                    userMailMapper.insert(userMailDO);
                } catch (DuplicateKeyException dke) {
                    log.error("用户 [{}] 注册邮箱 [{}] 重复", requestParam.getUsername(), requestParam.getMail());
                    throw new ServiceException(MAIL_REGISTERED);
                }
            }
            String username = requestParam.getUsername();
            //删除可复用username
            userReuseMapper.delete(Wrappers.update(new UserReuseDO(username)));
            StringRedisTemplate instance = (StringRedisTemplate) distributedCache.getInstance();
            instance.opsForSet().remove(USER_REGISTER_REUSE_SHARDING + hashShardingIdx(username), username);
            userRegisterCachePenetrationBloomFilter.add(username);
        }finally {
            lock.unlock();
        }
        return BeanUtil.convert(requestParam, UserRegisterRespDTO.class);
    }

    @Override
    public boolean hasUsername(String username) {
        boolean hasUsername = userRegisterCachePenetrationBloomFilter.contains(username);
        if (!hasUsername) return false;
        StringRedisTemplate instance = (StringRedisTemplate) distributedCache.getInstance();
        return instance.opsForSet().isMember(USER_REGISTER_REUSE_SHARDING + hashShardingIdx(username), username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletion(UserDeletionReqDTO requestParam) {
        String username = UserContext.getUsername();
        if (!Objects.equals(username, requestParam.getUsername())) {
            //注销账号与登录账号不一致
            //风控
            throw new ClientException("注销账号与登录账号不一致");
        }
        RLock lock = redissonClient.getLock(USER_DELETION + requestParam.getUsername());
        lock.lock();
        try {
            //获取用户信息
            UserQueryRespDTO userQueryRespDTO = userService.queryUserByUsername(username);
            UserDeletionDO userDeletionDO = UserDeletionDO.builder()
                    .idType(userQueryRespDTO.getIdType())
                    .idCard(userQueryRespDTO.getIdCard())
                    .build();
            //记录删除username
            userDeletionMapper.insert(userDeletionDO);
            //尝试删除User表
            UserDO userDO = new UserDO();
            userDO.setDeletionTime(System.currentTimeMillis());
            userDO.setUsername(username);
            // MyBatis Plus 不支持修改语句变更 del_flag 字段
            userMapper.deletionUser(userDO);
            //尝试删除Phone表
            UserPhoneDO userPhoneDO = UserPhoneDO.builder()
                    .phone(userQueryRespDTO.getPhone())
                    .deletionTime(System.currentTimeMillis())
                    .build();
            userPhoneMapper.deletionUser(userPhoneDO);
            //尝试删除Mail表(可选)
            if (StrUtil.isNotBlank(userQueryRespDTO.getMail())) {
                UserMailDO userMailDO = UserMailDO.builder()
                        .mail(userQueryRespDTO.getMail())
                        .deletionTime(System.currentTimeMillis())
                        .build();
                userMailMapper.deletionUser(userMailDO);
            }
            //删除token
            distributedCache.delete(UserContext.getToken());

            //添加可复用username
            userReuseMapper.insert(new UserReuseDO(username));
            StringRedisTemplate instance = (StringRedisTemplate) distributedCache.getInstance();
            instance.opsForSet().add(USER_REGISTER_REUSE_SHARDING + hashShardingIdx(username), username);
        }finally {
            lock.unlock();
        }
    }
}
