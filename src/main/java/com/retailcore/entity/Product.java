package com.retailcore.entity;

import com.retailcore.orm.Column;
import com.retailcore.orm.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_Product")
public class Product {

    @Column(name = "ProductID", primaryKey = true, autoIncrement = true)
    private Integer productId;

    @Column(name = "SKU", nullable = false, length = 50)
    private String sku;

    @Column(name = "UPC", length = 20)
    private String upc;

    @Column(name = "ProductName", nullable = false, length = 300)
    private String productName;

    @Column(name = "ShortDescription", length = 500)
    private String shortDescription;

    @Column(name = "LongDescription")
    private String longDescription;

    @Column(name = "CategoryID", nullable = false)
    private Integer categoryId;

    @Column(name = "VendorID")
    private Integer vendorId;

    @Column(name = "Brand", length = 100)
    private String brand;

    @Column(name = "ModelNumber", length = 100)
    private String modelNumber;

    @Column(name = "UnitCost", nullable = false)
    private BigDecimal unitCost;

    @Column(name = "RetailPrice", nullable = false)
    private BigDecimal retailPrice;

    @Column(name = "SalePrice")
    private BigDecimal salePrice;

    @Column(name = "SaleStartDate")
    private Date saleStartDate;

    @Column(name = "SaleEndDate")
    private Date saleEndDate;

    @Column(name = "Weight")
    private BigDecimal weight;

    @Column(name = "WeightUnit", length = 10)
    private String weightUnit;

    @Column(name = "Length")
    private BigDecimal length;

    @Column(name = "Width")
    private BigDecimal width;

    @Column(name = "Height")
    private BigDecimal height;

    @Column(name = "DimensionUnit", length = 10)
    private String dimensionUnit;

    @Column(name = "Color", length = 50)
    private String color;

    @Column(name = "Size", length = 50)
    private String size;

    @Column(name = "Material", length = 100)
    private String material;

    @Column(name = "IsTaxable", nullable = false)
    private Boolean isTaxable;

    @Column(name = "IsDiscountable", nullable = false)
    private Boolean isDiscountable;

    @Column(name = "IsReturnable", nullable = false)
    private Boolean isReturnable;

    @Column(name = "ReturnWindowDays", nullable = false)
    private Integer returnWindowDays;

    @Column(name = "MinStockLevel", nullable = false)
    private Integer minStockLevel;

    @Column(name = "MaxStockLevel", nullable = false)
    private Integer maxStockLevel;

    @Column(name = "ReorderPoint", nullable = false)
    private Integer reorderPoint;

    @Column(name = "ReorderQuantity", nullable = false)
    private Integer reorderQuantity;

    @Column(name = "Status", nullable = false)
    private Byte status;

    @Column(name = "ImagePath", length = 500)
    private String imagePath;

    @Column(name = "CreatedDate", nullable = false, insertable = false, updatable = false)
    private Date createdDate;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    public Product() {
        this.unitCost = BigDecimal.ZERO;
        this.retailPrice = BigDecimal.ZERO;
        this.weightUnit = "lb";
        this.dimensionUnit = "in";
        this.isTaxable = true;
        this.isDiscountable = true;
        this.isReturnable = true;
        this.returnWindowDays = 30;
        this.minStockLevel = 5;
        this.maxStockLevel = 500;
        this.reorderPoint = 10;
        this.reorderQuantity = 50;
        this.status = 1;
    }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getUpc() { return upc; }
    public void setUpc(String upc) { this.upc = upc; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }
    public String getLongDescription() { return longDescription; }
    public void setLongDescription(String longDescription) { this.longDescription = longDescription; }
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    public Integer getVendorId() { return vendorId; }
    public void setVendorId(Integer vendorId) { this.vendorId = vendorId; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModelNumber() { return modelNumber; }
    public void setModelNumber(String modelNumber) { this.modelNumber = modelNumber; }
    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }
    public BigDecimal getRetailPrice() { return retailPrice; }
    public void setRetailPrice(BigDecimal retailPrice) { this.retailPrice = retailPrice; }
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    public Date getSaleStartDate() { return saleStartDate; }
    public void setSaleStartDate(Date saleStartDate) { this.saleStartDate = saleStartDate; }
    public Date getSaleEndDate() { return saleEndDate; }
    public void setSaleEndDate(Date saleEndDate) { this.saleEndDate = saleEndDate; }
    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }
    public String getWeightUnit() { return weightUnit; }
    public void setWeightUnit(String weightUnit) { this.weightUnit = weightUnit; }
    public BigDecimal getLength() { return length; }
    public void setLength(BigDecimal length) { this.length = length; }
    public BigDecimal getWidth() { return width; }
    public void setWidth(BigDecimal width) { this.width = width; }
    public BigDecimal getHeight() { return height; }
    public void setHeight(BigDecimal height) { this.height = height; }
    public String getDimensionUnit() { return dimensionUnit; }
    public void setDimensionUnit(String dimensionUnit) { this.dimensionUnit = dimensionUnit; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    public Boolean getIsTaxable() { return isTaxable; }
    public void setIsTaxable(Boolean isTaxable) { this.isTaxable = isTaxable; }
    public Boolean getIsDiscountable() { return isDiscountable; }
    public void setIsDiscountable(Boolean isDiscountable) { this.isDiscountable = isDiscountable; }
    public Boolean getIsReturnable() { return isReturnable; }
    public void setIsReturnable(Boolean isReturnable) { this.isReturnable = isReturnable; }
    public Integer getReturnWindowDays() { return returnWindowDays; }
    public void setReturnWindowDays(Integer returnWindowDays) { this.returnWindowDays = returnWindowDays; }
    public Integer getMinStockLevel() { return minStockLevel; }
    public void setMinStockLevel(Integer minStockLevel) { this.minStockLevel = minStockLevel; }
    public Integer getMaxStockLevel() { return maxStockLevel; }
    public void setMaxStockLevel(Integer maxStockLevel) { this.maxStockLevel = maxStockLevel; }
    public Integer getReorderPoint() { return reorderPoint; }
    public void setReorderPoint(Integer reorderPoint) { this.reorderPoint = reorderPoint; }
    public Integer getReorderQuantity() { return reorderQuantity; }
    public void setReorderQuantity(Integer reorderQuantity) { this.reorderQuantity = reorderQuantity; }
    public Byte getStatus() { return status; }
    public void setStatus(Byte status) { this.status = status; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public Date getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(Date modifiedDate) { this.modifiedDate = modifiedDate; }
}
