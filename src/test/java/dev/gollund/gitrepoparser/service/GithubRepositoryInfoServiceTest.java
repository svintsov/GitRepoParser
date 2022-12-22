package dev.gollund.gitrepoparser.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.gollund.gitrepoparser.model.UserRepoInfo;
import dev.gollund.gitrepoparser.service.client.GitHubClient;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GithubRepositoryInfoServiceTest {

    @Mock
    GitHubClient client;
    @InjectMocks
    RepositoryInfoService underTest;

    @Test
    void whenAnyNamePassedThenReturnEmptyList() {
        //when
        var userName = "SomeUser";
        //then
        List<UserRepoInfo> result = underTest.getAllReposForUser(userName);

        assertTrue(result.isEmpty());
    }
}