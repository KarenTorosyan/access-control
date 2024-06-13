package authorization;

public class AuthorizationConfigurationHolder {

    public static AuthorizationConfiguration authorizationConfiguration;

    public static void setAuthorizationConfiguration(AuthorizationConfiguration authorizationConfiguration) {
        AuthorizationConfigurationHolder.authorizationConfiguration = authorizationConfiguration;
    }

    public static AuthorizationConfiguration getAuthorizationConfiguration() {
        return authorizationConfiguration;
    }
}
