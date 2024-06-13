package authorization;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocalAttributeRepository implements AttributeRepository {

    private final List<Attribute> attributes = new ArrayList<>();

    @Override
    public Optional<Attribute> loadAttribute(String id) {
        return attributes.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    @Override
    public Attribute saveAttribute(Attribute attribute) {
        if (attributes.contains(attribute)) {
            int index = attributes.indexOf(attribute);
            Attribute existent = attributes.get(index);
            existent.setValue(attribute.getValue());
        } else {
            attributes.add(attribute);
        }
        return attribute;
    }

    @Override
    public void deleteAttribute(Attribute attribute) {
        attributes.remove(attribute);
    }

    @Override
    public List<Attribute> loadAttributes() {
        return attributes;
    }
}
