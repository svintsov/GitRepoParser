package dev.gollund.gitrepoparser.service.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import dev.gollund.gitrepoparser.dataProvider.GitResponseDataProvider;
import dev.gollund.gitrepoparser.it.BaseIntegrationTest;
import dev.gollund.gitrepoparser.service.client.impl.GithubBranchClient;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class GithubBranchClientTest extends BaseIntegrationTest {

    @Autowired
    private GithubBranchClient underTest;

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
