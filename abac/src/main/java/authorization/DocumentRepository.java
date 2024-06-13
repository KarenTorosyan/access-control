package authorization;

import java.util.Optional;

public interface DocumentRepository {

    Document saveDocument(Document document);

    Optional<Document> loadDocument(String id);
}
