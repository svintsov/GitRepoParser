package dev.gollund.gitrepoparser.service.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import dev.gollund.gitrepoparser.dataProvider.GitResponseDataProvider;
import dev.gollund.gitrepoparser.exception.UserNotFoundException;
import dev.gollund.gitrepoparser.it.BaseIntegrationTest;
import dev.gollund.gitrepoparser.service.client.impl.GithubRepositoryClient;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class GithubRepositoryClientTest extends BaseIntegrationTest {

    @Autowired
    private GithubRepositoryClient underTest;

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


}