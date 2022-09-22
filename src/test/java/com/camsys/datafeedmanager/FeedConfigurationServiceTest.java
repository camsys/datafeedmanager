package com.camsys.datafeedmanager.service;

import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FeedConfigurationServiceTest {

    @Autowired
    private FeedConfigurationService feedConfigurationService;

    private FeedConfiguration getFeedConfigurationSample(){
        FeedConfiguration feedConfiguration = new FeedConfiguration();
        feedConfiguration.setFeedConfigName("configName");
        feedConfiguration.setBackupDirectory("/backupDir");
        feedConfiguration.setTargetDirectory("/targetDir");
        return feedConfiguration;
    }

    @Test
    public void testFeedConfigurationCRUD() throws ParseException {

        feedConfigurationService.saveFeedConfiguration(getFeedConfigurationSample());

        List<FeedConfiguration> feedConfigurations = feedConfigurationService.getFeedConfigurations();

        assertEquals(1, feedConfigurations.size());

        FeedConfiguration feedConfigurationResult = feedConfigurations.stream().findFirst().get();

        FeedInfo feedInfoResult = feedConfigurationResult.getFeedInfo().stream().findFirst().get();

        assertEquals("Metro Transit", feedInfoResult.getFeedName());

        feedInfoResult.setFeedName("Updated Metro Transit");

        feedConfigurationService.saveFeedConfiguration(feedConfigurationResult);

        List<FeedConfiguration> updatedFeedConfigurations = feedConfigurationService.getFeedConfigurations();

        FeedConfiguration updatedFeedConfigurationResult = updatedFeedConfigurations.stream().findFirst().get();

        FeedInfo updatedFeedInfoResult = updatedFeedConfigurationResult.getFeedInfo().stream().findFirst().get();

        assertEquals("Updated Metro Transit", updatedFeedInfoResult.getFeedName());

    }
}
