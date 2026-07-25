package com.retailcore.config;

import com.retailcore.orm.ConnectionPool;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {

    private static DatabaseConfig instance;
    private Properties properties;
    private boolean initialized;

    private DatabaseConfig() {
        properties = new Properties();
        initialized = false;
    }

    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    public void initialize() throws SQLException {
        if (initialized) {
            return;
        }

        loadProperties();

        String url = properties.getProperty("db.url", "jdbc:sqlserver://localhost:1433;databaseName=RetailCoreDB");
        String driverClass = properties.getProperty("db.driver", "").trim();
        String username = properties.getProperty("db.username", "sa");
        String password = properties.getProperty("db.password", "");
        int initialPoolSize = Integer.parseInt(properties.getProperty("db.pool.initial", "5"));
        int maxPoolSize = Integer.parseInt(properties.getProperty("db.pool.max", "20"));

        ConnectionPool pool = ConnectionPool.getInstance();
        pool.setConnectionTimeout(Long.parseLong(properties.getProperty("db.connection.timeout", "30000")));
        pool.setValidationTimeout(Long.parseLong(properties.getProperty("db.validation.timeout", "5000")));
        pool.initialize(url, username, password, initialPoolSize, maxPoolSize, driverClass);

        initialized = true;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input != null) {
                properties.load(input);
                return;
            }
        } catch (IOException ignored) {}

        try (FileInputStream fis = new FileInputStream("config/database.properties")) {
            properties.load(fis);
        } catch (IOException ignored) {}
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public void shutdown() {
        ConnectionPool.getInstance().shutdown();
        initialized = false;
    }
}
