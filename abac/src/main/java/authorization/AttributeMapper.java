package authorization;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AttributeMapper {
    
    public Map<String, Object> toMap(Set<Attribute> attributes) {
        return attributes.stream()
                .collect(Collectors.groupingBy(Attribute::getName, Collectors.mapping(Attribute::getValue, Collectors.toSet())))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
