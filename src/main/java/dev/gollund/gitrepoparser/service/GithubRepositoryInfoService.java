package dev.gollund.gitrepoparser.service;

import com.spotify.github.v3.repos.ImmutableBranch;
import dev.gollund.gitrepoparser.model.BranchInfo;
import dev.gollund.gitrepoparser.model.UserRepoInfo;
import dev.gollund.gitrepoparser.service.client.GitHubClient;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GithubRepositoryInfoService implements RepositoryInfoService {

    private final GitHubClient gitHubClient;

    public GithubRepositoryInfoService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    @Override
    public List<UserRepoInfo> getAllReposForUser(String name) {
        var repos = gitHubClient.getRepos(name).block();

        var branches = Flux.fromStream(repos.stream().filter(data -> !data.fork()))
                .flatMapSequential(repo -> convertToUserRepo(repo.owner().login(), repo.name()))
                .collectList()
                .block();
        return Collections.emptyList();
    }

    private Mono<Map<String, List<BranchInfo>>> convertToUserRepo(String login, String name) {
        return convertToBranchInfo(gitHubClient.getBranches(login, name))
                .map(data -> Map.of(name, data));
    }

    private Mono<List<BranchInfo>> convertToBranchInfo(Mono<List<ImmutableBranch>> branches) {
        return branches.flatMapIterable(list -> list)
                .map(branch -> new BranchInfo(branch.name(), branch.commit().sha()))
                .collectList();
    }
}
