package com.sycamore.ticketing_system.userService.dto.req;

import lombok.Data;

/**
 * 乘车人添加&修改请求参数
 *
 *
 */
@Data
public class PassengerReqDTO {

    /**
     * 乘车人id
     */
    private String id;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 证件类型
     */
    private Integer idType;

    /**
     * 证件号码
     */
    private String idCard;

    /**
     * 优惠类型
     */
    private Integer discountType;

    /**
     * 手机号
     */
    private String phone;
}
