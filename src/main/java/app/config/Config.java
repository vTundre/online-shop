package app.config;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final String PROPERTIES_LOCATION = "config/application.properties";
    private static Config config;
    private static Properties properties;

    private static int port;
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    public static Properties getProperties() {
        return instance().properties;
    }

    public static int getPort() {
        return port;
    }

    public static String getTemplatePath() {
        return instance().properties.getProperty("template.path");
    }

    public static String getDbURL() {
        return instance().properties.getProperty("db.type") + dbUrl;
    }

    public static String getDbUser() {
        return dbUser;
    }

    public static String getDbPassword() {
        return dbPassword;
    }

    private static synchronized Config instance() {
        if (config == null) {

            config = new Config();
            config.properties = new Properties();
            try (InputStream resourceAsStream = config.getClass().getClassLoader().getResourceAsStream(PROPERTIES_LOCATION)
            ) {
                config.properties.load(resourceAsStream);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            //heroku
            String envPort = System.getenv("PORT");
            if (envPort == null) {
                port = Integer.parseInt(instance().properties.getProperty("port"));
            } else {
                port = Integer.parseInt(envPort);
            }

            //heroku
            String envURL = System.getenv("DATABASE_URL");
            if (envURL == null) {
                dbUrl = instance().properties.getProperty("db.url");
                dbUser = instance().properties.getProperty("db.user");
                dbPassword = instance().properties.getProperty("db.password");
            } else {
                dbUrl = envURL.split("@")[1];
                dbUser = envURL.split(":")[1].substring(2);
                dbPassword = envURL.split(":")[2].split("@")[0];
            }
        }
        return config;
    }

    private Config() {
    }
}
