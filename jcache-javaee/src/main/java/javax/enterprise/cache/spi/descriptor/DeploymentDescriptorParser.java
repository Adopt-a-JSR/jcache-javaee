package javax.enterprise.cache.spi.descriptor;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * <pre>
 * <code>
 * &lt;caches&gt;
 * &lt;class&gt;org.jsr107.ri.spi.RICachingProvider&lt;/class&gt;
 * &lt;cache name="it"&gt;
 * &lt;configuration&gt;
 * &lt;property name="store.by.value" value="true"/&gt;
 * &lt;property name="management.enabled" value="true"/&gt;
 * &lt;property name="statistics.enabled" value="true"/&gt;
 * &lt;property name="vendor.specific" value="unicorn"/&gt;
 * &lt;/configuration&gt;
 * &lt;/cache&gt;
 * &lt;/caches&gt;
 * </code>
 * </pre>
 *
 * @author airhacks.com
 */
public interface DeploymentDescriptorParser {

    static String ELEMENT_CACHES = "caches";
    static String ELEMENT_CACHE = "cache";
    static String ELEMENT_CACHING_PROVIDER = "caching-provider";
    static String ELEMENT_CONFIGURATION = "configuration";
    static String ELEMENT_PROPERTY = "property";
    static String ATTRIBUTE_CACHE_NAME = "name";
    static String ATTRIBUTE_NAME = "name";
    static String ATTRIBUTE_KEY = "key";
    static String ATTRIBUTE_VALUE = "value";

    static CachesMetaData parse(String content) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        CachesMetaData caches = new CachesMetaData();
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            throw new IllegalStateException(ex);
        }
        try (StringReader reader = new StringReader(content)) {
            try {
                Document document = builder.parse(new InputSource(reader));
                Node caching = document.getElementsByTagName(ELEMENT_CACHES).item(0);
                String cachingProvider = findSubNode(ELEMENT_CACHING_PROVIDER, caching);
                caches.setCachingProviderClass(cachingProvider);

                NodeList cachesChildren = caching.getChildNodes();
                for (int i = 0; i < cachesChildren.getLength(); i++) {
                    Node item = cachesChildren.item(i);
                    if (ELEMENT_CACHE.equalsIgnoreCase(item.getNodeName())) {
                        CacheMetaData cacheMetaData = new CacheMetaData();
                        String cacheName = getAttributeValue(item, ATTRIBUTE_CACHE_NAME);
                        cacheMetaData.setName(cacheName);
                        getAttribute(item, ATTRIBUTE_KEY).
                                ifPresent((t)
                                        -> cacheMetaData.setKey(
                                        getAttributeValue(item, ATTRIBUTE_KEY))
                                );
                        getAttribute(item, ATTRIBUTE_VALUE).
                                ifPresent((t)
                                        -> cacheMetaData.setValue(
                                        getAttributeValue(item, ATTRIBUTE_VALUE))
                                );

                        NodeList cacheChildren = item.getChildNodes();
                        for (int j = 0; j < cacheChildren.getLength(); j++) {
                            Node cacheKid = cacheChildren.item(j);
                            if (ELEMENT_CONFIGURATION.equalsIgnoreCase(cacheKid.getNodeName())) {
                                Map<String, String> properties = extractProperties(cacheKid);
                                cacheMetaData.setConfigurationProperties(properties);
                            }
                        }
                        caches.add(cacheMetaData);
                    }
                }
            } catch (SAXException | IOException ex) {
                throw new IllegalStateException(ex);
            }
        }
        return caches;
    }

    static Map<String, String> extractProperties(Node configuration) {
        Map<String, String> properties = new HashMap<>();
        NodeList cacheChildren = configuration.getChildNodes();
        for (int i = 0; i < cacheChildren.getLength(); i++) {
            Node cacheKid = cacheChildren.item(i);
            if (ELEMENT_PROPERTY.equalsIgnoreCase(cacheKid.getNodeName())) {
                getAttribute(cacheKid, ATTRIBUTE_NAME).
                        ifPresent((t)
                                -> properties.put(t.getNodeValue(),
                                getAttributeValue(cacheKid, ATTRIBUTE_VALUE))
                        );
            }
        }
        return properties;
    }

    static Optional<Node> getAttribute(Node node, String name) {
        return Optional.ofNullable(node.getAttributes().getNamedItem(name));
    }

    static String getAttributeValue(Node node, String name) {
        Node attributeItem = node.getAttributes().getNamedItem(name);
        if (attributeItem == null) {
            return null;
        } else {
            return attributeItem.getNodeValue();
        }
    }

    static String findSubNode(String nodeName, Node parent) {
        NodeList childNodes = parent.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (nodeName.equals(item.getNodeName())) {
                return item.getTextContent();
            }
        }
        return null;
    }
}
