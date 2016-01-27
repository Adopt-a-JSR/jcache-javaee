# jcache-javaee
JCache-Java EE Integration Bridge

# Goal

Integrate JSR-107 (JCache) with Java EE 8 considering already established conventions in specs like JPA or JAX-RS

# Cache descriptor

Inspired by JPA

```xml

<cache>
    <cache-unit name="articles">
    <class>com.hazelcast.cache.HazelcastCachingProvider</class>
        <configuration>
            <property name="store.by.value" value="true"/>
            <property name="management.enabled" value="true"/>
            <property name="statistics.enabled" value="true"/>
        </configuration>
    </cache-unit>
</cache>

```