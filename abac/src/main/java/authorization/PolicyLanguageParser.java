package authorization;

public interface PolicyLanguageParser {

    <T> T parse(String rule, Object context, Class<T> type);
}
