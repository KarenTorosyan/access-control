package authorization;

public class DocumentService {

    private final DocumentRepository documentRepository;
    private final AttributeAuthorizationManager attributeAuthorizationManager;

    public DocumentService(DocumentRepository documentRepository, AttributeAuthorizationManager attributeAuthorizationManager) {
        this.documentRepository = documentRepository;
        this.attributeAuthorizationManager = attributeAuthorizationManager;
    }

    public Document getDocument(String id) {
        return attributeAuthorizationManager.preAuthorize("document", ActionType.READ,
                () -> id,
                () -> documentRepository.loadDocument(id).orElseThrow());
    }

    public Document editDocument(Document document) {
        Document documentGranted = attributeAuthorizationManager.authorize("document", ActionType.WRITE,
                document::getId,
                () -> document);
        return documentRepository.saveDocument(documentGranted);
    }
}
