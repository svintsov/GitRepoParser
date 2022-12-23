package dev.gollund.gitrepoparser.dataProvider;

import com.spotify.github.v3.repos.ImmutableBranch;
import com.spotify.github.v3.repos.ImmutableRepository;
import java.util.List;

public class GitResponseDataProvider {

    private static final String RESPONSE_REPO_DATA_PATH = "/__files/response/repo.json";
    private static final String RESPONSE__BRANCH_DATA_PATH = "/__files/response/branches.json";

    public static List<ImmutableRepository> readRepoOutputData() {
        try {
            return JsonReader.readDataFromFile(RESPONSE_REPO_DATA_PATH, ImmutableRepository.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<ImmutableBranch> readBranchOutputData() {
        try {
            return JsonReader.readDataFromFile(RESPONSE__BRANCH_DATA_PATH, ImmutableBranch.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
