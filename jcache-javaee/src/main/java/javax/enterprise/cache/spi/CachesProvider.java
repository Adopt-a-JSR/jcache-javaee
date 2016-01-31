package javax.enterprise.cache.spi;

import java.io.IOException;
import javax.enterprise.cache.spi.descriptor.CachesMetaData;
import javax.enterprise.cache.spi.descriptor.DeploymentDescriptorLoader;
import javax.enterprise.cache.spi.descriptor.DeploymentDescriptorParser;

/**
 *
 * @author airhacks.com
 */
public interface CachesProvider {

    static CachesMetaData getCacheUnits() {
        try {
            String ddContent = DeploymentDescriptorLoader.load();
            return DeploymentDescriptorParser.parse(ddContent);
        } catch (IOException ex) {
            throw new IllegalStateException("Interpreting DD failed", ex);
        }

    }
}
