package com.camsys.datafeedmanager;

import com.camsys.datafeedmanager.dto.FeedInfoDto;
import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import com.camsys.datafeedmanager.model.entities.FeedInfo;
import com.camsys.datafeedmanager.service.FeedConfigurationService;
import com.camsys.datafeedmanager.service.FeedInfoService;
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

        Assert.assertEquals(2, feedConfigurationService.getFeedConfiguration(1l).getFeedInfo().size());

        String expectedGetOutput = getResourceAsString("feedInfo/output/expectedFeedInfoSaveOutput.json");
        String actualGetOutput = restTemplate.getForObject(feedInfoUrl + "/" + savedFeedInfoId, String.class);

        JSONAssert.assertEquals(expectedGetOutput, actualGetOutput, false);
    }

}
