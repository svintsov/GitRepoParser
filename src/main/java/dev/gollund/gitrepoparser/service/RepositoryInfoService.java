package dev.gollund.gitrepoparser.service;

import dev.gollund.gitrepoparser.model.UserRepoInfo;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface RepositoryInfoService {

    List<UserRepoInfo> getAllReposForUser(String name);
}
