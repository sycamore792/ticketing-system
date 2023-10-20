package com.sycamore.ticketing_system.userService.service;

import com.sycamore.ticketing_system.userService.dto.req.UserUpdateReqDTO;
import com.sycamore.ticketing_system.userService.dto.resp.UserQueryActualRespDTO;
import com.sycamore.ticketing_system.userService.dto.resp.UserQueryRespDTO;

/**
 * HIS IS A INTERFACE
 *
 * @PROJECT_NAME: ticketing_system
 * @INTERFACE_NAME: UserService
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/20 1:04
 */
public interface UserService {
    UserQueryRespDTO queryUserByUsername(String username);

    Integer queryUserDeletionNum(Integer idType, String idCard);

    UserQueryActualRespDTO queryActualUserByUsername(String username);

    void update(UserUpdateReqDTO requestParam);
}
