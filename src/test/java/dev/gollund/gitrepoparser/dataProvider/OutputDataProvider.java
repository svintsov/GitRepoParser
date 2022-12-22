package dev.gollund.gitrepoparser.dataProvider;

import dev.gollund.gitrepoparser.model.UserRepoInfo;
import java.util.List;

public class OutputDataProvider {

    private static final String OUTPUT_DATA_PATH = "/output/output.json";

    public static List<UserRepoInfo> readOutputData() {
        try {
            return JsonReader.readDataFromFile(OUTPUT_DATA_PATH, UserRepoInfo.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
