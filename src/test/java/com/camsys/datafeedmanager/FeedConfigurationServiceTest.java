package com.camsys.datafeedmanager;

import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.service.FeedConfigurationService;
import com.camsys.datafeedmanager.service.FeedInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.util.List;

import static data.FeedConfigMockData.getFeedConfigurationSample;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class FeedConfigurationServiceTest {

    @Autowired
    private FeedConfigurationService feedConfigurationService;

    @Autowired
    private FeedInfoService feedInfoService;



    @Test
    public void feedConfigurationCRUDTest() throws ParseException {
        // Create Test
        assertEquals(0, feedConfigurationService.getFeedConfigurations().size());

        Long savedFeedConfigurationId = feedConfigurationService.saveFeedConfiguration(getFeedConfigurationSample());

        assertNotNull(savedFeedConfigurationId);

        assertEquals(1, feedConfigurationService.getFeedConfigurations().size());

        // Read Test
        FeedConfiguration feedConfigurationResult = feedConfigurationService.getFeedConfiguration(savedFeedConfigurationId);

        assertEquals("configName", feedConfigurationResult.getFeedConfigName());

        assertEquals("/backupDir", feedConfigurationResult.getBackupDirectory());

        assertEquals("/targetDir", feedConfigurationResult.getTargetDirectory());

        // Update Test
        feedConfigurationResult.setFeedConfigName("Updated Feed Config Name");

        feedConfigurationService.saveFeedConfiguration(feedConfigurationResult);

        List<FeedConfiguration> updatedFeedConfigurations = feedConfigurationService.getFeedConfigurations();

        FeedConfiguration updatedFeedConfigurationResult = updatedFeedConfigurations.stream().findFirst().get();

        assertEquals("Updated Feed Config Name", updatedFeedConfigurationResult.getFeedConfigName());

        // Delete Test
        List<FeedInfo> feedInfo = feedInfoService.getFeedInfos();

        assertEquals(1, feedInfo.size());

        feedConfigurationService.deleteFeedConfiguration(updatedFeedConfigurationResult.getId());

        List<FeedConfiguration> latestFeedConfigurations = feedConfigurationService.getFeedConfigurations();

        assertEquals(0, latestFeedConfigurations.size());

        List<FeedInfo> feedInfoPostDelete = feedInfoService.getFeedInfos();

        assertEquals(0, feedInfoPostDelete.size());
    }
}
