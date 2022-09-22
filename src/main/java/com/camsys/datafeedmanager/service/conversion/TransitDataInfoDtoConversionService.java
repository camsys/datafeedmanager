package com.camsys.datafeedmanager.service.conversion;

import com.camsys.datafeedmanager.dto.TransitDataInfoDto;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.model.entities.TransitDataInfo;
import org.springframework.stereotype.Component;

@Component
public class TransitDataInfoDtoConversionService {

    public TransitDataInfoDto convertToDto(TransitDataInfo transitDataInfo) {
        TransitDataInfoDto transitDataInfoDto = TransitDataInfoDto.builder()
                .id(transitDataInfo.getId())
                .name(transitDataInfo.getName())
                .description(transitDataInfo.getDescription())
                .sourceURI(transitDataInfo.getSourceURI())
                .targetName(transitDataInfo.getTargetName())
                .merge(transitDataInfo.getMerge())
                .build();
        return transitDataInfoDto;
    }


    public TransitDataInfo convertToEntity(TransitDataInfoDto transitDataInfoDto, FeedInfo feedInfo) {
        TransitDataInfo transitDataInfo = new TransitDataInfo();
        transitDataInfo.setId(transitDataInfoDto.getId());
        transitDataInfo.setName(transitDataInfoDto.getName());
        transitDataInfo.setDescription(transitDataInfoDto.getDescription());
        transitDataInfo.setSourceURI(transitDataInfoDto.getSourceURI());
        transitDataInfo.setTargetName(transitDataInfoDto.getTargetName());
        transitDataInfo.setMerge(transitDataInfoDto.getMerge());
        transitDataInfo.setFeedInfo(feedInfo);

        return transitDataInfo;
    }
}
