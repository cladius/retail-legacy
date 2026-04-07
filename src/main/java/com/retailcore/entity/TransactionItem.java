package com.retailcore.entity;

import com.retailcore.orm.Column;
import com.retailcore.orm.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_TransactionItem")
public class TransactionItem {

    @Column(name = "TransactionItemID", primaryKey = true, autoIncrement = true)
    private Integer transactionItemId;

    @Column(name = "TransactionID", nullable = false)
    private Integer transactionId;

    @Column(name = "ProductID", nullable = false)
    private Integer productId;

    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

    @Column(name = "UnitPrice", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "DiscountAmount", nullable = false)
    private BigDecimal discountAmount;

    @Column(name = "TaxAmount", nullable = false)
    private BigDecimal taxAmount;

    @Column(name = "LineTotal", nullable = false)
    private BigDecimal lineTotal;

    @Column(name = "ReturnQuantity", nullable = false)
    private Integer returnQuantity;

    @Column(name = "SerialNumber", length = 100)
    private String serialNumber;

    @Column(name = "IsVoided", nullable = false)
    private Boolean isVoided;

    @Column(name = "VoidReason", length = 255)
    private String voidReason;

    @Column(name = "CreatedDate", nullable = false, insertable = false, updatable = false)
    private Date createdDate;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    public TransactionItem() {
        this.quantity = 1;
        this.unitPrice = BigDecimal.ZERO;
        this.discountAmount = BigDecimal.ZERO;
        this.taxAmount = BigDecimal.ZERO;
        this.lineTotal = BigDecimal.ZERO;
        this.returnQuantity = 0;
        this.isVoided = false;
    }

    public Integer getTransactionItemId() { return transactionItemId; }
    public void setTransactionItemId(Integer transactionItemId) { this.transactionItemId = transactionItemId; }
    public Integer getTransactionId() { return transactionId; }
    public void setTransactionId(Integer transactionId) { this.transactionId = transactionId; }
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    public BigDecimal getLineTotal() { return lineTotal; }
    public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
    public Integer getReturnQuantity() { return returnQuantity; }
    public void setReturnQuantity(Integer returnQuantity) { this.returnQuantity = returnQuantity; }
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public Boolean getIsVoided() { return isVoided; }
    public void setIsVoided(Boolean isVoided) { this.isVoided = isVoided; }
    public String getVoidReason() { return voidReason; }
    public void setVoidReason(String voidReason) { this.voidReason = voidReason; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public Date getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(Date modifiedDate) { this.modifiedDate = modifiedDate; }
}
