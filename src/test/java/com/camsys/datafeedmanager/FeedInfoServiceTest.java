package com.camsys.datafeedmanager;

import com.camsys.datafeedmanager.model.RealtimeFeedType;
import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.service.FeedConfigurationService;
import com.camsys.datafeedmanager.service.FeedInfoService;
import data.FeedInfoMockData;
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
public class FeedInfoServiceTest {

    @Autowired
    private FeedConfigurationService feedConfigurationService;

    @Autowired
    private FeedInfoService feedInfoService;


    @Test
    public void feedInfoCRUDTest() throws ParseException {
        // Create Test
        assertEquals(0, feedInfoService.getFeedInfos().size());

        Long savedFeedConfigurationId = feedConfigurationService.saveFeedConfiguration(getFeedConfigurationSample());

        assertNotNull(savedFeedConfigurationId);

        assertEquals(1, feedInfoService.getFeedInfos().size());

        FeedConfiguration feedConfiguration = feedConfigurationService.getFeedConfiguration(savedFeedConfigurationId);

        FeedInfo feedInfo = FeedInfoMockData.getFeedInfoSample();

        feedInfo.setFeedConfiguration(feedConfiguration);

        Long savedFeedInfoId = feedInfoService.saveFeedInfo(feedInfo);

        assertNotNull(savedFeedInfoId);

        assertEquals(2, feedInfoService.getFeedInfos().size());

        // Read Test
        FeedInfo feedInfoResult = feedInfoService.getFeedInfo(savedFeedInfoId);

        assertNotNull(feedInfoResult);

        assertEquals("Minneapolis Metro Transit 2", feedInfo.getAgency());

        assertEquals("Metro Transit 2", feedInfo.getFeedName());

        assertEquals("Minnesota Shuttles", feedInfo.getServiceName());

        assertEquals(true, feedInfo.getEnabled());

        assertEquals(RealtimeFeedType.GTFSRT, feedInfo.getRealtimeDataInfo().get(0).getFeedType());

        assertEquals("https://sample-url/mtgtfs/tripupdates.pb", feedInfo.getRealtimeDataInfo().get(0).getFeedURI());

        assertEquals("MT Shuttles GTFS", feedInfo.getTransitDataInfo().get(0).getName());

        assertEquals(false, feedInfo.getTransitDataInfo().get(0).getMerge());


        // Update Test
        feedInfoResult.setFeedName("Updated Feed Name 2");

        feedInfoResult.setEnabled(false);

        feedInfoResult.getTransitDataInfo().get(0).setName("Updated MT Shuttles GTFS");

        feedInfoResult.getTransitDataInfo().get(0).setMerge(true);

        feedInfoResult.getRealtimeDataInfo().get(0).setFeedURI("http://new");

        feedInfoService.saveFeedInfo(feedInfoResult);

        FeedInfo updatedFeedInfo = feedInfoService.getFeedInfo(savedFeedInfoId);

        assertEquals("Updated Feed Name 2", updatedFeedInfo.getFeedName());

        assertFalse(updatedFeedInfo.getEnabled());

        assertEquals("Updated MT Shuttles GTFS", updatedFeedInfo.getTransitDataInfo().get(0).getName());

        assertTrue(updatedFeedInfo.getTransitDataInfo().get(0).getMerge());

        assertEquals("http://new", updatedFeedInfo.getRealtimeDataInfo().get(0).getFeedURI());

        // Delete Test
        feedInfoService.deleteFeedInfo(savedFeedInfoId);

        List<FeedInfo> feedInfos = feedInfoService.getFeedInfos();

        assertEquals(1, feedInfoService.getFeedInfos().size());
    }
}
