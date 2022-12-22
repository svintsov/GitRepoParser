package dev.gollund.gitrepoparser.service;

import dev.gollund.gitrepoparser.model.UserRepoInfo;
import dev.gollund.gitrepoparser.service.client.GitHubClient;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GithubRepositoryInfoService implements RepositoryInfoService {

    private final GitHubClient gitHubClient;

    public GithubRepositoryInfoService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    @Override
    public List<UserRepoInfo> getAllReposForUser(String name) {
        var s = gitHubClient.getRepos(name).block();
        return Collections.emptyList();
    }
}
