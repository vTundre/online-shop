package app.dao.jdbc;

import app.dao.DatabaseDAO;
import security.service.PasswordService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

public class JDBCDatabaseDAO implements DatabaseDAO {
    private DataSource dataSource;
    private PasswordService passwordService;

    public JDBCDatabaseDAO(DataSource dataSource, PasswordService passwordService) {
        this.dataSource = dataSource;
        this.passwordService = passwordService;
    }

    @Override
    public void init() {
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()
        ) {
            runStatement(statement, "drop sequence user_seq");
            runStatement(statement, "drop table public.user");
            runStatement(statement, "create sequence user_seq");
            runStatement(statement, "create table public.user(id integer, name varchar(1000, password varchar(1000), role varchar(1000))");

            runStatement(statement, "drop sequence product_seq");
            runStatement(statement, "drop table product");
            runStatement(statement, "create sequence product_seq");
            runStatement(statement, "create table product(id integer, name varchar(100), description varchar(100), price numeric)");

            runStatement(statement, "insert into public.user values(nextval('user_seq'), 'admin', '" + passwordService.getSaltedHash("1") + "', 'ADMIN')");
            runStatement(statement, "insert into public.user values(nextval('user_seq'), 'user', '" + passwordService.getSaltedHash("1") + "', 'USER')");
        } catch (Exception e) {
            System.out.println("Database setup error");
        }
    }

    private void runStatement(Statement statement, String SQL) {
        try {
            statement.executeUpdate(SQL);
        } catch (Exception e) {
            System.out.println("Statement execution error");
        }
    }
}
