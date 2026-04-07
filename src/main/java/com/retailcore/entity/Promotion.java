package com.retailcore.entity;

import com.retailcore.orm.Column;
import com.retailcore.orm.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_Promotion")
public class Promotion {

    @Column(name = "PromotionID", primaryKey = true, autoIncrement = true)
    private Integer promotionId;

    @Column(name = "PromotionName", nullable = false, length = 200)
    private String promotionName;

    @Column(name = "PromotionCode", nullable = false, length = 30)
    private String promotionCode;

    @Column(name = "Description", length = 500)
    private String description;

    @Column(name = "DiscountType", nullable = false)
    private Byte discountType;

    @Column(name = "DiscountValue", nullable = false)
    private BigDecimal discountValue;

    @Column(name = "MinPurchaseAmount")
    private BigDecimal minPurchaseAmount;

    @Column(name = "MaxDiscountAmount")
    private BigDecimal maxDiscountAmount;

    @Column(name = "StartDate", nullable = false)
    private Date startDate;

    @Column(name = "EndDate", nullable = false)
    private Date endDate;

    @Column(name = "UsageLimit")
    private Integer usageLimit;

    @Column(name = "UsageCount", nullable = false)
    private Integer usageCount;

    @Column(name = "PerCustomerLimit")
    private Integer perCustomerLimit;

    @Column(name = "ApplicableCategoryID")
    private Integer applicableCategoryId;

    @Column(name = "ApplicableProductID")
    private Integer applicableProductId;

    @Column(name = "ApplicableStoreID")
    private Integer applicableStoreId;

    @Column(name = "RequiresLoyaltyTier")
    private Byte requiresLoyaltyTier;

    @Column(name = "IsStackable", nullable = false)
    private Boolean isStackable;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;

    @Column(name = "CreatedDate", nullable = false, insertable = false, updatable = false)
    private Date createdDate;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    public Promotion() {
        this.discountType = 1;
        this.discountValue = BigDecimal.ZERO;
        this.usageCount = 0;
        this.isStackable = false;
        this.isActive = true;
    }

    public Integer getPromotionId() { return promotionId; }
    public void setPromotionId(Integer promotionId) { this.promotionId = promotionId; }
    public String getPromotionName() { return promotionName; }
    public void setPromotionName(String promotionName) { this.promotionName = promotionName; }
    public String getPromotionCode() { return promotionCode; }
    public void setPromotionCode(String promotionCode) { this.promotionCode = promotionCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Byte getDiscountType() { return discountType; }
    public void setDiscountType(Byte discountType) { this.discountType = discountType; }
    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }
    public BigDecimal getMinPurchaseAmount() { return minPurchaseAmount; }
    public void setMinPurchaseAmount(BigDecimal minPurchaseAmount) { this.minPurchaseAmount = minPurchaseAmount; }
    public BigDecimal getMaxDiscountAmount() { return maxDiscountAmount; }
    public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) { this.maxDiscountAmount = maxDiscountAmount; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public Integer getUsageLimit() { return usageLimit; }
    public void setUsageLimit(Integer usageLimit) { this.usageLimit = usageLimit; }
    public Integer getUsageCount() { return usageCount; }
    public void setUsageCount(Integer usageCount) { this.usageCount = usageCount; }
    public Integer getPerCustomerLimit() { return perCustomerLimit; }
    public void setPerCustomerLimit(Integer perCustomerLimit) { this.perCustomerLimit = perCustomerLimit; }
    public Integer getApplicableCategoryId() { return applicableCategoryId; }
    public void setApplicableCategoryId(Integer applicableCategoryId) { this.applicableCategoryId = applicableCategoryId; }
    public Integer getApplicableProductId() { return applicableProductId; }
    public void setApplicableProductId(Integer applicableProductId) { this.applicableProductId = applicableProductId; }
    public Integer getApplicableStoreId() { return applicableStoreId; }
    public void setApplicableStoreId(Integer applicableStoreId) { this.applicableStoreId = applicableStoreId; }
    public Byte getRequiresLoyaltyTier() { return requiresLoyaltyTier; }
    public void setRequiresLoyaltyTier(Byte requiresLoyaltyTier) { this.requiresLoyaltyTier = requiresLoyaltyTier; }
    public Boolean getIsStackable() { return isStackable; }
    public void setIsStackable(Boolean isStackable) { this.isStackable = isStackable; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public Date getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(Date modifiedDate) { this.modifiedDate = modifiedDate; }
}
