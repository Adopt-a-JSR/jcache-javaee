package javax.enterprise.cache.spi.descriptor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
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
        CachesMetaData cachesMeta = DeploymentDescriptorParser.parse(ddContent);
        List<CacheMetaData> caches = cachesMeta.getCaches();
        String name = caches.get(0).getName();
        assertThat(name, is("named"));
    }

    @Test
    public void cacheProviderClass() throws IOException {
        String ddContent = getDeploymentDescriptor("cache-provider.xml");
        CachesMetaData unit = DeploymentDescriptorParser.parse(ddContent);
        String cachingProviderClass = unit.getCachingProviderClass();
        assertThat(cachingProviderClass, is("java.Duke"));
    }

    static String getDeploymentDescriptor(String descriptorName) throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/test/resources/META-INF/" + descriptorName)));
    }

}
