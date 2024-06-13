package authorization;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonAuthorizationConfigurationSource implements AuthorizationConfigurationSource {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final File file;

    public JsonAuthorizationConfigurationSource(File file) {
        this.file = file;
    }

    @Override
    public AuthorizationConfiguration getAuthorizationConfiguration() {
        try {
            return objectMapper.readValue(file, AuthorizationConfiguration.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read configuration source file", e);
        }
    }
}
