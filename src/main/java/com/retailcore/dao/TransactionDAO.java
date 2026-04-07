package com.retailcore.dao;

import com.retailcore.entity.Transaction;
import com.retailcore.entity.TransactionItem;
import com.retailcore.entity.Payment;
import com.retailcore.orm.BaseDAO;
import com.retailcore.orm.QueryBuilder;
import com.retailcore.orm.ResultMapper;
import com.retailcore.orm.TransactionManager;
import com.retailcore.orm.PagedResult;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TransactionDAO extends BaseDAO<Transaction> {

    private static TransactionDAO instance;

    private TransactionDAO() {
        super(Transaction.class);
    }

    public static synchronized TransactionDAO getInstance() {
        if (instance == null) {
            instance = new TransactionDAO();
        }
        return instance;
    }

    public Transaction findByTransactionNumber(String transactionNumber) throws SQLException {
        return findOneByColumn("TransactionNumber", transactionNumber);
    }

    public List<Transaction> findByStore(int storeId) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("StoreID", storeId)
                .orderBy("TransactionDate", QueryBuilder.SortDirection.DESC);
        return findByQuery(qb);
    }

    public List<Transaction> findByEmployee(int employeeId) throws SQLException {
        return findByColumn("EmployeeID", employeeId);
    }

    public List<Transaction> findByCustomer(int customerId) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("CustomerID", customerId)
                .orderBy("TransactionDate", QueryBuilder.SortDirection.DESC);
        return findByQuery(qb);
    }

    public List<Transaction> findByDateRange(int storeId, Date startDate, Date endDate) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("StoreID", storeId)
                .whereBetween("TransactionDate", startDate, endDate)
                .whereEquals("Status", 1)
                .orderBy("TransactionDate", QueryBuilder.SortDirection.DESC);
        return findByQuery(qb);
    }

    public List<Transaction> findByDateRange(Date startDate, Date endDate) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereBetween("TransactionDate", startDate, endDate)
                .whereEquals("Status", 1)
                .orderBy("TransactionDate", QueryBuilder.SortDirection.DESC);
        return findByQuery(qb);
    }

    public int createFullTransaction(Transaction transaction, List<TransactionItem> items, List<Payment> payments) throws SQLException {
        Connection conn = null;
        PreparedStatement stmtTxn = null;
        PreparedStatement stmtItem = null;
        PreparedStatement stmtPay = null;
        ResultSet rs = null;

        try {
            TransactionManager.begin();
            conn = TransactionManager.getConnectionForOperation();

            String txnSql = "INSERT INTO [dbo].[tbl_Transaction] " +
                    "(TransactionNumber, StoreID, RegisterNumber, EmployeeID, CustomerID, " +
                    "TransactionDate, TransactionType, SubTotal, DiscountTotal, TaxTotal, " +
                    "GrandTotal, TenderAmount, ChangeAmount, PromotionID, " +
                    "LoyaltyPointsEarned, LoyaltyPointsRedeemed, Status, Notes) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            stmtTxn = conn.prepareStatement(txnSql, Statement.RETURN_GENERATED_KEYS);
            setParameter(stmtTxn, 1, transaction.getTransactionNumber());
            setParameter(stmtTxn, 2, transaction.getStoreId());
            setParameter(stmtTxn, 3, transaction.getRegisterNumber());
            setParameter(stmtTxn, 4, transaction.getEmployeeId());
            setParameter(stmtTxn, 5, transaction.getCustomerId());
            setParameter(stmtTxn, 6, transaction.getTransactionDate());
            setParameter(stmtTxn, 7, transaction.getTransactionType());
            setParameter(stmtTxn, 8, transaction.getSubTotal());
            setParameter(stmtTxn, 9, transaction.getDiscountTotal());
            setParameter(stmtTxn, 10, transaction.getTaxTotal());
            setParameter(stmtTxn, 11, transaction.getGrandTotal());
            setParameter(stmtTxn, 12, transaction.getTenderAmount());
            setParameter(stmtTxn, 13, transaction.getChangeAmount());
            setParameter(stmtTxn, 14, transaction.getPromotionId());
            setParameter(stmtTxn, 15, transaction.getLoyaltyPointsEarned());
            setParameter(stmtTxn, 16, transaction.getLoyaltyPointsRedeemed());
            setParameter(stmtTxn, 17, transaction.getStatus());
            setParameter(stmtTxn, 18, transaction.getNotes());
            stmtTxn.executeUpdate();

            rs = stmtTxn.getGeneratedKeys();
            int transactionId = 0;
            if (rs.next()) {
                transactionId = rs.getInt(1);
                transaction.setTransactionId(transactionId);
            }
            rs.close();

            String itemSql = "INSERT INTO [dbo].[tbl_TransactionItem] " +
                    "(TransactionID, ProductID, Quantity, UnitPrice, DiscountAmount, " +
                    "TaxAmount, LineTotal, SerialNumber) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            stmtItem = conn.prepareStatement(itemSql);
            for (TransactionItem item : items) {
                item.setTransactionId(transactionId);
                setParameter(stmtItem, 1, transactionId);
                setParameter(stmtItem, 2, item.getProductId());
                setParameter(stmtItem, 3, item.getQuantity());
                setParameter(stmtItem, 4, item.getUnitPrice());
                setParameter(stmtItem, 5, item.getDiscountAmount());
                setParameter(stmtItem, 6, item.getTaxAmount());
                setParameter(stmtItem, 7, item.getLineTotal());
                setParameter(stmtItem, 8, item.getSerialNumber());
                stmtItem.addBatch();
            }
            stmtItem.executeBatch();

            String paySql = "INSERT INTO [dbo].[tbl_Payment] " +
                    "(TransactionID, PaymentMethod, Amount, ReferenceNumber, CardType, " +
                    "CardLastFour, AuthorizationCode, CheckNumber, GiftCardNumber, " +
                    "Status, ProcessedDate) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";

            stmtPay = conn.prepareStatement(paySql);
            for (Payment payment : payments) {
                payment.setTransactionId(transactionId);
                setParameter(stmtPay, 1, transactionId);
                setParameter(stmtPay, 2, payment.getPaymentMethod());
                setParameter(stmtPay, 3, payment.getAmount());
                setParameter(stmtPay, 4, payment.getReferenceNumber());
                setParameter(stmtPay, 5, payment.getCardType());
                setParameter(stmtPay, 6, payment.getCardLastFour());
                setParameter(stmtPay, 7, payment.getAuthorizationCode());
                setParameter(stmtPay, 8, payment.getCheckNumber());
                setParameter(stmtPay, 9, payment.getGiftCardNumber());
                setParameter(stmtPay, 10, payment.getStatus());
                stmtPay.addBatch();
            }
            stmtPay.executeBatch();

            String updateInv = "UPDATE [dbo].[tbl_Inventory] SET QuantityOnHand = QuantityOnHand - ?, " +
                    "ModifiedDate = GETDATE() WHERE ProductID = ? AND StoreID = ?";
            PreparedStatement stmtInv = conn.prepareStatement(updateInv);
            for (TransactionItem item : items) {
                setParameter(stmtInv, 1, item.getQuantity());
                setParameter(stmtInv, 2, item.getProductId());
                setParameter(stmtInv, 3, transaction.getStoreId());
                stmtInv.addBatch();
            }
            stmtInv.executeBatch();
            stmtInv.close();

            TransactionManager.commit();
            return transactionId;
        } catch (SQLException e) {
            TransactionManager.rollback();
            throw e;
        } finally {
            closeResultSet(rs);
            closeStatement(stmtTxn);
            closeStatement(stmtItem);
            closeStatement(stmtPay);
        }
    }

    public int voidTransaction(int transactionId, int voidEmployeeId, String reason) throws SQLException {
        String sql = "UPDATE [dbo].[tbl_Transaction] SET Status = 3, VoidReason = ?, " +
                "VoidEmployeeID = ?, ModifiedDate = GETDATE() WHERE TransactionID = ?";
        return executeUpdate(sql, reason, voidEmployeeId, transactionId);
    }

    public BigDecimal getDailySales(int storeId, Date date) throws SQLException {
        String sql = "SELECT ISNULL(SUM(GrandTotal), 0) FROM [dbo].[tbl_Transaction] " +
                "WHERE StoreID = ? AND CAST(TransactionDate AS DATE) = CAST(? AS DATE) AND Status = 1";
        return executeScalar(sql, BigDecimal.class, storeId, date);
    }

    public int getDailyTransactionCount(int storeId, Date date) throws SQLException {
        String sql = "SELECT COUNT(*) FROM [dbo].[tbl_Transaction] " +
                "WHERE StoreID = ? AND CAST(TransactionDate AS DATE) = CAST(? AS DATE) AND Status = 1";
        return executeScalar(sql, Integer.class, storeId, date);
    }

    public BigDecimal getAverageTransactionValue(int storeId, Date startDate, Date endDate) throws SQLException {
        String sql = "SELECT AVG(GrandTotal) FROM [dbo].[tbl_Transaction] " +
                "WHERE StoreID = ? AND TransactionDate BETWEEN ? AND ? AND Status = 1";
        return executeScalar(sql, BigDecimal.class, storeId, startDate, endDate);
    }

    public List<Map<String, Object>> getSalesByHour(int storeId, Date date) throws SQLException {
        String sql = "SELECT DATEPART(HOUR, TransactionDate) AS SaleHour, " +
                "COUNT(*) AS TransactionCount, SUM(GrandTotal) AS TotalSales " +
                "FROM [dbo].[tbl_Transaction] " +
                "WHERE StoreID = ? AND CAST(TransactionDate AS DATE) = CAST(? AS DATE) AND Status = 1 " +
                "GROUP BY DATEPART(HOUR, TransactionDate) ORDER BY SaleHour";
        return executeQuery(sql, storeId, date);
    }

    public List<Map<String, Object>> getSalesByEmployee(int storeId, Date startDate, Date endDate) throws SQLException {
        String sql = "SELECT e.EmployeeID, e.FirstName + ' ' + e.LastName AS EmployeeName, " +
                "COUNT(t.TransactionID) AS TransactionCount, SUM(t.GrandTotal) AS TotalSales " +
                "FROM [dbo].[tbl_Transaction] t " +
                "INNER JOIN [dbo].[tbl_Employee] e ON t.EmployeeID = e.EmployeeID " +
                "WHERE t.StoreID = ? AND t.TransactionDate BETWEEN ? AND ? AND t.Status = 1 " +
                "GROUP BY e.EmployeeID, e.FirstName, e.LastName " +
                "ORDER BY TotalSales DESC";
        return executeQuery(sql, storeId, startDate, endDate);
    }

    public String generateTransactionNumber(int storeId, int registerNumber) throws SQLException {
        String sql = "SELECT RIGHT('000' + CAST(? AS VARCHAR), 3) + '-' + " +
                "RIGHT('00' + CAST(? AS VARCHAR), 2) + '-' + " +
                "FORMAT(GETDATE(), 'yyyyMMdd') + '-' + " +
                "RIGHT('0000' + CAST(ISNULL(MAX(CAST(RIGHT(TransactionNumber, 4) AS INT)), 0) + 1 AS VARCHAR), 4) " +
                "FROM [dbo].[tbl_Transaction] " +
                "WHERE StoreID = ? AND CAST(TransactionDate AS DATE) = CAST(GETDATE() AS DATE)";
        return executeScalar(sql, String.class, storeId, registerNumber, storeId);
    }
}
