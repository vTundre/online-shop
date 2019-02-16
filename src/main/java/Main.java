import app.entity.UserRole;
import config.Config;
import app.dao.DatabaseDAO;
import app.dao.ProductDao;
import app.dao.UserDao;
import app.dao.jdbc.JDBCDatabaseDAO;
import app.dao.jdbc.JDBCProductDao;
import app.dao.jdbc.JDBCUserDao;
import security.service.PasswordService;
import security.service.SecurityService;
import security.service.impl.DefaultPasswordService;
import security.web.filter.AuthFilter;
import security.service.impl.DefaultSecurityService;
import security.web.filter.RoleFilter;
import security.web.servlet.LoginServlet;
import security.web.servlet.LogoutServlet;
import security.web.servlet.UserAddServlet;
import app.service.ProductService;
import app.service.UserService;
import app.service.impl.DefaultProductService;
import app.service.impl.DefaultUserService;
import app.web.page.PageGenerator;
import app.web.servlet.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.postgresql.ds.PGSimpleDataSource;

import javax.servlet.DispatcherType;
import java.util.EnumSet;


public class Main {
    private static final String PROPERTIES_LOCATION = "src/main/resources/config/application.properties";

    public static void main(String[] args) throws Exception {

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

        //initial database setup
        DatabaseDAO databaseDAO = new JDBCDatabaseDAO(dataSource, passwordService);
        databaseDAO.init();

        //servlets
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new LoginServlet(securityService)), "/");
        context.addServlet(new ServletHolder(new LoginServlet(securityService)), "/login");
        context.addServlet(new ServletHolder(new LogoutServlet(securityService)), "/logout");
        context.addServlet(new ServletHolder(new UserAddServlet(userService, securityService)), "/user/add/*");

        context.addServlet(new ServletHolder(new ProductShowServlet(productService, securityService)), "/products");
        context.addServlet(new ServletHolder(new ProductAddServlet(productService)), "/product/add/*");
        context.addServlet(new ServletHolder(new ProductEditServlet(productService)), "/product/edit/*");
        context.addServlet(new ServletHolder(new ProductDeleteServlet(productService)), "/product/delete/*");

        //filters
        //authorization filters
        context.addFilter(new FilterHolder(new AuthFilter(securityService)), "/products",
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
        context.addFilter(new FilterHolder(new AuthFilter(securityService)), "/product/*",
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));

        //admin filters
        context.addFilter(new FilterHolder(new RoleFilter(securityService, UserRole.ADMIN)), "/product/*",
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));

        //user filters
        context.addFilter(new FilterHolder(new RoleFilter(securityService, UserRole.USER)), "/product/login",
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
        context.addFilter(new FilterHolder(new RoleFilter(securityService, UserRole.USER)), "/product/products",
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));

        //Start
        Server server = new Server(Config.getPort());
        server.setHandler(context);
        server.start();
    }
}