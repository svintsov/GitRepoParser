package dev.gollund.gitrepoparser.it;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import com.spotify.github.v3.repos.ImmutableBranch;
import com.spotify.github.v3.repos.ImmutableRepository;
import dev.gollund.gitrepoparser.dataProvider.JsonReader;
import java.util.List;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

@SpringBootTest
public class BaseIntegrationTest {

    protected static WireMockServer wireMockServer;

    protected void setupMockForOKRepositories(String username,
            List<ImmutableRepository> expectedResponse) {
        wireMockServer.stubFor(
                WireMock.get(WireMock.urlEqualTo(String.format("/users/%s/repos", username)))
                        .withHeader("Authorization",
                                new EqualToPattern("Bearer accessToken-mocked"))
                        .willReturn(WireMock.aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                                .withStatus(HttpStatus.SC_OK)
                                .withBody(JsonReader.convertObjectToString(expectedResponse))));
    }

    protected void setupMockForOKBranches(String owner, String repoName,
            List<ImmutableBranch> expectedBranches) {
        wireMockServer.stubFor(
                WireMock.get(WireMock.urlEqualTo(
                                String.format("/repos/%s/%s/branches", owner, repoName)))
                        .withHeader("Authorization",
                                new EqualToPattern("Bearer accessToken-mocked"))
                        .willReturn(WireMock.aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                                .withStatus(HttpStatus.SC_OK)
                                .withBody(JsonReader.convertObjectToString(expectedBranches))));
    }

    @BeforeAll
    static void setupWiremock() {
        wireMockServer = new WireMockServer(8090);
        wireMockServer.start();
    }

    @AfterAll
    static void teardown() {
        wireMockServer.stop();
    }

    @BeforeEach
    public void setUp() {
        wireMockServer.resetAll();
    }
}
