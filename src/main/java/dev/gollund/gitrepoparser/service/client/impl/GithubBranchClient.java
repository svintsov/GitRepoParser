package dev.gollund.gitrepoparser.service.client.impl;

import com.spotify.github.v3.repos.ImmutableBranch;
import dev.gollund.gitrepoparser.service.client.GitBranchClient;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GithubBranchClient implements GitBranchClient<ImmutableBranch> {

    private final WebClient webClient;

    public GithubBranchClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<List<ImmutableBranch>> getBranches(String owner, String repoName) {
        return webClient.get()
                .uri(String.format("/repos/%s/%s/branches", owner, repoName))
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(new ParameterizedTypeReference<>() {
                        });
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                });
    }
}
