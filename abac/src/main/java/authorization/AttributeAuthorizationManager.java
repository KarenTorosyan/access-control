package authorization;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AttributeAuthorizationManager {

    private final PolicyLanguageProcessing policyLanguageProcessing = new PolicyLanguageProcessing(new SpelPolicyLanguageParser());
    private final AuthorizationConfiguration authorizationConfiguration = AuthorizationConfigurationHolder.getAuthorizationConfiguration();
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final AttributeMapper attributeMapper = new AttributeMapper();

    private final AttributeBinder attributeBinder;
    private final AttributeRepository attributeRepository;

    public AttributeAuthorizationManager(AttributeBinder attributeBinder, AttributeRepository attributeRepository) {
        this.attributeBinder = attributeBinder;
        this.attributeRepository = attributeRepository;
    }

    public <T> T authorize(String objectType, ActionType actionType, Supplier<String> objectIdSupplier, Supplier<T> objectSupplier) {
        Set<Policy> policies = getPolicies(objectType, actionType);
        Map<String, Object> subjectAttributes = getSubjectAttributes();

        String objectId = objectIdSupplier.get();
        T object = objectSupplier.get();

        Map<String, Object> objectAttributes = getObjectAttributes(objectId, object);

        for (Policy policy : policies) {
            boolean isPreloadGranted = policyLanguageProcessing.preloadStage(policy.getPreloadRule(), actionType, subjectAttributes);
            if (!isPreloadGranted) {
                throw new AccessDeniedException("Access denied");
            }
            boolean isGranted = policyLanguageProcessing.finalStage(policy.getRule(), actionType, subjectAttributes, objectAttributes);
            if (!isGranted) {
                throw new AccessDeniedException("Access denied");
            }
        }
        return object;
    }

    private Set<Policy> getPolicies(String objectType, ActionType actionType) {
        return authorizationConfiguration.getPolicies().stream()
                .filter(policy -> (policy.getObjectType().equals("*") && policy.getActionType() == actionType) ||
                        policy.getObjectType().equalsIgnoreCase(objectType))
                .collect(Collectors.toSet());
    }

    private Map<String, Object> getSubjectAttributes() {
        Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
        if (authentication == null || authentication.getClass() == AnonymousAuthenticationToken.class) {
            throw new AccessDeniedException("Authentication required");
        }

        boolean authenticated = authentication.isAuthenticated();
        String principal = authentication.getName();

        Set<Attribute> subjectAttributes = attributeBinder.getAttributes(principal).stream()
                .map(attrId -> attributeRepository.loadAttribute(attrId).orElseThrow(() -> new RuntimeException("Attribute not found")))
                .collect(Collectors.toSet());
        Map<String, Object> subjectAttributesMap = attributeMapper.toMap(subjectAttributes);
        subjectAttributesMap.put("authenticated", authenticated);
        subjectAttributesMap.put("principal", principal);

        return subjectAttributesMap;
    }

    private Map<String, Object> getObjectAttributes(String objectId, Object object) {
        Set<Attribute> objectAttributes = attributeBinder.getAttributes(objectId).stream()
                .map(attrId -> attributeRepository.loadAttribute(attrId).orElseThrow())
                .collect(Collectors.toSet());
        Map<String, Object> objectAttributesMap = attributeMapper.toMap(objectAttributes);
        objectAttributesMap.putAll(PolicyAttributeProcessor.loadAttributes(object));
        return objectAttributesMap;
    }

    public <T> T preAuthorize(String objectType, ActionType actionType, Supplier<String> objectIdSupplier, Supplier<T> objectSupplier) {

        Set<Policy> policies = getPolicies(objectType, actionType);

        Map<String, Object> subjectAttributesMap = getSubjectAttributes();

        for (Policy policy : policies) {
            boolean isPreloadGranted = policyLanguageProcessing.preloadStage(policy.getPreloadRule(), actionType, subjectAttributesMap);
            if (!isPreloadGranted) {
                throw new AccessDeniedException("Access denied");
            }
        }

        String objectId = objectIdSupplier.get();
        T object = objectSupplier.get();

        Map<String, Object> objectAttributes = getObjectAttributes(objectId, object);

        for (Policy policy : policies) {
            boolean isGranted = policyLanguageProcessing.finalStage(policy.getRule(), actionType, subjectAttributesMap, objectAttributes);
            if (!isGranted) {
                throw new AccessDeniedException("Access denied");
            }
        }
        return object;
    }
}
