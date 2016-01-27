package javax.enterprise.cache.spi;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author airhacks.com
 */
public class CacheUnit {

    public String getName() {
        return "articles";
    }

    public String getCachingProvider() {
        return "com.hazelcast.cache.HazelcastCachingProvider";
    }

    public boolean isStoreByValue() {
        return true;
    }

    public boolean isManagementEnabled() {
        return true;
    }

    public boolean isStatisticsEnabled() {
        return true;
    }

    public Map<String, String> getConfigurationProperties() {
        return new HashMap<>();

    }

}
