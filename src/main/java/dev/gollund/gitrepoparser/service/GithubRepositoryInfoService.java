package dev.gollund.gitrepoparser.service;

import dev.gollund.gitrepoparser.model.UserRepoInfo;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GithubRepositoryInfoService implements RepositoryInfoService {

    @Override
    public List<UserRepoInfo> getAllReposForUser(String name) {
        return Collections.emptyList();
    }
}
