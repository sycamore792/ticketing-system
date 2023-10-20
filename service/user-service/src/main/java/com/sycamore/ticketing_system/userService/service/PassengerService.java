package com.sycamore.ticketing_system.userService.service;

import com.sycamore.ticketing_system.userService.dto.req.PassengerRemoveReqDTO;
import com.sycamore.ticketing_system.userService.dto.req.PassengerReqDTO;
import com.sycamore.ticketing_system.userService.dto.resp.PassengerActualRespDTO;
import com.sycamore.ticketing_system.userService.dto.resp.PassengerRespDTO;

import java.util.List;

/**
 * HIS IS A INTERFACE
 *
 * @PROJECT_NAME: ticketing_system
 * @INTERFACE_NAME: PassengerService
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/20 10:19
 */
public interface PassengerService {
    void savePassenger(PassengerReqDTO requestParam);

    void removePassenger(PassengerRemoveReqDTO requestParam);

    void updatePassenger(PassengerReqDTO requestParam);

    List<PassengerRespDTO> listPassengerQueryByUsername(String username);

    List<PassengerActualRespDTO> listPassengerQueryByIds(String username, List<Long> ids);
}
