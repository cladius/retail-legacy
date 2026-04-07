package com.retailcore.entity;

import com.retailcore.orm.Column;
import com.retailcore.orm.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_Vendor")
public class Vendor {

    @Column(name = "VendorID", primaryKey = true, autoIncrement = true)
    private Integer vendorId;

    @Column(name = "VendorName", nullable = false, length = 200)
    private String vendorName;

    @Column(name = "VendorCode", nullable = false, length = 20)
    private String vendorCode;

    @Column(name = "ContactName", length = 150)
    private String contactName;

    @Column(name = "ContactEmail", length = 150)
    private String contactEmail;

    @Column(name = "ContactPhone", length = 30)
    private String contactPhone;

    @Column(name = "Address1", length = 255)
    private String address1;

    @Column(name = "Address2", length = 255)
    private String address2;

    @Column(name = "City", length = 100)
    private String city;

    @Column(name = "StateProvince", length = 50)
    private String stateProvince;

    @Column(name = "PostalCode", length = 20)
    private String postalCode;

    @Column(name = "CountryCode", nullable = false, length = 5)
    private String countryCode;

    @Column(name = "PaymentTerms", length = 50)
    private String paymentTerms;

    @Column(name = "LeadTimeDays")
    private Integer leadTimeDays;

    @Column(name = "MinOrderAmount")
    private BigDecimal minOrderAmount;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;

    @Column(name = "CreatedDate", nullable = false, insertable = false, updatable = false)
    private Date createdDate;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    public Vendor() {
        this.countryCode = "US";
        this.leadTimeDays = 14;
        this.isActive = true;
    }

    public Integer getVendorId() { return vendorId; }
    public void setVendorId(Integer vendorId) { this.vendorId = vendorId; }
    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }
    public String getVendorCode() { return vendorCode; }
    public void setVendorCode(String vendorCode) { this.vendorCode = vendorCode; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
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
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public String getPaymentTerms() { return paymentTerms; }
    public void setPaymentTerms(String paymentTerms) { this.paymentTerms = paymentTerms; }
    public Integer getLeadTimeDays() { return leadTimeDays; }
    public void setLeadTimeDays(Integer leadTimeDays) { this.leadTimeDays = leadTimeDays; }
    public BigDecimal getMinOrderAmount() { return minOrderAmount; }
    public void setMinOrderAmount(BigDecimal minOrderAmount) { this.minOrderAmount = minOrderAmount; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public Date getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(Date modifiedDate) { this.modifiedDate = modifiedDate; }
}
