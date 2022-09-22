package data;

import com.camsys.datafeedmanager.model.RealtimeFeedType;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.model.entities.RealtimeDataInfo;
import com.camsys.datafeedmanager.model.entities.TransitDataInfo;

import java.util.ArrayList;
import java.util.List;

public class FeedInfoMockData {
    public static FeedInfo getFeedInfoSample(){
        FeedInfo feedInfo = new FeedInfo();
        feedInfo.setAgency("Minneapolis Metro Transit 2");
        feedInfo.setFeedName("Metro Transit 2");
        feedInfo.setServiceName("Minnesota Shuttles");
        feedInfo.setEnabled(true);

        RealtimeDataInfo realtimeDataInfoTripUpdates = new RealtimeDataInfo();
        realtimeDataInfoTripUpdates.setName("MT Shuttles TripUpdates Feed");
        realtimeDataInfoTripUpdates.setFeedURI("https://sample-url/mtgtfs/tripupdates.pb");
        realtimeDataInfoTripUpdates.setFeedType(RealtimeFeedType.GTFSRT);
        realtimeDataInfoTripUpdates.setFeedInfo(feedInfo);

        RealtimeDataInfo realtimeDataInfoVehiclePositions = new RealtimeDataInfo();
        realtimeDataInfoVehiclePositions.setName("MT Shuttles VehiclePositions Feed");
        realtimeDataInfoVehiclePositions.setFeedURI("https://sample-url/mtgtfs/vehiclepositions.pb");
        realtimeDataInfoVehiclePositions.setFeedType(RealtimeFeedType.GTFSRT);
        realtimeDataInfoVehiclePositions.setFeedInfo(feedInfo);

        List<RealtimeDataInfo> realtimeData = new ArrayList<>();
        realtimeData.add(realtimeDataInfoTripUpdates);
        realtimeData.add(realtimeDataInfoVehiclePositions);

        TransitDataInfo transitDataInfo = new TransitDataInfo();
        transitDataInfo.setName("MT Shuttles GTFS");
        transitDataInfo.setSourceURI("https://sample-url/mtgtfs/shuttles.zip");
        transitDataInfo.setMerge(false);
        transitDataInfo.setTargetName("shuttles.zip");
        transitDataInfo.setFeedInfo(feedInfo);

        List<TransitDataInfo> transitData = new ArrayList<>();
        transitData.add(transitDataInfo);

        feedInfo.setRealtimeDataInfo(realtimeData);
        feedInfo.setTransitDataInfo(transitData);

        return feedInfo;
    }
}
