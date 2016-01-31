package javax.enterprise.cache.spi;

import javax.cache.Cache;
import javax.enterprise.cache.CacheContext;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * @author airhacks.com
 */
@RunWith(SingleCacheForDefaultInjection.class)
public class ConventionalCacheInjection {

    @Inject
    Cache<String, String> articles;

    @Inject
    Cache<String, String> another;

    @Inject
    @CacheContext("it")
    Cache<String, String> hackers;

    @Test
    public void articlesCacheIsInjectable() {
        assertNotNull(articles);
    }

    @Test
    public void identity() {
        assertSame(articles, another);
    }

}
