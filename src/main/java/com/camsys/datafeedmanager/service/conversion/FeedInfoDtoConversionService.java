package com.camsys.datafeedmanager.service.conversion;

import com.camsys.datafeedmanager.dto.*;
import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.model.entities.RealtimeDataInfo;
import com.camsys.datafeedmanager.model.entities.TransitDataInfo;
import com.camsys.datafeedmanager.service.FeedConfigurationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FeedInfoDtoConversionService {

    @Autowired
    private TransitDataInfoDtoConversionService transitDataInfoDtoConversionService;

    @Autowired
    private RealtimeDataInfoDtoConversionService realtimeDataInfoDtoConversionService;

    @Autowired
    private FeedConfigurationService feedConfigurationService;

    public FeedInfoDto convertToDto(FeedInfo feedInfo) {
        List<RealtimeDataInfoDto> realtimeDataInfoDto = feedInfo.getRealtimeDataInfo().stream()
            .map(realtimeDataInfoDtoConversionService::convertToDto)
            .collect(Collectors.toList());

        List<TransitDataInfoDto> transitDataInfoDto = feedInfo.getTransitDataInfo().stream()
                .map(transitDataInfoDtoConversionService::convertToDto)
                .collect(Collectors.toList());

        FeedInfoDto feedInfoDto = FeedInfoDto.builder()
            .id(feedInfo.getId())
            .feedName(feedInfo.getFeedName())
            .agency(feedInfo.getAgency())
            .serviceName(feedInfo.getServiceName())
            .enabled(feedInfo.getEnabled())
            .realtimeDataInfo(realtimeDataInfoDto)
            .transitDataInfo(transitDataInfoDto)
            .feedConfigurationId(feedInfo.getFeedConfiguration().getId())
            .build();

        return feedInfoDto;
    }

    public FeedInfo convertToEntity(FeedInfoDto feedInfoDto){
        FeedConfiguration feedConfiguration =
                feedConfigurationService.getFeedConfiguration(feedInfoDto.getFeedConfigurationId());
        return convertToEntity(feedInfoDto, feedConfiguration);
    }

    public FeedInfo convertToEntity(FeedInfoDto feedInfoDto, FeedConfiguration feedConfiguration) {
        FeedInfo feedInfo = new FeedInfo();
        feedInfo.setId(feedInfoDto.getId());
        feedInfo.setFeedName(feedInfoDto.getFeedName());
        feedInfo.setServiceName(feedInfoDto.getServiceName());
        feedInfo.setAgency(feedInfoDto.getAgency());
        feedInfo.setEnabled(feedInfoDto.getEnabled());
        feedInfo.setEnabled(feedInfoDto.getEnabled());
        feedInfo.setFeedConfiguration(feedConfiguration);

        List<TransitDataInfo> transitDataInfo = feedInfoDto.getTransitDataInfo().stream()
                .map(t -> transitDataInfoDtoConversionService.convertToEntity(t, feedInfo))
                .collect(Collectors.toList());

        List<RealtimeDataInfo> realtimeDataInfo = feedInfoDto.getRealtimeDataInfo().stream()
                .map(r -> realtimeDataInfoDtoConversionService.convertToEntity(r, feedInfo))
                .collect(Collectors.toList());

        feedInfo.setTransitDataInfo(transitDataInfo);
        feedInfo.setRealtimeDataInfo(realtimeDataInfo);

        return feedInfo;
    }



}
