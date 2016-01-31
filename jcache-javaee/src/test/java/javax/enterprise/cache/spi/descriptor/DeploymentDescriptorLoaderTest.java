package javax.enterprise.cache.spi;

import java.io.IOException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class DeploymentDescriptorLoaderTest {

    @Test
    public void loadDDFromJAR() throws IOException {
        String ddContent = DeploymentDescriptorLoader.load();
        assertNotNull(ddContent);
        assertFalse(ddContent.isEmpty());
    }

}
