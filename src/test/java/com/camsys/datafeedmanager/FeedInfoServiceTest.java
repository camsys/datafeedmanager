package com.camsys.datafeedmanager;

import com.camsys.datafeedmanager.model.RealtimeFeedType;
import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.model.entities.RealtimeDataInfo;
import com.camsys.datafeedmanager.model.entities.TransitDataInfo;
import com.camsys.datafeedmanager.service.FeedConfigurationService;
import com.camsys.datafeedmanager.service.FeedInfoService;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class FeedConfigurationServiceTest {

    @Autowired
    private FeedConfigurationService feedConfigurationService;

    @Autowired
    private FeedInfoService feedInfoService;

    public static FeedConfiguration getFeedConfigurationSample(){
        FeedInfo feedInfo = new FeedInfo();
        feedInfo.setAgency("Minneapolis Metro Transit");
        feedInfo.setFeedName("Metro Transit");
        feedInfo.setServiceName("Minnesota Buses");

        RealtimeDataInfo realtimeDataInfoTripUpdates = new RealtimeDataInfo();
        realtimeDataInfoTripUpdates.setName("MT TripUpdates Feed");
        realtimeDataInfoTripUpdates.setFeedURI("https://svc.metrotransit.org/mtgtfs/tripupdates.pb");
        realtimeDataInfoTripUpdates.setFeedType(RealtimeFeedType.GTFSRT);
        realtimeDataInfoTripUpdates.setFeedInfo(feedInfo);

        RealtimeDataInfo realtimeDataInfoVehiclePositions = new RealtimeDataInfo();
        realtimeDataInfoVehiclePositions.setName("MT VehiclePositions Feed");
        realtimeDataInfoVehiclePositions.setFeedURI("https://svc.metrotransit.org/mtgtfs/vehiclepositions.pb");
        realtimeDataInfoVehiclePositions.setFeedType(RealtimeFeedType.GTFSRT);
        realtimeDataInfoVehiclePositions.setFeedInfo(feedInfo);

        List<RealtimeDataInfo> realtimeData = new ArrayList<>();
        realtimeData.add(realtimeDataInfoTripUpdates);
        realtimeData.add(realtimeDataInfoVehiclePositions);

        TransitDataInfo transitDataInfo = new TransitDataInfo();
        transitDataInfo.setName("MT TripUpdates Feed");
        transitDataInfo.setSourceURI("https://svc.metrotransit.org/mtgtfs/tripupdates.pb");
        transitDataInfo.setMerge(false);
        transitDataInfo.setTargetName("mtgtfs.zip");
        transitDataInfo.setFeedInfo(feedInfo);

        List<TransitDataInfo> transitData = new ArrayList<>();
        transitData.add(transitDataInfo);

        feedInfo.setRealtimeDataInfo(realtimeData);
        feedInfo.setTransitDataInfo(transitData);

        FeedConfiguration feedConfiguration = new FeedConfiguration();
        feedConfiguration.setFeedConfigName("configName");
        feedConfiguration.setBackupDirectory("/backupDir");
        feedConfiguration.setTargetDirectory("/targetDir");
        feedConfiguration.addFeedInfo(feedInfo);

        feedInfo.setFeedConfiguration(feedConfiguration);

        return feedConfiguration;
    }

    @Test
    public void feedConfigurationCRUDTest() throws ParseException {
        // Create Test
        Long savedFeedConfigurationId = feedConfigurationService.saveFeedConfiguration(getFeedConfigurationSample());

        assertNotNull(savedFeedConfigurationId);

        // Read Test
        List<FeedConfiguration> feedConfigurations = feedConfigurationService.getFeedConfigurations();

        assertEquals(1, feedConfigurations.size());

        // Update Test
        FeedConfiguration feedConfigurationResult = feedConfigurations.stream().findFirst().get();

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
