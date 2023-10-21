package com.sycamore.ticketing_system.ticketService.service.impl;

import com.sycamore.ticketing_system.ticketService.dto.req.TicketPageQueryReqDTO;
import com.sycamore.ticketing_system.ticketService.dto.resp.TicketPageQueryRespDTO;
import com.sycamore.ticketing_system.ticketService.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * THIS IS A CLASS
 *
 * @PROJECT_NAME: ticketing_system
 * @CLASS_NAME: TicketServiceImpl
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/20 22:11
 */
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    @Override
    public TicketPageQueryRespDTO pageListTicketQueryV1(TicketPageQueryReqDTO requestParam) {
        return null;
    }
}
