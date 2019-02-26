package app.service.impl;

import app.dao.UserDao;
import app.entity.User;
import app.entity.UserRole;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserService implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User getByName(String name) {
        return userDao.getByName(name);
    }

    @Override
    public void insert(User user) {
        userDao.insert(user);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
