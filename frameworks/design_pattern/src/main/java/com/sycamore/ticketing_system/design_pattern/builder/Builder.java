package com.sycamore.ticketing_system.design_pattern.builder;

import java.io.Serializable;

public interface Builder<T> extends Serializable {

    /**
     * 构建方法
     *
     * @return 构建后的对象
     */
    T build();
}