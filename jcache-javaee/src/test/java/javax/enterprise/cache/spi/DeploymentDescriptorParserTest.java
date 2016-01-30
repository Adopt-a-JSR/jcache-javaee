package javax.enterprise.cache.spi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class DeploymentDescriptorParserTest {

    @Test
    public void parse() throws IOException {
        String ddContent = new String(Files.readAllBytes(Paths.get("src/test/resources/META-INF/named-cache.xml")));
        CacheUnit unit = DeploymentDescriptorParser.parse(ddContent);
        String name = unit.getName();
        assertThat(name, is("named"));
    }

}
