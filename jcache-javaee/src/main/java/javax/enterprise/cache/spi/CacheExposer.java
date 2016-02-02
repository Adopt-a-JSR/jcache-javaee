package javax.enterprise.cache.spi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.Configuration;
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

    CachingProvider cachingProvider;
    CacheManager cacheManager;

    @Inject
    Instance<Map<String, String>> initialValues;

    Map<String, Cache> caches;

    public void init(@Observes @Initialized(ApplicationScoped.class) Object doesntMatter) {
        this.caches = new HashMap<>();
        CachesMetaData cachesMetaData = CachesProvider.getCacheUnits();
        this.cachingProvider = Caching.getCachingProvider(cachesMetaData.getCachingProviderClass());
        this.cacheManager = cachingProvider.getCacheManager();
        this.caches = createCaches(cachesMetaData.getCaches());

    }

    Map<String, Cache> createCaches(List<CacheMetaData> cacheUnits) {
        return cacheUnits.stream().collect(Collectors.toMap(CacheMetaData::getName, this::createFrom));
    }

    Cache createFrom(CacheMetaData unit) {
        Configuration<String, String> configuration = unit.getConfiguration();
        String cacheName = unit.getName();
        Cache<String, String> cache = this.cacheManager.getCache(cacheName, unit.getKey(), unit.getValue());
        if (cache == null) {
            cache = this.cacheManager.createCache(cacheName, configuration);
        }
        return cache;
    }

    @Produces
    public Cache exposeCache(InjectionPoint ip) {
        Annotated annotated = ip.getAnnotated();
        CacheContext annotation = annotated.getAnnotation(CacheContext.class);
        //single cache defined in DD, no annotation
        if (doesConventionApply(annotation)) {
            return this.caches.values().iterator().next();
        }
        //annotation defined, name is used to lookup cache
        if (annotation != null) {
            String cacheName = annotation.value();
            Cache<String, String> cache = this.caches.get(cacheName);
            if (cache == null) {
                throw new IllegalStateException("Unsatisfied cache dependency error. Cache with name: " + cacheName + " does not exist");
            }
            return cache;
        }
        String fieldName = ip.getMember().getDeclaringClass().getName() + "." + ip.getMember().getName();
        if (this.caches.isEmpty()) {
            throw new IllegalStateException("No caches defined " + fieldName);
        } else {
            throw new IllegalStateException("Ambiguous caches exception: " + this.caches.keySet() + " for field name " + fieldName);
        }
    }

    boolean doesConventionApply(CacheContext annotation) {
        return this.caches.size() == 1 && annotation == null;
    }

    public void shutdown(@Observes @Destroyed(ApplicationScoped.class) Object doesntMatter) {
        this.cachingProvider.close();
    }

}
