package com.retailcore.dao;

import com.retailcore.entity.Employee;
import com.retailcore.orm.BaseDAO;
import com.retailcore.orm.QueryBuilder;
import com.retailcore.orm.ResultMapper;
import com.retailcore.orm.TransactionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class EmployeeDAO extends BaseDAO<Employee> {

    private static EmployeeDAO instance;

    private EmployeeDAO() {
        super(Employee.class);
    }

    public static synchronized EmployeeDAO getInstance() {
        if (instance == null) {
            instance = new EmployeeDAO();
        }
        return instance;
    }

    public Employee findByEmployeeNumber(String employeeNumber) throws SQLException {
        return findOneByColumn("EmployeeNumber", employeeNumber);
    }

    public Employee findByPinCode(String pinCode) throws SQLException {
        return findOneByColumn("PinCode", pinCode);
    }

    public List<Employee> findByStore(int storeId) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("StoreID", storeId)
                .whereEquals("IsActive", true)
                .orderBy("LastName", QueryBuilder.SortDirection.ASC);
        return findByQuery(qb);
    }

    public List<Employee> findByDepartment(int departmentId) throws SQLException {
        return findByColumn("DepartmentID", departmentId);
    }

    public List<Employee> findByManager(int managerEmployeeId) throws SQLException {
        return findByColumn("ManagerEmployeeID", managerEmployeeId);
    }

    public List<Employee> findByAccessLevel(int accessLevel) throws SQLException {
        return findByColumn("AccessLevel", accessLevel);
    }

    public Employee authenticateByPin(int storeId, String pinCode) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("StoreID", storeId)
                .whereEquals("PinCode", pinCode)
                .whereEquals("IsActive", true);
        return findOneByQuery(qb);
    }

    public int terminate(int employeeId) throws SQLException {
        String sql = "UPDATE tbl_Employee SET IsActive = 0, TerminationDate = CURRENT_TIMESTAMP, " +
                "ModifiedDate = CURRENT_TIMESTAMP WHERE EmployeeID = ?";
        return executeUpdate(sql, employeeId);
    }

    public List<Map<String, Object>> getEmployeeSalesPerformance(int storeId, java.util.Date startDate, java.util.Date endDate) throws SQLException {
        String sql = "SELECT e.EmployeeID, e.FirstName || ' ' || e.LastName AS EmployeeName, " +
                "COUNT(t.TransactionID) AS TransactionCount, " +
                "SUM(t.GrandTotal) AS TotalSales, " +
                "AVG(t.GrandTotal) AS AvgTransaction " +
                "FROM tbl_Employee e " +
                "LEFT JOIN tbl_Transaction t ON e.EmployeeID = t.EmployeeID " +
                "AND t.TransactionDate BETWEEN ? AND ? AND t.Status = 1 " +
                "WHERE e.StoreID = ? AND e.IsActive = 1 " +
                "GROUP BY e.EmployeeID, e.FirstName, e.LastName " +
                "ORDER BY TotalSales DESC";
        return executeQuery(sql, startDate, endDate, storeId);
    }

    public String generateEmployeeNumber() throws SQLException {
        String sql = "SELECT 'E' || RIGHT('00000' || CAST(COALESCE(MAX(CAST(SUBSTRING(EmployeeNumber, 2, 5) AS INT)), 0) + 1 AS VARCHAR), 5) " +
                "FROM tbl_Employee";
        return executeScalar(sql, String.class);
    }
}
