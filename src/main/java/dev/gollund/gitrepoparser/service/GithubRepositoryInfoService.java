package dev.gollund.gitrepoparser.service;

import com.spotify.github.v3.repos.ImmutableBranch;
import com.spotify.github.v3.repos.ImmutableRepository;
import dev.gollund.gitrepoparser.model.BranchInfo;
import dev.gollund.gitrepoparser.model.UserRepoInfo;
import dev.gollund.gitrepoparser.service.client.GitBranchClient;
import dev.gollund.gitrepoparser.service.client.GitRepositoryClient;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GithubRepositoryInfoService implements RepositoryInfoService {

    private final GitRepositoryClient<ImmutableRepository> githubRepositoryClient;
    private final GitBranchClient<ImmutableBranch> githubBranchClient;

    public GithubRepositoryInfoService(
            GitRepositoryClient<ImmutableRepository> gitHubClient,
            GitBranchClient<ImmutableBranch> githubBranchClient) {
        this.githubRepositoryClient = gitHubClient;
        this.githubBranchClient = githubBranchClient;
    }

    @Override
    public List<UserRepoInfo> getAllReposForUser(String name) {
        Objects.requireNonNull(name, "Name must not be null to make a call");
        long startTime = System.nanoTime();
        var repos = githubRepositoryClient.getRepos(name)
                .blockOptional()
                .orElse(Collections.emptyList());

        List<UserRepoInfo> userRepoInfo = getUserRepoInfos(repos);
        long endTime = System.nanoTime();
        System.out.println("Elapsed time: " + (endTime - startTime));
        return userRepoInfo;
    }

    private List<UserRepoInfo> getUserRepoInfos(List<ImmutableRepository> repos) {
        return Flux.fromIterable(repos)
                .filter(data -> Boolean.FALSE.equals(data.fork()))
                .flatMap(repo -> makeCallForBranchesAndFillUserRepoInfo(
                        repo.owner() != null ? repo.owner().login() : null,
                        repo.name()))
                .collectList()
                .block();
    }

    private Mono<UserRepoInfo> makeCallForBranchesAndFillUserRepoInfo(String login, String name) {
        Objects.requireNonNull(login, "Login must not be null to make a call");
        Objects.requireNonNull(name, "Name must not be null to make a call");
        return convertToBranchInfo(githubBranchClient.getBranches(login, name))
                .map(data -> new UserRepoInfo(name, login, data));
    }

    private Mono<List<BranchInfo>> convertToBranchInfo(Mono<List<ImmutableBranch>> branches) {
        return branches.flatMapIterable(list -> list)
                .map(branch ->
                        new BranchInfo(
                                branch.name(),
                                branch.commit() != null ? branch.commit().sha() : null))
                .collectList();
    }
}
