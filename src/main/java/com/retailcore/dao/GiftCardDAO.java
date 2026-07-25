package com.retailcore.dao;

import com.retailcore.entity.GiftCard;
import com.retailcore.orm.BaseDAO;
import com.retailcore.orm.QueryBuilder;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class GiftCardDAO extends BaseDAO<GiftCard> {

    private static GiftCardDAO instance;

    private GiftCardDAO() {
        super(GiftCard.class);
    }

    public static synchronized GiftCardDAO getInstance() {
        if (instance == null) {
            instance = new GiftCardDAO();
        }
        return instance;
    }

    public GiftCard findByCardNumber(String cardNumber) throws SQLException {
        return findOneByColumn("CardNumber", cardNumber);
    }

    public GiftCard findByCardNumberAndPin(String cardNumber, String pin) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("CardNumber", cardNumber)
                .whereEquals("PIN", pin)
                .whereEquals("Status", 1);
        return findOneByQuery(qb);
    }

    public int deductBalance(int giftCardId, BigDecimal amount) throws SQLException {
        String sql = "UPDATE tbl_GiftCard SET CurrentBalance = CurrentBalance - ?, " +
                "ModifiedDate = CURRENT_TIMESTAMP WHERE GiftCardID = ? AND CurrentBalance >= ? AND Status = 1";
        return executeUpdate(sql, amount, giftCardId, amount);
    }

    public int addBalance(int giftCardId, BigDecimal amount) throws SQLException {
        String sql = "UPDATE tbl_GiftCard SET CurrentBalance = CurrentBalance + ?, " +
                "ModifiedDate = CURRENT_TIMESTAMP WHERE GiftCardID = ? AND Status = 1";
        return executeUpdate(sql, amount, giftCardId);
    }

    public int deactivate(int giftCardId) throws SQLException {
        String sql = "UPDATE tbl_GiftCard SET Status = 2, ModifiedDate = CURRENT_TIMESTAMP WHERE GiftCardID = ?";
        return executeUpdate(sql, giftCardId);
    }

    public List<GiftCard> findExpired() throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("Status", 1)
                .whereIsNotNull("ExpirationDate")
                .where("ExpirationDate < CURRENT_DATE");
        return findByQuery(qb);
    }

    public BigDecimal getTotalOutstandingBalance() throws SQLException {
        String sql = "SELECT SUM(CurrentBalance) FROM tbl_GiftCard WHERE Status = 1";
        return executeScalar(sql, BigDecimal.class);
    }
}
