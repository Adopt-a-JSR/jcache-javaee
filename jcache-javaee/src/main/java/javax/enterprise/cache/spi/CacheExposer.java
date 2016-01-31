package javax.enterprise.cache.spi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import javax.enterprise.cache.CacheContext;
import javax.enterprise.cache.spi.descriptor.CacheMetaData;
import javax.enterprise.cache.spi.descriptor.CachesMetaData;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
@ApplicationScoped
public class CacheExposer {

    Cache<String, String> store;
    CachingProvider cachingProvider;
    CacheManager cacheManager;

    @Inject
    Instance<Map<String, String>> initialValues;

    Map<String, Cache<String, String>> caches;

    public void init(@Observes @Initialized(ApplicationScoped.class) Object doesntMatter) {
        this.caches = new HashMap<>();
        CachesMetaData caches = CachesProvider.getCacheUnits();
        this.cachingProvider = Caching.getCachingProvider(caches.getCachingProviderClass());
        this.cacheManager = cachingProvider.getCacheManager();
        this.caches = createCaches(caches.getCaches());

    }

    Map<String, Cache<String, String>> createCaches(List<CacheMetaData> cacheUnits) {
        return cacheUnits.stream().collect(Collectors.toMap(CacheMetaData::getName, this::createFrom));
    }

    Cache<String, String> createFrom(CacheMetaData unit) {
        Configuration<String, String> configuration = this.getConfiguration(unit, String.class, String.class);
        String cacheName = unit.getName();
        Cache<String, String> cache = this.cacheManager.getCache(cacheName, String.class, String.class);
        if (cache == null) {
            cache = this.cacheManager.createCache(cacheName, configuration);
        }
        return cache;
    }

    @Produces
    public Cache<String, String> exposeCache(InjectionPoint ip) {
        Annotated annotated = ip.getAnnotated();
        CacheContext annotation = annotated.getAnnotation(CacheContext.class);
        if (doesConventionWork() && annotation == null) {
            return this.caches.values().iterator().next();
        }
        String cacheName = annotation.value();
        Cache<String, String> cache = this.caches.get(cacheName);
        if (cache == null) {
            throw new IllegalStateException("Unsatisfied cache dependency error. Cache with name: " + cacheName + " does not exist");
        }
        return cache;
    }

    boolean doesConventionWork() {
        return this.caches.size() == 1;
    }

    public Configuration<String, String> getConfiguration(CacheMetaData unit, Class key, Class value) {
        MutableConfiguration<String, String> configuration = new MutableConfiguration<>();
        configuration.setStoreByValue(unit.isStoreByValue()).
                setTypes(key, value).
                setManagementEnabled(unit.isManagementEnabled()).
                setStatisticsEnabled(unit.isStatisticsEnabled());
        return configuration;
    }

    public void shutdown(@Observes @Destroyed(ApplicationScoped.class) Object doesntMatter) {
        this.cachingProvider.close();
    }

}
