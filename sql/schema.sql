USE master;
GO

IF EXISTS (SELECT name FROM sys.databases WHERE name = N'RetailCoreDB')
    DROP DATABASE RetailCoreDB;
GO

CREATE DATABASE RetailCoreDB;
GO

USE RetailCoreDB;
GO

CREATE TABLE tbl_Region (
    RegionID INT IDENTITY(1,1) PRIMARY KEY,
    RegionName VARCHAR(100) NOT NULL,
    RegionCode VARCHAR(10) NOT NULL UNIQUE,
    CountryCode VARCHAR(5) NOT NULL DEFAULT 'US',
    TaxRate DECIMAL(5,4) NOT NULL DEFAULT 0.0000,
    IsActive BIT NOT NULL DEFAULT 1,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL
);

CREATE TABLE tbl_Store (
    StoreID INT IDENTITY(1,1) PRIMARY KEY,
    StoreName VARCHAR(200) NOT NULL,
    StoreCode VARCHAR(20) NOT NULL UNIQUE,
    RegionID INT NOT NULL FOREIGN KEY REFERENCES tbl_Region(RegionID),
    Address1 VARCHAR(255) NOT NULL,
    Address2 VARCHAR(255) NULL,
    City VARCHAR(100) NOT NULL,
    StateProvince VARCHAR(50) NOT NULL,
    PostalCode VARCHAR(20) NOT NULL,
    Phone VARCHAR(30) NULL,
    Fax VARCHAR(30) NULL,
    Email VARCHAR(150) NULL,
    ManagerEmployeeID INT NULL,
    OpenDate DATE NOT NULL,
    CloseDate DATE NULL,
    SquareFootage INT NULL,
    StoreType TINYINT NOT NULL DEFAULT 1,
    IsActive BIT NOT NULL DEFAULT 1,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL
);

CREATE TABLE tbl_Department (
    DepartmentID INT IDENTITY(1,1) PRIMARY KEY,
    DepartmentName VARCHAR(100) NOT NULL,
    DepartmentCode VARCHAR(10) NOT NULL UNIQUE,
    ParentDepartmentID INT NULL FOREIGN KEY REFERENCES tbl_Department(DepartmentID),
    SortOrder INT NOT NULL DEFAULT 0,
    IsActive BIT NOT NULL DEFAULT 1,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL
);

CREATE TABLE tbl_Category (
    CategoryID INT IDENTITY(1,1) PRIMARY KEY,
    CategoryName VARCHAR(150) NOT NULL,
    CategoryCode VARCHAR(20) NOT NULL UNIQUE,
    DepartmentID INT NOT NULL FOREIGN KEY REFERENCES tbl_Department(DepartmentID),
    ParentCategoryID INT NULL FOREIGN KEY REFERENCES tbl_Category(CategoryID),
    Description VARCHAR(500) NULL,
    ImagePath VARCHAR(500) NULL,
    SortOrder INT NOT NULL DEFAULT 0,
    IsActive BIT NOT NULL DEFAULT 1,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL
);

CREATE TABLE tbl_Vendor (
    VendorID INT IDENTITY(1,1) PRIMARY KEY,
    VendorName VARCHAR(200) NOT NULL,
    VendorCode VARCHAR(20) NOT NULL UNIQUE,
    ContactName VARCHAR(150) NULL,
    ContactEmail VARCHAR(150) NULL,
    ContactPhone VARCHAR(30) NULL,
    Address1 VARCHAR(255) NULL,
    Address2 VARCHAR(255) NULL,
    City VARCHAR(100) NULL,
    StateProvince VARCHAR(50) NULL,
    PostalCode VARCHAR(20) NULL,
    CountryCode VARCHAR(5) NOT NULL DEFAULT 'US',
    PaymentTerms VARCHAR(50) NULL,
    LeadTimeDays INT NULL DEFAULT 14,
    MinOrderAmount DECIMAL(12,2) NULL,
    IsActive BIT NOT NULL DEFAULT 1,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL
);

