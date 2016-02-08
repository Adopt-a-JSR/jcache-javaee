package javax.enterprise.cache.spi;

import org.junit.Test;
import org.junit.runner.RunWith;

import javax.cache.Cache;
import javax.enterprise.cache.CacheContext;
import javax.inject.Inject;
import java.util.Date;

import static org.junit.Assert.assertNotNull;


/**
 *
 * @author Hendrik Ebbers
 */
@RunWith(DifferentTypesCacheInjection.class)
public class DifferentTypesCacheInjectionTest {

    @Inject
    @CacheContext("int-to-date")
    Cache<Integer, Date> cache;

    @Test
    public void cacheCreated() {
        assertNotNull(cache);
    }

    @Test
    public void cacheTypes() {
        cache.put(1, new Date());
        Date d = cache.get(1);
        assertNotNull(d);
    }

}
