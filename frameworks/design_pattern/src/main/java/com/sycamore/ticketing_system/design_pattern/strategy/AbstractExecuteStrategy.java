package com.sycamore.ticketing_system.design_pattern.strategy;

/**
 * HIS IS A INTERFACE
 *
 * @PROJECT_NAME: ticketing_system
 * @INTERFACE_NAME: AbstractExecuteStrategy
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/13 15:14
 */

/**
 * 策略执行抽象
 */
public interface AbstractExecuteStrategy <req,resp> {
    /**
     * 执行策略标识
     */
    default String mark() {
        return null;
    }

    /**
     * 执行策略范匹配标识
     */
    default String patternMatchMark() {
        return null;
    }

    /**
     * 执行策略
     *
     * @param requestParam 执行策略入参
     */
    default void execute(req requestParam) {
    }
    /**
     * 执行策略，带返回值
     *
     * @param requestParam 执行策略入参
     * @return 执行策略后返回值
     */
    default resp executeResp(req requestParam) {
        return null;
    }
}
