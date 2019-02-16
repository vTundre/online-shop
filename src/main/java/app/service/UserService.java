package app.service;

import app.entity.User;

public interface UserService {

    User getByName(String name);

    void insert(User user);
}
