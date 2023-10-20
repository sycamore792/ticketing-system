package com.sycamore.ticketing_system.design_pattern.chain;

import org.springframework.core.Ordered;

/**
 * THIS IS A CLASS
 *
 * @PROJECT_NAME: ticketing_system
 * @CLASS_NAME: AbstractChainHandler
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/13 14:47
 */
public interface AbstractChainHandler<T> extends Ordered {
    /**
     * 执行责任链逻辑
     *
     * @param requestParam 责任链执行入参
     */
    void handler(T requestParam);



    /**
     * @return 责任链组件标识
     */
    String mark();
}