CREATE TABLE tbl_Product (
    ProductID INT IDENTITY(1,1) PRIMARY KEY,
    SKU VARCHAR(50) NOT NULL UNIQUE,
    UPC VARCHAR(20) NULL,
    ProductName VARCHAR(300) NOT NULL,
    ShortDescription VARCHAR(500) NULL,
    LongDescription TEXT NULL,
    CategoryID INT NOT NULL FOREIGN KEY REFERENCES tbl_Category(CategoryID),
    VendorID INT NULL FOREIGN KEY REFERENCES tbl_Vendor(VendorID),
    Brand VARCHAR(100) NULL,
    ModelNumber VARCHAR(100) NULL,
    UnitCost DECIMAL(12,4) NOT NULL DEFAULT 0.0000,
    RetailPrice DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    SalePrice DECIMAL(12,2) NULL,
    SaleStartDate DATETIME NULL,
    SaleEndDate DATETIME NULL,
    Weight DECIMAL(10,4) NULL,
    WeightUnit VARCHAR(10) NULL DEFAULT 'lb',
    Length DECIMAL(10,2) NULL,
    Width DECIMAL(10,2) NULL,
    Height DECIMAL(10,2) NULL,
    DimensionUnit VARCHAR(10) NULL DEFAULT 'in',
    Color VARCHAR(50) NULL,
    Size VARCHAR(50) NULL,
    Material VARCHAR(100) NULL,
    IsTaxable BIT NOT NULL DEFAULT 1,
    IsDiscountable BIT NOT NULL DEFAULT 1,
    IsReturnable BIT NOT NULL DEFAULT 1,
    ReturnWindowDays INT NOT NULL DEFAULT 30,
    MinStockLevel INT NOT NULL DEFAULT 5,
    MaxStockLevel INT NOT NULL DEFAULT 500,
    ReorderPoint INT NOT NULL DEFAULT 10,
    ReorderQuantity INT NOT NULL DEFAULT 50,
    Status TINYINT NOT NULL DEFAULT 1,
    ImagePath VARCHAR(500) NULL,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL
);

CREATE TABLE tbl_Inventory (
    InventoryID INT IDENTITY(1,1) PRIMARY KEY,
    ProductID INT NOT NULL FOREIGN KEY REFERENCES tbl_Product(ProductID),
    StoreID INT NOT NULL FOREIGN KEY REFERENCES tbl_Store(StoreID),
    QuantityOnHand INT NOT NULL DEFAULT 0,
    QuantityReserved INT NOT NULL DEFAULT 0,
    QuantityOnOrder INT NOT NULL DEFAULT 0,
    BinLocation VARCHAR(50) NULL,
    AisleName VARCHAR(50) NULL,
    ShelfNumber VARCHAR(20) NULL,
    LastCountDate DATETIME NULL,
    LastReceivedDate DATETIME NULL,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL,
    CONSTRAINT UQ_Inventory_Product_Store UNIQUE (ProductID, StoreID)
);

CREATE TABLE tbl_Employee (
    EmployeeID INT IDENTITY(1,1) PRIMARY KEY,
    EmployeeNumber VARCHAR(20) NOT NULL UNIQUE,
    FirstName VARCHAR(100) NOT NULL,
    LastName VARCHAR(100) NOT NULL,
    MiddleName VARCHAR(100) NULL,
    Email VARCHAR(150) NULL,
    Phone VARCHAR(30) NULL,
    StoreID INT NULL FOREIGN KEY REFERENCES tbl_Store(StoreID),
    DepartmentID INT NULL FOREIGN KEY REFERENCES tbl_Department(DepartmentID),
    JobTitle VARCHAR(100) NULL,
    HireDate DATE NOT NULL,
    TerminationDate DATE NULL,
    HourlyRate DECIMAL(8,2) NULL,
    SalaryAmount DECIMAL(12,2) NULL,
    CommissionRate DECIMAL(5,4) NULL DEFAULT 0.0000,
    ManagerEmployeeID INT NULL FOREIGN KEY REFERENCES tbl_Employee(EmployeeID),
    AccessLevel TINYINT NOT NULL DEFAULT 1,
    PinCode VARCHAR(10) NULL,
    IsActive BIT NOT NULL DEFAULT 1,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL
);

ALTER TABLE tbl_Store ADD CONSTRAINT FK_Store_Manager FOREIGN KEY (ManagerEmployeeID) REFERENCES tbl_Employee(EmployeeID);

