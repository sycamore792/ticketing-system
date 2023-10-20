package com.sycamore.ticketing_system.convention.page;

import lombok.Data;

/**
 * THIS IS A CLASS
 *
 * @PROJECT_NAME: ticketing_system
 * @CLASS_NAME: PageReq
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/13 12:29
 */
@Data
public class PageReq {
    /**
     * 当前页
     */
    private Long current = 1L;

    /**
     * 每页显示条数
     */
    private Long size = 10L;
}
