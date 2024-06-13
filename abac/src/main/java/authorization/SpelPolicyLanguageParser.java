package authorization;

import org.springframework.expression.spel.standard.SpelExpressionParser;

public class SpelPolicyLanguageParser implements PolicyLanguageParser {

    private final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

    @Override
    public <T> T parse(String rule, Object context, Class<T> type) {
        T value = spelExpressionParser.parseExpression(rule)
                .getValue(context, type);

        if (value == null) {
            throw new RuntimeException("The policy configuration contains a error. Maybe the specified attribute does not exist");
        }

        return value;
    }
}
