package com.camsys.datafeedmanager.service;

import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.repository.FeedConfigurationRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;

@Service
public class FeedConfigurationService {
    @Autowired
    private FeedConfigurationRespository configurationRespository;

    public List<FeedConfiguration> getFeedConfigurations(){
        return configurationRespository.findAll();
    }

    public FeedConfiguration getFeedConfiguration(Long id) {
        return configurationRespository.findById(id).orElseThrow();
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

}
