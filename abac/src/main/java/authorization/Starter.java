package authorization;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.File;
import java.text.MessageFormat;
import java.util.Set;
import java.util.UUID;

public class Starter {

    private static final Log log = LogFactory.getLog(Starter.class);

    public static void main(String[] args) {
        File file = new File("abac/src/main/resources/configuration.json");
        AuthorizationConfigurationSource authorizationConfigurationSource = new JsonAuthorizationConfigurationSource(file);
        AuthorizationConfigurationHolder.setAuthorizationConfiguration(authorizationConfigurationSource.getAuthorizationConfiguration());

        User user = new User(UUID.randomUUID().toString(), "User", "user@email.com");

        Document document = new Document(UUID.randomUUID().toString(), "Document", user);
        DocumentRepository documentRepository = new InMemoryDocumentRepository();
        documentRepository.saveDocument(document);

        AttributeRepository attributeRepository = new LocalAttributeRepository();
        Attribute editorsGroup = attributeRepository.saveAttribute(new Attribute(UUID.randomUUID().toString(), "group", "editors"));
        Attribute userRole = attributeRepository.saveAttribute(new Attribute(UUID.randomUUID().toString(), "role", "user"));

        AttributeBinder attributeBinder = new ObjectAttributeBinder();
        attributeBinder.bind(user.getEmail(), editorsGroup.getId());
        attributeBinder.bind(user.getEmail(), userRole.getId());

        SecurityContext securityContext = SecurityContextHolder.getContextHolderStrategy().createEmptyContext();
        securityContext.setAuthentication(UsernamePasswordAuthenticationToken.authenticated(user.getEmail(), null, Set.of()));
        SecurityContextHolder.getContextHolderStrategy().setContext(securityContext);

        AttributeAuthorizationManager attributeAuthorizationManager = new AttributeAuthorizationManager(attributeBinder, attributeRepository);

        DocumentService documentService = new DocumentService(documentRepository, attributeAuthorizationManager);
        Document loaded = documentService.getDocument(document.getId());
        log.info(MessageFormat.format("Access granted to read: {0}", loaded));

        document.setName("Name updated");
        Document edited = documentService.editDocument(document);
        log.info(MessageFormat.format("Access granted to edit: {0}", edited));
    }
}
