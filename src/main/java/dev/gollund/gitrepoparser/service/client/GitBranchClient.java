package dev.gollund.gitrepoparser.service.client;

import java.util.List;
import reactor.core.publisher.Mono;

public interface GitBranchClient<T> {

    Mono<List<T>> getBranches(String owner, String repoName);

}
