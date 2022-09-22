package com.camsys.datafeedmanager.controller;

import com.camsys.datafeedmanager.dto.FeedInfoDto;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.service.FeedInfoService;
import com.camsys.datafeedmanager.service.conversion.FeedInfoDtoConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feed/feed-info")
public class FeedInfoController {

    @Autowired
    private FeedInfoService feedInfoService;

    @Autowired
    private FeedInfoDtoConversionService feedInfoDtoConversionService;


    @GetMapping("/{id}")
    FeedInfoDto getFeedInfo(@PathVariable("id") long id) {
        FeedInfo feedInfo =  feedInfoService.getFeedInfo(id);
        return feedInfoDtoConversionService.convertToDto(feedInfo);
    }

    @PostMapping("/save")
    Long saveFeedInfo(@RequestBody FeedInfoDto feedInfoDto) {
        FeedInfo feedInfo = feedInfoDtoConversionService.convertToEntity(feedInfoDto);
        return feedInfoService.saveFeedInfo(feedInfo);
    }

    @GetMapping("/delete/{id}")
    void deleteFeedInfo(@PathVariable("id") long id) {
        feedInfoService.deleteFeedInfo(id);
    }


}
