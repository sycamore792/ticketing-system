/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sycamore.ticketing_system.base.container;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * THIS IS A CLASS
 *
 * @PROJECT_NAME: ticketing_system
 * @CLASS_NAME: ApplicationContextHolder
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/13 11:49
 */
public class ApplicationContextHolder implements ApplicationContextAware {
    private static ApplicationContext CONTEXT;

    /**
     * 初始化context容器
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.CONTEXT = applicationContext;
    }

    /**
     * 从IOC容器中获取bean byName || byType
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> T getBean(Class<T> clazz){
        return CONTEXT.getBean(clazz);
    }

    /**
     * 获取容器中的类集合
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T>Map<String,T> getBeans(Class<T> clazz){
        return CONTEXT.getBeansOfType(clazz);
    }


    /**
     *
     * @param beanName
     * @param annotationType
     * @return
     * @param <A>
     */
    public static <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) {
        return CONTEXT.findAnnotationOnBean(beanName, annotationType);
    }

    /**
     * 获取ioc容器
     * @return
     */
    public static ApplicationContext getInstance() {
        return CONTEXT;
    }
}
