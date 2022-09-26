package com.camsys.datafeedmanager.service;

import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.model.entities.TransitDataInfo;
import com.camsys.datafeedmanager.repository.FeedInfoRespository;
import com.camsys.datafeedmanager.repository.TransitDataInfoRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransitDataInfoService {
    @Autowired
    private FeedInfoRespository feedInfoRespository;

    @Autowired
    private TransitDataInfoRespository transitDataInfoRepository;

    /**
     * Create/Update TransitDataInfo
     * @param transitDataInfo
     * @return
     */
    public Long saveTransitDataInfo(TransitDataInfo transitDataInfo)  {
        TransitDataInfo savedFeedInfo = transitDataInfoRepository.save(transitDataInfo);
        return savedFeedInfo.getId();
    }

    /**
     * Read All TransitDataInfo
     * @return
     */
    public List<TransitDataInfo> getAllTransitDataInfo(){
        return transitDataInfoRepository.findAll();
    }

    /**
     * Read Single TransitDataInfo
     * @param id
     * @return
     */
    public TransitDataInfo getTransitDataInfo(Long id){
        return transitDataInfoRepository.findById(id).orElseThrow();
    }


    /**
     * Delete Single TransitDataInfo
     * @param id
     */
    public void deleteTransitDataInfo(Long id){
        TransitDataInfo transitDataInfo = transitDataInfoRepository.findById(id).orElseThrow();
        FeedInfo feedInfo = transitDataInfo.getFeedInfo();
        if(feedInfo != null){
            feedInfo.getTransitDataInfo().remove(transitDataInfo);
            feedInfoRespository.save(feedInfo);
        }
    }
}
