package com.sycamore.ticketing_system.ticketService.controller;

import com.sycamore.ticketing_system.convention.result.Result;
import com.sycamore.ticketing_system.ticketService.dto.resp.TrainStationQueryRespDTO;
import com.sycamore.ticketing_system.ticketService.service.TrainStationService;
import com.sycamore.ticketing_system.web.handler.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 列车站点控制层
 *
 *  
 */
@RestController
@RequiredArgsConstructor
public class TrainStationController {

    private final TrainStationService trainStationService;

    /**
     * 根据列车 ID 查询站点信息
     */
    @GetMapping("/api/ticket-service/train-station/query")
    public Result<List<TrainStationQueryRespDTO>> listTrainStationQuery(String trainId) {
        return Results.success(trainStationService.listTrainStationQuery(trainId));
    }
}