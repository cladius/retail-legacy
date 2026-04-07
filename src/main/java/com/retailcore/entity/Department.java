package com.retailcore.entity;

import com.retailcore.orm.Column;
import com.retailcore.orm.Table;
import java.util.Date;

@Table(name = "tbl_Department")
public class Department {

    @Column(name = "DepartmentID", primaryKey = true, autoIncrement = true)
    private Integer departmentId;

    @Column(name = "DepartmentName", nullable = false, length = 100)
    private String departmentName;

    @Column(name = "DepartmentCode", nullable = false, length = 10)
    private String departmentCode;

    @Column(name = "ParentDepartmentID")
    private Integer parentDepartmentId;

    @Column(name = "SortOrder", nullable = false)
    private Integer sortOrder;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;

    @Column(name = "CreatedDate", nullable = false, insertable = false, updatable = false)
    private Date createdDate;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    public Department() {
        this.sortOrder = 0;
        this.isActive = true;
    }

    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getDepartmentCode() { return departmentCode; }
    public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }
    public Integer getParentDepartmentId() { return parentDepartmentId; }
    public void setParentDepartmentId(Integer parentDepartmentId) { this.parentDepartmentId = parentDepartmentId; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public Date getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(Date modifiedDate) { this.modifiedDate = modifiedDate; }
}
