package javax.enterprise.cache.spi;

import javax.cache.Cache;
import javax.inject.Inject;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author airhacks.com
 */
@RunWith(CdiTestRunner.class)
public class CacheExposerIT {

    @Inject
    Cache<String, String> articles;

    @Inject
    Cache<String, String> another;

    @Test
    public void articlesCacheIsInjectable() {
        assertNotNull(articles);
    }

    @Test
    public void identity() {
        assertSame(articles, another);
    }

}
