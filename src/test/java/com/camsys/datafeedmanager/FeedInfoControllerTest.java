package com.camsys.datafeedmanager;

import com.camsys.datafeedmanager.service.FeedConfigurationService;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.camsys.datafeedmanager.FeedConfigurationServiceTest.getFeedConfigurationSample;
import static java.nio.charset.StandardCharsets.UTF_8;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FeedConfigurationControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FeedConfigurationService feedConfigurationService;

    private String getResourceAsString(String resourceName) throws IOException {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourceName)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            inputStream.transferTo(bos);
            String content = bos.toString(UTF_8);
            return content;
        } catch (Exception e){
            throw e;
        }
    }

    @Test
    public void feedConfigurationRestOutput() throws Exception {
        String feedConfigurationsUrl = "http://localhost:" + port + "/feed/configurations";

        feedConfigurationService.saveFeedConfiguration(getFeedConfigurationSample());

        String expectedListOutput = getResourceAsString("feedConfiguration/expectedFeedConfigListOutput.json");
        String actualListOutput = restTemplate.getForObject(feedConfigurationsUrl + "/list", String.class);

        JSONAssert.assertEquals(expectedListOutput, actualListOutput, false);

        String expectedGetOutput = getResourceAsString("feedConfiguration/expectedFeedConfigGetOutput.json");
        String actualGetOutput = restTemplate.getForObject(feedConfigurationsUrl + "/1", String.class);

        JSONAssert.assertEquals(expectedGetOutput, actualGetOutput, false);

        String expectedFeedInfoListOutput = getResourceAsString("feedConfiguration/expectedFeedInfoListOutput.json");

        String actualFeedInfoListOutput = restTemplate.getForObject(feedConfigurationsUrl + "/1/feed-info/list", String.class);

        JSONAssert.assertEquals(expectedFeedInfoListOutput, actualFeedInfoListOutput, false);
    }
}
