package app.dao.jdbc.mapper;

import app.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name").toLowerCase());
        user.setPassword(resultSet.getString("password"));
        user.setRole(resultSet.getString("role").toUpperCase());
        return user;
    }
}
