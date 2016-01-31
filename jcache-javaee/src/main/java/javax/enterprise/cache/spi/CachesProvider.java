package javax.enterprise.cache.spi;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author airhacks.com
 */
public class CacheUnitProvider {

    static List<CacheUnit> getCacheUnits() {
        try {
            String ddContent = DeploymentDescriptorLoader.load();
            return Arrays.asList(DeploymentDescriptorParser.parse(ddContent));
        } catch (IOException ex) {
            throw new IllegalStateException("Interpreting DD failed", ex);
        }

    }
}
