package com.camsys.datafeedmanager.controller;

import com.camsys.datafeedmanager.dto.FeedConfigurationDto;
import com.camsys.datafeedmanager.dto.FeedInfoDto;
import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import com.camsys.datafeedmanager.service.FeedConfigurationService;
import com.camsys.datafeedmanager.service.conversion.FeedConfigurationDtoConversionService;
import com.camsys.datafeedmanager.service.conversion.FeedInfoDtoConversionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/feed/configurations")
public class FeedConfigurationController {

    @Autowired
    private FeedConfigurationService feedConfigurationService;

    @Autowired
    private FeedConfigurationDtoConversionService feedConfigDtoConversionService;

    @Autowired
    private FeedInfoDtoConversionService feedInfoDtoConversionService;


    @Operation(summary = "Get FeedConfigurations", description = "Get list of FeedConfigurations")
    @GetMapping("/list")
    ResponseEntity<List<FeedConfigurationDto>> getFeedConfigurations() {
        List<FeedConfiguration> feedConfigurations = feedConfigurationService.getFeedConfigurations();
        return ResponseEntity.ok(feedConfigurations.stream()
                .map(feedConfigDtoConversionService::convertToDto)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Get FeedConfiguration", description = "Get a single FeedConfiguration")
    @GetMapping("/{id}")
    FeedConfigurationDto getFeedConfiguration(@Parameter(description = "The id of FeedConfiguration that needs to " +
            "be fetched", required = true) @PathVariable Long id) {
        FeedConfiguration feedConfiguration =  feedConfigurationService.getFeedConfiguration(id);
        return feedConfigDtoConversionService.convertToDto(feedConfiguration);
    }

    @Operation(summary = "Save FeedConfiguration", description = "Save a single FeedConfiguration ")
    @PostMapping("/save")
    void saveFeedConfiguration(@RequestBody FeedConfigurationDto feedConfigurationDto) throws ParseException {
        FeedConfiguration feedConfiguration = feedConfigDtoConversionService.convertToEntity(feedConfigurationDto);
        feedConfigurationService.saveFeedConfiguration(feedConfiguration);
    }

    @Operation(summary = "Delete FeedConfiguration", description = "Delete a single FeedConfiguration ")
    @GetMapping("/delete/{id}")
    void deleteFeedConfiguration(@Parameter(description = "The id of FeedConfiguration that needs to " +
            "be deleted", required = true) @PathVariable("id") long id) {
        feedConfigurationService.deleteFeedConfiguration(id);
    }

    @Operation(summary = "Get FeedConfiguration Feed Infos", description = "Get all the FeedInfos associated with the " +
            "specified Feed Configuration")
    @GetMapping("/{id}/feed-info/list")
    List<FeedInfoDto> getFeedInfoList(@Parameter(description = "The id of FeedConfiguration that needs to " +
            "be deleted", required = true) @PathVariable("id") long id) {
        FeedConfiguration feedConfiguration = feedConfigurationService.getFeedConfiguration(id);
        return feedConfiguration.getFeedInfo().stream()
                .map(feedInfoDtoConversionService::convertToDto)
                .collect(Collectors.toList());
    }

}
