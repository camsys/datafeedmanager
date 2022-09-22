package data;

import com.camsys.datafeedmanager.model.RealtimeFeedType;
import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.model.entities.RealtimeDataInfo;
import com.camsys.datafeedmanager.model.entities.TransitDataInfo;

import java.util.ArrayList;
import java.util.List;

public class FeedConfigMockData {
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

}
