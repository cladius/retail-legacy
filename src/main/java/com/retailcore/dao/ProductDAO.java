package com.retailcore.dao;

import com.retailcore.entity.Product;
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

public class ProductDAO extends BaseDAO<Product> {

    private static ProductDAO instance;

    private ProductDAO() {
        super(Product.class);
    }

    public static synchronized ProductDAO getInstance() {
        if (instance == null) {
            instance = new ProductDAO();
        }
        return instance;
    }

    public Product findBySku(String sku) throws SQLException {
        return findOneByColumn("SKU", sku);
    }

    public Product findByUpc(String upc) throws SQLException {
        return findOneByColumn("UPC", upc);
    }

    public List<Product> findByCategory(int categoryId) throws SQLException {
        return findByColumn("CategoryID", categoryId);
    }

    public List<Product> findByVendor(int vendorId) throws SQLException {
        return findByColumn("VendorID", vendorId);
    }

    public List<Product> findByBrand(String brand) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("Brand", brand)
                .whereEquals("Status", 1)
                .orderBy("ProductName", QueryBuilder.SortDirection.ASC);
        return findByQuery(qb);
    }

    public List<Product> findByStatus(int status) throws SQLException {
        return findByColumn("Status", status);
    }

    public List<Product> searchByName(String searchTerm) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereLike("ProductName", "%" + searchTerm + "%")
                .orderBy("ProductName", QueryBuilder.SortDirection.ASC);
        return findByQuery(qb);
    }

    public List<Product> findOnSale() throws SQLException {
        String sql = "SELECT * FROM [dbo].[tbl_Product] WHERE SalePrice IS NOT NULL " +
                "AND SaleStartDate <= GETDATE() AND SaleEndDate >= GETDATE() AND Status = 1";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            return ResultMapper.mapRows(rs, Product.class);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public List<Product> findLowStock(int storeId) throws SQLException {
        String sql = "SELECT p.* FROM [dbo].[tbl_Product] p " +
                "INNER JOIN [dbo].[tbl_Inventory] i ON p.ProductID = i.ProductID " +
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
            return ResultMapper.mapRows(rs, Product.class);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereBetween("RetailPrice", minPrice, maxPrice)
                .whereEquals("Status", 1)
                .orderBy("RetailPrice", QueryBuilder.SortDirection.ASC);
        return findByQuery(qb);
    }

    public int updatePrice(int productId, BigDecimal newPrice, int employeeId) throws SQLException {
        String insertHistory = "INSERT INTO [dbo].[tbl_PriceHistory] " +
                "(ProductID, OldRetailPrice, NewRetailPrice, ChangedByEmployeeID, EffectiveDate) " +
                "SELECT ProductID, RetailPrice, ?, ?, GETDATE() FROM [dbo].[tbl_Product] WHERE ProductID = ?";
        String updateProduct = "UPDATE [dbo].[tbl_Product] SET RetailPrice = ?, ModifiedDate = GETDATE() WHERE ProductID = ?";

        Connection conn = null;
        PreparedStatement stmtHistory = null;
        PreparedStatement stmtUpdate = null;

        try {
            TransactionManager.begin();
            conn = TransactionManager.getConnectionForOperation();

            stmtHistory = conn.prepareStatement(insertHistory);
            setParameter(stmtHistory, 1, newPrice);
            setParameter(stmtHistory, 2, employeeId);
            setParameter(stmtHistory, 3, productId);
            stmtHistory.executeUpdate();

            stmtUpdate = conn.prepareStatement(updateProduct);
            setParameter(stmtUpdate, 1, newPrice);
            setParameter(stmtUpdate, 2, productId);
            int result = stmtUpdate.executeUpdate();

            TransactionManager.commit();
            return result;
        } catch (SQLException e) {
            TransactionManager.rollback();
            throw e;
        } finally {
            closeStatement(stmtHistory);
            closeStatement(stmtUpdate);
        }
    }

    public List<Product> findTopSelling(int storeId, int limit) throws SQLException {
        String sql = "SELECT TOP (?) p.* FROM [dbo].[tbl_Product] p " +
                "INNER JOIN [dbo].[tbl_TransactionItem] ti ON p.ProductID = ti.ProductID " +
                "INNER JOIN [dbo].[tbl_Transaction] t ON ti.TransactionID = t.TransactionID " +
                "WHERE t.StoreID = ? AND t.Status = 1 AND ti.IsVoided = 0 " +
                "GROUP BY p.ProductID, p.SKU, p.UPC, p.ProductName, p.ShortDescription, " +
                "p.LongDescription, p.CategoryID, p.VendorID, p.Brand, p.ModelNumber, " +
                "p.UnitCost, p.RetailPrice, p.SalePrice, p.SaleStartDate, p.SaleEndDate, " +
                "p.Weight, p.WeightUnit, p.Length, p.Width, p.Height, p.DimensionUnit, " +
                "p.Color, p.Size, p.Material, p.IsTaxable, p.IsDiscountable, p.IsReturnable, " +
                "p.ReturnWindowDays, p.MinStockLevel, p.MaxStockLevel, p.ReorderPoint, " +
                "p.ReorderQuantity, p.Status, p.ImagePath, p.CreatedDate, p.ModifiedDate " +
                "ORDER BY SUM(ti.Quantity) DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);
            setParameter(stmt, 1, limit);
            setParameter(stmt, 2, storeId);
            rs = stmt.executeQuery();
            return ResultMapper.mapRows(rs, Product.class);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public BigDecimal getAverageMargin(int categoryId) throws SQLException {
        String sql = "SELECT AVG(RetailPrice - UnitCost) FROM [dbo].[tbl_Product] WHERE CategoryID = ? AND Status = 1";
        return executeScalar(sql, BigDecimal.class, categoryId);
    }

    public int countActiveByCategory(int categoryId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM [dbo].[tbl_Product] WHERE CategoryID = ? AND Status = 1";
        return executeScalar(sql, Integer.class, categoryId);
    }
}
