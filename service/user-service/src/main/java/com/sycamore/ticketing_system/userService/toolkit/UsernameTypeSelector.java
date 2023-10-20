package com.sycamore.ticketing_system.userService.toolkit;

import com.sycamore.ticketing_system.userService.common.enums.UsernameTypeEnum;

/**
 * THIS IS A CLASS
 *
 * @PROJECT_NAME: ticketing_system
 * @CLASS_NAME: UsernameTypeSelector
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/19 14:10
 */
public class UsernameTypeSelector {
    public static UsernameTypeEnum selectUsernameType(String username) {
        return UsernameTypeEnum.USERNAME;
    }
}
