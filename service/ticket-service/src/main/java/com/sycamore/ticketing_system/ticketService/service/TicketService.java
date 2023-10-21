package com.sycamore.ticketing_system.ticketService.service;

import com.sycamore.ticketing_system.ticketService.dto.req.TicketPageQueryReqDTO;
import com.sycamore.ticketing_system.ticketService.dto.resp.TicketPageQueryRespDTO;

/**
 * HIS IS A INTERFACE
 *
 * @PROJECT_NAME: ticketing_system
 * @INTERFACE_NAME: TicketService
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/20 22:11
 */
public interface TicketService {
    TicketPageQueryRespDTO pageListTicketQueryV1(TicketPageQueryReqDTO requestParam);
}
