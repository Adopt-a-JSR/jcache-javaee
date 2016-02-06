# jcache-javaee

JCache Integration Bridge for Java EE 8

# Goal

Integrate JSR-107 (JCache) with Java EE 8 considering already established conventions in specs like JPA or JAX-RS

# Cache descriptor

Inspired by JPA

## Location

1. Within WAR: WEB-INF/classes/META-INF/cache.xml
2. Within JAR: META-INF/cache.xml

```xml

<caches>
    <caching-provider>org.jsr107.ri.spi.RICachingProvider</caching-provider>
    <cache name="it" key="java.lang.String" value="java.lang.String">
        <configuration>
            <property name="store.by.value" value="true"/>
            <property name="management.enabled" value="false"/>
            <property name="statistics.enabled" value="true"/>
            <property name="vendor.specific" value="unicorn"/>
        </configuration>
    </cache>
    <cache name="hack3rz" key="java.lang.String" value="java.lang.String">
        <configuration>
            <property name="store.by.value" value="false"/>
            <property name="management.enabled" value="true"/>
            <property name="statistics.enabled" value="false"/>
            <property name="java" value="duke"/>
        </configuration>
    </cache>
</caches>

```

# Usage

```java

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
    
```

# Conventions

1. Cache key does not have to be specified -- java.lang.String is assumed as default
2. If there is only one cache defined, @CacheContext annotation can be omitted. 

