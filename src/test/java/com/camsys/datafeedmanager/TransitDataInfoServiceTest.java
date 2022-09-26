package com.camsys.datafeedmanager;

import com.camsys.datafeedmanager.model.RealtimeFeedType;
import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.model.entities.RealtimeDataInfo;
import com.camsys.datafeedmanager.model.entities.TransitDataInfo;
import com.camsys.datafeedmanager.service.FeedConfigurationService;
import com.camsys.datafeedmanager.service.RealtimeDataInfoService;
import com.camsys.datafeedmanager.service.TransitDataInfoService;
import data.RealtimeDataInfoMockData;
import data.TransitDataInfoMockData;
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
public class TransitDataInfoServiceTest {

    @Autowired
    private FeedConfigurationService feedConfigurationService;

    @Autowired
    private TransitDataInfoService transitDataInfoService;


    @Test
    public void realtimeDataCRUDTest() throws ParseException {
        // Create Test
        assertEquals(0, transitDataInfoService.getAllTransitDataInfo().size());

        Long savedFeedConfigurationId = feedConfigurationService.saveFeedConfiguration(getFeedConfigurationSample());

        assertNotNull(savedFeedConfigurationId);

        FeedConfiguration feedConfiguration = feedConfigurationService.getFeedConfiguration(savedFeedConfigurationId);

        FeedInfo feedInfo = feedConfiguration.getFeedInfo().stream().findFirst().orElseThrow();

        assertEquals(1, feedInfo.getTransitDataInfo().size());

        TransitDataInfo transitDataInfo = TransitDataInfoMockData.getTransitDataInfoSample();

        transitDataInfo.setFeedInfo(feedInfo);

        Long saveTransitDataInfoId = transitDataInfoService.saveTransitDataInfo(transitDataInfo);

        assertNotNull(saveTransitDataInfoId);

        assertEquals(2, transitDataInfoService.getAllTransitDataInfo().size());

        // Read Test
        TransitDataInfo transitDataInfoResult = transitDataInfoService.getTransitDataInfo(saveTransitDataInfoId);

        assertNotNull(transitDataInfoResult);

        assertEquals("TransitDataInfo GTFS File", transitDataInfoResult.getName());

        assertEquals("https://file/mndot.zip", transitDataInfoResult.getSourceURI());

        assertEquals("new_file.zip", transitDataInfoResult.getTargetName());

        assertFalse(transitDataInfoResult.getMerge());

        assertEquals("Description", transitDataInfoResult.getDescription());

        // Update Test
        transitDataInfoResult.setName("TransitDataInfo GTFS File Update");

        transitDataInfoResult.setSourceURI("https://updated_file/mndot.zip");

        transitDataInfoResult.setTargetName("updated_zip_file.zip");

        transitDataInfoResult.setMerge(true);

        transitDataInfoResult.setDescription(null);

        transitDataInfoService.saveTransitDataInfo(transitDataInfoResult);

        TransitDataInfo updatedTransitDataInfo = transitDataInfoService.getTransitDataInfo(saveTransitDataInfoId);

        assertEquals("TransitDataInfo GTFS File Update", updatedTransitDataInfo.getName());

        assertEquals("https://updated_file/mndot.zip", updatedTransitDataInfo.getSourceURI());

        assertEquals("updated_zip_file.zip", updatedTransitDataInfo.getTargetName());

        assertTrue(updatedTransitDataInfo.getMerge());

        assertNull(updatedTransitDataInfo.getDescription());

        assertEquals(2, transitDataInfoService.getAllTransitDataInfo().size());

        // Delete Test
        transitDataInfoService.deleteTransitDataInfo(saveTransitDataInfoId);

        assertEquals(1, transitDataInfoService.getAllTransitDataInfo().size());
    }
}
