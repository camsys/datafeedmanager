package com.camsys.datafeedmanager.service;

import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import com.camsys.datafeedmanager.repository.FeedConfigurationRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedService {
    @Autowired
    private FeedConfigurationRespository configurationRespository;

    public List<FeedConfiguration> getFeedConfigurations(){
        return configurationRespository.findAll();
    }

    public void addFeedConfiguration(FeedConfiguration feedConfiguration){
        configurationRespository.save(feedConfiguration);
    }

    public void deleteFeedConfiguration(Long id){
        configurationRespository.deleteById(id);
    }
}
