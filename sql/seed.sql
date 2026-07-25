/*
    RetailCore development seed data

    Prerequisite:
      Run sql/schema.sql first.

    This script is intended for local/development environments. It is
    rerunnable and identifies records by their natural keys rather than by
    identity values. It does not delete existing data.

    Test credentials/data:
      Store code: STORE-001
      Employee number: E00001
      Employee PIN: 1234
      Product SKU: SKU-001
      Customer number: C00000001
      Promotion code: SAVE10
      Gift card number: GC-00000001
*/

USE retail;
GO

SET XACT_ABORT ON;
GO

BEGIN TRY
    BEGIN TRANSACTION;

    DECLARE @RegionID INT;
    DECLARE @DepartmentID INT;
    DECLARE @SubDepartmentID INT;
    DECLARE @CategoryID INT;
    DECLARE @SubCategoryID INT;
    DECLARE @VendorID INT;
    DECLARE @StoreID INT;
    DECLARE @ManagerEmployeeID INT;
    DECLARE @CashierEmployeeID INT;
    DECLARE @ProductID INT;
    DECLARE @SecondProductID INT;
    DECLARE @CustomerID INT;

    /* Region */
    IF NOT EXISTS (SELECT 1 FROM dbo.tbl_Region WHERE RegionCode = 'US-WEST')
    BEGIN
        INSERT INTO dbo.tbl_Region
            (RegionName, RegionCode, CountryCode, TaxRate, IsActive)
        VALUES
            ('United States West', 'US-WEST', 'US', 0.0825, 1);
    END;

    SELECT @RegionID = RegionID
    FROM dbo.tbl_Region
    WHERE RegionCode = 'US-WEST';

    /* Department hierarchy */
    IF NOT EXISTS (SELECT 1 FROM dbo.tbl_Department WHERE DepartmentCode = 'GROC')
    BEGIN
        INSERT INTO dbo.tbl_Department
            (DepartmentName, DepartmentCode, ParentDepartmentID, SortOrder, IsActive)
        VALUES
            ('Grocery', 'GROC', NULL, 1, 1);
    END;

    SELECT @DepartmentID = DepartmentID
    FROM dbo.tbl_Department
    WHERE DepartmentCode = 'GROC';

    IF NOT EXISTS (SELECT 1 FROM dbo.tbl_Department WHERE DepartmentCode = 'SNACK')
    BEGIN
        INSERT INTO dbo.tbl_Department
            (DepartmentName, DepartmentCode, ParentDepartmentID, SortOrder, IsActive)
        VALUES
            ('Snacks', 'SNACK', @DepartmentID, 2, 1);
    END;

    SELECT @SubDepartmentID = DepartmentID
    FROM dbo.tbl_Department
    WHERE DepartmentCode = 'SNACK';

    /* Category hierarchy */
    IF NOT EXISTS (SELECT 1 FROM dbo.tbl_Category WHERE CategoryCode = 'PANTRY')
    BEGIN
        INSERT INTO dbo.tbl_Category
            (CategoryName, CategoryCode, DepartmentID, ParentCategoryID, SortOrder, IsActive)
        VALUES
            ('Pantry Staples', 'PANTRY', @DepartmentID, NULL, 1, 1);
    END;

    SELECT @CategoryID = CategoryID
    FROM dbo.tbl_Category
    WHERE CategoryCode = 'PANTRY';

    IF NOT EXISTS (SELECT 1 FROM dbo.tbl_Category WHERE CategoryCode = 'CHIPS')
    BEGIN
        INSERT INTO dbo.tbl_Category
            (CategoryName, CategoryCode, DepartmentID, ParentCategoryID, SortOrder, IsActive)
        VALUES
            ('Chips and Snacks', 'CHIPS', @SubDepartmentID, @CategoryID, 2, 1);
    END;

    SELECT @SubCategoryID = CategoryID
    FROM dbo.tbl_Category
    WHERE CategoryCode = 'CHIPS';

    /* Vendor */
    IF NOT EXISTS (SELECT 1 FROM dbo.tbl_Vendor WHERE VendorCode = 'VEND-001')
    BEGIN
        INSERT INTO dbo.tbl_Vendor
            (VendorName, VendorCode, ContactName, ContactEmail, ContactPhone,
             City, StateProvince, PostalCode, CountryCode, PaymentTerms,
             LeadTimeDays, IsActive)
        VALUES
            ('West Coast Wholesale', 'VEND-001', 'Jordan Lee',
             'orders@example.test', '555-0100', 'Portland', 'OR', '97205',
             'US', 'Net 30', 7, 1);
    END;

    SELECT @VendorID = VendorID
    FROM dbo.tbl_Vendor
    WHERE VendorCode = 'VEND-001';

    /* Store is created before employees because Store.ManagerEmployeeID
       and Employee.StoreID form a circular relationship. */
    IF NOT EXISTS (SELECT 1 FROM dbo.tbl_Store WHERE StoreCode = 'STORE-001')
    BEGIN
        INSERT INTO dbo.tbl_Store
            (StoreName, StoreCode, RegionID, Address1, City, StateProvince,
             PostalCode, Phone, Email, ManagerEmployeeID, OpenDate,
             SquareFootage, StoreType, IsActive)
        VALUES
            ('RetailCore Downtown', 'STORE-001', @RegionID, '100 Main Street',
             'Portland', 'OR', '97205', '555-0110', 'store001@example.test',
             NULL, '2024-01-01', 12000, 1, 1);
    END;

    SELECT @StoreID = StoreID
    FROM dbo.tbl_Store
    WHERE StoreCode = 'STORE-001';

    /* Products */
    IF NOT EXISTS (SELECT 1 FROM dbo.tbl_Product WHERE SKU = 'SKU-001')
    BEGIN
        INSERT INTO dbo.tbl_Product
            (SKU, UPC, ProductName, ShortDescription, CategoryID, VendorID,
             Brand, UnitCost, RetailPrice, IsTaxable, IsDiscountable,
             IsReturnable, ReturnWindowDays, MinStockLevel, MaxStockLevel,
             ReorderPoint, ReorderQuantity, Status)
        VALUES
            ('SKU-001', '000000000001', 'Classic Potato Chips',
             'Salted potato chips', @SubCategoryID, @VendorID, 'RetailCore',
             1.2500, 2.99, 1, 1, 1, 30, 10, 500, 25, 100, 1);
    END;

    SELECT @ProductID = ProductID
    FROM dbo.tbl_Product
    WHERE SKU = 'SKU-001';

    IF NOT EXISTS (SELECT 1 FROM dbo.tbl_Product WHERE SKU = 'SKU-002')
    BEGIN
        INSERT INTO dbo.tbl_Product
            (SKU, UPC, ProductName, ShortDescription, CategoryID, VendorID,
             Brand, UnitCost, RetailPrice, SalePrice, SaleStartDate,
             SaleEndDate, IsTaxable, IsDiscountable, IsReturnable,
             ReturnWindowDays, MinStockLevel, MaxStockLevel, ReorderPoint,
             ReorderQuantity, Status)
        VALUES
            ('SKU-002', '000000000002', 'Organic Tomato Sauce',
             'Tomato sauce, 24 oz', @CategoryID, @VendorID, 'RetailCore',
             2.0000, 4.49, 3.99, DATEADD(day, -30, GETDATE()),
             DATEADD(day, 30, GETDATE()), 1, 1, 1, 30, 10, 500, 25, 100, 1);
    END;

    SELECT @SecondProductID = ProductID
    FROM dbo.tbl_Product
    WHERE SKU = 'SKU-002';

    /* Employees are inserted without manager links; those links are added
       after all employee identities exist. */
    IF NOT EXISTS (SELECT 1 FROM dbo.tbl_Employee WHERE EmployeeNumber = 'E00001')
    BEGIN
        INSERT INTO dbo.tbl_Employee
            (EmployeeNumber, FirstName, LastName, Email, Phone, StoreID,
             DepartmentID, JobTitle, HireDate, HourlyRate, AccessLevel,
             PinCode, IsActive)
        VALUES
            ('E00001', 'Alex', 'Manager', 'alex.manager@example.test',
             '555-0120', @StoreID, @DepartmentID, 'Store Manager',
             '2024-01-01', 32.00, 9, '1234', 1);
    END;

    SELECT @ManagerEmployeeID = EmployeeID
    FROM dbo.tbl_Employee
    WHERE EmployeeNumber = 'E00001';

    IF NOT EXISTS (SELECT 1 FROM dbo.tbl_Employee WHERE EmployeeNumber = 'E00002')
    BEGIN
        INSERT INTO dbo.tbl_Employee
            (EmployeeNumber, FirstName, LastName, Email, Phone, StoreID,
             DepartmentID, JobTitle, HireDate, HourlyRate, AccessLevel,
             PinCode, IsActive)
        VALUES
            ('E00002', 'Sam', 'Cashier', 'sam.cashier@example.test',
             '555-0121', @StoreID, @DepartmentID, 'Cashier',
             '2024-02-01', 18.00, 1, '5678', 1);
    END;

    SELECT @CashierEmployeeID = EmployeeID
    FROM dbo.tbl_Employee
    WHERE EmployeeNumber = 'E00002';

    UPDATE dbo.tbl_Employee
    SET ManagerEmployeeID = @ManagerEmployeeID,
        ModifiedDate = GETDATE()
    WHERE EmployeeNumber = 'E00002'
      AND (ManagerEmployeeID IS NULL OR ManagerEmployeeID <> @ManagerEmployeeID);

    UPDATE dbo.tbl_Store
    SET ManagerEmployeeID = @ManagerEmployeeID,
        ModifiedDate = GETDATE()
    WHERE StoreCode = 'STORE-001'
      AND (ManagerEmployeeID IS NULL OR ManagerEmployeeID <> @ManagerEmployeeID);

    /* Customer */
    IF NOT EXISTS (SELECT 1 FROM dbo.tbl_Customer WHERE CustomerNumber = 'C00000001')
    BEGIN
        INSERT INTO dbo.tbl_Customer
            (CustomerNumber, FirstName, LastName, Email, Phone, MobilePhone,
             Address1, City, StateProvince, PostalCode, CountryCode,
             LoyaltyPoints, LoyaltyTier, TotalSpend, VisitCount,
             PreferredStoreID, TaxExempt, IsActive)
        VALUES
            ('C00000001', 'Taylor', 'Customer', 'taylor.customer@example.test',
             '555-0130', '555-0131', '200 Oak Avenue', 'Portland', 'OR',
             '97205', 'US', 1200, 1, 250.00, 3, @StoreID, 0, 1);
    END;

    SELECT @CustomerID = CustomerID
    FROM dbo.tbl_Customer
    WHERE CustomerNumber = 'C00000001';

    /* Inventory: one row per seeded product/store pair. */
    IF NOT EXISTS
    (
        SELECT 1
        FROM dbo.tbl_Inventory
        WHERE ProductID = @ProductID AND StoreID = @StoreID
    )
    BEGIN
        INSERT INTO dbo.tbl_Inventory
            (ProductID, StoreID, QuantityOnHand, QuantityReserved,
             QuantityOnOrder, BinLocation, AisleName, ShelfNumber,
             LastCountDate)
        VALUES
            (@ProductID, @StoreID, 100, 0, 0, 'A-01-01', 'A', '01', GETDATE());
    END;

    IF NOT EXISTS
    (
        SELECT 1
        FROM dbo.tbl_Inventory
        WHERE ProductID = @SecondProductID AND StoreID = @StoreID
    )
    BEGIN
        INSERT INTO dbo.tbl_Inventory
            (ProductID, StoreID, QuantityOnHand, QuantityReserved,
             QuantityOnOrder, BinLocation, AisleName, ShelfNumber,
             LastCountDate)
        VALUES
            (@SecondProductID, @StoreID, 75, 0, 0, 'A-02-01', 'A', '02', GETDATE());
    END;

    /* Promotion */
    IF NOT EXISTS (SELECT 1 FROM dbo.tbl_Promotion WHERE PromotionCode = 'SAVE10')
    BEGIN
        INSERT INTO dbo.tbl_Promotion
            (PromotionName, PromotionCode, Description, DiscountType,
             DiscountValue, StartDate, EndDate, UsageLimit, ApplicableStoreID,
             IsStackable, IsActive)
        VALUES
            ('Ten Percent Off', 'SAVE10', 'Ten percent off eligible products',
             1, 10.00, DATEADD(day, -30, GETDATE()),
             DATEADD(day, 30, GETDATE()), 1000, @StoreID, 0, 1);
    END;

    /* Gift card */
    IF NOT EXISTS (SELECT 1 FROM dbo.tbl_GiftCard WHERE CardNumber = 'GC-00000001')
    BEGIN
        INSERT INTO dbo.tbl_GiftCard
            (CardNumber, PIN, OriginalBalance, CurrentBalance,
             PurchasedAtStoreID, PurchasedByCustomerID, PurchaseDate, Status)
        VALUES
            ('GC-00000001', '2468', 100.00, 100.00, @StoreID, @CustomerID,
             GETDATE(), 1);
    END;

    /* Tax rule corresponding to the region's default tax rate. */
    IF NOT EXISTS
    (
        SELECT 1
        FROM dbo.tbl_TaxRule
        WHERE RuleName = 'US West Standard Tax'
          AND RegionID = @RegionID
    )
    BEGIN
        INSERT INTO dbo.tbl_TaxRule
            (RuleName, RegionID, TaxRate, Priority, StartDate, IsActive)
        VALUES
            ('US West Standard Tax', @RegionID, 0.0825, 0,
             '2024-01-01', 1);
    END;

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF XACT_STATE() <> 0
        ROLLBACK TRANSACTION;

    THROW;
END CATCH;
GO

/* Basic post-seed verification. */
SELECT 'Region' AS [Entity], COUNT(*) AS [RowCount]
FROM dbo.tbl_Region
WHERE RegionCode = 'US-WEST'
UNION ALL
SELECT 'Store' AS [Entity], COUNT(*) AS [RowCount]
FROM dbo.tbl_Store
WHERE StoreCode = 'STORE-001'
UNION ALL
SELECT 'Products' AS [Entity], COUNT(*) AS [RowCount]
FROM dbo.tbl_Product
WHERE SKU IN ('SKU-001', 'SKU-002')
UNION ALL
SELECT 'Employees' AS [Entity], COUNT(*) AS [RowCount]
FROM dbo.tbl_Employee
WHERE EmployeeNumber IN ('E00001', 'E00002')
UNION ALL
SELECT 'Inventory' AS [Entity], COUNT(*) AS [RowCount]
FROM dbo.tbl_Inventory AS i
INNER JOIN dbo.tbl_Store AS s ON s.StoreID = i.StoreID
WHERE s.StoreCode = 'STORE-001';
GO
