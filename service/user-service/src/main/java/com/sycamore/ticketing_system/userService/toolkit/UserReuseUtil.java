package com.sycamore.ticketing_system.userService.toolkit;

import static com.sycamore.ticketing_system.userService.common.constants.SystemConstant.USER_REGISTER_REUSE_SHARDING_COUNT;

/**
 * 用户名可复用工具类
 *
 *
 */
public final class UserReuseUtil {

    /**
     * 计算分片位置
     */
    public static int hashShardingIdx(String username) {
        return Math.abs(username.hashCode() % USER_REGISTER_REUSE_SHARDING_COUNT);
    }
}
