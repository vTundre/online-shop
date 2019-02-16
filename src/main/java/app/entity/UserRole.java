package app.entity;

public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER"),
    GUEST("GUEST");

    private final String name;

    UserRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