CREATE TABLE tbl_Customer (
    CustomerID INT IDENTITY(1,1) PRIMARY KEY,
    CustomerNumber VARCHAR(20) NOT NULL UNIQUE,
    FirstName VARCHAR(100) NOT NULL,
    LastName VARCHAR(100) NOT NULL,
    MiddleName VARCHAR(100) NULL,
    Email VARCHAR(150) NULL,
    Phone VARCHAR(30) NULL,
    MobilePhone VARCHAR(30) NULL,
    Address1 VARCHAR(255) NULL,
    Address2 VARCHAR(255) NULL,
    City VARCHAR(100) NULL,
    StateProvince VARCHAR(50) NULL,
    PostalCode VARCHAR(20) NULL,
    CountryCode VARCHAR(5) NOT NULL DEFAULT 'US',
    DateOfBirth DATE NULL,
    Gender CHAR(1) NULL,
    LoyaltyPoints INT NOT NULL DEFAULT 0,
    LoyaltyTier TINYINT NOT NULL DEFAULT 0,
    TotalSpend DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    VisitCount INT NOT NULL DEFAULT 0,
    LastVisitDate DATETIME NULL,
    PreferredStoreID INT NULL FOREIGN KEY REFERENCES tbl_Store(StoreID),
    TaxExempt BIT NOT NULL DEFAULT 0,
    TaxExemptNumber VARCHAR(50) NULL,
    Notes TEXT NULL,
    IsActive BIT NOT NULL DEFAULT 1,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL
);

CREATE TABLE tbl_Promotion (
    PromotionID INT IDENTITY(1,1) PRIMARY KEY,
    PromotionName VARCHAR(200) NOT NULL,
    PromotionCode VARCHAR(30) NOT NULL UNIQUE,
    Description VARCHAR(500) NULL,
    DiscountType TINYINT NOT NULL DEFAULT 1,
    DiscountValue DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    MinPurchaseAmount DECIMAL(12,2) NULL,
    MaxDiscountAmount DECIMAL(12,2) NULL,
    StartDate DATETIME NOT NULL,
    EndDate DATETIME NOT NULL,
    UsageLimit INT NULL,
    UsageCount INT NOT NULL DEFAULT 0,
    PerCustomerLimit INT NULL,
    ApplicableCategoryID INT NULL FOREIGN KEY REFERENCES tbl_Category(CategoryID),
    ApplicableProductID INT NULL FOREIGN KEY REFERENCES tbl_Product(ProductID),
    ApplicableStoreID INT NULL FOREIGN KEY REFERENCES tbl_Store(StoreID),
    RequiresLoyaltyTier TINYINT NULL,
    IsStackable BIT NOT NULL DEFAULT 0,
    IsActive BIT NOT NULL DEFAULT 1,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL
);

CREATE TABLE tbl_Transaction (
    TransactionID INT IDENTITY(1,1) PRIMARY KEY,
    TransactionNumber VARCHAR(30) NOT NULL UNIQUE,
    StoreID INT NOT NULL FOREIGN KEY REFERENCES tbl_Store(StoreID),
    RegisterNumber INT NOT NULL DEFAULT 1,
    EmployeeID INT NOT NULL FOREIGN KEY REFERENCES tbl_Employee(EmployeeID),
    CustomerID INT NULL FOREIGN KEY REFERENCES tbl_Customer(CustomerID),
    TransactionDate DATETIME NOT NULL DEFAULT GETDATE(),
    TransactionType TINYINT NOT NULL DEFAULT 1,
    SubTotal DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    DiscountTotal DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    TaxTotal DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    GrandTotal DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    TenderAmount DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    ChangeAmount DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    PromotionID INT NULL FOREIGN KEY REFERENCES tbl_Promotion(PromotionID),
    LoyaltyPointsEarned INT NOT NULL DEFAULT 0,
    LoyaltyPointsRedeemed INT NOT NULL DEFAULT 0,
    Status TINYINT NOT NULL DEFAULT 1,
    VoidReason VARCHAR(255) NULL,
    VoidEmployeeID INT NULL FOREIGN KEY REFERENCES tbl_Employee(EmployeeID),
    Notes VARCHAR(500) NULL,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL
);

