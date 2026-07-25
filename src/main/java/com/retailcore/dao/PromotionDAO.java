package com.retailcore.dao;

import com.retailcore.entity.Promotion;
import com.retailcore.orm.BaseDAO;
import com.retailcore.orm.QueryBuilder;
import com.retailcore.orm.ResultMapper;
import com.retailcore.orm.TransactionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PromotionDAO extends BaseDAO<Promotion> {

    private static PromotionDAO instance;

    private PromotionDAO() {
        super(Promotion.class);
    }

    public static synchronized PromotionDAO getInstance() {
        if (instance == null) {
            instance = new PromotionDAO();
        }
        return instance;
    }

    public Promotion findByPromotionCode(String promotionCode) throws SQLException {
        return findOneByColumn("PromotionCode", promotionCode);
    }

    public List<Promotion> findActivePromotions() throws SQLException {
        String sql = "SELECT * FROM tbl_Promotion WHERE IsActive = 1 " +
                "AND StartDate <= CURRENT_TIMESTAMP AND EndDate >= CURRENT_TIMESTAMP " +
                "ORDER BY StartDate ASC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            return ResultMapper.mapRows(rs, Promotion.class);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public List<Promotion> findByCategory(int categoryId) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("ApplicableCategoryID", categoryId)
                .whereEquals("IsActive", true);
        return findByQuery(qb);
    }

    public List<Promotion> findByProduct(int productId) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("ApplicableProductID", productId)
                .whereEquals("IsActive", true);
        return findByQuery(qb);
    }

    public List<Promotion> findByStore(int storeId) throws SQLException {
        String sql = "SELECT * FROM tbl_Promotion WHERE IsActive = 1 " +
                "AND (ApplicableStoreID IS NULL OR ApplicableStoreID = ?) " +
                "AND StartDate <= CURRENT_TIMESTAMP AND EndDate >= CURRENT_TIMESTAMP";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);
            setParameter(stmt, 1, storeId);
            rs = stmt.executeQuery();
            return ResultMapper.mapRows(rs, Promotion.class);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public int incrementUsage(int promotionId) throws SQLException {
        String sql = "UPDATE tbl_Promotion SET UsageCount = UsageCount + 1, " +
                "ModifiedDate = CURRENT_TIMESTAMP WHERE PromotionID = ?";
        return executeUpdate(sql, promotionId);
    }

    public boolean isPromotionValid(int promotionId, int customerId) throws SQLException {
        String sql = "SELECT CASE WHEN EXISTS (" +
                "SELECT 1 FROM tbl_Promotion p WHERE p.PromotionID = ? AND p.IsActive = 1 " +
                "AND p.StartDate <= CURRENT_TIMESTAMP AND p.EndDate >= CURRENT_TIMESTAMP " +
                "AND (p.UsageLimit IS NULL OR p.UsageCount < p.UsageLimit) " +
                "AND (p.RequiresLoyaltyTier IS NULL OR EXISTS (" +
                "SELECT 1 FROM tbl_Customer c WHERE c.CustomerID = ? AND c.LoyaltyTier >= p.RequiresLoyaltyTier))" +
                ") THEN 1 ELSE 0 END";
        return executeScalar(sql, Boolean.class, promotionId, customerId);
    }
}
