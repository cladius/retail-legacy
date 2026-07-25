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

DO $$
DECLARE
    region_id integer;
    department_id integer;
    sub_department_id integer;
    category_id integer;
    sub_category_id integer;
    vendor_id integer;
    store_id integer;
    manager_employee_id integer;
    product_id integer;
    second_product_id integer;
    customer_id integer;
BEGIN
    INSERT INTO tbl_region (regionname, regioncode, countrycode, taxrate, isactive)
    VALUES ('United States West', 'US-WEST', 'US', 0.0825, TRUE)
    ON CONFLICT (regioncode) DO NOTHING;

    SELECT regionid INTO region_id
    FROM tbl_region
    WHERE regioncode = 'US-WEST';

    INSERT INTO tbl_department (departmentname, departmentcode, parentdepartmentid, sortorder, isactive)
    VALUES ('Grocery', 'GROC', NULL, 1, TRUE)
    ON CONFLICT (departmentcode) DO NOTHING;

    SELECT departmentid INTO department_id
    FROM tbl_department
    WHERE departmentcode = 'GROC';

    INSERT INTO tbl_department (departmentname, departmentcode, parentdepartmentid, sortorder, isactive)
    VALUES ('Snacks', 'SNACK', department_id, 2, TRUE)
    ON CONFLICT (departmentcode) DO NOTHING;

    SELECT departmentid INTO sub_department_id
    FROM tbl_department
    WHERE departmentcode = 'SNACK';

    INSERT INTO tbl_category (categoryname, categorycode, departmentid, parentcategoryid, sortorder, isactive)
    VALUES ('Pantry Staples', 'PANTRY', department_id, NULL, 1, TRUE)
    ON CONFLICT (categorycode) DO NOTHING;

    SELECT categoryid INTO category_id
    FROM tbl_category
    WHERE categorycode = 'PANTRY';

    INSERT INTO tbl_category (categoryname, categorycode, departmentid, parentcategoryid, sortorder, isactive)
    VALUES ('Chips and Snacks', 'CHIPS', sub_department_id, category_id, 2, TRUE)
    ON CONFLICT (categorycode) DO NOTHING;

    SELECT categoryid INTO sub_category_id
    FROM tbl_category
    WHERE categorycode = 'CHIPS';

    INSERT INTO tbl_vendor (vendorname, vendorcode, contactname, contactemail, contactphone, city, stateprovince, postalcode, countrycode, paymentterms, leadtimedays, isactive)
    VALUES ('West Coast Wholesale', 'VEND-001', 'Jordan Lee', 'orders@example.test', '555-0100', 'Portland', 'OR', '97205', 'US', 'Net 30', 7, TRUE)
    ON CONFLICT (vendorcode) DO NOTHING;

    SELECT vendorid INTO vendor_id
    FROM tbl_vendor
    WHERE vendorcode = 'VEND-001';

    INSERT INTO tbl_store (storename, storecode, regionid, address1, city, stateprovince, postalcode, phone, email, manageremployeeid, opendate, squarefootage, storetype, isactive)
    VALUES ('RetailCore Downtown', 'STORE-001', region_id, '100 Main Street', 'Portland', 'OR', '97205', '555-0110', 'store001@example.test', NULL, '2024-01-01', 12000, 1, TRUE)
    ON CONFLICT (storecode) DO NOTHING;

    SELECT storeid INTO store_id
    FROM tbl_store
    WHERE storecode = 'STORE-001';

    INSERT INTO tbl_product (sku, upc, productname, shortdescription, categoryid, vendorid, brand, unitcost, retailprice, istaxable, isdiscountable, isreturnable, returnwindowdays, minstocklevel, maxstocklevel, reorderpoint, reorderquantity, status)
    VALUES ('SKU-001', '000000000001', 'Classic Potato Chips', 'Salted potato chips', sub_category_id, vendor_id, 'RetailCore', 1.2500, 2.99, TRUE, TRUE, TRUE, 30, 10, 500, 25, 100, 1)
    ON CONFLICT (sku) DO NOTHING;

    SELECT productid INTO product_id
    FROM tbl_product
    WHERE sku = 'SKU-001';

    INSERT INTO tbl_product (sku, upc, productname, shortdescription, categoryid, vendorid, brand, unitcost, retailprice, saleprice, salestartdate, saleenddate, istaxable, isdiscountable, isreturnable, returnwindowdays, minstocklevel, maxstocklevel, reorderpoint, reorderquantity, status)
    VALUES ('SKU-002', '000000000002', 'Organic Tomato Sauce', 'Tomato sauce, 24 oz', category_id, vendor_id, 'RetailCore', 2.0000, 4.49, 3.99, CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP + INTERVAL '30 days', TRUE, TRUE, TRUE, 30, 10, 500, 25, 100, 1)
    ON CONFLICT (sku) DO NOTHING;

    SELECT productid INTO second_product_id
    FROM tbl_product
    WHERE sku = 'SKU-002';

    INSERT INTO tbl_employee (employeenumber, firstname, lastname, email, phone, storeid, departmentid, jobtitle, hiredate, hourlyrate, accesslevel, pincode, isactive)
    VALUES ('E00001', 'Alex', 'Manager', 'alex.manager@example.test', '555-0120', store_id, department_id, 'Store Manager', '2024-01-01', 32.00, 9, '1234', TRUE)
    ON CONFLICT (employeenumber) DO NOTHING;

    SELECT employeeid INTO manager_employee_id
    FROM tbl_employee
    WHERE employeenumber = 'E00001';

    INSERT INTO tbl_employee (employeenumber, firstname, lastname, email, phone, storeid, departmentid, jobtitle, hiredate, hourlyrate, accesslevel, pincode, isactive)
    VALUES ('E00002', 'Sam', 'Cashier', 'sam.cashier@example.test', '555-0121', store_id, department_id, 'Cashier', '2024-02-01', 18.00, 1, '5678', TRUE)
    ON CONFLICT (employeenumber) DO NOTHING;

    UPDATE tbl_employee
    SET manageremployeeid = manager_employee_id,
        modifieddate = CURRENT_TIMESTAMP
    WHERE employeenumber = 'E00002'
      AND (manageremployeeid IS NULL OR manageremployeeid <> manager_employee_id);

    UPDATE tbl_store
    SET manageremployeeid = manager_employee_id,
        modifieddate = CURRENT_TIMESTAMP
    WHERE storecode = 'STORE-001'
      AND (manageremployeeid IS NULL OR manageremployeeid <> manager_employee_id);

    INSERT INTO tbl_customer (customernumber, firstname, lastname, email, phone, mobilephone, address1, city, stateprovince, postalcode, countrycode, loyaltypoints, loyaltytier, totalspend, visitcount, preferredstoreid, taxexempt, isactive)
    VALUES ('C00000001', 'Taylor', 'Customer', 'taylor.customer@example.test', '555-0130', '555-0131', '200 Oak Avenue', 'Portland', 'OR', '97205', 'US', 1200, 1, 250.00, 3, store_id, FALSE, TRUE)
    ON CONFLICT (customernumber) DO NOTHING;

    SELECT customerid INTO customer_id
    FROM tbl_customer
    WHERE customernumber = 'C00000001';

    INSERT INTO tbl_inventory (productid, storeid, quantityonhand, quantityreserved, quantityonorder, binlocation, aislename, shelfnumber, lastcountdate)
    VALUES (product_id, store_id, 100, 0, 0, 'A-01-01', 'A', '01', CURRENT_TIMESTAMP)
    ON CONFLICT DO NOTHING;

    INSERT INTO tbl_inventory (productid, storeid, quantityonhand, quantityreserved, quantityonorder, binlocation, aislename, shelfnumber, lastcountdate)
    VALUES (second_product_id, store_id, 75, 0, 0, 'A-02-01', 'A', '02', CURRENT_TIMESTAMP)
    ON CONFLICT DO NOTHING;

    INSERT INTO tbl_promotion (promotionname, promotioncode, description, discounttype, discountvalue, startdate, enddate, usagelimit, applicablestoreid, isstackable, isactive)
    VALUES ('Ten Percent Off', 'SAVE10', 'Ten percent off eligible products', 1, 10.00, CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP + INTERVAL '30 days', 1000, store_id, FALSE, TRUE)
    ON CONFLICT (promotioncode) DO NOTHING;

    INSERT INTO tbl_giftcard (cardnumber, pin, originalbalance, currentbalance, purchasedatstoreid, purchasedbycustomerid, purchasedate, status)
    VALUES ('GC-00000001', '2468', 100.00, 100.00, store_id, customer_id, CURRENT_TIMESTAMP, 1)
    ON CONFLICT (cardnumber) DO NOTHING;

    INSERT INTO tbl_taxrule (rulename, regionid, taxrate, priority, startdate, isactive)
    VALUES ('US West Standard Tax', region_id, 0.0825, 0, '2024-01-01', TRUE)
    ON CONFLICT DO NOTHING;
END $$;

SELECT 'Region' AS entity, COUNT(*) AS rowcount
FROM tbl_region
WHERE regioncode = 'US-WEST'
UNION ALL
SELECT 'Store' AS entity, COUNT(*) AS rowcount
FROM tbl_store
WHERE storecode = 'STORE-001'
UNION ALL
SELECT 'Products' AS entity, COUNT(*) AS rowcount
FROM tbl_product
WHERE sku IN ('SKU-001', 'SKU-002')
UNION ALL
SELECT 'Employees' AS entity, COUNT(*) AS rowcount
FROM tbl_employee
WHERE employeenumber IN ('E00001', 'E00002')
UNION ALL
SELECT 'Inventory' AS entity, COUNT(*) AS rowcount
FROM tbl_inventory AS i
INNER JOIN tbl_store AS s ON s.storeid = i.storeid
WHERE s.storecode = 'STORE-001';
