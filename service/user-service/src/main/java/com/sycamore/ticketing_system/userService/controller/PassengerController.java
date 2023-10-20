package com.sycamore.ticketing_system.userService.controller;

/**
 * THIS IS A CLASS
 *
 * @PROJECT_NAME: ticketing_system
 * @CLASS_NAME: PassengerController
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/20 10:19
 */

import com.sycamore.ticketing_system.convention.result.Result;
import com.sycamore.ticketing_system.idempotent.annotation.Idempotent;
import com.sycamore.ticketing_system.idempotent.enums.IdempotentSceneEnum;
import com.sycamore.ticketing_system.idempotent.enums.IdempotentTypeEnum;
import com.sycamore.ticketing_system.user.core.UserContext;
import com.sycamore.ticketing_system.userService.dto.req.PassengerRemoveReqDTO;
import com.sycamore.ticketing_system.userService.dto.req.PassengerReqDTO;
import com.sycamore.ticketing_system.userService.dto.resp.PassengerActualRespDTO;
import com.sycamore.ticketing_system.userService.dto.resp.PassengerRespDTO;
import com.sycamore.ticketing_system.userService.service.PassengerService;
import com.sycamore.ticketing_system.web.handler.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 乘车人控制层
 *
 *
 */
@RestController
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;

    /**
     * 新增乘车人
     */
    @Idempotent(
            uniqueKeyPrefix = "ticketing_system-user:lock_passenger-alter:",
            key = "T(com.sycamore.ticketing_system.user.core.UserContext).getUsername()",
            type = IdempotentTypeEnum.SPEL,
            scene = IdempotentSceneEnum.RESTAPI,
            message = "正在新增乘车人，请稍后再试..."
    )
    @PostMapping("/api/user-service/passenger/save")
    public Result<Void> savePassenger(@RequestBody PassengerReqDTO requestParam) {
        passengerService.savePassenger(requestParam);
        return Results.success();
    }

    /**
     * 移除乘车人
     */
    @Idempotent(
            uniqueKeyPrefix = "ticketing_system-user:lock_passenger-alter:",
            key = "T(com.sycamore.ticketing_system.user.core.UserContext).getUsername()",
            type = IdempotentTypeEnum.SPEL,
            scene = IdempotentSceneEnum.RESTAPI,
            message = "正在移除乘车人，请稍后再试..."
    )
    @PostMapping("/api/user-service/passenger/remove")
    public Result<Void> removePassenger(@RequestBody PassengerRemoveReqDTO requestParam) {
        passengerService.removePassenger(requestParam);
        return Results.success();
    }

    /**
     * 修改乘车人
     */
    @Idempotent(
            uniqueKeyPrefix = "ticketing_system-user:lock_passenger-alter:",
            key = "T(com.sycamore.ticketing_system.user.core.UserContext).getUsername()",
            type = IdempotentTypeEnum.SPEL,
            scene = IdempotentSceneEnum.RESTAPI,
            message = "正在修改乘车人，请稍后再试..."
    )
    @PostMapping("/api/user-service/passenger/update")
    public Result<Void> updatePassenger(@RequestBody PassengerReqDTO requestParam) {
        passengerService.updatePassenger(requestParam);
        return Results.success();
    }

    /**
     * 根据用户名查询乘车人列表
     */
//    @Idempotent(
//            uniqueKeyPrefix = "ticketing_system-user:lock_passenger-alter:",
//            key = "T(com.sycamore.ticketing_system.user.core.UserContext).getUsername()",
//            type = IdempotentTypeEnum.SPEL,
//            scene = IdempotentSceneEnum.RESTAPI,
//            message = "查询过于频繁，请稍后再试..."
//    )
    @GetMapping("/api/user-service/passenger/query")
    public Result<List<PassengerRespDTO>> listPassengerQueryByUsername() {
        return Results.success(passengerService.listPassengerQueryByUsername(UserContext.getUsername()));
    }

    /**
     * 根据乘车人 ID 集合查询乘车人列表
     */
    @GetMapping("/api/user-service/inner/passenger/actual/query/ids")
    public Result<List<PassengerActualRespDTO>> listPassengerQueryByIds(@RequestParam("username") String username, @RequestParam("ids") List<Long> ids) {
        return Results.success(passengerService.listPassengerQueryByIds(username, ids));
    }

}
