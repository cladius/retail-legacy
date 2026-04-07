package com.retailcore.entity;

import com.retailcore.orm.Column;
import com.retailcore.orm.Table;
import java.util.Date;

@Table(name = "tbl_Store")
public class Store {

    @Column(name = "StoreID", primaryKey = true, autoIncrement = true)
    private Integer storeId;

    @Column(name = "StoreName", nullable = false, length = 200)
    private String storeName;

    @Column(name = "StoreCode", nullable = false, length = 20)
    private String storeCode;

    @Column(name = "RegionID", nullable = false)
    private Integer regionId;

    @Column(name = "Address1", nullable = false, length = 255)
    private String address1;

    @Column(name = "Address2", length = 255)
    private String address2;

    @Column(name = "City", nullable = false, length = 100)
    private String city;

    @Column(name = "StateProvince", nullable = false, length = 50)
    private String stateProvince;

    @Column(name = "PostalCode", nullable = false, length = 20)
    private String postalCode;

    @Column(name = "Phone", length = 30)
    private String phone;

    @Column(name = "Fax", length = 30)
    private String fax;

    @Column(name = "Email", length = 150)
    private String email;

    @Column(name = "ManagerEmployeeID")
    private Integer managerEmployeeId;

    @Column(name = "OpenDate", nullable = false)
    private Date openDate;

    @Column(name = "CloseDate")
    private Date closeDate;

    @Column(name = "SquareFootage")
    private Integer squareFootage;

    @Column(name = "StoreType", nullable = false)
    private Byte storeType;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;

    @Column(name = "CreatedDate", nullable = false, insertable = false, updatable = false)
    private Date createdDate;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    public Store() {
        this.storeType = 1;
        this.isActive = true;
    }

    public Integer getStoreId() { return storeId; }
    public void setStoreId(Integer storeId) { this.storeId = storeId; }
    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public String getStoreCode() { return storeCode; }
    public void setStoreCode(String storeCode) { this.storeCode = storeCode; }
    public Integer getRegionId() { return regionId; }
    public void setRegionId(Integer regionId) { this.regionId = regionId; }
    public String getAddress1() { return address1; }
    public void setAddress1(String address1) { this.address1 = address1; }
    public String getAddress2() { return address2; }
    public void setAddress2(String address2) { this.address2 = address2; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getStateProvince() { return stateProvince; }
    public void setStateProvince(String stateProvince) { this.stateProvince = stateProvince; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getFax() { return fax; }
    public void setFax(String fax) { this.fax = fax; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getManagerEmployeeId() { return managerEmployeeId; }
    public void setManagerEmployeeId(Integer managerEmployeeId) { this.managerEmployeeId = managerEmployeeId; }
    public Date getOpenDate() { return openDate; }
    public void setOpenDate(Date openDate) { this.openDate = openDate; }
    public Date getCloseDate() { return closeDate; }
    public void setCloseDate(Date closeDate) { this.closeDate = closeDate; }
    public Integer getSquareFootage() { return squareFootage; }
    public void setSquareFootage(Integer squareFootage) { this.squareFootage = squareFootage; }
    public Byte getStoreType() { return storeType; }
    public void setStoreType(Byte storeType) { this.storeType = storeType; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public Date getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(Date modifiedDate) { this.modifiedDate = modifiedDate; }
}
