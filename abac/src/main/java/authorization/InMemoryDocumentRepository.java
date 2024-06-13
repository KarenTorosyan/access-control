package authorization;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryDocumentRepository implements DocumentRepository {

    private final List<Document> documents = new ArrayList<>();

    @Override
    public Document saveDocument(Document document) {
        if (!documents.contains(document)) {
            documents.add(document);
        } else {
            documents.set(documents.indexOf(document), document);
        }
        return document;
    }

    @Override
    public Optional<Document> loadDocument(String id) {
        return documents.stream().filter(document -> document.getId().equals(id)).findFirst();
    }
}
