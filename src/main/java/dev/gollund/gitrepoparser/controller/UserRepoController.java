package dev.gollund.gitrepoparser.controller;

import dev.gollund.gitrepoparser.model.UserRepoInfo;
import dev.gollund.gitrepoparser.service.AcceptHeaderValidator;
import dev.gollund.gitrepoparser.service.GithubRepositoryInfoService;
import dev.gollund.gitrepoparser.service.HeaderValidator;
import dev.gollund.gitrepoparser.service.RepositoryInfoService;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/repositories")
public class UserRepoController {

    private final RepositoryInfoService infoService;
    private final HeaderValidator headerValidator;

    UserRepoController(GithubRepositoryInfoService infoService, AcceptHeaderValidator validator) {
        this.infoService = infoService;
        this.headerValidator = validator;
    }

    @GetMapping(value = "/{username}")
    public List<UserRepoInfo> getRepositoriesInfo(
            @RequestHeader(HttpHeaders.ACCEPT) String acceptHeader,
            @PathVariable String username) {
        headerValidator.accept(acceptHeader);
        return infoService.getAllReposForUser(username);
    }
}
