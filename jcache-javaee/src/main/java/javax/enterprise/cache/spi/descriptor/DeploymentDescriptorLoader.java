package javax.enterprise.cache.spi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 *
 * @author airhacks.com
 */
public interface DeploymentDescriptorLoader {

    static final String DD_JAR_LOCATION = "META-INF/cache.xml";
    static final String DD_WAR_LOCATION = "WEB-INF/classes/" + DD_JAR_LOCATION;

    static String load() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try (InputStream ddStream = classLoader.getResourceAsStream(DD_JAR_LOCATION)) {
            if (ddStream != null) {
                return read(ddStream);
            }
        }

        try (InputStream ddStream = classLoader.getResourceAsStream(DD_WAR_LOCATION)) {
            if (ddStream == null) {
                throw new IllegalStateException("cache.xml neither found in " + DD_JAR_LOCATION + " nor " + DD_WAR_LOCATION);
            } else {
                return read(ddStream);
            }
        }

    }

    static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

}
