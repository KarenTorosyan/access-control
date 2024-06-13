package authorization;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class AuthorizationConfiguration {

    @JsonProperty("policies")
    private Set<Policy> policies;

    @JsonProperty("attributes")
    private Set<Attribute> attributes;

    public void setPolicies(Set<Policy> policies) {
        this.policies = policies;
    }

    public Set<Policy> getPolicies() {
        return policies;
    }

    public void setAttributes(Set<Attribute> attributes) {
        this.attributes = attributes;
    }

    public Set<Attribute> getAttributes() {
        return attributes;
    }
}
