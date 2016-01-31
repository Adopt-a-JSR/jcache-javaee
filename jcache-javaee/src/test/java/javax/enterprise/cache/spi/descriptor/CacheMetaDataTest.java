package javax.enterprise.cache.spi.descriptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class CacheMetaDataTest {

    CacheMetaData cut;

    @Before
    public void init() {
        this.cut = new CacheMetaData();
    }

    @Test
    public void notExistingBooleanProperty() {
        boolean value = this.cut.getPropertyAsBoolean("unknown");
        assertFalse(value);
    }

    @Test
    public void malformedBooleanProperty() {
        String KEY = "malformed";
        this.cut.addProperty(KEY, "asdf");
        boolean value = this.cut.getPropertyAsBoolean(KEY);
        assertFalse(value);
    }

    @Test
    public void trueBooleanProperty() {
        String KEY = "true";
        this.cut.addProperty(KEY, "TRUE");
        boolean value = this.cut.getPropertyAsBoolean(KEY);
        assertTrue(value);
    }

}
