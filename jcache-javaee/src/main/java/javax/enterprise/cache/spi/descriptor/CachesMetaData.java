package javax.enterprise.cache.spi.descriptor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author airhacks.com
 */
public class CachesMetaData {

    private String cachingProviderClass;

    private List<CacheMetaData> caches;

    CachesMetaData() {
        this.caches = new ArrayList<>();
    }

    public CachesMetaData(String cachingProviderClass) {
        this();
        this.cachingProviderClass = cachingProviderClass;
    }

    public List<CacheMetaData> getCaches() {
        return caches;
    }

    public String getCachingProviderClass() {
        return cachingProviderClass;
    }

    public void setCachingProviderClass(String cachingProviderClass) {
        this.cachingProviderClass = cachingProviderClass;
    }

    public void add(CacheMetaData cacheMetaData) {
        this.caches.add(cacheMetaData);
    }

}
