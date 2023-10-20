package com.sycamore.ticketing_system.convention.error_code;

/**
 * HIS IS A INTERFACE
 *
 * @PROJECT_NAME: ticketing_system
 * @INTERFACE_NAME: IError  错误码抽象
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/13 12:26
 */

public interface IError {
    /**
     * 错误码
     */
    String code();

    /**
     * 错误信息
     */
    String message();
}
