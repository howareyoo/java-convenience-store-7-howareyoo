package store.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ItemUtil {

    public static List<String> curProcuts(String fileName) throws URISyntaxException {
        Path path = Paths.get(ClassLoader.getSystemResource(fileName).toURI());
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            return List.of();
        }
    }
}
