package javax.enterprise.cache.spi.descriptor;

import java.util.HashMap;
import java.util.Map;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;

/**
 *
 * @author airhacks.com
 */
public class CacheMetaData {

    private String name;
    private Map<String, String> configurationProperties;
    static final String PROPERTY_KEY_STORE_BY_VALUE = "store.by.value";
    static final String PROPERTY_KEY_IS_MANAGEMENT_ENABLED = "management.enabled";
    static final String PROPERTY_KEY_IS_STATISTICS_ENABLED = "statistics.enabled";

    private Class key;
    private Class value;

    public CacheMetaData() {
        this.configurationProperties = new HashMap<>();
    }

    public CacheMetaData(String name, boolean storeByValue,
            boolean managementEnabled, boolean statisticsEnabled) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStoreByValue() {
        return getPropertyAsBoolean(PROPERTY_KEY_STORE_BY_VALUE);
    }

    public boolean isManagementEnabled() {
        return getPropertyAsBoolean(PROPERTY_KEY_IS_MANAGEMENT_ENABLED);
    }

    public boolean isStatisticsEnabled() {
        return getPropertyAsBoolean(PROPERTY_KEY_IS_STATISTICS_ENABLED);
    }

    public Map<String, String> getConfigurationProperties() {
        return configurationProperties;
    }

    public void setConfigurationProperties(Map<String, String> configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    public void addProperty(String key, String value) {
        this.configurationProperties.put(key, value);
    }

    boolean getPropertyAsBoolean(String key) {
        String value = this.configurationProperties.getOrDefault(key, "false");
        return Boolean.parseBoolean(value);
    }

    public void setKey(String key) {
        try {
            this.key = Class.forName(key);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Cannot load class: " + value + " for as Cache key");
        }
    }

    public void setValue(String value) {
        try {
            this.value = Class.forName(value);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Cannot load class: " + value + " for as Cache value");
        }
    }

    public Class getKey() {
        return key;
    }

    public Class getValue() {
        return value;
    }

    public Configuration<String, String> getConfiguration() {
        MutableConfiguration<String, String> configuration = new MutableConfiguration<>();
        configuration.setStoreByValue(isStoreByValue()).
                setTypes(this.key, this.value).
                setManagementEnabled(isManagementEnabled()).
                setStatisticsEnabled(isStatisticsEnabled());
        return configuration;
    }

}