CREATE TABLE tbl_TransactionItem (
    TransactionItemID INT IDENTITY(1,1) PRIMARY KEY,
    TransactionID INT NOT NULL FOREIGN KEY REFERENCES tbl_Transaction(TransactionID),
    ProductID INT NOT NULL FOREIGN KEY REFERENCES tbl_Product(ProductID),
    Quantity INT NOT NULL DEFAULT 1,
    UnitPrice DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    DiscountAmount DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    TaxAmount DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    LineTotal DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    ReturnQuantity INT NOT NULL DEFAULT 0,
    SerialNumber VARCHAR(100) NULL,
    IsVoided BIT NOT NULL DEFAULT 0,
    VoidReason VARCHAR(255) NULL,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL
);

CREATE TABLE tbl_Payment (
    PaymentID INT IDENTITY(1,1) PRIMARY KEY,
    TransactionID INT NOT NULL FOREIGN KEY REFERENCES tbl_Transaction(TransactionID),
    PaymentMethod TINYINT NOT NULL DEFAULT 1,
    Amount DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    ReferenceNumber VARCHAR(100) NULL,
    CardType VARCHAR(30) NULL,
    CardLastFour VARCHAR(4) NULL,
    AuthorizationCode VARCHAR(50) NULL,
    CheckNumber VARCHAR(20) NULL,
    GiftCardNumber VARCHAR(30) NULL,
    Status TINYINT NOT NULL DEFAULT 1,
    ProcessedDate DATETIME NOT NULL DEFAULT GETDATE(),
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL
);

CREATE TABLE tbl_Return (
    ReturnID INT IDENTITY(1,1) PRIMARY KEY,
    ReturnNumber VARCHAR(30) NOT NULL UNIQUE,
    OriginalTransactionID INT NOT NULL FOREIGN KEY REFERENCES tbl_Transaction(TransactionID),
    StoreID INT NOT NULL FOREIGN KEY REFERENCES tbl_Store(StoreID),
    EmployeeID INT NOT NULL FOREIGN KEY REFERENCES tbl_Employee(EmployeeID),
    CustomerID INT NULL FOREIGN KEY REFERENCES tbl_Customer(CustomerID),
    ReturnDate DATETIME NOT NULL DEFAULT GETDATE(),
    ReturnReason TINYINT NOT NULL DEFAULT 1,
    ReasonDescription VARCHAR(500) NULL,
    RefundAmount DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    RefundMethod TINYINT NOT NULL DEFAULT 1,
    Status TINYINT NOT NULL DEFAULT 1,
    ManagerApprovalID INT NULL FOREIGN KEY REFERENCES tbl_Employee(EmployeeID),
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL
);

CREATE TABLE tbl_ReturnItem (
    ReturnItemID INT IDENTITY(1,1) PRIMARY KEY,
    ReturnID INT NOT NULL FOREIGN KEY REFERENCES tbl_Return(ReturnID),
    TransactionItemID INT NOT NULL FOREIGN KEY REFERENCES tbl_TransactionItem(TransactionItemID),
    ProductID INT NOT NULL FOREIGN KEY REFERENCES tbl_Product(ProductID),
    Quantity INT NOT NULL DEFAULT 1,
    RefundAmount DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    Condition TINYINT NOT NULL DEFAULT 1,
    RestockFlag BIT NOT NULL DEFAULT 1,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL
);

CREATE TABLE tbl_PurchaseOrder (
    PurchaseOrderID INT IDENTITY(1,1) PRIMARY KEY,
    PONumber VARCHAR(30) NOT NULL UNIQUE,
    VendorID INT NOT NULL FOREIGN KEY REFERENCES tbl_Vendor(VendorID),
    StoreID INT NOT NULL FOREIGN KEY REFERENCES tbl_Store(StoreID),
    OrderedByEmployeeID INT NOT NULL FOREIGN KEY REFERENCES tbl_Employee(EmployeeID),
    ApprovedByEmployeeID INT NULL FOREIGN KEY REFERENCES tbl_Employee(EmployeeID),
    OrderDate DATETIME NOT NULL DEFAULT GETDATE(),
    ExpectedDeliveryDate DATE NULL,
    ActualDeliveryDate DATE NULL,
    SubTotal DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    ShippingCost DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    TaxAmount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    TotalAmount DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    Status TINYINT NOT NULL DEFAULT 1,
    Notes TEXT NULL,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL
);

