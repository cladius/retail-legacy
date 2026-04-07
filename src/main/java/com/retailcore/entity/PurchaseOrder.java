package com.retailcore.entity;

import com.retailcore.orm.Column;
import com.retailcore.orm.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_PurchaseOrder")
public class PurchaseOrder {

    @Column(name = "PurchaseOrderID", primaryKey = true, autoIncrement = true)
    private Integer purchaseOrderId;

    @Column(name = "PONumber", nullable = false, length = 30)
    private String poNumber;

    @Column(name = "VendorID", nullable = false)
    private Integer vendorId;

    @Column(name = "StoreID", nullable = false)
    private Integer storeId;

    @Column(name = "OrderedByEmployeeID", nullable = false)
    private Integer orderedByEmployeeId;

    @Column(name = "ApprovedByEmployeeID")
    private Integer approvedByEmployeeId;

    @Column(name = "OrderDate", nullable = false)
    private Date orderDate;

    @Column(name = "ExpectedDeliveryDate")
    private Date expectedDeliveryDate;

    @Column(name = "ActualDeliveryDate")
    private Date actualDeliveryDate;

    @Column(name = "SubTotal", nullable = false)
    private BigDecimal subTotal;

    @Column(name = "ShippingCost", nullable = false)
    private BigDecimal shippingCost;

    @Column(name = "TaxAmount", nullable = false)
    private BigDecimal taxAmount;

    @Column(name = "TotalAmount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "Status", nullable = false)
    private Byte status;

    @Column(name = "Notes")
    private String notes;

    @Column(name = "CreatedDate", nullable = false, insertable = false, updatable = false)
    private Date createdDate;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    public PurchaseOrder() {
        this.subTotal = BigDecimal.ZERO;
        this.shippingCost = BigDecimal.ZERO;
        this.taxAmount = BigDecimal.ZERO;
        this.totalAmount = BigDecimal.ZERO;
        this.status = 1;
    }

    public Integer getPurchaseOrderId() { return purchaseOrderId; }
    public void setPurchaseOrderId(Integer purchaseOrderId) { this.purchaseOrderId = purchaseOrderId; }
    public String getPoNumber() { return poNumber; }
    public void setPoNumber(String poNumber) { this.poNumber = poNumber; }
    public Integer getVendorId() { return vendorId; }
    public void setVendorId(Integer vendorId) { this.vendorId = vendorId; }
    public Integer getStoreId() { return storeId; }
    public void setStoreId(Integer storeId) { this.storeId = storeId; }
    public Integer getOrderedByEmployeeId() { return orderedByEmployeeId; }
    public void setOrderedByEmployeeId(Integer orderedByEmployeeId) { this.orderedByEmployeeId = orderedByEmployeeId; }
    public Integer getApprovedByEmployeeId() { return approvedByEmployeeId; }
    public void setApprovedByEmployeeId(Integer approvedByEmployeeId) { this.approvedByEmployeeId = approvedByEmployeeId; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public Date getExpectedDeliveryDate() { return expectedDeliveryDate; }
    public void setExpectedDeliveryDate(Date expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate; }
    public Date getActualDeliveryDate() { return actualDeliveryDate; }
    public void setActualDeliveryDate(Date actualDeliveryDate) { this.actualDeliveryDate = actualDeliveryDate; }
    public BigDecimal getSubTotal() { return subTotal; }
    public void setSubTotal(BigDecimal subTotal) { this.subTotal = subTotal; }
    public BigDecimal getShippingCost() { return shippingCost; }
    public void setShippingCost(BigDecimal shippingCost) { this.shippingCost = shippingCost; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public Byte getStatus() { return status; }
    public void setStatus(Byte status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public Date getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(Date modifiedDate) { this.modifiedDate = modifiedDate; }
}
