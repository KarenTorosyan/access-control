package authorization;

import java.util.Optional;
import java.util.Set;

public interface AttributeBinder {

    void bind(String objectId, String attributeId);

    void unbind(String objectId, String attributeId);

    Optional<String> getAttribute(String objectId, String attributeId);

    Set<String> getAttributes(String objectId);
}
