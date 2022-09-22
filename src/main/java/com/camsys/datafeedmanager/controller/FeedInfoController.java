package com.camsys.datafeedmanager.controller;

import com.camsys.datafeedmanager.dto.FeedConfigurationDto;
import com.camsys.datafeedmanager.dto.FeedInfoDto;
import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import com.camsys.datafeedmanager.service.FeedConfigurationService;
import com.camsys.datafeedmanager.service.conversion.FeedConfigurationDtoConversionService;
import com.camsys.datafeedmanager.service.conversion.FeedInfoDtoConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/feed")
public class FeedConfigurationController {

    @Autowired
    private FeedConfigurationService feedConfigurationService;

    @Autowired
    private FeedConfigurationDtoConversionService feedConfigDtoConversionService;

    @Autowired
    private FeedInfoDtoConversionService feedInfoDtoConversionService;


    @GetMapping("/configurations/list")
    List<FeedConfigurationDto> getFeedConfigurations() {
        List<FeedConfiguration> feedConfigurations = feedConfigurationService.getFeedConfigurations();
        return feedConfigurations.stream()
                .map(feedConfigurationService::)
                .collect(Collectors.toList());
    }

    @GetMapping("/configurations/{id}")
    FeedConfigurationDto getFeedConfiguration(@PathVariable Long id) {
        FeedConfiguration feedConfiguration =  feedConfigurationService.getFeedConfiguration(id);
        return dtoConversionService.convertToDto(feedConfiguration);
    }

    @PostMapping("/configurations/save")
    void saveFeedConfiguration(@RequestBody FeedConfigurationDto feedConfigurationDto) throws ParseException {
        FeedConfiguration feedConfiguration = dtoConversionService.convertToEntity(feedConfigurationDto);
        feedConfigurationService.saveFeedConfiguration(feedConfiguration);
    }

    @GetMapping("/configurations/delete/{id}")
    void deleteFeedConfiguration(@PathVariable("id") long id) {
        feedConfigurationService.deleteFeedConfiguration(id);
    }


    @GetMapping("/configurations/{configId}/feed-info/list")
    List<FeedInfoDto> getFeedInfoList(@PathVariable("configId") long configId) {
        FeedConfiguration feedConfiguration = feedConfigurationService.getFeedConfiguration(configId);
        return feedConfiguration.getFeedInfo().stream()
                .map(feedInfoDtoConversionService::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/configurations/{configId}/feed-info/{id}")
    FeedInfoDto getFeedInfo(@PathVariable("configId") long configId, @PathVariable("id") long id) {
        FeedConfiguration feedConfiguration = feedConfigurationService.getFeedConfiguration(configId);
        FeedInfoDto feedInfoDto =  feedConfigurationService.getFeedConfigurations();
        return feedConfigurations.stream()
                .map(dtoConversionService::convertToDto)
                .collect(Collectors.toList());
    }
}
