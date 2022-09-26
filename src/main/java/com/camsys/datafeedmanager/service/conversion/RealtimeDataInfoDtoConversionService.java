package com.camsys.datafeedmanager.service.conversion;

import com.camsys.datafeedmanager.dto.RealtimeDataInfoDto;
import com.camsys.datafeedmanager.model.RealtimeFeedType;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.model.entities.RealtimeDataInfo;
import com.camsys.datafeedmanager.service.FeedInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RealtimeDataInfoDtoConversionService {

    @Autowired
    FeedInfoService feedInfoService;

    public RealtimeDataInfoDto convertToDto(RealtimeDataInfo realtimeDataInfo) {
        RealtimeDataInfoDto realtimeDataInfoDto = RealtimeDataInfoDto.builder()
                .id(realtimeDataInfo.getId())
                .name(realtimeDataInfo.getName())
                .description(realtimeDataInfo.getDescription())
                .feedURI(realtimeDataInfo.getFeedURI())
                .feedType(realtimeDataInfo.getFeedType().toString())
                .build();
        return realtimeDataInfoDto;
    }

    public RealtimeDataInfo convertToEntity(RealtimeDataInfoDto realtimeDataInfoDto, Long id) {
        FeedInfo feedInfo= feedInfoService.getFeedInfo(id);
        RealtimeDataInfo realtimeDataInfo = new RealtimeDataInfo();
        realtimeDataInfo.setId(realtimeDataInfoDto.getId());
        realtimeDataInfo.setName(realtimeDataInfoDto.getName());
        realtimeDataInfo.setDescription(realtimeDataInfoDto.getDescription());
        realtimeDataInfo.setFeedURI(realtimeDataInfoDto.getFeedURI());
        realtimeDataInfo.setFeedType(RealtimeFeedType.valueOf(realtimeDataInfoDto.getFeedType()));
        realtimeDataInfo.setFeedInfo(feedInfo);
        return realtimeDataInfo;
    }

    public RealtimeDataInfo convertToEntity(RealtimeDataInfoDto realtimeDataInfoDto, FeedInfo feedInfo) {
        RealtimeDataInfo realtimeDataInfo = new RealtimeDataInfo();
        realtimeDataInfo.setId(realtimeDataInfoDto.getId());
        realtimeDataInfo.setName(realtimeDataInfoDto.getName());
        realtimeDataInfo.setDescription(realtimeDataInfoDto.getDescription());
        realtimeDataInfo.setFeedURI(realtimeDataInfoDto.getFeedURI());
        realtimeDataInfo.setFeedType(RealtimeFeedType.valueOf(realtimeDataInfoDto.getFeedType()));
        realtimeDataInfo.setFeedInfo(feedInfo);
        return realtimeDataInfo;
    }

}
