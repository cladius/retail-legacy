package com.retailcore.orm;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

public class TransactionManager {

    private static final ThreadLocal<Connection> transactionConnection = new ThreadLocal<>();
    private static final ThreadLocal<Integer> transactionDepth = new ThreadLocal<>();

    public static void begin() throws SQLException {
        Connection conn = transactionConnection.get();
        if (conn != null) {
            Integer depth = transactionDepth.get();
            transactionDepth.set(depth != null ? depth + 1 : 1);
            return;
        }

        conn = ConnectionPool.getInstance().getConnection();
        conn.setAutoCommit(false);
        transactionConnection.set(conn);
        transactionDepth.set(1);
    }

    public static void commit() throws SQLException {
        Integer depth = transactionDepth.get();
        if (depth == null || depth <= 0) {
            throw new SQLException("No active transaction to commit");
        }

        if (depth == 1) {
            Connection conn = transactionConnection.get();
            if (conn != null) {
                conn.commit();
                conn.setAutoCommit(true);
                ConnectionPool.getInstance().releaseConnection(conn);
                transactionConnection.remove();
            }
            transactionDepth.remove();
        } else {
            transactionDepth.set(depth - 1);
        }
    }

    public static void rollback() {
        Integer depth = transactionDepth.get();
        Connection conn = transactionConnection.get();

        if (conn != null) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException ignored) {
            } finally {
                ConnectionPool.getInstance().releaseConnection(conn);
                transactionConnection.remove();
                transactionDepth.remove();
            }
        }
    }

    public static Savepoint setSavepoint(String name) throws SQLException {
        Connection conn = transactionConnection.get();
        if (conn == null) {
            throw new SQLException("No active transaction for savepoint");
        }
        return conn.setSavepoint(name);
    }

    public static void rollbackToSavepoint(Savepoint savepoint) throws SQLException {
        Connection conn = transactionConnection.get();
        if (conn == null) {
            throw new SQLException("No active transaction for rollback to savepoint");
        }
        conn.rollback(savepoint);
    }

    public static Connection getCurrentConnection() {
        return transactionConnection.get();
    }

    public static boolean isInTransaction() {
        return transactionConnection.get() != null;
    }

    public static Connection getConnectionForOperation() throws SQLException {
        Connection conn = transactionConnection.get();
        if (conn != null) {
            return conn;
        }
        return ConnectionPool.getInstance().getConnection();
    }

    public static void releaseConnectionIfNotInTransaction(Connection conn) {
        if (!isInTransaction() && conn != null) {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }
}
