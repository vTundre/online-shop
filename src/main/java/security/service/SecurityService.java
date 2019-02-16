package security.service;

import app.entity.UserRole;

import javax.servlet.http.Cookie;

public interface SecurityService {
    boolean hasAccess(Cookie[] cookies);

    boolean hasRoleAccess(Cookie[] cookies, UserRole userRole);

    void deleteSession(Cookie[] cookies);

    String getSessionTokenByNameAndPassword(String name, String password);

    PasswordService getPasswordService();
}
