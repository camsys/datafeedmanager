package com.camsys.datafeedmanager.service.conversion;

import com.camsys.datafeedmanager.dto.FeedConfigurationDto;
import com.camsys.datafeedmanager.model.entities.FeedConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class FeedConfigurationDtoConversionService {


    public FeedConfigurationDto convertToDto(FeedConfiguration feedConfiguration) {

        FeedConfigurationDto feedConfigurationDto = FeedConfigurationDto.builder()
            .id(feedConfiguration.getId())
            .feedConfigName(feedConfiguration.getFeedConfigName())
            .backupDirectory(feedConfiguration.getBackupDirectory())
            .targetDirectory(feedConfiguration.getTargetDirectory())
            .build();

        return feedConfigurationDto;
    }

    public FeedConfiguration convertToEntity(FeedConfigurationDto feedConfigurationDto) {

        FeedConfiguration feedConfiguration = new FeedConfiguration();
        feedConfiguration.setId(feedConfigurationDto.getId());
        feedConfiguration.setFeedConfigName(feedConfigurationDto.getFeedConfigName());
        feedConfiguration.setTargetDirectory(feedConfigurationDto.getTargetDirectory());
        feedConfiguration.setBackupDirectory(feedConfigurationDto.getBackupDirectory());

        return feedConfiguration;
    }
}
