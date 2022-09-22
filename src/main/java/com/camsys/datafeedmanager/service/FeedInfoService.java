package com.camsys.datafeedmanager.service;

import com.camsys.datafeedmanager.dto.FeedConfigurationDto;
import com.camsys.datafeedmanager.dto.FeedConfigurationListDto;
import com.camsys.datafeedmanager.dto.FeedInfoDto;
import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.model.entities.RealtimeDataInfo;
import com.camsys.datafeedmanager.model.entities.TransitDataInfo;
import com.camsys.datafeedmanager.repository.FeedConfigurationRespository;
import com.camsys.datafeedmanager.repository.FeedInfoRespository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;

@Service
public class FeedInfoService {
    @Autowired
    private FeedConfigurationRespository feedConfigurationRespository;

    @Autowired
    private FeedInfoRespository feedInfoRespository;

    public List<FeedInfo> getFeedInfos(){
        return feedInfoRespository.findAll();
    }

    public FeedInfo getFeedInfo(Long id){
        return feedInfoRespository.findById(id).orElseThrow();
    }

    public Long saveFeedInfo(FeedInfo feedInfo)  {
        FeedInfo savedFeedInfo = feedInfoRespository.save(feedInfo);
        return savedFeedInfo.getId();
    }

    public void deleteFeedInfo(Long id){
        FeedInfo feedInfo = feedInfoRespository.findById(id).orElseThrow();
        FeedConfiguration feedConfiguration = feedInfo.getFeedConfiguration();
        if(feedConfiguration != null){
            feedConfiguration.getFeedInfo().remove(feedInfo);
            feedConfigurationRespository.save(feedConfiguration);
        }
    }

    @Transactional
    public List<RealtimeDataInfo> getRealtimeDataInfo(FeedInfo feedInfo){
        return feedInfo.getRealtimeDataInfo();
    }

    @Transactional
    public List<TransitDataInfo> getTransitDataInfo(FeedInfo feedInfo){
        return feedInfo.getTransitDataInfo();
    }

}
