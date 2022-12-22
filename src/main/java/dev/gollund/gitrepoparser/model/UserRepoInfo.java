package dev.gollund.gitrepoparser.model;

import java.util.List;

public record UserRepoInfo(String name, String ownerName, List<BranchInfo> branches) {

}
