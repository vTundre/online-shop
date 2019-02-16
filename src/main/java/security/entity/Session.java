package security.entity;

import app.entity.User;

import java.time.LocalDateTime;

public class Session {
    private String token;
    private User user;
    private LocalDateTime expireDate;

    public Session(String token, User user, LocalDateTime expireDate) {
        this.token = token;
        this.user = user;
        this.expireDate = expireDate;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

}
