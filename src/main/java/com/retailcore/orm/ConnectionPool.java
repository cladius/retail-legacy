package com.retailcore.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPool {

    private static ConnectionPool instance;
    private BlockingQueue<Connection> availableConnections;
    private BlockingQueue<Connection> usedConnections;
    private String url;
    private String username;
    private String password;
    private int maxPoolSize;
    private int initialPoolSize;
    private AtomicInteger totalCreated;
    private long connectionTimeout;
    private long validationTimeout;
    private boolean autoCommit;
    private int transactionIsolation;

    private ConnectionPool() {
        totalCreated = new AtomicInteger(0);
        connectionTimeout = 30000;
        validationTimeout = 5000;
        autoCommit = true;
        transactionIsolation = Connection.TRANSACTION_READ_COMMITTED;
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public void initialize(String url, String username, String password, int initialSize, int maxSize) throws SQLException {
        this.url = url;
        this.username = username;
        this.password = password;
        this.initialPoolSize = initialSize;
        this.maxPoolSize = maxSize;
        this.availableConnections = new ArrayBlockingQueue<>(maxSize);
        this.usedConnections = new ArrayBlockingQueue<>(maxSize);

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQL Server JDBC driver not found", e);
        }

        for (int i = 0; i < initialSize; i++) {
            availableConnections.add(createConnection());
        }
    }

    private Connection createConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(url, username, password);
        conn.setAutoCommit(autoCommit);
        conn.setTransactionIsolation(transactionIsolation);
        totalCreated.incrementAndGet();
        return conn;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            connection = availableConnections.poll(connectionTimeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("Interrupted while waiting for connection", e);
        }

        if (connection == null) {
            if (totalCreated.get() < maxPoolSize) {
                connection = createConnection();
            } else {
                throw new SQLException("Connection pool exhausted. Max pool size: " + maxPoolSize);
            }
        }

        if (!isConnectionValid(connection)) {
            connection = createConnection();
        }

        usedConnections.add(connection);
        return connection;
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            usedConnections.remove(connection);
            try {
                if (!connection.isClosed()) {
                    if (!connection.getAutoCommit()) {
                        connection.rollback();
                        connection.setAutoCommit(autoCommit);
                    }
                    availableConnections.offer(connection);
                } else {
                    totalCreated.decrementAndGet();
                }
            } catch (SQLException e) {
                totalCreated.decrementAndGet();
            }
        }
    }

    private boolean isConnectionValid(Connection connection) {
        try {
            if (connection == null || connection.isClosed()) {
                return false;
            }
            return connection.isValid((int) (validationTimeout / 1000));
        } catch (SQLException e) {
            return false;
        }
    }

    public void shutdown() {
        for (Connection conn : usedConnections) {
            try {
                conn.close();
            } catch (SQLException ignored) {}
        }
        for (Connection conn : availableConnections) {
            try {
                conn.close();
            } catch (SQLException ignored) {}
        }
        availableConnections.clear();
        usedConnections.clear();
        totalCreated.set(0);
    }

    public int getAvailableCount() {
        return availableConnections.size();
    }

    public int getUsedCount() {
        return usedConnections.size();
    }

    public int getTotalCreated() {
        return totalCreated.get();
    }

    public void setConnectionTimeout(long timeout) {
        this.connectionTimeout = timeout;
    }

    public void setValidationTimeout(long timeout) {
        this.validationTimeout = timeout;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public void setTransactionIsolation(int level) {
        this.transactionIsolation = level;
    }
}
