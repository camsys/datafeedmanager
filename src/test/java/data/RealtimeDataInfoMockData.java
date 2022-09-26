package data;

import com.camsys.datafeedmanager.model.RealtimeFeedType;
import com.camsys.datafeedmanager.model.entities.RealtimeDataInfo;

public class RealtimeDataInfoMockData {

    public static RealtimeDataInfo getRealtimeDataInfoSample(){

        RealtimeDataInfo realtimeDataInfoTripUpdates = new RealtimeDataInfo();
        realtimeDataInfoTripUpdates.setName("RealtimeData TripUpdates Feed");
        realtimeDataInfoTripUpdates.setFeedURI("https://realtimedatainfo/tripupdates.pb");
        realtimeDataInfoTripUpdates.setFeedType(RealtimeFeedType.GTFSRT);

        return realtimeDataInfoTripUpdates;
    }
}
