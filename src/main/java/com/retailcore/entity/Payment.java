package com.retailcore.entity;

import com.retailcore.orm.Column;
import com.retailcore.orm.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_Payment")
public class Payment {

    @Column(name = "PaymentID", primaryKey = true, autoIncrement = true)
    private Integer paymentId;

    @Column(name = "TransactionID", nullable = false)
    private Integer transactionId;

    @Column(name = "PaymentMethod", nullable = false)
    private Byte paymentMethod;

    @Column(name = "Amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "ReferenceNumber", length = 100)
    private String referenceNumber;

    @Column(name = "CardType", length = 30)
    private String cardType;

    @Column(name = "CardLastFour", length = 4)
    private String cardLastFour;

    @Column(name = "AuthorizationCode", length = 50)
    private String authorizationCode;

    @Column(name = "CheckNumber", length = 20)
    private String checkNumber;

    @Column(name = "GiftCardNumber", length = 30)
    private String giftCardNumber;

    @Column(name = "Status", nullable = false)
    private Byte status;

    @Column(name = "ProcessedDate", nullable = false)
    private Date processedDate;

    @Column(name = "CreatedDate", nullable = false, insertable = false, updatable = false)
    private Date createdDate;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    public Payment() {
        this.paymentMethod = 1;
        this.amount = BigDecimal.ZERO;
        this.status = 1;
    }

    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }
    public Integer getTransactionId() { return transactionId; }
    public void setTransactionId(Integer transactionId) { this.transactionId = transactionId; }
    public Byte getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(Byte paymentMethod) { this.paymentMethod = paymentMethod; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }
    public String getCardLastFour() { return cardLastFour; }
    public void setCardLastFour(String cardLastFour) { this.cardLastFour = cardLastFour; }
    public String getAuthorizationCode() { return authorizationCode; }
    public void setAuthorizationCode(String authorizationCode) { this.authorizationCode = authorizationCode; }
    public String getCheckNumber() { return checkNumber; }
    public void setCheckNumber(String checkNumber) { this.checkNumber = checkNumber; }
    public String getGiftCardNumber() { return giftCardNumber; }
    public void setGiftCardNumber(String giftCardNumber) { this.giftCardNumber = giftCardNumber; }
    public Byte getStatus() { return status; }
    public void setStatus(Byte status) { this.status = status; }
    public Date getProcessedDate() { return processedDate; }
    public void setProcessedDate(Date processedDate) { this.processedDate = processedDate; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public Date getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(Date modifiedDate) { this.modifiedDate = modifiedDate; }
}
