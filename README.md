# jcache-javaee
JCache-Java EE Integration Bridge

# Goal

Integrate JSR-107 (JCache) with Java EE 8 considering already established conventions in specs like JPA or JAX-RS

# Cache descriptor

Inspired by JPA

```xml

<caches>
    <caching-provider>org.jsr107.ri.spi.RICachingProvider</caching-provider>
    <cache name="it">
        <configuration>
            <property name="store.by.value" value="true"/>
            <property name="management.enabled" value="false"/>
            <property name="statistics.enabled" value="true"/>
            <property name="vendor.specific" value="unicorn"/>
        </configuration>
    </cache>
</caches>

```
