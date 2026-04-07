package com.retailcore.entity;

import com.retailcore.orm.Column;
import com.retailcore.orm.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_Employee")
public class Employee {

    @Column(name = "EmployeeID", primaryKey = true, autoIncrement = true)
    private Integer employeeId;

    @Column(name = "EmployeeNumber", nullable = false, length = 20)
    private String employeeNumber;

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

    @Column(name = "StoreID")
    private Integer storeId;

    @Column(name = "DepartmentID")
    private Integer departmentId;

    @Column(name = "JobTitle", length = 100)
    private String jobTitle;

    @Column(name = "HireDate", nullable = false)
    private Date hireDate;

    @Column(name = "TerminationDate")
    private Date terminationDate;

    @Column(name = "HourlyRate")
    private BigDecimal hourlyRate;

    @Column(name = "SalaryAmount")
    private BigDecimal salaryAmount;

    @Column(name = "CommissionRate")
    private BigDecimal commissionRate;

    @Column(name = "ManagerEmployeeID")
    private Integer managerEmployeeId;

    @Column(name = "AccessLevel", nullable = false)
    private Byte accessLevel;

    @Column(name = "PinCode", length = 10)
    private String pinCode;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;

    @Column(name = "CreatedDate", nullable = false, insertable = false, updatable = false)
    private Date createdDate;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    public Employee() {
        this.commissionRate = BigDecimal.ZERO;
        this.accessLevel = 1;
        this.isActive = true;
    }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }
    public String getEmployeeNumber() { return employeeNumber; }
    public void setEmployeeNumber(String employeeNumber) { this.employeeNumber = employeeNumber; }
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
    public Integer getStoreId() { return storeId; }
    public void setStoreId(Integer storeId) { this.storeId = storeId; }
    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public Date getHireDate() { return hireDate; }
    public void setHireDate(Date hireDate) { this.hireDate = hireDate; }
    public Date getTerminationDate() { return terminationDate; }
    public void setTerminationDate(Date terminationDate) { this.terminationDate = terminationDate; }
    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }
    public BigDecimal getSalaryAmount() { return salaryAmount; }
    public void setSalaryAmount(BigDecimal salaryAmount) { this.salaryAmount = salaryAmount; }
    public BigDecimal getCommissionRate() { return commissionRate; }
    public void setCommissionRate(BigDecimal commissionRate) { this.commissionRate = commissionRate; }
    public Integer getManagerEmployeeId() { return managerEmployeeId; }
    public void setManagerEmployeeId(Integer managerEmployeeId) { this.managerEmployeeId = managerEmployeeId; }
    public Byte getAccessLevel() { return accessLevel; }
    public void setAccessLevel(Byte accessLevel) { this.accessLevel = accessLevel; }
    public String getPinCode() { return pinCode; }
    public void setPinCode(String pinCode) { this.pinCode = pinCode; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public Date getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(Date modifiedDate) { this.modifiedDate = modifiedDate; }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
