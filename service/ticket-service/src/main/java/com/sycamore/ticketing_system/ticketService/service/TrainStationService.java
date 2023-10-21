package com.sycamore.ticketing_system.ticketService.service;

import com.sycamore.ticketing_system.ticketService.dto.resp.TrainStationQueryRespDTO;

import java.util.List;

/**
 * HIS IS A INTERFACE
 *
 * @PROJECT_NAME: ticketing_system
 * @INTERFACE_NAME: TrainStationService
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/20 22:08
 */
public interface TrainStationService {
    List<TrainStationQueryRespDTO> listTrainStationQuery(String trainId);
}
