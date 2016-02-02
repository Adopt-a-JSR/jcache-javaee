package javax.enterprise.cache.spi;

import javax.cache.Cache;
import javax.enterprise.cache.CacheContext;
import javax.inject.Inject;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author airhacks.com
 */
@RunWith(SingleCacheForDefaultInjection.class)
public class ConventionalCacheInjection {

    @Inject
    Cache articles;

    @Inject
    Cache another;

    @Inject
    @CacheContext("it")
    Cache hackers;

    @Test
    public void articlesCacheIsInjectable() {
        assertNotNull(articles);
    }

    @Test
    public void identity() {
        assertSame(articles, another);
    }

}
