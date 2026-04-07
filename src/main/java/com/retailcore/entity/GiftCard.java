package com.retailcore.entity;

import com.retailcore.orm.Column;
import com.retailcore.orm.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_GiftCard")
public class GiftCard {

    @Column(name = "GiftCardID", primaryKey = true, autoIncrement = true)
    private Integer giftCardId;

    @Column(name = "CardNumber", nullable = false, length = 30)
    private String cardNumber;

    @Column(name = "PIN", length = 10)
    private String pin;

    @Column(name = "OriginalBalance", nullable = false)
    private BigDecimal originalBalance;

    @Column(name = "CurrentBalance", nullable = false)
    private BigDecimal currentBalance;

    @Column(name = "PurchasedAtStoreID")
    private Integer purchasedAtStoreId;

    @Column(name = "PurchasedByCustomerID")
    private Integer purchasedByCustomerId;

    @Column(name = "PurchaseDate", nullable = false)
    private Date purchaseDate;

    @Column(name = "ExpirationDate")
    private Date expirationDate;

    @Column(name = "Status", nullable = false)
    private Byte status;

    @Column(name = "CreatedDate", nullable = false, insertable = false, updatable = false)
    private Date createdDate;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    public GiftCard() {
        this.originalBalance = BigDecimal.ZERO;
        this.currentBalance = BigDecimal.ZERO;
        this.status = 1;
    }

    public Integer getGiftCardId() { return giftCardId; }
    public void setGiftCardId(Integer giftCardId) { this.giftCardId = giftCardId; }
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }
    public BigDecimal getOriginalBalance() { return originalBalance; }
    public void setOriginalBalance(BigDecimal originalBalance) { this.originalBalance = originalBalance; }
    public BigDecimal getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; }
    public Integer getPurchasedAtStoreId() { return purchasedAtStoreId; }
    public void setPurchasedAtStoreId(Integer purchasedAtStoreId) { this.purchasedAtStoreId = purchasedAtStoreId; }
    public Integer getPurchasedByCustomerId() { return purchasedByCustomerId; }
    public void setPurchasedByCustomerId(Integer purchasedByCustomerId) { this.purchasedByCustomerId = purchasedByCustomerId; }
    public Date getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }
    public Date getExpirationDate() { return expirationDate; }
    public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }
    public Byte getStatus() { return status; }
    public void setStatus(Byte status) { this.status = status; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public Date getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(Date modifiedDate) { this.modifiedDate = modifiedDate; }
}
