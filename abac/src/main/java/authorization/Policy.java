package authorization;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Policy {

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("objectType")
    private String objectType;

    @JsonProperty("action")
    private String action;

    @JsonProperty("preloadRule")
    private String preloadRule;

    @JsonProperty("rule")
    private String rule;

    @JsonProperty("author")
    private String author;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public ActionType getActionType() {
        return ActionType.valueOf(action.toUpperCase());
    }

    public String getPreloadRule() {
        return preloadRule;
    }

    public void setPreloadRule(String preloadRule) {
        this.preloadRule = preloadRule;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
