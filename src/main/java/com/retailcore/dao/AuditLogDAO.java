package com.retailcore.dao;

import com.retailcore.entity.AuditLog;
import com.retailcore.orm.BaseDAO;
import com.retailcore.orm.QueryBuilder;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class AuditLogDAO extends BaseDAO<AuditLog> {

    private static AuditLogDAO instance;

    private AuditLogDAO() {
        super(AuditLog.class);
    }

    public static synchronized AuditLogDAO getInstance() {
        if (instance == null) {
            instance = new AuditLogDAO();
        }
        return instance;
    }

    public List<AuditLog> findByTable(String tableName) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("TableName", tableName)
                .orderBy("CreatedDate", QueryBuilder.SortDirection.DESC);
        return findByQuery(qb);
    }

    public List<AuditLog> findByRecord(String tableName, int recordId) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("TableName", tableName)
                .whereEquals("RecordID", recordId)
                .orderBy("CreatedDate", QueryBuilder.SortDirection.DESC);
        return findByQuery(qb);
    }

    public List<AuditLog> findByEmployee(int employeeId) throws SQLException {
        return findByColumn("EmployeeID", employeeId);
    }

    public List<AuditLog> findByDateRange(Date startDate, Date endDate) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereBetween("CreatedDate", startDate, endDate)
                .orderBy("CreatedDate", QueryBuilder.SortDirection.DESC);
        return findByQuery(qb);
    }

    public void logAction(String tableName, int recordId, String action, String oldValues, String newValues, Integer employeeId) throws SQLException {
        String sql = "INSERT INTO tbl_AuditLog (TableName, RecordID, Action, OldValues, NewValues, EmployeeID) VALUES (?, ?, ?, ?, ?, ?)";
        executeUpdate(sql, tableName, recordId, action, oldValues, newValues, employeeId);
    }

    public int purgeOldLogs(int daysToKeep) throws SQLException {
        String sql = "DELETE FROM tbl_AuditLog WHERE CreatedDate < CURRENT_TIMESTAMP - (? || ' days')::interval";
        return executeUpdate(sql, -daysToKeep);
    }
}
