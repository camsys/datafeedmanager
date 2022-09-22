package com.camsys.datafeedmanager.service.conversion;

import com.camsys.datafeedmanager.dto.FeedConfigurationDto;
import com.camsys.datafeedmanager.dto.FeedConfigurationListDto;
import com.camsys.datafeedmanager.dto.FeedInfoDto;
import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class FeedInfoDtoConversionService {

    @Autowired
    private ModelMapper modelMapper;


    public FeedInfoDto convertToDto(FeedInfo feedInfo) {
        FeedInfoDto feedInfoDto = modelMapper.map(feedInfo, FeedInfoDto.class);
        return feedInfoDto;
    }

    public FeedInfo convertToEntity(FeedInfoDto feedInfoDto) {
        FeedInfo feedInfo = modelMapper.map(feedInfoDto, FeedInfo.class);
        return feedInfo;
    }

}
