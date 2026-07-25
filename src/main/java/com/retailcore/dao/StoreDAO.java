package com.retailcore.dao;

import com.retailcore.entity.Store;
import com.retailcore.orm.BaseDAO;
import com.retailcore.orm.QueryBuilder;
import com.retailcore.orm.TransactionManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class StoreDAO extends BaseDAO<Store> {

    private static StoreDAO instance;

    private StoreDAO() {
        super(Store.class);
    }

    public static synchronized StoreDAO getInstance() {
        if (instance == null) {
            instance = new StoreDAO();
        }
        return instance;
    }

    public Store findByStoreCode(String storeCode) throws SQLException {
        return findOneByColumn("StoreCode", storeCode);
    }

    public List<Store> findByRegion(int regionId) throws SQLException {
        return findByColumn("RegionID", regionId);
    }

    public List<Store> findByCity(String city) throws SQLException {
        return findByColumn("City", city);
    }

    public List<Store> findByState(String state) throws SQLException {
        return findByColumn("StateProvince", state);
    }

    public List<Store> findByType(int storeType) throws SQLException {
        return findByColumn("StoreType", storeType);
    }

    public List<Map<String, Object>> getStoreSalesSummary(Date startDate, Date endDate) throws SQLException {
        String sql = "SELECT s.StoreID, s.StoreName, s.StoreCode, " +
                "COUNT(t.TransactionID) AS TransactionCount, " +
                "COALESCE(SUM(t.GrandTotal), 0) AS TotalSales, " +
                "COALESCE(AVG(t.GrandTotal), 0) AS AvgTransaction " +
                "FROM tbl_Store s " +
                "LEFT JOIN tbl_Transaction t ON s.StoreID = t.StoreID " +
                "AND t.TransactionDate BETWEEN ? AND ? AND t.Status = 1 " +
                "WHERE s.IsActive = 1 " +
                "GROUP BY s.StoreID, s.StoreName, s.StoreCode " +
                "ORDER BY TotalSales DESC";
        return executeQuery(sql, startDate, endDate);
    }

    public BigDecimal getStoreRevenue(int storeId, Date startDate, Date endDate) throws SQLException {
        String sql = "SELECT COALESCE(SUM(GrandTotal), 0) FROM tbl_Transaction " +
                "WHERE StoreID = ? AND TransactionDate BETWEEN ? AND ? AND Status = 1";
        return executeScalar(sql, BigDecimal.class, storeId, startDate, endDate);
    }

    public int getStoreEmployeeCount(int storeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM tbl_Employee WHERE StoreID = ? AND IsActive = 1";
        return executeScalar(sql, Integer.class, storeId);
    }

    public int getStoreProductCount(int storeId) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT ProductID) FROM tbl_Inventory WHERE StoreID = ? AND QuantityOnHand > 0";
        return executeScalar(sql, Integer.class, storeId);
    }
}
