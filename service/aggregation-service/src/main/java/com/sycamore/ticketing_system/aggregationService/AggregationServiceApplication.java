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

package com.sycamore.ticketing_system.aggregationService;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *  聚合服务应用启动器
 *
 *
 */
//@EnableDynamicThreadPool
@SpringBootApplication(scanBasePackages = {
        "com.sycamore.ticketing_system.userService",
        "com.sycamore.ticketing_system.ticketService"
})
//@EnableRetry
@MapperScan(value = {
        "com.sycamore.ticketing_system.userService.dao.mapper",
        "com.sycamore.ticketing_system.ticketservice.dao.mapper"
})
//@EnableFeignClients(value = {
//        "org.opengoofy.index12306.biz.ticketservice.remote",
//        "org.opengoofy.index12306.biz.orderservice.remote"
//})
//@EnableCrane4j(enumPackages = "org.opengoofy.index12306.biz.orderservice.common.enums")
public class AggregationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AggregationServiceApplication.class, args);
    }
}
