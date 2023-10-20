package com.sycamore.ticketing_system.userService.service;

import com.sycamore.ticketing_system.convention.result.Result;
import com.sycamore.ticketing_system.userService.dto.req.UserDeletionReqDTO;
import com.sycamore.ticketing_system.userService.dto.req.UserLoginReqDTO;
import com.sycamore.ticketing_system.userService.dto.req.UserRegisterReqDTO;
import com.sycamore.ticketing_system.userService.dto.resp.UserLoginRespDTO;
import com.sycamore.ticketing_system.userService.dto.resp.UserRegisterRespDTO;

/**
 * HIS IS A INTERFACE
 *
 * @PROJECT_NAME: ticketing_system
 * @INTERFACE_NAME: UserLoginService
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/19 14:05
 */
public interface UserLoginService {
    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    UserLoginRespDTO checkLogin(String accessToken);

    void logout(String accessToken);

    UserRegisterRespDTO register(UserRegisterReqDTO requestParam);

    boolean hasUsername(String username);

    void deletion(UserDeletionReqDTO requestParam);
}
