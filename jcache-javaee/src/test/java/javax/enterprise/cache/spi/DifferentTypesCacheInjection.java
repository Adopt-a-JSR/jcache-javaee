package javax.enterprise.cache.spi;

import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.runners.model.InitializationError;

import javax.enterprise.cache.spi.descriptor.DeploymentDescriptorLoader;

/**
 *
 * @author Hendrik Ebbers
 */
public class DifferentTypesCacheInjection extends CdiTestRunner {

    static {
        System.setProperty(DeploymentDescriptorLoader.DD_FILE_NAME, "different-types.xml");
    }

    public DifferentTypesCacheInjection(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

}
