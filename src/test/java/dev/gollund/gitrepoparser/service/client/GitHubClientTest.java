package dev.gollund.gitrepoparser.service.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import com.spotify.github.v3.repos.ImmutableBranch;
import com.spotify.github.v3.repos.ImmutableRepository;
import dev.gollund.gitrepoparser.dataProvider.GitResponseDataProvider;
import dev.gollund.gitrepoparser.dataProvider.JsonReader;
import dev.gollund.gitrepoparser.exception.UserNotFoundException;
import java.util.List;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class GitHubClientTest {

    private static WireMockServer wireMockServer;

    @Autowired
    private GitHubClient underTest;

    @BeforeEach
    public void setUp() {
        wireMockServer.resetAll();
    }

    @Test
    void whenUserExistThenReturnRepositories() {
        var username = "name";
        var expectedResponse = GitResponseDataProvider.readRepoOutputData();
        setupMockForOKRepositories(username, expectedResponse);

        var resultMono = underTest.getRepos(username);
        StepVerifier.create(resultMono)
                .expectNext(expectedResponse)
                .expectComplete()
                .verify();
    }

    @Test
    void whenUserNotExistThenThrowUserNotFoundExceptionException() {
        var username = "name";
        setupMockForNotFoundUser(username);

        var resultMono = underTest.getRepos(username);
        StepVerifier.create(resultMono)
                .expectError(UserNotFoundException.class)
                .verify();
    }

    @Test
    void whenUserExistButServerHasErrorThenThrowException() {
        var username = "name";
        setupMockForServerError(username);

        var resultMono = underTest.getRepos(username);
        StepVerifier.create(resultMono)
                .expectError(WebClientResponseException.class)
                .verify();
    }

    @Test
    void whenBranchesExistThenReturnCollection() {
        var owner = "octocat";
        var repo = "Hello-World";
        var expectedResponse = GitResponseDataProvider.readBranchOutputData();
        setupMockForOKBranches(owner, repo, expectedResponse);

        var resultMono = underTest.getBranches(owner, repo);
        StepVerifier.create(resultMono)
                .expectNext(expectedResponse)
                .expectComplete()
                .verify();
    }

    @Test
    void whenServerHasErrorThenThrowException() {
        var owner = "octocat";
        var repo = "Hello-World";
        setupMockForServerError(owner, repo);

        var resultMono = underTest.getBranches(owner, repo);
        StepVerifier.create(resultMono)
                .expectError(WebClientResponseException.class)
                .verify();
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

    private void setupMockForOKRepositories(String username,
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

    private void setupMockForNotFoundUser(String username) {
        wireMockServer.stubFor(
                WireMock.get(WireMock.urlEqualTo(String.format("/users/%s/repos", username)))
                        .withHeader("Authorization",
                                new EqualToPattern("Bearer accessToken-mocked"))
                        .willReturn(WireMock.aResponse()
                                .withStatus(HttpStatus.SC_NOT_FOUND)));
    }

    private void setupMockForServerError(String username) {
        wireMockServer.stubFor(
                WireMock.get(WireMock.urlEqualTo(String.format("/users/%s/repos", username)))
                        .withHeader("Authorization",
                                new EqualToPattern("Bearer accessToken-mocked"))
                        .willReturn(WireMock.aResponse()
                                .withStatus(HttpStatus.SC_SERVER_ERROR)));
    }

    private void setupMockForOKBranches(String owner, String repoName,
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

    private void setupMockForServerError(String owner, String repoName) {
        wireMockServer.stubFor(
                WireMock.get(WireMock.urlEqualTo(
                                String.format("/repos/%s/%s/branches", owner, repoName)))
                        .withHeader("Authorization",
                                new EqualToPattern("Bearer accessToken-mocked"))
                        .willReturn(WireMock.aResponse()
                                .withStatus(HttpStatus.SC_SERVER_ERROR)));
    }

}