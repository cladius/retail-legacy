package com.retailcore.entity;

import com.retailcore.orm.Column;
import com.retailcore.orm.Table;
import java.util.Date;

@Table(name = "tbl_Inventory")
public class Inventory {

    @Column(name = "InventoryID", primaryKey = true, autoIncrement = true)
    private Integer inventoryId;

    @Column(name = "ProductID", nullable = false)
    private Integer productId;

    @Column(name = "StoreID", nullable = false)
    private Integer storeId;

    @Column(name = "QuantityOnHand", nullable = false)
    private Integer quantityOnHand;

    @Column(name = "QuantityReserved", nullable = false)
    private Integer quantityReserved;

    @Column(name = "QuantityOnOrder", nullable = false)
    private Integer quantityOnOrder;

    @Column(name = "BinLocation", length = 50)
    private String binLocation;

    @Column(name = "AisleName", length = 50)
    private String aisleName;

    @Column(name = "ShelfNumber", length = 20)
    private String shelfNumber;

    @Column(name = "LastCountDate")
    private Date lastCountDate;

    @Column(name = "LastReceivedDate")
    private Date lastReceivedDate;

    @Column(name = "CreatedDate", nullable = false, insertable = false, updatable = false)
    private Date createdDate;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    public Inventory() {
        this.quantityOnHand = 0;
        this.quantityReserved = 0;
        this.quantityOnOrder = 0;
    }

    public Integer getInventoryId() { return inventoryId; }
    public void setInventoryId(Integer inventoryId) { this.inventoryId = inventoryId; }
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public Integer getStoreId() { return storeId; }
    public void setStoreId(Integer storeId) { this.storeId = storeId; }
    public Integer getQuantityOnHand() { return quantityOnHand; }
    public void setQuantityOnHand(Integer quantityOnHand) { this.quantityOnHand = quantityOnHand; }
    public Integer getQuantityReserved() { return quantityReserved; }
    public void setQuantityReserved(Integer quantityReserved) { this.quantityReserved = quantityReserved; }
    public Integer getQuantityOnOrder() { return quantityOnOrder; }
    public void setQuantityOnOrder(Integer quantityOnOrder) { this.quantityOnOrder = quantityOnOrder; }
    public String getBinLocation() { return binLocation; }
    public void setBinLocation(String binLocation) { this.binLocation = binLocation; }
    public String getAisleName() { return aisleName; }
    public void setAisleName(String aisleName) { this.aisleName = aisleName; }
    public String getShelfNumber() { return shelfNumber; }
    public void setShelfNumber(String shelfNumber) { this.shelfNumber = shelfNumber; }
    public Date getLastCountDate() { return lastCountDate; }
    public void setLastCountDate(Date lastCountDate) { this.lastCountDate = lastCountDate; }
    public Date getLastReceivedDate() { return lastReceivedDate; }
    public void setLastReceivedDate(Date lastReceivedDate) { this.lastReceivedDate = lastReceivedDate; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public Date getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(Date modifiedDate) { this.modifiedDate = modifiedDate; }

    public int getAvailableQuantity() {
        return quantityOnHand - quantityReserved;
    }
}
