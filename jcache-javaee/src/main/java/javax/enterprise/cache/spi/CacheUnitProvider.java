package javax.enterprise.cache.spi;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author airhacks.com
 */
public class CacheUnitProvider {

    public List<CacheUnit> getCacheUnits() {
        return Arrays.asList(new CacheUnit());
    }
}
