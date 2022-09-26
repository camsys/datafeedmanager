package com.camsys.datafeedmanager;

import com.camsys.datafeedmanager.model.RealtimeFeedType;
import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.model.entities.RealtimeDataInfo;
import com.camsys.datafeedmanager.service.FeedConfigurationService;
import com.camsys.datafeedmanager.service.RealtimeDataInfoService;
import data.FeedInfoMockData;
import data.RealtimeDataInfoMockData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.util.List;

import static data.FeedConfigMockData.getFeedConfigurationSample;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class RealtimeDataInfoServiceTest {

    @Autowired
    private FeedConfigurationService feedConfigurationService;

    @Autowired
    private RealtimeDataInfoService realtimeDataService;


    @Test
    public void realtimeDataCRUDTest() throws ParseException {
        // Create Test
        assertEquals(0, realtimeDataService.getAllRealtimeDataInfo().size());

        Long savedFeedConfigurationId = feedConfigurationService.saveFeedConfiguration(getFeedConfigurationSample());

        assertNotNull(savedFeedConfigurationId);

        FeedConfiguration feedConfiguration = feedConfigurationService.getFeedConfiguration(savedFeedConfigurationId);

        FeedInfo feedInfo = feedConfiguration.getFeedInfo().stream().findFirst().orElseThrow();

        assertEquals(2, feedInfo.getRealtimeDataInfo().size());

        RealtimeDataInfo realtimeData = RealtimeDataInfoMockData.getRealtimeDataInfoSample();

        realtimeData.setFeedInfo(feedInfo);

        Long saveRealtimeDataInfoId = realtimeDataService.saveRealtimeDataInfo(realtimeData);

        assertNotNull(saveRealtimeDataInfoId);

        assertEquals(3, realtimeDataService.getAllRealtimeDataInfo().size());

        // Read Test
        RealtimeDataInfo realtimeDataResult = realtimeDataService.getRealtimeDataInfo(saveRealtimeDataInfoId);

        assertNotNull(realtimeDataResult);

        assertEquals("RealtimeData TripUpdates Feed", realtimeDataResult.getName());

        assertEquals("https://realtimedatainfo/tripupdates.pb", realtimeDataResult.getFeedURI());

        assertEquals(RealtimeFeedType.GTFSRT, realtimeDataResult.getFeedType());


        // Update Test
        realtimeDataResult.setName("Updated Feed Name 2");

        realtimeDataResult.setFeedURI("http://new");

        realtimeDataService.saveRealtimeDataInfo(realtimeDataResult);

        RealtimeDataInfo updatedRealtimeDataInfo = realtimeDataService.getRealtimeDataInfo(saveRealtimeDataInfoId);

        assertEquals("Updated Feed Name 2", updatedRealtimeDataInfo.getName());

        assertEquals("http://new", updatedRealtimeDataInfo.getFeedURI());

        assertEquals(3, realtimeDataService.getAllRealtimeDataInfo().size());

        // Delete Test
        realtimeDataService.deleteRealtimeDataInfo(saveRealtimeDataInfoId);

        assertEquals(2, realtimeDataService.getAllRealtimeDataInfo().size());
    }
}
