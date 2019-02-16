package app.dao;

import app.entity.User;

public interface UserDao {

    User getByName(String name);

    void insert(User user);
}
