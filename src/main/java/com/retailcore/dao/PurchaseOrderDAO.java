package com.retailcore.dao;

import com.retailcore.entity.PurchaseOrder;
import com.retailcore.entity.PurchaseOrderItem;
import com.retailcore.orm.BaseDAO;
import com.retailcore.orm.QueryBuilder;
import com.retailcore.orm.ResultMapper;
import com.retailcore.orm.TransactionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PurchaseOrderDAO extends BaseDAO<PurchaseOrder> {

    private static PurchaseOrderDAO instance;

    private PurchaseOrderDAO() {
        super(PurchaseOrder.class);
    }

    public static synchronized PurchaseOrderDAO getInstance() {
        if (instance == null) {
            instance = new PurchaseOrderDAO();
        }
        return instance;
    }

    public PurchaseOrder findByPONumber(String poNumber) throws SQLException {
        return findOneByColumn("PONumber", poNumber);
    }

    public List<PurchaseOrder> findByVendor(int vendorId) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("VendorID", vendorId)
                .orderBy("OrderDate", QueryBuilder.SortDirection.DESC);
        return findByQuery(qb);
    }

    public List<PurchaseOrder> findByStore(int storeId) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("StoreID", storeId)
                .orderBy("OrderDate", QueryBuilder.SortDirection.DESC);
        return findByQuery(qb);
    }

    public List<PurchaseOrder> findByStatus(int status) throws SQLException {
        return findByColumn("Status", status);
    }

    public List<PurchaseOrder> findPending() throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("Status", 1)
                .orderBy("OrderDate", QueryBuilder.SortDirection.ASC);
        return findByQuery(qb);
    }

    public int createWithItems(PurchaseOrder po, List<PurchaseOrderItem> items) throws SQLException {
        Connection conn = null;
        PreparedStatement stmtPO = null;
        PreparedStatement stmtItem = null;
        ResultSet rs = null;

        try {
            TransactionManager.begin();
            conn = TransactionManager.getConnectionForOperation();

            String poSql = "INSERT INTO [dbo].[tbl_PurchaseOrder] " +
                    "(PONumber, VendorID, StoreID, OrderedByEmployeeID, OrderDate, " +
                    "ExpectedDeliveryDate, SubTotal, ShippingCost, TaxAmount, TotalAmount, Status, Notes) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            stmtPO = conn.prepareStatement(poSql, Statement.RETURN_GENERATED_KEYS);
            setParameter(stmtPO, 1, po.getPoNumber());
            setParameter(stmtPO, 2, po.getVendorId());
            setParameter(stmtPO, 3, po.getStoreId());
            setParameter(stmtPO, 4, po.getOrderedByEmployeeId());
            setParameter(stmtPO, 5, po.getOrderDate());
            setParameter(stmtPO, 6, po.getExpectedDeliveryDate());
            setParameter(stmtPO, 7, po.getSubTotal());
            setParameter(stmtPO, 8, po.getShippingCost());
            setParameter(stmtPO, 9, po.getTaxAmount());
            setParameter(stmtPO, 10, po.getTotalAmount());
            setParameter(stmtPO, 11, po.getStatus());
            setParameter(stmtPO, 12, po.getNotes());
            stmtPO.executeUpdate();

            rs = stmtPO.getGeneratedKeys();
            int poId = 0;
            if (rs.next()) {
                poId = rs.getInt(1);
                po.setPurchaseOrderId(poId);
            }
            rs.close();

            String itemSql = "INSERT INTO [dbo].[tbl_PurchaseOrderItem] " +
                    "(PurchaseOrderID, ProductID, QuantityOrdered, UnitCost, LineTotal, Status) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            stmtItem = conn.prepareStatement(itemSql);
            for (PurchaseOrderItem item : items) {
                item.setPurchaseOrderId(poId);
                setParameter(stmtItem, 1, poId);
                setParameter(stmtItem, 2, item.getProductId());
                setParameter(stmtItem, 3, item.getQuantityOrdered());
                setParameter(stmtItem, 4, item.getUnitCost());
                setParameter(stmtItem, 5, item.getLineTotal());
                setParameter(stmtItem, 6, item.getStatus());
                stmtItem.addBatch();
            }
            stmtItem.executeBatch();

            TransactionManager.commit();
            return poId;
        } catch (SQLException e) {
            TransactionManager.rollback();
            throw e;
        } finally {
            closeResultSet(rs);
            closeStatement(stmtPO);
            closeStatement(stmtItem);
        }
    }

    public int approvePurchaseOrder(int poId, int approverEmployeeId) throws SQLException {
        String sql = "UPDATE [dbo].[tbl_PurchaseOrder] SET Status = 2, ApprovedByEmployeeID = ?, " +
                "ModifiedDate = GETDATE() WHERE PurchaseOrderID = ? AND Status = 1";
        return executeUpdate(sql, approverEmployeeId, poId);
    }

    public int receiveFullOrder(int poId) throws SQLException {
        try {
            TransactionManager.begin();

            String updateItems = "UPDATE [dbo].[tbl_PurchaseOrderItem] SET QuantityReceived = QuantityOrdered, " +
                    "Status = 3, ModifiedDate = GETDATE() WHERE PurchaseOrderID = ?";
            executeUpdate(updateItems, poId);

            String updatePO = "UPDATE [dbo].[tbl_PurchaseOrder] SET Status = 4, ActualDeliveryDate = GETDATE(), " +
                    "ModifiedDate = GETDATE() WHERE PurchaseOrderID = ?";
            int result = executeUpdate(updatePO, poId);

            TransactionManager.commit();
            return result;
        } catch (SQLException e) {
            TransactionManager.rollback();
            throw e;
        }
    }

    public String generatePONumber(int storeId) throws SQLException {
        String sql = "SELECT 'PO-' + RIGHT('000' + CAST(? AS VARCHAR), 3) + '-' + " +
                "FORMAT(GETDATE(), 'yyyyMMdd') + '-' + " +
                "RIGHT('000' + CAST(ISNULL(MAX(CAST(RIGHT(PONumber, 3) AS INT)), 0) + 1 AS VARCHAR), 3) " +
                "FROM [dbo].[tbl_PurchaseOrder] WHERE StoreID = ? AND CAST(OrderDate AS DATE) = CAST(GETDATE() AS DATE)";
        return executeScalar(sql, String.class, storeId, storeId);
    }
}
