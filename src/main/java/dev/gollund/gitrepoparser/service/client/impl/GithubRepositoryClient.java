package dev.gollund.gitrepoparser.service.client.impl;

import com.spotify.github.v3.repos.ImmutableRepository;
import dev.gollund.gitrepoparser.exception.UserNotFoundException;
import dev.gollund.gitrepoparser.service.client.GitRepositoryClient;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GithubRepositoryClient implements GitRepositoryClient<ImmutableRepository> {

    private final WebClient webClient;

    public GithubRepositoryClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<List<ImmutableRepository>> getRepos(String username) {
        return webClient.get()
                .uri(String.format("/users/%s/repos", username))
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(new ParameterizedTypeReference<>() {
                        });
                    } else if (response.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        throw new UserNotFoundException(username);
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                });
    }
}
