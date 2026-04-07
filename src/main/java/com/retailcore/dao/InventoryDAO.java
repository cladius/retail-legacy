package com.retailcore.dao;

import com.retailcore.entity.Inventory;
import com.retailcore.orm.BaseDAO;
import com.retailcore.orm.QueryBuilder;
import com.retailcore.orm.ResultMapper;
import com.retailcore.orm.TransactionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class InventoryDAO extends BaseDAO<Inventory> {

    private static InventoryDAO instance;

    private InventoryDAO() {
        super(Inventory.class);
    }

    public static synchronized InventoryDAO getInstance() {
        if (instance == null) {
            instance = new InventoryDAO();
        }
        return instance;
    }

    public Inventory findByProductAndStore(int productId, int storeId) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("ProductID", productId)
                .whereEquals("StoreID", storeId);
        return findOneByQuery(qb);
    }

    public List<Inventory> findByStore(int storeId) throws SQLException {
        return findByColumn("StoreID", storeId);
    }

    public List<Inventory> findByProduct(int productId) throws SQLException {
        return findByColumn("ProductID", productId);
    }

    public int adjustQuantity(int productId, int storeId, int adjustment) throws SQLException {
        String sql = "UPDATE [dbo].[tbl_Inventory] SET QuantityOnHand = QuantityOnHand + ?, " +
                "ModifiedDate = GETDATE() WHERE ProductID = ? AND StoreID = ?";
        return executeUpdate(sql, adjustment, productId, storeId);
    }

    public int reserveStock(int productId, int storeId, int quantity) throws SQLException {
        String sql = "UPDATE [dbo].[tbl_Inventory] SET QuantityReserved = QuantityReserved + ?, " +
                "ModifiedDate = GETDATE() WHERE ProductID = ? AND StoreID = ? " +
                "AND (QuantityOnHand - QuantityReserved) >= ?";
        return executeUpdate(sql, quantity, productId, storeId, quantity);
    }

    public int releaseReservation(int productId, int storeId, int quantity) throws SQLException {
        String sql = "UPDATE [dbo].[tbl_Inventory] SET QuantityReserved = QuantityReserved - ?, " +
                "ModifiedDate = GETDATE() WHERE ProductID = ? AND StoreID = ? AND QuantityReserved >= ?";
        return executeUpdate(sql, quantity, productId, storeId, quantity);
    }

    public int receiveStock(int productId, int storeId, int quantity) throws SQLException {
        String sql = "UPDATE [dbo].[tbl_Inventory] SET QuantityOnHand = QuantityOnHand + ?, " +
                "QuantityOnOrder = CASE WHEN QuantityOnOrder >= ? THEN QuantityOnOrder - ? ELSE 0 END, " +
                "LastReceivedDate = GETDATE(), ModifiedDate = GETDATE() " +
                "WHERE ProductID = ? AND StoreID = ?";
        return executeUpdate(sql, quantity, quantity, quantity, productId, storeId);
    }

    public List<Inventory> findLowStock(int storeId) throws SQLException {
        String sql = "SELECT i.* FROM [dbo].[tbl_Inventory] i " +
                "INNER JOIN [dbo].[tbl_Product] p ON i.ProductID = p.ProductID " +
                "WHERE i.StoreID = ? AND i.QuantityOnHand <= p.ReorderPoint AND p.Status = 1 " +
                "ORDER BY i.QuantityOnHand ASC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);
            setParameter(stmt, 1, storeId);
            rs = stmt.executeQuery();
            return ResultMapper.mapRows(rs, Inventory.class);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public List<Inventory> findOutOfStock(int storeId) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("StoreID", storeId)
                .whereLessOrEqual("QuantityOnHand", 0);
        return findByQuery(qb);
    }

    public int getAvailableQuantity(int productId, int storeId) throws SQLException {
        String sql = "SELECT (QuantityOnHand - QuantityReserved) FROM [dbo].[tbl_Inventory] " +
                "WHERE ProductID = ? AND StoreID = ?";
        Integer result = executeScalar(sql, Integer.class, productId, storeId);
        return result != null ? result : 0;
    }

    public int getTotalQuantityAcrossStores(int productId) throws SQLException {
        String sql = "SELECT SUM(QuantityOnHand) FROM [dbo].[tbl_Inventory] WHERE ProductID = ?";
        Integer result = executeScalar(sql, Integer.class, productId);
        return result != null ? result : 0;
    }

    public List<Map<String, Object>> getInventoryValueByStore(int storeId) throws SQLException {
        String sql = "SELECT i.ProductID, p.ProductName, p.SKU, i.QuantityOnHand, " +
                "p.UnitCost, (i.QuantityOnHand * p.UnitCost) AS TotalValue " +
                "FROM [dbo].[tbl_Inventory] i " +
                "INNER JOIN [dbo].[tbl_Product] p ON i.ProductID = p.ProductID " +
                "WHERE i.StoreID = ? AND i.QuantityOnHand > 0 " +
                "ORDER BY (i.QuantityOnHand * p.UnitCost) DESC";
        return executeQuery(sql, storeId);
    }

    public int updateBinLocation(int productId, int storeId, String binLocation, String aisle, String shelf) throws SQLException {
        String sql = "UPDATE [dbo].[tbl_Inventory] SET BinLocation = ?, AisleName = ?, ShelfNumber = ?, " +
                "ModifiedDate = GETDATE() WHERE ProductID = ? AND StoreID = ?";
        return executeUpdate(sql, binLocation, aisle, shelf, productId, storeId);
    }
}