CREATE TABLE tbl_PurchaseOrderItem (
    PurchaseOrderItemID INT IDENTITY(1,1) PRIMARY KEY,
    PurchaseOrderID INT NOT NULL FOREIGN KEY REFERENCES tbl_PurchaseOrder(PurchaseOrderID),
    ProductID INT NOT NULL FOREIGN KEY REFERENCES tbl_Product(ProductID),
    QuantityOrdered INT NOT NULL DEFAULT 0,
    QuantityReceived INT NOT NULL DEFAULT 0,
    UnitCost DECIMAL(12,4) NOT NULL DEFAULT 0.0000,
    LineTotal DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    Status TINYINT NOT NULL DEFAULT 1,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL
);

CREATE TABLE tbl_PriceHistory (
    PriceHistoryID INT IDENTITY(1,1) PRIMARY KEY,
    ProductID INT NOT NULL FOREIGN KEY REFERENCES tbl_Product(ProductID),
    OldRetailPrice DECIMAL(12,2) NOT NULL,
    NewRetailPrice DECIMAL(12,2) NOT NULL,
    OldCost DECIMAL(12,4) NULL,
    NewCost DECIMAL(12,4) NULL,
    ChangedByEmployeeID INT NULL FOREIGN KEY REFERENCES tbl_Employee(EmployeeID),
    ChangeReason VARCHAR(255) NULL,
    EffectiveDate DATETIME NOT NULL DEFAULT GETDATE(),
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE()
);

CREATE TABLE tbl_InventoryAdjustment (
    AdjustmentID INT IDENTITY(1,1) PRIMARY KEY,
    AdjustmentNumber VARCHAR(30) NOT NULL UNIQUE,
    StoreID INT NOT NULL FOREIGN KEY REFERENCES tbl_Store(StoreID),
    EmployeeID INT NOT NULL FOREIGN KEY REFERENCES tbl_Employee(EmployeeID),
    AdjustmentDate DATETIME NOT NULL DEFAULT GETDATE(),
    AdjustmentType TINYINT NOT NULL DEFAULT 1,
    Reason VARCHAR(500) NULL,
    ApprovedByEmployeeID INT NULL FOREIGN KEY REFERENCES tbl_Employee(EmployeeID),
    Status TINYINT NOT NULL DEFAULT 1,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL
);

CREATE TABLE tbl_InventoryAdjustmentItem (
    AdjustmentItemID INT IDENTITY(1,1) PRIMARY KEY,
    AdjustmentID INT NOT NULL FOREIGN KEY REFERENCES tbl_InventoryAdjustment(AdjustmentID),
    ProductID INT NOT NULL FOREIGN KEY REFERENCES tbl_Product(ProductID),
    QuantityBefore INT NOT NULL DEFAULT 0,
    QuantityAfter INT NOT NULL DEFAULT 0,
    QuantityDifference INT NOT NULL DEFAULT 0,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE()
);

CREATE TABLE tbl_GiftCard (
    GiftCardID INT IDENTITY(1,1) PRIMARY KEY,
    CardNumber VARCHAR(30) NOT NULL UNIQUE,
    PIN VARCHAR(10) NULL,
    OriginalBalance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    CurrentBalance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    PurchasedAtStoreID INT NULL FOREIGN KEY REFERENCES tbl_Store(StoreID),
    PurchasedByCustomerID INT NULL FOREIGN KEY REFERENCES tbl_Customer(CustomerID),
    PurchaseDate DATETIME NOT NULL DEFAULT GETDATE(),
    ExpirationDate DATE NULL,
    Status TINYINT NOT NULL DEFAULT 1,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL
);

CREATE TABLE tbl_AuditLog (
    AuditLogID BIGINT IDENTITY(1,1) PRIMARY KEY,
    TableName VARCHAR(100) NOT NULL,
    RecordID INT NOT NULL,
    Action VARCHAR(10) NOT NULL,
    OldValues TEXT NULL,
    NewValues TEXT NULL,
    EmployeeID INT NULL,
    IPAddress VARCHAR(45) NULL,
    Workstation VARCHAR(100) NULL,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE()
);

CREATE TABLE tbl_SystemConfig (
    ConfigID INT IDENTITY(1,1) PRIMARY KEY,
    ConfigKey VARCHAR(100) NOT NULL UNIQUE,
    ConfigValue VARCHAR(500) NOT NULL,
    ConfigGroup VARCHAR(50) NULL,
    DataType VARCHAR(20) NOT NULL DEFAULT 'STRING',
    Description VARCHAR(255) NULL,
    IsEditable BIT NOT NULL DEFAULT 1,
    ModifiedDate DATETIME NULL
);

