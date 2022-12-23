package dev.gollund.gitrepoparser.dataProvider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class JsonReader {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new Jdk8Module());

    public static <T> List<T> readDataFromFile(String pathToFile,
            Class<T> classOnWhichArrayIsDefined)
            throws ClassNotFoundException {
        Class<T[]> arrayClass = (Class<T[]>) Class.forName(
                "[L" + classOnWhichArrayIsDefined.getName() + ";");

        try (InputStream is = JsonReader.class.getResourceAsStream(pathToFile)) {
            T[] objects = OBJECT_MAPPER.readValue(is, arrayClass);
            return Arrays.asList(objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertObjectToString(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
