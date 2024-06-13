package authorization;

public enum ActionType {
    READ("read"), WRITE("write");

    private final String value;

    ActionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
