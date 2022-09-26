package com.camsys.datafeedmanager;

import com.camsys.datafeedmanager.dto.FeedInfoDto;
import com.camsys.datafeedmanager.dto.RealtimeDataInfoDto;
import com.camsys.datafeedmanager.dto.TransitDataInfoDto;
import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.model.entities.RealtimeDataInfo;
import com.camsys.datafeedmanager.model.entities.TransitDataInfo;
import com.camsys.datafeedmanager.service.FeedConfigurationService;
import com.camsys.datafeedmanager.service.FeedInfoService;
import com.camsys.datafeedmanager.service.conversion.RealtimeDataInfoDtoConversionService;
import com.camsys.datafeedmanager.service.conversion.TransitDataInfoDtoConversionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static data.FeedConfigMockData.getFeedConfigurationSample;
import static util.TestUtil.getResourceAsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class FeedInfoControllerTest {

    @LocalServerPort
    private int port;

    private static final String host = "http://localhost";

    private static final String path = "/api/feed/feed-info";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FeedConfigurationService feedConfigurationService;

    @Autowired
    private FeedInfoService feedInfoService;

    @Autowired
    private RealtimeDataInfoDtoConversionService realtimeDataInfoDtoConversionService;

    @Autowired
    private TransitDataInfoDtoConversionService transitDataInfoDtoConversionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void feedInfoRestDeleteTest() throws Exception {
        String feedInfoUrl = host + ":" + port + path;

        Assert.assertEquals(0, feedConfigurationService.getFeedConfigurations().size());

        Long feedConfigId = feedConfigurationService.saveFeedConfiguration(getFeedConfigurationSample());

        FeedConfiguration feedConfiguration = feedConfigurationService.getFeedConfiguration(feedConfigId);

        Assert.assertEquals(1, feedConfigurationService.getFeedConfiguration(feedConfigId).getFeedInfo().size());

        FeedInfo feedInfo = feedConfiguration.getFeedInfo().stream().findFirst().orElseThrow();

        Assert.assertNotNull(feedInfo);

        restTemplate.getForObject(feedInfoUrl + "/delete/" + feedInfo.getId(), String.class);

        List<FeedInfo> feedInfoList = feedInfoService.getFeedInfos();

        Assert.assertEquals(0, feedInfoService.getFeedInfos().size());

        Assert.assertEquals(0, feedConfigurationService.getFeedConfigurations().stream().findFirst().get().getFeedInfo().size());

    }

    @Test
    public void feedInfoGetTest() throws Exception {
        // REST URL
        String feedInfoUrl = host + ":" + port + path;

        // Setting up FeedConfiguration Data Prereq
        feedConfigurationService.saveFeedConfiguration(getFeedConfigurationSample());

        String expectedFeedInfoListOutput = getResourceAsString("feedInfo/output/expectedFeedInfoGetOutput.json");

        String actualFeedInfoListOutput = restTemplate.getForObject(feedInfoUrl + "/2", String.class);

        JSONAssert.assertEquals(expectedFeedInfoListOutput, actualFeedInfoListOutput, false);
    }

    @Test
    public void feedInfoUpdateTest() throws Exception {
        // REST URL
        String feedInfoUrl = host + ":" + port + path;

        Assert.assertEquals(0, feedConfigurationService.getFeedConfigurations().size());

        feedConfigurationService.saveFeedConfiguration(getFeedConfigurationSample());

        String feedInfoJson = restTemplate.getForObject(feedInfoUrl + "/" + 2, String.class);

        FeedInfoDto feedInfo = objectMapper.readValue(feedInfoJson, FeedInfoDto.class);

        feedInfo.setFeedName("Metro Transit Updated");
        feedInfo.setEnabled(true);
        feedInfo.getTransitDataInfo().get(0).setTargetName("mtgtfs_updated.zip");
        feedInfo.getRealtimeDataInfo().get(0).setDescription("updated description");

        String updatedFeedInfoInput = objectMapper.writeValueAsString(feedInfo);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<>(updatedFeedInfoInput, headers);

        restTemplate.postForObject(feedInfoUrl + "/save", request, String.class);

        Assert.assertEquals(1, feedConfigurationService.getFeedConfigurations().size());

        String expectedGetOutput = getResourceAsString("feedInfo/output/expectedFeedInfoUpdateOutput.json");
        String actualGetOutput = restTemplate.getForObject(feedInfoUrl + "/2", String.class);

        JSONAssert.assertEquals(expectedGetOutput, actualGetOutput, false);
    }

    @Ignore
    public void feedInfoListOutputTest() throws Exception {
        String feedInfoUrl = host + ":" + port + path;

        feedConfigurationService.saveFeedConfiguration(getFeedConfigurationSample());

        String expectedFeedInfoListOutput = getResourceAsString("feedInfo/output/expectedFeedInfoListOutput.json");

        String actualFeedInfoListOutput = restTemplate.getForObject(feedInfoUrl + "/list", String.class);

        System.out.println(actualFeedInfoListOutput);

        JSONAssert.assertEquals(expectedFeedInfoListOutput, actualFeedInfoListOutput, false);

    }

    @Test
    public void feedInfoSaveGetTest() throws Exception {
        String feedInfoUrl = host + ":" + port + path;

        // Setting up FeedConfiguration Data Prereq
        Long feedConfigId = feedConfigurationService.saveFeedConfiguration(getFeedConfigurationSample());

        Assert.assertEquals(1, feedConfigurationService.getFeedConfiguration(feedConfigId).getFeedInfo().size());

        String feedConfigInput = getResourceAsString("feedInfo/input/feedInfoInput.json");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<>(feedConfigInput, headers);

        String savedFeedInfoId = restTemplate.postForObject(feedInfoUrl + "/save", request, String.class);

        Assert.assertEquals(2, feedConfigurationService.getFeedConfiguration(feedConfigId).getFeedInfo().size());

        String expectedGetOutput = getResourceAsString("feedInfo/output/expectedFeedInfoSaveOutput.json");
        String actualGetOutput = restTemplate.getForObject(feedInfoUrl + "/" + savedFeedInfoId, String.class);

        JSONAssert.assertEquals(expectedGetOutput, actualGetOutput, false);
    }

    @Test
    public void realTimeDataInfoSaveGetTest() throws Exception {
        String feedInfoUrl = host + ":" + port + path;

        // Setting up FeedConfiguration Data Prereq
        Long feedConfigId = feedConfigurationService.saveFeedConfiguration(getFeedConfigurationSample());

        FeedConfiguration feedConfigurationPreUpdate = feedConfigurationService.getFeedConfiguration(feedConfigId);

        FeedInfo feedInfoPreUpdate = feedConfigurationPreUpdate.getFeedInfo().stream().findFirst().get();

        Long feedInfoId = feedInfoPreUpdate.getId();

        Assert.assertEquals(2, feedInfoPreUpdate.getRealtimeDataInfo().size());

        String realtimeDataInfoInput = getResourceAsString("realtimeDataInfo/input/realtimeDataInfoInput.json");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(realtimeDataInfoInput, headers);

        restTemplate.postForObject(feedInfoUrl + "/" + feedInfoId + "/realtime-data/save", request, String.class);

        FeedConfiguration feedConfigurationPostSave = feedConfigurationService.getFeedConfiguration(feedConfigId);

        FeedInfo feedInfoPostSave = feedConfigurationPostSave.getFeedInfo().stream().findFirst().get();

        Assert.assertEquals(3, feedInfoPostSave.getRealtimeDataInfo().size());

        String expectedGetOutput = getResourceAsString("realtimeDataInfo/output/expectedRealtimeDataInfoSaveOutput.json");

        String actualGetOutput = restTemplate.getForObject(feedInfoUrl + "/" + feedInfoId, String.class);

        JSONAssert.assertEquals(expectedGetOutput, actualGetOutput, false);
    }

    @Test
    public void realTimeDataInfoUpdateGetTest() throws Exception {
        String feedInfoUrl = host + ":" + port + path;

        // Setting up FeedConfiguration Data Prereq
        Long feedConfigId = feedConfigurationService.saveFeedConfiguration(getFeedConfigurationSample());

        FeedConfiguration feedConfigurationPreSave = feedConfigurationService.getFeedConfiguration(feedConfigId);

        FeedInfo feedInfoPreSave = feedConfigurationPreSave.getFeedInfo().stream().findFirst().get();

        Long feedInfoId = feedInfoPreSave.getId();

        Assert.assertEquals(2, feedInfoPreSave.getRealtimeDataInfo().size());

        RealtimeDataInfo realtimeDataInfo = feedInfoPreSave.getRealtimeDataInfo().stream().findFirst().get();

        RealtimeDataInfoDto realtimeDataInfoDto = realtimeDataInfoDtoConversionService.convertToDto(realtimeDataInfo);

        realtimeDataInfoDto.setName("MT TripUpdates Feed Updated");

        realtimeDataInfoDto.setDescription("Updated Description");

        realtimeDataInfoDto.setFeedURI("https://updated-url/mtgtfs/tripupdates.pb");

        String realtimeDataInfoInput = objectMapper.writeValueAsString(realtimeDataInfoDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(realtimeDataInfoInput, headers);

        restTemplate.postForObject(feedInfoUrl + "/" + feedInfoId + "/realtime-data/save", request, String.class);

        FeedConfiguration feedConfigurationPostSave = feedConfigurationService.getFeedConfiguration(feedConfigId);

        FeedInfo feedInfoPostSave = feedConfigurationPostSave.getFeedInfo().stream().findFirst().get();

        Assert.assertEquals(2, feedInfoPostSave.getRealtimeDataInfo().size());

        String expectedGetOutput = getResourceAsString("realtimeDataInfo/output/expectedRealtimeDataInfoUpdateOutput.json");

        String actualGetOutput = restTemplate.getForObject(feedInfoUrl + "/" + feedInfoId, String.class);

        JSONAssert.assertEquals(expectedGetOutput, actualGetOutput, false);
    }

    @Test
    public void transitDataInfoSaveGetTest() throws Exception {
        String feedInfoUrl = host + ":" + port + path;

        // Setting up FeedConfiguration Data Prereq
        Long feedConfigId = feedConfigurationService.saveFeedConfiguration(getFeedConfigurationSample());

        FeedConfiguration feedConfigurationPreUpdate = feedConfigurationService.getFeedConfiguration(feedConfigId);

        FeedInfo feedInfoPreUpdate = feedConfigurationPreUpdate.getFeedInfo().stream().findFirst().get();

        Long feedInfoId = feedInfoPreUpdate.getId();

        Assert.assertEquals(1, feedInfoPreUpdate.getTransitDataInfo().size());

        String transitDataInfoInput = getResourceAsString("transitDataInfo/input/transitDataInfoInput.json");

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(transitDataInfoInput, headers);

        restTemplate.postForObject(feedInfoUrl + "/" + feedInfoId + "/transit-data/save", request, String.class);

        FeedConfiguration feedConfigurationPostSave = feedConfigurationService.getFeedConfiguration(feedConfigId);

        FeedInfo feedInfoPostSave = feedConfigurationPostSave.getFeedInfo().stream().findFirst().get();

        Assert.assertEquals(2, feedInfoPostSave.getTransitDataInfo().size());

        String expectedGetOutput = getResourceAsString("transitDataInfo/output/expectedTransitDataInfoSaveOutput.json");

        String actualGetOutput = restTemplate.getForObject(feedInfoUrl + "/" + feedInfoId, String.class);

        JSONAssert.assertEquals(expectedGetOutput, actualGetOutput, false);
    }

    @Test
    public void transitTimeDataInfoUpdateGetTest() throws Exception {
        String feedInfoUrl = host + ":" + port + path;

        // Setting up FeedConfiguration Data Prereq
        Long feedConfigId = feedConfigurationService.saveFeedConfiguration(getFeedConfigurationSample());

        FeedConfiguration feedConfigurationPreSave = feedConfigurationService.getFeedConfiguration(feedConfigId);

        FeedInfo feedInfoPreSave = feedConfigurationPreSave.getFeedInfo().stream().findFirst().get();

        Long feedInfoId = feedInfoPreSave.getId();

        Assert.assertEquals(1, feedInfoPreSave.getTransitDataInfo().size());

        TransitDataInfo transitDataInfo = feedInfoPreSave.getTransitDataInfo().stream().findFirst().get();

        TransitDataInfoDto transitDataInfoDto = transitDataInfoDtoConversionService.convertToDto(transitDataInfo);

        transitDataInfoDto.setName("Transit Data File Feed Update");

        transitDataInfoDto.setDescription("Updated Description");

        transitDataInfoDto.setSourceURI("https://svc.metrotransit.org/mtgtfs/googleUpdated.zip");

        transitDataInfoDto.setTargetName("mtgtfs_updated.zip");

        transitDataInfoDto.setMerge(false);

        String transitDataInfoInput = objectMapper.writeValueAsString(transitDataInfoDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(transitDataInfoInput, headers);

        restTemplate.postForObject(feedInfoUrl + "/" + feedInfoId + "/transit-data/save", request, String.class);

        FeedConfiguration feedConfigurationPostSave = feedConfigurationService.getFeedConfiguration(feedConfigId);

        FeedInfo feedInfoPostSave = feedConfigurationPostSave.getFeedInfo().stream().findFirst().get();

        Assert.assertEquals(1, feedInfoPostSave.getTransitDataInfo().size());

        String expectedGetOutput = getResourceAsString("transitDataInfo/output/expectedTransitDataInfoUpdateOutput.json");

        String actualGetOutput = restTemplate.getForObject(feedInfoUrl + "/" + feedInfoId, String.class);

        JSONAssert.assertEquals(expectedGetOutput, actualGetOutput, false);
    }

}
