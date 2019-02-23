package service;

import app.dao.ProductDao;
import app.dao.UserDao;
import app.dao.jdbc.JDBCProductDao;
import app.dao.jdbc.JDBCUserDao;
import app.service.ProductService;
import app.service.UserService;
import app.service.impl.DefaultProductService;
import app.service.impl.DefaultUserService;
import app.web.page.PageGenerator;
import config.Config;
import org.postgresql.ds.PGSimpleDataSource;
import security.service.PasswordService;
import security.service.SecurityService;
import security.service.impl.DefaultPasswordService;
import security.service.impl.DefaultSecurityService;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
    private static final String PROPERTIES_LOCATION = "config/application.properties";
    private static final Map<Class<?>, Object> SERVICES = new HashMap<>();

    static {
        //config
        Config.setPropertiesLocation(PROPERTIES_LOCATION);
        PageGenerator.setTemplatePath(Config.getTemplatePath());

        //db connection
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUser(Config.getDbUser());
        dataSource.setPassword(Config.getDbPassword());
        dataSource.setURL(Config.getDbURL());

        //dao
        UserDao userDao = new JDBCUserDao(dataSource);
        ProductDao productDao = new JDBCProductDao(dataSource);

        //services
        UserService userService = new DefaultUserService(userDao);
        ProductService productService = new DefaultProductService(productDao);
        PasswordService passwordService = new DefaultPasswordService();
        SecurityService securityService = new DefaultSecurityService(userService, passwordService);

        SERVICES.put(UserService.class, userService);
        SERVICES.put(ProductService.class, productService);
        SERVICES.put(PasswordService.class, passwordService);
        SERVICES.put(SecurityService.class, securityService);
        SERVICES.put(String.class, PROPERTIES_LOCATION);
    }

    public static <T> T getService(Class<T> clazz) {
        return clazz.cast(SERVICES.get(clazz));
    }
}