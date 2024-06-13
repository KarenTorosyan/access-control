package authorization;

import java.util.*;

public class ObjectAttributeBinder implements AttributeBinder {

    private final Map<String, Set<String>> attributes = new HashMap<>(5);

    @Override
    public void bind(String objectId, String attributeId) {
        Set<String> attributes = this.attributes.get(objectId);
        if (attributes == null) {
            attributes = new HashSet<>();
        }
        attributes.add(attributeId);
        this.attributes.put(objectId, attributes);
    }

    @Override
    public void unbind(String objectId, String attributeId) {
        Set<String> attributes = this.attributes.get(objectId);
        if (attributes != null) {
            attributes.remove(attributeId);
            this.attributes.put(objectId, attributes);
        }
    }

    @Override
    public Optional<String> getAttribute(String objectId, String attributeId) {
        return attributes.get(objectId)
                .stream()
                .filter(attributeId::equals)
                .findFirst();
    }

    @Override
    public Set<String> getAttributes(String objectId) {
        Set<String> attributes = this.attributes.get(objectId);
        return attributes != null ? attributes : new HashSet<>(0);
    }
}
