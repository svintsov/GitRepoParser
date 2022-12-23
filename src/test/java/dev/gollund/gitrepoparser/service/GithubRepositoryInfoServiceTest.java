package dev.gollund.gitrepoparser.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.gollund.gitrepoparser.dataProvider.GitResponseDataProvider;
import dev.gollund.gitrepoparser.dataProvider.OutputDataProvider;
import dev.gollund.gitrepoparser.model.UserRepoInfo;
import dev.gollund.gitrepoparser.service.client.GitHubClient;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class GithubRepositoryInfoServiceTest {

    @Mock
    GitHubClient client;
    @InjectMocks
    GithubRepositoryInfoService underTest;

    @Test
    void whenExistingUserThenReturnListOfRepoBranchInfo() {
        var username = "octocat";
        var expectedResult = OutputDataProvider.readOutputData();

        when(client.getRepos(username))
                .thenReturn(Mono.just(GitResponseDataProvider.readRepoOutputData()));
        when(client.getBranches("octocat", "Hello-World"))
                .thenReturn(Mono.just(GitResponseDataProvider.readBranchOutputData()));

        List<UserRepoInfo> result = underTest.getAllReposForUser(username);

        assertEquals(result.size(), 1);
        assertEquals(expectedResult.get(0), result.get(0));
    }

    @Test
    void whenExistingUserButNoReposThenReturnEmptyList() {
        var username = "octocat";
        when(client.getRepos(username))
                .thenReturn(Mono.just(Collections.emptyList()));

        List<UserRepoInfo> result = underTest.getAllReposForUser(username);

        assertTrue(result.isEmpty());
        verify(client, never()).getBranches(anyString(), anyString());
    }

    @Test
    void whenExistingUserHasReposButFetchingBranchesFailsThenThrowException() {
        var username = "octocat";

        when(client.getRepos(username))
                .thenReturn(Mono.just(GitResponseDataProvider.readRepoOutputData()));
        when(client.getBranches("octocat", "Hello-World"))
                .thenThrow(WebClientResponseException.class);

        assertThrows(WebClientResponseException.class,
                () -> underTest.getAllReposForUser(username));
    }

    @Test()
    void whenUserIsNullThenThrowNPE() {
        assertThrows(NullPointerException.class, () -> underTest.getAllReposForUser(null));
    }

}