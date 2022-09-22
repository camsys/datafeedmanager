package com.camsys.datafeedmanager;

import com.camsys.datafeedmanager.dto.FeedConfigurationDto;
import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import com.camsys.datafeedmanager.service.FeedConfigurationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
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

import static data.FeedConfigMockData.getFeedConfigurationSample;
import static util.TestUtil.getResourceAsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class FeedConfigurationControllerTest {

    @LocalServerPort
    private int port;

    private static final String host = "http://localhost";

    private static final String path = "/api/feed/configurations";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FeedConfigurationService feedConfigurationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void feedConfigurationRestDeleteTest() throws Exception {
        String feedConfigurationsUrl = host + ":" + port + path;

        Assert.assertEquals(0, feedConfigurationService.getFeedConfigurations().size());

        Long feedConfigId = feedConfigurationService.saveFeedConfiguration(getFeedConfigurationSample());

        Assert.assertEquals(1, feedConfigurationService.getFeedConfigurations().size());

        restTemplate.getForObject(feedConfigurationsUrl + "/delete/" + feedConfigId, String.class);

        Assert.assertEquals(0, feedConfigurationService.getFeedConfigurations().size());

    }

    @Test
    public void feedConfigurationSaveGetTest() throws Exception {
        String feedConfigurationsUrl = host + ":" + port + path;

        Assert.assertEquals(0, feedConfigurationService.getFeedConfigurations().size());

        String feedConfigInput = getResourceAsString("feedConfiguration/input/feedConfigSaveInput.json");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<>(feedConfigInput, headers);

        restTemplate.postForObject(feedConfigurationsUrl + "/save", request, String.class);

        Assert.assertEquals(1, feedConfigurationService.getFeedConfigurations().size());

        String expectedGetOutput = getResourceAsString("feedConfiguration/output/expectedFeedConfigGetOutput.json");
        String actualGetOutput = restTemplate.getForObject(feedConfigurationsUrl + "/1", String.class);

        JSONAssert.assertEquals(expectedGetOutput, actualGetOutput, false);
    }

    @Test
    public void feedConfigurationUpdateTest() throws Exception {
        String feedConfigurationsUrl = host + ":" + port + path;

        Assert.assertEquals(0, feedConfigurationService.getFeedConfigurations().size());

        Long feedConfigId = feedConfigurationService.saveFeedConfiguration(getFeedConfigurationSample());

        String feedConfigJson = restTemplate.getForObject(feedConfigurationsUrl + "/" + feedConfigId, String.class);

        FeedConfigurationDto feedConfiguration = objectMapper.readValue(feedConfigJson, FeedConfigurationDto.class);

        feedConfiguration.setFeedConfigName("updatedConfigName");

        String updatedFeedConfigInput = objectMapper.writeValueAsString(feedConfiguration);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<>(updatedFeedConfigInput, headers);

        restTemplate.postForObject(feedConfigurationsUrl + "/save", request, String.class);

        Assert.assertEquals(1, feedConfigurationService.getFeedConfigurations().size());

        String expectedGetOutput = getResourceAsString("feedConfiguration/output/expectedFeedConfigUpdateOutput.json");
        String actualGetOutput = restTemplate.getForObject(feedConfigurationsUrl + "/1", String.class);

        JSONAssert.assertEquals(expectedGetOutput, actualGetOutput, false);
    }

    @Test
    public void feedConfigurationFeedInfoListTest() throws Exception {
        String feedConfigurationsUrl = host + ":" + port + path;

        feedConfigurationService.saveFeedConfiguration(getFeedConfigurationSample());

        String expectedListOutput = getResourceAsString("feedConfiguration/output/expectedFeedConfigListOutput.json");
        String actualListOutput = restTemplate.getForObject(feedConfigurationsUrl + "/list", String.class);

        JSONAssert.assertEquals(expectedListOutput, actualListOutput, false);

        String expectedGetOutput = getResourceAsString("feedConfiguration/output/expectedFeedConfigGetOutput.json");
        String actualGetOutput = restTemplate.getForObject(feedConfigurationsUrl + "/1", String.class);

        JSONAssert.assertEquals(expectedGetOutput, actualGetOutput, false);

        String expectedFeedInfoListOutput = getResourceAsString("feedConfiguration/output/expectedFeedInfoListOutput.json");

        String actualFeedInfoListOutput = restTemplate.getForObject(feedConfigurationsUrl + "/1/feed-info/list", String.class);

        JSONAssert.assertEquals(expectedFeedInfoListOutput, actualFeedInfoListOutput, false);

    }
}