CREATE TABLE tbl_TaxRule (
    TaxRuleID INT IDENTITY(1,1) PRIMARY KEY,
    RuleName VARCHAR(100) NOT NULL,
    RegionID INT NOT NULL FOREIGN KEY REFERENCES tbl_Region(RegionID),
    CategoryID INT NULL FOREIGN KEY REFERENCES tbl_Category(CategoryID),
    TaxRate DECIMAL(5,4) NOT NULL DEFAULT 0.0000,
    Priority INT NOT NULL DEFAULT 0,
    StartDate DATE NOT NULL,
    EndDate DATE NULL,
    IsActive BIT NOT NULL DEFAULT 1,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    ModifiedDate DATETIME NULL
);

CREATE INDEX IX_Product_SKU ON tbl_Product(SKU);
CREATE INDEX IX_Product_UPC ON tbl_Product(UPC);
CREATE INDEX IX_Product_Category ON tbl_Product(CategoryID);
CREATE INDEX IX_Product_Vendor ON tbl_Product(VendorID);
CREATE INDEX IX_Product_Status ON tbl_Product(Status);
CREATE INDEX IX_Inventory_ProductStore ON tbl_Inventory(ProductID, StoreID);
CREATE INDEX IX_Transaction_Date ON tbl_Transaction(TransactionDate);
CREATE INDEX IX_Transaction_Store ON tbl_Transaction(StoreID);
CREATE INDEX IX_Transaction_Customer ON tbl_Transaction(CustomerID);
CREATE INDEX IX_Transaction_Employee ON tbl_Transaction(EmployeeID);
CREATE INDEX IX_TransactionItem_Transaction ON tbl_TransactionItem(TransactionID);
CREATE INDEX IX_TransactionItem_Product ON tbl_TransactionItem(ProductID);
CREATE INDEX IX_Customer_Number ON tbl_Customer(CustomerNumber);
CREATE INDEX IX_Customer_Email ON tbl_Customer(Email);
CREATE INDEX IX_Customer_LastName ON tbl_Customer(LastName);
CREATE INDEX IX_Employee_Number ON tbl_Employee(EmployeeNumber);
CREATE INDEX IX_Employee_Store ON tbl_Employee(StoreID);
CREATE INDEX IX_PurchaseOrder_Vendor ON tbl_PurchaseOrder(VendorID);
CREATE INDEX IX_PurchaseOrder_Store ON tbl_PurchaseOrder(StoreID);
CREATE INDEX IX_AuditLog_Table ON tbl_AuditLog(TableName, RecordID);
CREATE INDEX IX_AuditLog_Date ON tbl_AuditLog(CreatedDate);
GO

INSERT INTO tbl_SystemConfig (ConfigKey, ConfigValue, ConfigGroup, DataType) VALUES
('RECEIPT_HEADER', 'RetailCore Systems Inc.', 'RECEIPT', 'STRING'),
('RECEIPT_FOOTER', 'Thank you for shopping with us!', 'RECEIPT', 'STRING'),
('TAX_INCLUDED_IN_PRICE', '0', 'TAX', 'BOOLEAN'),
('DEFAULT_TAX_RATE', '0.0825', 'TAX', 'DECIMAL'),
('LOYALTY_POINTS_PER_DOLLAR', '10', 'LOYALTY', 'INTEGER'),
('LOYALTY_TIER_SILVER', '1000', 'LOYALTY', 'INTEGER'),
('LOYALTY_TIER_GOLD', '5000', 'LOYALTY', 'INTEGER'),
('LOYALTY_TIER_PLATINUM', '15000', 'LOYALTY', 'INTEGER'),
('MAX_RETURN_DAYS', '30', 'RETURNS', 'INTEGER'),
('REQUIRE_MANAGER_APPROVAL_AMOUNT', '500.00', 'RETURNS', 'DECIMAL'),
('INVENTORY_LOW_STOCK_ALERT', '1', 'INVENTORY', 'BOOLEAN'),
('PO_AUTO_APPROVE_LIMIT', '1000.00', 'PURCHASING', 'DECIMAL');
GO
