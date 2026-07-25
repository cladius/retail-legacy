package com.retailcore.dao;

import com.retailcore.entity.Customer;
import com.retailcore.orm.BaseDAO;
import com.retailcore.orm.QueryBuilder;
import com.retailcore.orm.ResultMapper;
import com.retailcore.orm.TransactionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CustomerDAO extends BaseDAO<Customer> {

    private static CustomerDAO instance;

    private CustomerDAO() {
        super(Customer.class);
    }

    public static synchronized CustomerDAO getInstance() {
        if (instance == null) {
            instance = new CustomerDAO();
        }
        return instance;
    }

    public Customer findByCustomerNumber(String customerNumber) throws SQLException {
        return findOneByColumn("CustomerNumber", customerNumber);
    }

    public Customer findByEmail(String email) throws SQLException {
        return findOneByColumn("Email", email);
    }

    public List<Customer> findByLastName(String lastName) throws SQLException {
        return findByColumn("LastName", lastName);
    }

    public List<Customer> searchByName(String searchTerm) throws SQLException {
        String sql = "SELECT * FROM tbl_Customer WHERE " +
                "(FirstName LIKE ? OR LastName LIKE ? OR Email LIKE ?) AND IsActive = 1 " +
                "ORDER BY LastName, FirstName";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);
            String pattern = "%" + searchTerm + "%";
            setParameter(stmt, 1, pattern);
            setParameter(stmt, 2, pattern);
            setParameter(stmt, 3, pattern);
            rs = stmt.executeQuery();
            return ResultMapper.mapRows(rs, Customer.class);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public List<Customer> findByLoyaltyTier(int tier) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("LoyaltyTier", tier)
                .whereEquals("IsActive", true)
                .orderBy("TotalSpend", QueryBuilder.SortDirection.DESC);
        return findByQuery(qb);
    }

    public List<Customer> findByPreferredStore(int storeId) throws SQLException {
        return findByColumn("PreferredStoreID", storeId);
    }

    public int updateLoyaltyPoints(int customerId, int pointsDelta) throws SQLException {
        String sql = "UPDATE tbl_Customer SET LoyaltyPoints = LoyaltyPoints + ?, " +
                "ModifiedDate = CURRENT_TIMESTAMP WHERE CustomerID = ?";
        return executeUpdate(sql, pointsDelta, customerId);
    }

    public int updateLoyaltyTier(int customerId, byte newTier) throws SQLException {
        String sql = "UPDATE tbl_Customer SET LoyaltyTier = ?, ModifiedDate = CURRENT_TIMESTAMP WHERE CustomerID = ?";
        return executeUpdate(sql, newTier, customerId);
    }

    public int recordVisit(int customerId, BigDecimal spendAmount) throws SQLException {
        String sql = "UPDATE tbl_Customer SET VisitCount = VisitCount + 1, " +
                "TotalSpend = TotalSpend + ?, LastVisitDate = CURRENT_TIMESTAMP, " +
                "ModifiedDate = CURRENT_TIMESTAMP WHERE CustomerID = ?";
        return executeUpdate(sql, spendAmount, customerId);
    }

    public List<Customer> findTopSpenders(int limit) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .top(limit)
                .from(metadata.getFullTableName())
                .whereEquals("IsActive", true)
                .orderBy("TotalSpend", QueryBuilder.SortDirection.DESC);
        return findByQuery(qb);
    }

    public List<Customer> findInactiveCustomers(int daysSinceLastVisit) throws SQLException {
        String sql = "SELECT * FROM tbl_Customer WHERE IsActive = 1 " +
                "AND LastVisitDate < CURRENT_TIMESTAMP - (? || ' days')::interval " +
                "ORDER BY LastVisitDate ASC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);
            setParameter(stmt, 1, -daysSinceLastVisit);
            rs = stmt.executeQuery();
            return ResultMapper.mapRows(rs, Customer.class);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public String generateCustomerNumber() throws SQLException {
        String sql = "SELECT 'C' || RIGHT('00000000' || CAST(COALESCE(MAX(CAST(SUBSTRING(CustomerNumber, 2, 8) AS INT)), 0) + 1 AS VARCHAR), 8) " +
                "FROM tbl_Customer";
        return executeScalar(sql, String.class);
    }

    public int countByStore(int storeId) throws SQLException {
        return countByColumn("PreferredStoreID", storeId);
    }

    public BigDecimal getAverageSpend() throws SQLException {
        String sql = "SELECT AVG(TotalSpend) FROM tbl_Customer WHERE IsActive = 1 AND VisitCount > 0";
        return executeScalar(sql, BigDecimal.class);
    }
}
