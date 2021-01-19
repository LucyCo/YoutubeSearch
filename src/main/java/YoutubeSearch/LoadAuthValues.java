package YoutubeSearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

public class LoadAuthValues {
    private static Resource loadCredentialsWithClassPathResource() {
        return new ClassPathResource("init/authKeys.json");
    }

    public static AuthValues getAuthValues() throws NoAuthFileException, IOException {
        Resource authFile = loadCredentialsWithClassPathResource();
        File file;
        try {
            file = authFile.getFile();
        } catch (IOException e) {
            throw new NoAuthFileException();
        }
        return new ObjectMapper().readValue(file, AuthValues.class);
    }
}
