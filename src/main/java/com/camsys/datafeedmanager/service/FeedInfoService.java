package com.camsys.datafeedmanager.service;

import com.camsys.datafeedmanager.dto.FeedConfigurationDto;
import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.repository.FeedConfigurationRespository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;

@Service
public class FeedConfigurationService {
    @Autowired
    private FeedConfigurationRespository configurationRespository;

    @Autowired
    private ModelMapper modelMapper;

    public List<FeedConfiguration> getFeedConfigurations(){
        return configurationRespository.findAll();
    }

    public Long saveFeedConfiguration(FeedConfiguration feedConfiguration) throws ParseException {
        FeedConfiguration savedFeedConfiguration = configurationRespository.save(feedConfiguration);
        return savedFeedConfiguration.getId();
    }

    public void deleteFeedConfiguration(Long id){
        configurationRespository.deleteById(id);
    }

    @Transactional
    public List<FeedInfo> getFeedInfo(FeedConfiguration feedConfiguration){
        return feedConfiguration.getFeedInfo();
    }

    public FeedConfigurationDto convertToDto(FeedConfiguration feedConfiguration) {
        FeedConfigurationDto feedConfigurationDto = modelMapper.map(feedConfiguration, FeedConfigurationDto.class);
        return feedConfigurationDto;
    }

    public FeedConfiguration convertToEntity(FeedConfigurationDto feedConfigurationDto) throws ParseException {
        FeedConfiguration feedConfiguration = modelMapper.map(feedConfigurationDto, FeedConfiguration.class);
        return feedConfiguration;
    }
}
