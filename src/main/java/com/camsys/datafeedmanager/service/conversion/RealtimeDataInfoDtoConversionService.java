package com.camsys.datafeedmanager.service.conversion;

import com.camsys.datafeedmanager.dto.FeedInfoDto;
import com.camsys.datafeedmanager.dto.RealtimeDataInfoDto;
import com.camsys.datafeedmanager.model.RealtimeFeedType;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.model.entities.RealtimeDataInfo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RealtimeDataInfoDtoConversionService {


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
