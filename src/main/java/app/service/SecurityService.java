package app.service;

import app.entity.UserRole;

import java.util.List;

public interface SecurityService {
    boolean hasAccess(List<String> tokens);

    boolean hasRoleAccess(List<String> tokens, UserRole userRole);

    void deleteSession(List<String> tokens);

    String getSessionTokenByNameAndPassword(String name, String password);

    PasswordService getPasswordService();
}
