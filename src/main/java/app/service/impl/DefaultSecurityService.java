package app.service.impl;

import app.config.Config;
import app.entity.Session;
import app.entity.User;
import app.entity.UserRole;
import app.service.PasswordService;
import app.service.SecurityService;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DefaultSecurityService implements SecurityService {
    private static final int sessionExpireHours = Integer.parseInt(Config.getProperties().getProperty("session.expire.hours"));
    private List<Session> sessionList = new ArrayList<>();

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordService passwordService;


    @Override
    public boolean hasAccess(List<String> tokens) {
        Session session = getSessionByToken(tokens);
        return session != null;
    }

    @Override
    public boolean hasRoleAccess(List<String> tokens, UserRole role) {
        Session session = getSessionByToken(tokens);
        if (session == null) {
            return false;
        }
        User user = session.getUser();
        return role.getName().equalsIgnoreCase(user.getRole());
    }

    @Override
    public void deleteSession(List<String> tokens) {
        for (Session session : sessionList) {
            if (tokens.contains(session.getToken())) {
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

    private Session getSessionByToken(List<String> tokens) {
        for (Session session : sessionList) {
            if (tokens.contains(session.getToken())) {
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

    public PasswordService getPasswordService() {
        return passwordService;
    }

    public void setPasswordService(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
