package com.camsys.datafeedmanager.service.conversion;

import com.camsys.datafeedmanager.dto.FeedConfigurationDto;
import com.camsys.datafeedmanager.dto.FeedConfigurationListDto;
import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class FeedConfigurationDtoConversionService {

    @Autowired
    private ModelMapper modelMapper;


    public FeedConfigurationDto convertToDto(FeedConfiguration feedConfiguration) {
        FeedConfigurationDto feedConfigurationDto = modelMapper.map(feedConfiguration, FeedConfigurationDto.class);
        return feedConfigurationDto;
    }

    public FeedConfiguration convertToEntity(FeedConfigurationDto feedConfigurationDto) throws ParseException {
        FeedConfiguration feedConfiguration = modelMapper.map(feedConfigurationDto, FeedConfiguration.class);
        return feedConfiguration;
    }

    public FeedConfigurationListDto convertToListDto(FeedConfiguration feedConfiguration) {
        FeedConfigurationListDto feedConfigurationListDto = modelMapper.map(feedConfiguration, FeedConfigurationListDto.class);
        return feedConfigurationListDto;
    }
}
