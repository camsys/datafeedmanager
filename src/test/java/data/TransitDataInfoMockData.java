package data;

import com.camsys.datafeedmanager.model.entities.TransitDataInfo;

public class TransitDataInfoMockData {

    public static TransitDataInfo getTransitDataInfoSample(){

        TransitDataInfo transitDataInfo = new TransitDataInfo();
        transitDataInfo.setName("TransitDataInfo GTFS File");
        transitDataInfo.setSourceURI("https://file/mndot.zip");
        transitDataInfo.setTargetName("new_file.zip");
        transitDataInfo.setMerge(false);
        transitDataInfo.setDescription("Description");

        return transitDataInfo;
    }
}
