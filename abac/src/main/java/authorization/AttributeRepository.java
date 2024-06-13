package authorization;

import java.util.List;
import java.util.Optional;

public interface AttributeRepository {

    Optional<Attribute> loadAttribute(String id);

    Attribute saveAttribute(Attribute attribute);

    void deleteAttribute(Attribute attribute);

    List<Attribute> loadAttributes();
}
