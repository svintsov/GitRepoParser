package dev.gollund.gitrepoparser.controller;

import dev.gollund.gitrepoparser.model.UserRepoInfo;
import dev.gollund.gitrepoparser.service.AcceptHeaderValidator;
import dev.gollund.gitrepoparser.service.GithubRepositoryInfoService;
import dev.gollund.gitrepoparser.service.HeaderValidator;
import dev.gollund.gitrepoparser.service.RepositoryInfoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.HttpHeaders;
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

    @ApiOperation("A main endpoint for receiving a list of repositories"
            + " enriched with a basic information about it's branches")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "406", description = "Invalid Accept header value"),
            @ApiResponse(responseCode = "200", description = "Successful retrieval")})
    @GetMapping(value = "/{username}")
    public List<UserRepoInfo> getRepositoriesInfo(
            @RequestHeader(HttpHeaders.ACCEPT) String acceptHeader,
            @PathVariable String username) {
        headerValidator.accept(acceptHeader);
        return infoService.getAllReposForUser(username);
    }
}
