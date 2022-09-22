package com.camsys.datafeedmanager.controller;

import com.camsys.datafeedmanager.dto.FeedConfigurationDto;
import com.camsys.datafeedmanager.dto.FeedInfoDto;
import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import com.camsys.datafeedmanager.service.FeedConfigurationService;
import com.camsys.datafeedmanager.service.conversion.FeedConfigurationDtoConversionService;
import com.camsys.datafeedmanager.service.conversion.FeedInfoDtoConversionService;
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


    @GetMapping("/list")
    ResponseEntity<List<FeedConfigurationDto>> getFeedConfigurations() {
        List<FeedConfiguration> feedConfigurations = feedConfigurationService.getFeedConfigurations();
        return ResponseEntity.ok(feedConfigurations.stream()
                .map(feedConfigDtoConversionService::convertToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    FeedConfigurationDto getFeedConfiguration(@PathVariable Long id) {
        FeedConfiguration feedConfiguration =  feedConfigurationService.getFeedConfiguration(id);
        return feedConfigDtoConversionService.convertToDto(feedConfiguration);
    }

    @PostMapping("/save")
    void saveFeedConfiguration(@RequestBody FeedConfigurationDto feedConfigurationDto) throws ParseException {
        FeedConfiguration feedConfiguration = feedConfigDtoConversionService.convertToEntity(feedConfigurationDto);
        feedConfigurationService.saveFeedConfiguration(feedConfiguration);
    }

    @GetMapping("/delete/{id}")
    void deleteFeedConfiguration(@PathVariable("id") long id) {
        feedConfigurationService.deleteFeedConfiguration(id);
    }


    @GetMapping("/{id}/feed-info/list")
    List<FeedInfoDto> getFeedInfoList(@PathVariable("id") long id) {
        FeedConfiguration feedConfiguration = feedConfigurationService.getFeedConfiguration(id);
        return feedConfiguration.getFeedInfo().stream()
                .map(feedInfoDtoConversionService::convertToDto)
                .collect(Collectors.toList());
    }

}
