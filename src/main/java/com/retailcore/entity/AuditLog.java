package com.retailcore.entity;

import com.retailcore.orm.Column;
import com.retailcore.orm.Table;
import java.util.Date;

@Table(name = "tbl_AuditLog")
public class AuditLog {

    @Column(name = "AuditLogID", primaryKey = true, autoIncrement = true)
    private Long auditLogId;

    @Column(name = "TableName", nullable = false, length = 100)
    private String tableName;

    @Column(name = "RecordID", nullable = false)
    private Integer recordId;

    @Column(name = "Action", nullable = false, length = 10)
    private String action;

    @Column(name = "OldValues")
    private String oldValues;

    @Column(name = "NewValues")
    private String newValues;

    @Column(name = "EmployeeID")
    private Integer employeeId;

    @Column(name = "IPAddress", length = 45)
    private String ipAddress;

    @Column(name = "Workstation", length = 100)
    private String workstation;

    @Column(name = "CreatedDate", nullable = false, insertable = false, updatable = false)
    private Date createdDate;

    public AuditLog() {}

    public Long getAuditLogId() { return auditLogId; }
    public void setAuditLogId(Long auditLogId) { this.auditLogId = auditLogId; }
    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }
    public Integer getRecordId() { return recordId; }
    public void setRecordId(Integer recordId) { this.recordId = recordId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getOldValues() { return oldValues; }
    public void setOldValues(String oldValues) { this.oldValues = oldValues; }
    public String getNewValues() { return newValues; }
    public void setNewValues(String newValues) { this.newValues = newValues; }
    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getWorkstation() { return workstation; }
    public void setWorkstation(String workstation) { this.workstation = workstation; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
}
