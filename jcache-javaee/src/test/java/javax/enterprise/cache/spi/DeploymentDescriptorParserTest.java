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
    public void cacheUnitName() throws IOException {
        String ddContent = getDeploymentDescriptor("named-cache.xml");
        CacheUnit unit = DeploymentDescriptorParser.parse(ddContent);
        String name = unit.getName();
        assertThat(name, is("named"));
    }

    @Test
    public void cacheProviderClass() throws IOException {
        String ddContent = getDeploymentDescriptor("cache-provider.xml");
        CacheUnit unit = DeploymentDescriptorParser.parse(ddContent);
        String cachingProviderClass = unit.getCachingProviderClass();
        assertThat(cachingProviderClass, is("java.Duke"));
    }

    static String getDeploymentDescriptor(String descriptorName) throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/test/resources/META-INF/" + descriptorName)));
    }

}
