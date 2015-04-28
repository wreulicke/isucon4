package net.isucon.isucon4.provider;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.isucon.isucon4.config.Config;

import javax.inject.Provider;
import java.io.IOException;
import java.io.InputStream;

public class ConfigProvider implements Provider<Config> {
    @Override
    public Config get() {
        String env = System.getProperty("java-avans.env");
        if (env == null) {
            throw new RuntimeException("Missing java-avans.env");
        } else {
            String fileName = "config/" + env + ".yml";
            try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream(
                    fileName)) {
                if (stream == null) {
                    throw new RuntimeException("Cannot load " + fileName
                            + " from resource.");
                }
                try {
                    return new ObjectMapper(new YAMLFactory()).readValue(stream, Config.class);
                } catch (JsonParseException | JsonMappingException e) {
                    throw new RuntimeException(String.format("Cannot parse %s: %s",
                            fileName, e.getMessage()));
                }
            } catch (IOException e) {
                throw new RuntimeException(String.format("Cannot read %s from resources: %s",
                        fileName, e.getMessage()));
            }
        }
    }
}
