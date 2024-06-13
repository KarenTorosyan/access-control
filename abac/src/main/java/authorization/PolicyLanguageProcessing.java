package authorization;

import java.util.Map;

public class PolicyLanguageProcessing {

    private final PolicyLanguageParser policyLanguageParser;

    public PolicyLanguageProcessing(PolicyLanguageParser policyLanguageParser) {
        this.policyLanguageParser = policyLanguageParser;
    }

    record Context(Map<String, ?> object, Map<String, ?> subject, String action) {
    }

    public boolean preloadStage(String rule, ActionType actionType, Map<String, ?> subjectAttributes) {
        Context context = new Context(Map.of(), subjectAttributes, actionType.name().toLowerCase());
        return policyLanguageParser.parse(rule, context, Boolean.class);
    }

    public boolean finalStage(String rule, ActionType actionType, Map<String, ?> subjectAttributes, Map<String, ?> objectAttributes) {
        Context context = new Context(objectAttributes, subjectAttributes, actionType.name().toLowerCase());
        return policyLanguageParser.parse(rule, context, Boolean.class);
    }
}
