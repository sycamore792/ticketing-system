package com.sycamore.ticketing_system.ticketService.service;

import com.sycamore.ticketing_system.ticketService.dto.req.RegionStationQueryReqDTO;
import com.sycamore.ticketing_system.ticketService.dto.resp.RegionStationQueryRespDTO;
import com.sycamore.ticketing_system.ticketService.dto.resp.StationQueryRespDTO;

import java.util.List;

/**
 * HIS IS A INTERFACE
 *
 * @PROJECT_NAME: ticketing_system
 * @INTERFACE_NAME: RegionStationService
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/20 20:46
 */
public interface RegionStationService {
    List<StationQueryRespDTO> listAllStation();

    List<RegionStationQueryRespDTO> listRegionStation(RegionStationQueryReqDTO requestParam);
}
