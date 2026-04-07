package com.retailcore.entity;

import com.retailcore.orm.Column;
import com.retailcore.orm.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_Region")
public class Region {

    @Column(name = "RegionID", primaryKey = true, autoIncrement = true)
    private Integer regionId;

    @Column(name = "RegionName", nullable = false, length = 100)
    private String regionName;

    @Column(name = "RegionCode", nullable = false, length = 10)
    private String regionCode;

    @Column(name = "CountryCode", nullable = false, length = 5)
    private String countryCode;

    @Column(name = "TaxRate", nullable = false)
    private BigDecimal taxRate;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;

    @Column(name = "CreatedDate", nullable = false, insertable = false, updatable = false)
    private Date createdDate;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    public Region() {
        this.countryCode = "US";
        this.taxRate = BigDecimal.ZERO;
        this.isActive = true;
    }

    public Integer getRegionId() { return regionId; }
    public void setRegionId(Integer regionId) { this.regionId = regionId; }
    public String getRegionName() { return regionName; }
    public void setRegionName(String regionName) { this.regionName = regionName; }
    public String getRegionCode() { return regionCode; }
    public void setRegionCode(String regionCode) { this.regionCode = regionCode; }
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public BigDecimal getTaxRate() { return taxRate; }
    public void setTaxRate(BigDecimal taxRate) { this.taxRate = taxRate; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public Date getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(Date modifiedDate) { this.modifiedDate = modifiedDate; }
}
