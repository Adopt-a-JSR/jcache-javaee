package javax.enterprise.cache.spi;

import javax.cache.Cache;
import javax.enterprise.cache.CacheContext;
import javax.inject.Inject;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author airhacks.com
 */
@RunWith(CdiTestRunner.class)
public class CustomCacheInjectionTest {

    @Inject
    @CacheContext("it")
    Cache first;

    @Inject
    @CacheContext("hack3rz")
    Cache second;

    @Inject
    @CacheContext("hack3rz")
    Cache alreadyInjected;

    @Test
    public void cachesWithDifferentNamesAreNotSame() {
        assertNotSame(first, second);
    }

    @Test
    public void cachesWithSameNameAreSame() {
        assertSame(second, alreadyInjected);
    }

}
