package com.camsys.datafeedmanager.service;

import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.model.entities.RealtimeDataInfo;
import com.camsys.datafeedmanager.repository.FeedInfoRespository;
import com.camsys.datafeedmanager.repository.RealtimeDataInfoRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealtimeDataInfoService {
    @Autowired
    private FeedInfoRespository feedInfoRespository;

    @Autowired
    private RealtimeDataInfoRespository realtimeDataInfoRepository;

    /**
     * Create/Update RealtimeDataInfo
     * @param realtimeDataInfo
     * @return
     */
    public Long saveRealtimeDataInfo(RealtimeDataInfo realtimeDataInfo)  {
        RealtimeDataInfo savedFeedInfo = realtimeDataInfoRepository.save(realtimeDataInfo);
        return savedFeedInfo.getId();
    }

    /**
     * Read All RealtimeDataInfo
     * @return
     */
    public List<RealtimeDataInfo> getAllRealtimeDataInfo(){
        return realtimeDataInfoRepository.findAll();
    }

    /**
     * Read Single RealtimeDataInfo
     * @param id
     * @return
     */
    public RealtimeDataInfo getRealtimeDataInfo(Long id){
        return realtimeDataInfoRepository.findById(id).orElseThrow();
    }


    /**
     * Delete Single RealtimeDataInfo
     * @param id
     */
    public void deleteRealtimeDataInfo(Long id){
        RealtimeDataInfo realtimeDataInfo = realtimeDataInfoRepository.findById(id).orElseThrow();
        FeedInfo feedInfo = realtimeDataInfo.getFeedInfo();
        if(feedInfo != null){
            feedInfo.getRealtimeDataInfo().remove(realtimeDataInfo);
            feedInfoRespository.save(feedInfo);
        }
    }

}
