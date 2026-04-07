package com.retailcore.entity;

import com.retailcore.orm.Column;
import com.retailcore.orm.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_Transaction")
public class Transaction {

    @Column(name = "TransactionID", primaryKey = true, autoIncrement = true)
    private Integer transactionId;

    @Column(name = "TransactionNumber", nullable = false, length = 30)
    private String transactionNumber;

    @Column(name = "StoreID", nullable = false)
    private Integer storeId;

    @Column(name = "RegisterNumber", nullable = false)
    private Integer registerNumber;

    @Column(name = "EmployeeID", nullable = false)
    private Integer employeeId;

    @Column(name = "CustomerID")
    private Integer customerId;

    @Column(name = "TransactionDate", nullable = false)
    private Date transactionDate;

    @Column(name = "TransactionType", nullable = false)
    private Byte transactionType;

    @Column(name = "SubTotal", nullable = false)
    private BigDecimal subTotal;

    @Column(name = "DiscountTotal", nullable = false)
    private BigDecimal discountTotal;

    @Column(name = "TaxTotal", nullable = false)
    private BigDecimal taxTotal;

    @Column(name = "GrandTotal", nullable = false)
    private BigDecimal grandTotal;

    @Column(name = "TenderAmount", nullable = false)
    private BigDecimal tenderAmount;

    @Column(name = "ChangeAmount", nullable = false)
    private BigDecimal changeAmount;

    @Column(name = "PromotionID")
    private Integer promotionId;

    @Column(name = "LoyaltyPointsEarned", nullable = false)
    private Integer loyaltyPointsEarned;

    @Column(name = "LoyaltyPointsRedeemed", nullable = false)
    private Integer loyaltyPointsRedeemed;

    @Column(name = "Status", nullable = false)
    private Byte status;

    @Column(name = "VoidReason", length = 255)
    private String voidReason;

    @Column(name = "VoidEmployeeID")
    private Integer voidEmployeeId;

    @Column(name = "Notes", length = 500)
    private String notes;

    @Column(name = "CreatedDate", nullable = false, insertable = false, updatable = false)
    private Date createdDate;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    public Transaction() {
        this.registerNumber = 1;
        this.transactionType = 1;
        this.subTotal = BigDecimal.ZERO;
        this.discountTotal = BigDecimal.ZERO;
        this.taxTotal = BigDecimal.ZERO;
        this.grandTotal = BigDecimal.ZERO;
        this.tenderAmount = BigDecimal.ZERO;
        this.changeAmount = BigDecimal.ZERO;
        this.loyaltyPointsEarned = 0;
        this.loyaltyPointsRedeemed = 0;
        this.status = 1;
    }

    public Integer getTransactionId() { return transactionId; }
    public void setTransactionId(Integer transactionId) { this.transactionId = transactionId; }
    public String getTransactionNumber() { return transactionNumber; }
    public void setTransactionNumber(String transactionNumber) { this.transactionNumber = transactionNumber; }
    public Integer getStoreId() { return storeId; }
    public void setStoreId(Integer storeId) { this.storeId = storeId; }
    public Integer getRegisterNumber() { return registerNumber; }
    public void setRegisterNumber(Integer registerNumber) { this.registerNumber = registerNumber; }
    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    public Date getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }
    public Byte getTransactionType() { return transactionType; }
    public void setTransactionType(Byte transactionType) { this.transactionType = transactionType; }
    public BigDecimal getSubTotal() { return subTotal; }
    public void setSubTotal(BigDecimal subTotal) { this.subTotal = subTotal; }
    public BigDecimal getDiscountTotal() { return discountTotal; }
    public void setDiscountTotal(BigDecimal discountTotal) { this.discountTotal = discountTotal; }
    public BigDecimal getTaxTotal() { return taxTotal; }
    public void setTaxTotal(BigDecimal taxTotal) { this.taxTotal = taxTotal; }
    public BigDecimal getGrandTotal() { return grandTotal; }
    public void setGrandTotal(BigDecimal grandTotal) { this.grandTotal = grandTotal; }
    public BigDecimal getTenderAmount() { return tenderAmount; }
    public void setTenderAmount(BigDecimal tenderAmount) { this.tenderAmount = tenderAmount; }
    public BigDecimal getChangeAmount() { return changeAmount; }
    public void setChangeAmount(BigDecimal changeAmount) { this.changeAmount = changeAmount; }
    public Integer getPromotionId() { return promotionId; }
    public void setPromotionId(Integer promotionId) { this.promotionId = promotionId; }
    public Integer getLoyaltyPointsEarned() { return loyaltyPointsEarned; }
    public void setLoyaltyPointsEarned(Integer loyaltyPointsEarned) { this.loyaltyPointsEarned = loyaltyPointsEarned; }
    public Integer getLoyaltyPointsRedeemed() { return loyaltyPointsRedeemed; }
    public void setLoyaltyPointsRedeemed(Integer loyaltyPointsRedeemed) { this.loyaltyPointsRedeemed = loyaltyPointsRedeemed; }
    public Byte getStatus() { return status; }
    public void setStatus(Byte status) { this.status = status; }
    public String getVoidReason() { return voidReason; }
    public void setVoidReason(String voidReason) { this.voidReason = voidReason; }
    public Integer getVoidEmployeeId() { return voidEmployeeId; }
    public void setVoidEmployeeId(Integer voidEmployeeId) { this.voidEmployeeId = voidEmployeeId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public Date getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(Date modifiedDate) { this.modifiedDate = modifiedDate; }
}
