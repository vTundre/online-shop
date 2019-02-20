package app.dao.jdbc.mapper;

import app.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {
    public static User mapRow(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name").toLowerCase());
        user.setPassword(resultSet.getString("password"));
        user.setRole(resultSet.getString("role").toUpperCase());
        return user;
    }
}
