package dev.gollund.gitrepoparser.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.gollund.gitrepoparser.model.UserRepoInfo;
import java.util.List;
import org.junit.jupiter.api.Test;

class GithubRepositoryInfoServiceTest {

    RepositoryInfoService underTest = new GithubRepositoryInfoService();

    @Test
    void whenAnyNamePassedThenReturnEmptyList() {
        //when
        var userName = "SomeUser";
        //then
        List<UserRepoInfo> result = underTest.getAllReposForUser(userName);

        assertTrue(result.isEmpty());
    }
}