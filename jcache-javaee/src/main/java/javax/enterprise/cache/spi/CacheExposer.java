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
        this.cachingProvider = Caching.getCachingProvider();
        this.cacheManager = cachingProvider.getCacheManager();
        CacheUnitProvider cacheProvider = new CacheUnitProvider();
        List<CacheUnit> cacheUnits = cacheProvider.getCacheUnits();
        this.caches = cacheUnits.stream().
                collect(Collectors.toMap(CacheUnit::getName, this::createFrom));

    }

    Cache<String, String> createFrom(CacheUnit unit) {
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

    public Configuration<String, String> getConfiguration(CacheUnit unit, Class key, Class value) {
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
