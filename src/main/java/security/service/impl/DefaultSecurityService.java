package security.service.impl;

import config.Config;
import app.entity.User;
import app.entity.UserRole;
import security.entity.Session;
import security.service.PasswordService;
import security.service.SecurityService;
import app.service.UserService;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DefaultSecurityService implements SecurityService {
    private static final int sessionExpireHours = Integer.parseInt(Config.getProperties().getProperty("session.expire.hours"));
    private static List<Session> sessionList = new ArrayList<>();

    private UserService userService;
    private PasswordService passwordService;

    public DefaultSecurityService(UserService userService, PasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    @Override
    public boolean hasAccess(Cookie[] cookies) {
        List<String> tokensList = getTokensFromCookies(cookies);
        Session session = getSessionByTokens(tokensList);
        return session != null;
    }

    @Override
    public boolean hasRoleAccess(Cookie[] cookies, UserRole role) {
        List<String> tokensList = getTokensFromCookies(cookies);
        Session session = getSessionByTokens(tokensList);
        if (session == null) {
            return false;
        }
        User user = session.getUser();
        return role.getName().equalsIgnoreCase(user.getRole());
    }

    @Override
    public void deleteSession(Cookie[] cookies) {
        List<String> tokensList = getTokensFromCookies(cookies);
        for (Session session : sessionList) {
            if (tokensList.contains(session.getToken())) {
                sessionList.remove(session);
                System.out.println("Session has been deleted");
                break;
            }
        }
    }

    @Override
    public String getSessionTokenByNameAndPassword(String name, String password) {
        Session session = getSessionByNameAndPassword(name, password);
        return session != null ? session.getToken() : "";
    }

    private Session getSessionByNameAndPassword(String name, String password) {
        User user = userService.getByName(name);

        //has access
        if (user != null && passwordService.check(password, user.getPassword())) {
            //check if there is a valid session for this user
            for (Session session : sessionList) {
                if (session.getUser().getName().equals(name)) {
                    if (session.getExpireDate().isAfter(LocalDateTime.now())) {
                        return session;
                    }
                    sessionList.remove(session);
                    System.out.println("Expired session has been deleted");
                    break;
                }
            }
            //No valid session found, create new one
            String token = UUID.randomUUID().toString();
            Session session = new Session(token, user, LocalDateTime.now().plusHours(sessionExpireHours));
            sessionList.add(session);
            System.out.println("New session has been created");
            return session;
        }
        return null;
    }

    private Session getSessionByTokens(List<String> tokenList) {
        for (Session session : sessionList) {
            if (tokenList.contains(session.getToken())) {
                if (session.getExpireDate().isAfter(LocalDateTime.now())) {
                    return session;
                }
                sessionList.remove(session);
                System.out.println("Expired session has been deleted");
                break;
            }
        }
        return null;
    }

    private List<String> getTokensFromCookies(Cookie[] cookies) {
        List<String> tokensList = new ArrayList<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user-token")) {
                    tokensList.add(cookie.getValue());
                }
            }
        }
        return tokensList;
    }

    @Override
    public PasswordService getPasswordService() {
        return passwordService;
    }
}
