package javax.enterprise.cache.spi;

import javax.enterprise.cache.spi.descriptor.DeploymentDescriptorLoader;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.runners.model.InitializationError;

/**
 *
 * @author airhacks.com
 */
public class SingleCacheForDefaultInjection extends CdiTestRunner {

    static {
        System.setProperty(DeploymentDescriptorLoader.DD_FILE_NAME, "single.xml");
    }

    public SingleCacheForDefaultInjection(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

}
