package dev.gollund.gitrepoparser.service.client;

import com.spotify.github.v3.repos.ImmutableRepository;
import dev.gollund.gitrepoparser.exception.UserNotFoundException;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Primary
public class GitHubClient {

    private final WebClient webClient;

    public GitHubClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<List<ImmutableRepository>> getRepos(String name) {
        return webClient.get()
                .uri(String.format("/users/%s/repos", name))
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(new ParameterizedTypeReference<>() {
                        });

                    } else if (response.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        throw new UserNotFoundException(name);
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                });
    }
}
