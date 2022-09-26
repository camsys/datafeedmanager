package com.camsys.datafeedmanager.controller;

import com.camsys.datafeedmanager.dto.FeedInfoDto;
import com.camsys.datafeedmanager.dto.RealtimeDataInfoDto;
import com.camsys.datafeedmanager.dto.TransitDataInfoDto;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.model.entities.RealtimeDataInfo;
import com.camsys.datafeedmanager.model.entities.TransitDataInfo;
import com.camsys.datafeedmanager.service.FeedInfoService;
import com.camsys.datafeedmanager.service.RealtimeDataInfoService;
import com.camsys.datafeedmanager.service.TransitDataInfoService;
import com.camsys.datafeedmanager.service.conversion.FeedInfoDtoConversionService;
import com.camsys.datafeedmanager.service.conversion.RealtimeDataInfoDtoConversionService;
import com.camsys.datafeedmanager.service.conversion.TransitDataInfoDtoConversionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feed/feed-info")
public class FeedInfoController {

    @Autowired
    private FeedInfoService feedInfoService;

    @Autowired
    private FeedInfoDtoConversionService feedInfoDtoConversionService;

    @Autowired
    private RealtimeDataInfoService realtimeDataInfoService;

    @Autowired
    private RealtimeDataInfoDtoConversionService realtimeDataInfoDtoConversionService;

    @Autowired
    private TransitDataInfoService transitDataInfoService;

    @Autowired
    private TransitDataInfoDtoConversionService transitDataInfoDtoConversionService;

    @Operation(summary = "Get FeedInfo", description = "Get a single FeedInfo")
    @GetMapping("/{id}")
    FeedInfoDto getFeedInfo(@Parameter(description = "The id of FeedInfo that needs to be fetched", required = true)
                            @PathVariable("id") long id) {
        FeedInfo feedInfo =  feedInfoService.getFeedInfo(id);
        return feedInfoDtoConversionService.convertToDto(feedInfo);
    }

    @Operation(summary = "Save FeedInfo", description = "Save a single FeedInfo ")
    @PostMapping("/save")
    Long saveFeedInfo(@RequestBody FeedInfoDto feedInfoDto) {
        FeedInfo feedInfo = feedInfoDtoConversionService.convertToEntity(feedInfoDto);
        return feedInfoService.saveFeedInfo(feedInfo);
    }

    @Operation(summary = "Delete FeedInfo", description = "Delete a single FeedInfo ")
    @GetMapping("/delete/{id}")
    void deleteFeedInfo(@Parameter(description = "The id of FeedInfo that needs to be deleted", required = true)
                        @PathVariable("id") long id) {
        feedInfoService.deleteFeedInfo(id);
    }

    @Operation(summary = "Save RealtimeDataInfo", description = "Save a single RealtimeDataInfo ")
    @PostMapping("/{id}/realtime-data/save")
    Long saveRealtimeData(@RequestBody RealtimeDataInfoDto realtimeDataInfoDto, @PathVariable("id") long id) {
        RealtimeDataInfo realtimeDataInfo = realtimeDataInfoDtoConversionService.convertToEntity(realtimeDataInfoDto, id);
        return realtimeDataInfoService.saveRealtimeDataInfo(realtimeDataInfo);
    }

    @Operation(summary = "Save TransitDataInfo", description = "Save a single TransitDataInfo ")
    @PostMapping("/{id}/transit-data/save")
    Long saveTransitData(@RequestBody TransitDataInfoDto transitDataInfoDto, @PathVariable("id") long id) {
        TransitDataInfo transitDataInfo = transitDataInfoDtoConversionService.convertToEntity(transitDataInfoDto, id);
        return transitDataInfoService.saveTransitDataInfo(transitDataInfo);
    }
}
