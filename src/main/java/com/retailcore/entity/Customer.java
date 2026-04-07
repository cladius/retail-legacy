package com.retailcore.entity;

import com.retailcore.orm.Column;
import com.retailcore.orm.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_Customer")
public class Customer {

    @Column(name = "CustomerID", primaryKey = true, autoIncrement = true)
    private Integer customerId;

    @Column(name = "CustomerNumber", nullable = false, length = 20)
    private String customerNumber;

    @Column(name = "FirstName", nullable = false, length = 100)
    private String firstName;

    @Column(name = "LastName", nullable = false, length = 100)
    private String lastName;

    @Column(name = "MiddleName", length = 100)
    private String middleName;

    @Column(name = "Email", length = 150)
    private String email;

    @Column(name = "Phone", length = 30)
    private String phone;

    @Column(name = "MobilePhone", length = 30)
    private String mobilePhone;

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

    @Column(name = "DateOfBirth")
    private Date dateOfBirth;

    @Column(name = "Gender", length = 1)
    private String gender;

    @Column(name = "LoyaltyPoints", nullable = false)
    private Integer loyaltyPoints;

    @Column(name = "LoyaltyTier", nullable = false)
    private Byte loyaltyTier;

    @Column(name = "TotalSpend", nullable = false)
    private BigDecimal totalSpend;

    @Column(name = "VisitCount", nullable = false)
    private Integer visitCount;

    @Column(name = "LastVisitDate")
    private Date lastVisitDate;

    @Column(name = "PreferredStoreID")
    private Integer preferredStoreId;

    @Column(name = "TaxExempt", nullable = false)
    private Boolean taxExempt;

    @Column(name = "TaxExemptNumber", length = 50)
    private String taxExemptNumber;

    @Column(name = "Notes")
    private String notes;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;

    @Column(name = "CreatedDate", nullable = false, insertable = false, updatable = false)
    private Date createdDate;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    public Customer() {
        this.countryCode = "US";
        this.loyaltyPoints = 0;
        this.loyaltyTier = 0;
        this.totalSpend = BigDecimal.ZERO;
        this.visitCount = 0;
        this.taxExempt = false;
        this.isActive = true;
    }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    public String getCustomerNumber() { return customerNumber; }
    public void setCustomerNumber(String customerNumber) { this.customerNumber = customerNumber; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getMobilePhone() { return mobilePhone; }
    public void setMobilePhone(String mobilePhone) { this.mobilePhone = mobilePhone; }
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
    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Integer getLoyaltyPoints() { return loyaltyPoints; }
    public void setLoyaltyPoints(Integer loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }
    public Byte getLoyaltyTier() { return loyaltyTier; }
    public void setLoyaltyTier(Byte loyaltyTier) { this.loyaltyTier = loyaltyTier; }
    public BigDecimal getTotalSpend() { return totalSpend; }
    public void setTotalSpend(BigDecimal totalSpend) { this.totalSpend = totalSpend; }
    public Integer getVisitCount() { return visitCount; }
    public void setVisitCount(Integer visitCount) { this.visitCount = visitCount; }
    public Date getLastVisitDate() { return lastVisitDate; }
    public void setLastVisitDate(Date lastVisitDate) { this.lastVisitDate = lastVisitDate; }
    public Integer getPreferredStoreId() { return preferredStoreId; }
    public void setPreferredStoreId(Integer preferredStoreId) { this.preferredStoreId = preferredStoreId; }
    public Boolean getTaxExempt() { return taxExempt; }
    public void setTaxExempt(Boolean taxExempt) { this.taxExempt = taxExempt; }
    public String getTaxExemptNumber() { return taxExemptNumber; }
    public void setTaxExemptNumber(String taxExemptNumber) { this.taxExemptNumber = taxExemptNumber; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public Date getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(Date modifiedDate) { this.modifiedDate = modifiedDate; }

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        sb.append(firstName);
        if (middleName != null && !middleName.isEmpty()) {
            sb.append(" ").append(middleName);
        }
        sb.append(" ").append(lastName);
        return sb.toString();
    }
}
