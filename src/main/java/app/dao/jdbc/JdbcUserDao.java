package app.dao.jdbc;

import app.dao.UserDao;
import app.dao.jdbc.mapper.UserMapper;
import app.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class JdbcUserDao implements UserDao {
    private static final String SQL_FIND_BY_NAME = "select id, name, password, role from public.user where name = ?";
    private static final String SQL_INSERT = "insert into public.user values(nextval('user_seq'), ?, ?, ?)";

    @Autowired
    private DataSource dataSource;

    @Override
    public User getByName(String name) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<User> users = jdbcTemplate.query(SQL_FIND_BY_NAME, new UserMapper(), name);
        return users.size() == 0 ? null : users.get(0);
    }

    @Override
    public void insert(User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(SQL_INSERT, user.getName(), user.getPassword(), user.getRole());
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
