package dev.gollund.gitrepoparser.service.client;

import java.util.List;
import reactor.core.publisher.Mono;

public interface GitRepositoryClient<T> {

    Mono<List<T>> getRepos(String username);

}
