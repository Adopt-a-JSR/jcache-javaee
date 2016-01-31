package javax.enterprise.cache.spi.descriptor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class DeploymentDescriptorParserTest {

    public CachesMetaData extractCachesMetaData(String name) throws IOException {
        String content = getDeploymentDescriptor(name);
        return DeploymentDescriptorParser.parse(content);
    }

    @Test
    public void cacheUnitName() throws IOException {
        CachesMetaData cachesMetaData = extractCachesMetaData("named-cache.xml");
        String name = getFirstCache(cachesMetaData).getName();
        assertThat(name, is("named"));
    }

    @Test
    public void cacheProviderClass() throws IOException {
        CachesMetaData cachesMetaData = extractCachesMetaData("cache-provider.xml");
        String cachingProviderClass = cachesMetaData.getCachingProviderClass();
        assertThat(cachingProviderClass, is("java.Duke"));
    }

    @Test
    public void configurationProperties() throws IOException {
        CachesMetaData cachesMetaData = extractCachesMetaData("cache.xml");
        CacheMetaData cache = getFirstCache(cachesMetaData);
        Map<String, String> configurationProperties = cache.getConfigurationProperties();
        assertNotNull(configurationProperties);
    }

    CacheMetaData getFirstCache(CachesMetaData cachesMetaData) {
        List<CacheMetaData> caches = cachesMetaData.getCaches();
        CacheMetaData cache = caches.get(0);
        assertNotNull(cache);
        return cache;
    }

    static String getDeploymentDescriptor(String descriptorName) throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/test/resources/META-INF/" + descriptorName)));
    }

}
