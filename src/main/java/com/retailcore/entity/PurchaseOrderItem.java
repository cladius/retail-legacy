package com.retailcore.entity;

import com.retailcore.orm.Column;
import com.retailcore.orm.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_PurchaseOrderItem")
public class PurchaseOrderItem {

    @Column(name = "PurchaseOrderItemID", primaryKey = true, autoIncrement = true)
    private Integer purchaseOrderItemId;

    @Column(name = "PurchaseOrderID", nullable = false)
    private Integer purchaseOrderId;

    @Column(name = "ProductID", nullable = false)
    private Integer productId;

    @Column(name = "QuantityOrdered", nullable = false)
    private Integer quantityOrdered;

    @Column(name = "QuantityReceived", nullable = false)
    private Integer quantityReceived;

    @Column(name = "UnitCost", nullable = false)
    private BigDecimal unitCost;

    @Column(name = "LineTotal", nullable = false)
    private BigDecimal lineTotal;

    @Column(name = "Status", nullable = false)
    private Byte status;

    @Column(name = "CreatedDate", nullable = false, insertable = false, updatable = false)
    private Date createdDate;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    public PurchaseOrderItem() {
        this.quantityOrdered = 0;
        this.quantityReceived = 0;
        this.unitCost = BigDecimal.ZERO;
        this.lineTotal = BigDecimal.ZERO;
        this.status = 1;
    }

    public Integer getPurchaseOrderItemId() { return purchaseOrderItemId; }
    public void setPurchaseOrderItemId(Integer id) { this.purchaseOrderItemId = id; }
    public Integer getPurchaseOrderId() { return purchaseOrderId; }
    public void setPurchaseOrderId(Integer purchaseOrderId) { this.purchaseOrderId = purchaseOrderId; }
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public Integer getQuantityOrdered() { return quantityOrdered; }
    public void setQuantityOrdered(Integer quantityOrdered) { this.quantityOrdered = quantityOrdered; }
    public Integer getQuantityReceived() { return quantityReceived; }
    public void setQuantityReceived(Integer quantityReceived) { this.quantityReceived = quantityReceived; }
    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }
    public BigDecimal getLineTotal() { return lineTotal; }
    public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
    public Byte getStatus() { return status; }
    public void setStatus(Byte status) { this.status = status; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public Date getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(Date modifiedDate) { this.modifiedDate = modifiedDate; }
}
