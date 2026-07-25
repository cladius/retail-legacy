-- PostgreSQL-compatible schema for the retail legacy application.
-- The application uses mixed-case identifiers, so the database objects are created
-- with lowercase names to match PostgreSQL's unquoted-identifier folding.

CREATE TABLE tbl_region (
    regionid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    regionname varchar(100) NOT NULL,
    regioncode varchar(10) NOT NULL UNIQUE,
    countrycode varchar(5) NOT NULL DEFAULT 'US',
    taxrate numeric(5,4) NOT NULL DEFAULT 0.0000,
    isactive boolean NOT NULL DEFAULT TRUE,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL
);

CREATE TABLE tbl_store (
    storeid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    storename varchar(200) NOT NULL,
    storecode varchar(20) NOT NULL UNIQUE,
    regionid integer NOT NULL REFERENCES tbl_region(regionid),
    address1 varchar(255) NOT NULL,
    address2 varchar(255) NULL,
    city varchar(100) NOT NULL,
    stateprovince varchar(50) NOT NULL,
    postalcode varchar(20) NOT NULL,
    phone varchar(30) NULL,
    fax varchar(30) NULL,
    email varchar(150) NULL,
    manageremployeeid integer NULL,
    opendate date NOT NULL,
    closedate date NULL,
    squarefootage integer NULL,
    storetype smallint NOT NULL DEFAULT 1,
    isactive boolean NOT NULL DEFAULT TRUE,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL
);

CREATE TABLE tbl_department (
    departmentid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    departmentname varchar(100) NOT NULL,
    departmentcode varchar(10) NOT NULL UNIQUE,
    parentdepartmentid integer NULL REFERENCES tbl_department(departmentid),
    sortorder integer NOT NULL DEFAULT 0,
    isactive boolean NOT NULL DEFAULT TRUE,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL
);

CREATE TABLE tbl_category (
    categoryid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    categoryname varchar(150) NOT NULL,
    categorycode varchar(20) NOT NULL UNIQUE,
    departmentid integer NOT NULL REFERENCES tbl_department(departmentid),
    parentcategoryid integer NULL REFERENCES tbl_category(categoryid),
    description varchar(500) NULL,
    imagepath varchar(500) NULL,
    sortorder integer NOT NULL DEFAULT 0,
    isactive boolean NOT NULL DEFAULT TRUE,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL
);

CREATE TABLE tbl_vendor (
    vendorid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    vendorname varchar(200) NOT NULL,
    vendorcode varchar(20) NOT NULL UNIQUE,
    contactname varchar(150) NULL,
    contactemail varchar(150) NULL,
    contactphone varchar(30) NULL,
    address1 varchar(255) NULL,
    address2 varchar(255) NULL,
    city varchar(100) NULL,
    stateprovince varchar(50) NULL,
    postalcode varchar(20) NULL,
    countrycode varchar(5) NOT NULL DEFAULT 'US',
    paymentterms varchar(50) NULL,
    leadtimedays integer NULL DEFAULT 14,
    minorderamount numeric(12,2) NULL,
    isactive boolean NOT NULL DEFAULT TRUE,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL
);

CREATE TABLE tbl_product (
    productid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    sku varchar(50) NOT NULL UNIQUE,
    upc varchar(20) NULL,
    productname varchar(300) NOT NULL,
    shortdescription varchar(500) NULL,
    longdescription text NULL,
    categoryid integer NOT NULL REFERENCES tbl_category(categoryid),
    vendorid integer NULL REFERENCES tbl_vendor(vendorid),
    brand varchar(100) NULL,
    modelnumber varchar(100) NULL,
    unitcost numeric(12,4) NOT NULL DEFAULT 0.0000,
    retailprice numeric(12,2) NOT NULL DEFAULT 0.00,
    saleprice numeric(12,2) NULL,
    salestartdate timestamp NULL,
    saleenddate timestamp NULL,
    weight numeric(10,4) NULL,
    weightunit varchar(10) NULL DEFAULT 'lb',
    length numeric(10,2) NULL,
    width numeric(10,2) NULL,
    height numeric(10,2) NULL,
    dimensionunit varchar(10) NULL DEFAULT 'in',
    color varchar(50) NULL,
    size varchar(50) NULL,
    material varchar(100) NULL,
    istaxable boolean NOT NULL DEFAULT TRUE,
    isdiscountable boolean NOT NULL DEFAULT TRUE,
    isreturnable boolean NOT NULL DEFAULT TRUE,
    returnwindowdays integer NOT NULL DEFAULT 30,
    minstocklevel integer NOT NULL DEFAULT 5,
    maxstocklevel integer NOT NULL DEFAULT 500,
    reorderpoint integer NOT NULL DEFAULT 10,
    reorderquantity integer NOT NULL DEFAULT 50,
    status smallint NOT NULL DEFAULT 1,
    imagepath varchar(500) NULL,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL
);

CREATE TABLE tbl_inventory (
    inventoryid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    productid integer NOT NULL REFERENCES tbl_product(productid),
    storeid integer NOT NULL REFERENCES tbl_store(storeid),
    quantityonhand integer NOT NULL DEFAULT 0,
    quantityreserved integer NOT NULL DEFAULT 0,
    quantityonorder integer NOT NULL DEFAULT 0,
    binlocation varchar(50) NULL,
    aislename varchar(50) NULL,
    shelfnumber varchar(20) NULL,
    lastcountdate timestamp NULL,
    lastreceiveddate timestamp NULL,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL,
    CONSTRAINT uq_inventory_product_store UNIQUE (productid, storeid)
);

CREATE TABLE tbl_employee (
    employeeid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    employeenumber varchar(20) NOT NULL UNIQUE,
    firstname varchar(100) NOT NULL,
    lastname varchar(100) NOT NULL,
    middlename varchar(100) NULL,
    email varchar(150) NULL,
    phone varchar(30) NULL,
    storeid integer NULL REFERENCES tbl_store(storeid),
    departmentid integer NULL REFERENCES tbl_department(departmentid),
    jobtitle varchar(100) NULL,
    hiredate date NOT NULL,
    terminationdate date NULL,
    hourlyrate numeric(8,2) NULL,
    salaryamount numeric(12,2) NULL,
    commissionrate numeric(5,4) NULL DEFAULT 0.0000,
    manageremployeeid integer NULL REFERENCES tbl_employee(employeeid),
    accesslevel smallint NOT NULL DEFAULT 1,
    pincode varchar(10) NULL,
    isactive boolean NOT NULL DEFAULT TRUE,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL
);

ALTER TABLE tbl_store ADD CONSTRAINT fk_store_manager FOREIGN KEY (manageremployeeid) REFERENCES tbl_employee(employeeid);

CREATE TABLE tbl_customer (
    customerid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    customernumber varchar(20) NOT NULL UNIQUE,
    firstname varchar(100) NOT NULL,
    lastname varchar(100) NOT NULL,
    middlename varchar(100) NULL,
    email varchar(150) NULL,
    phone varchar(30) NULL,
    mobilephone varchar(30) NULL,
    address1 varchar(255) NULL,
    address2 varchar(255) NULL,
    city varchar(100) NULL,
    stateprovince varchar(50) NULL,
    postalcode varchar(20) NULL,
    countrycode varchar(5) NOT NULL DEFAULT 'US',
    dateofbirth date NULL,
    gender char(1) NULL,
    loyaltypoints integer NOT NULL DEFAULT 0,
    loyaltytier smallint NOT NULL DEFAULT 0,
    totalspend numeric(14,2) NOT NULL DEFAULT 0.00,
    visitcount integer NOT NULL DEFAULT 0,
    lastvisitdate timestamp NULL,
    preferredstoreid integer NULL REFERENCES tbl_store(storeid),
    taxexempt boolean NOT NULL DEFAULT FALSE,
    taxexemptnumber varchar(50) NULL,
    notes text NULL,
    isactive boolean NOT NULL DEFAULT TRUE,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL
);

CREATE TABLE tbl_promotion (
    promotionid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    promotionname varchar(200) NOT NULL,
    promotioncode varchar(30) NOT NULL UNIQUE,
    description varchar(500) NULL,
    discounttype smallint NOT NULL DEFAULT 1,
    discountvalue numeric(10,2) NOT NULL DEFAULT 0.00,
    minpurchaseamount numeric(12,2) NULL,
    maxdiscountamount numeric(12,2) NULL,
    startdate timestamp NOT NULL,
    enddate timestamp NOT NULL,
    usagelimit integer NULL,
    usagecount integer NOT NULL DEFAULT 0,
    percustomerlimit integer NULL,
    applicablecategoryid integer NULL REFERENCES tbl_category(categoryid),
    applicableproductid integer NULL REFERENCES tbl_product(productid),
    applicablestoreid integer NULL REFERENCES tbl_store(storeid),
    requiresloyaltytier smallint NULL,
    isstackable boolean NOT NULL DEFAULT FALSE,
    isactive boolean NOT NULL DEFAULT TRUE,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL
);

CREATE TABLE tbl_transaction (
    transactionid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    transactionnumber varchar(30) NOT NULL UNIQUE,
    storeid integer NOT NULL REFERENCES tbl_store(storeid),
    registernumber integer NOT NULL DEFAULT 1,
    employeeid integer NOT NULL REFERENCES tbl_employee(employeeid),
    customerid integer NULL REFERENCES tbl_customer(customerid),
    transactiondate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    transactiontype smallint NOT NULL DEFAULT 1,
    subtotal numeric(14,2) NOT NULL DEFAULT 0.00,
    discounttotal numeric(14,2) NOT NULL DEFAULT 0.00,
    taxtotal numeric(14,2) NOT NULL DEFAULT 0.00,
    grandtotal numeric(14,2) NOT NULL DEFAULT 0.00,
    tenderamount numeric(14,2) NOT NULL DEFAULT 0.00,
    changeamount numeric(14,2) NOT NULL DEFAULT 0.00,
    promotionid integer NULL REFERENCES tbl_promotion(promotionid),
    loyaltypointsearned integer NOT NULL DEFAULT 0,
    loyaltypointsredeemed integer NOT NULL DEFAULT 0,
    status smallint NOT NULL DEFAULT 1,
    voidreason varchar(255) NULL,
    voidemployeeid integer NULL REFERENCES tbl_employee(employeeid),
    notes varchar(500) NULL,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL
);

CREATE TABLE tbl_transactionitem (
    transactionitemid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    transactionid integer NOT NULL REFERENCES tbl_transaction(transactionid),
    productid integer NOT NULL REFERENCES tbl_product(productid),
    quantity integer NOT NULL DEFAULT 1,
    unitprice numeric(12,2) NOT NULL DEFAULT 0.00,
    discountamount numeric(12,2) NOT NULL DEFAULT 0.00,
    taxamount numeric(12,2) NOT NULL DEFAULT 0.00,
    linetotal numeric(14,2) NOT NULL DEFAULT 0.00,
    returnquantity integer NOT NULL DEFAULT 0,
    serialnumber varchar(100) NULL,
    isvoided boolean NOT NULL DEFAULT FALSE,
    voidreason varchar(255) NULL,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL
);

CREATE TABLE tbl_payment (
    paymentid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    transactionid integer NOT NULL REFERENCES tbl_transaction(transactionid),
    paymentmethod smallint NOT NULL DEFAULT 1,
    amount numeric(14,2) NOT NULL DEFAULT 0.00,
    referencenumber varchar(100) NULL,
    cardtype varchar(30) NULL,
    cardlastfour varchar(4) NULL,
    authorizationcode varchar(50) NULL,
    checknumber varchar(20) NULL,
    giftcardnumber varchar(30) NULL,
    status smallint NOT NULL DEFAULT 1,
    processeddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL
);

CREATE TABLE tbl_return (
    returnid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    returnnumber varchar(30) NOT NULL UNIQUE,
    originaltransactionid integer NOT NULL REFERENCES tbl_transaction(transactionid),
    storeid integer NOT NULL REFERENCES tbl_store(storeid),
    employeeid integer NOT NULL REFERENCES tbl_employee(employeeid),
    customerid integer NULL REFERENCES tbl_customer(customerid),
    returndate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    returnreason smallint NOT NULL DEFAULT 1,
    reasondescription varchar(500) NULL,
    refundamount numeric(14,2) NOT NULL DEFAULT 0.00,
    refundmethod smallint NOT NULL DEFAULT 1,
    status smallint NOT NULL DEFAULT 1,
    managerapprovalid integer NULL REFERENCES tbl_employee(employeeid),
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL
);

CREATE TABLE tbl_returnitem (
    returnitemid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    returnid integer NOT NULL REFERENCES tbl_return(returnid),
    transactionitemid integer NOT NULL REFERENCES tbl_transactionitem(transactionitemid),
    productid integer NOT NULL REFERENCES tbl_product(productid),
    quantity integer NOT NULL DEFAULT 1,
    refundamount numeric(12,2) NOT NULL DEFAULT 0.00,
    condition smallint NOT NULL DEFAULT 1,
    restockflag boolean NOT NULL DEFAULT TRUE,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL
);

CREATE TABLE tbl_purchaseorder (
    purchaseorderid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ponumber varchar(30) NOT NULL UNIQUE,
    vendorid integer NOT NULL REFERENCES tbl_vendor(vendorid),
    storeid integer NOT NULL REFERENCES tbl_store(storeid),
    orderedbyemployeeid integer NOT NULL REFERENCES tbl_employee(employeeid),
    approvedbyemployeeid integer NULL REFERENCES tbl_employee(employeeid),
    orderdate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expecteddeliverydate date NULL,
    actualdeliverydate date NULL,
    subtotal numeric(14,2) NOT NULL DEFAULT 0.00,
    shippingcost numeric(10,2) NOT NULL DEFAULT 0.00,
    taxamount numeric(10,2) NOT NULL DEFAULT 0.00,
    totalamount numeric(14,2) NOT NULL DEFAULT 0.00,
    status smallint NOT NULL DEFAULT 1,
    notes text NULL,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL
);

CREATE TABLE tbl_purchaseorderitem (
    purchaseorderitemid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    purchaseorderid integer NOT NULL REFERENCES tbl_purchaseorder(purchaseorderid),
    productid integer NOT NULL REFERENCES tbl_product(productid),
    quantityordered integer NOT NULL DEFAULT 0,
    quantityreceived integer NOT NULL DEFAULT 0,
    unitcost numeric(12,4) NOT NULL DEFAULT 0.0000,
    linetotal numeric(14,2) NOT NULL DEFAULT 0.00,
    status smallint NOT NULL DEFAULT 1,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL
);

CREATE TABLE tbl_pricehistory (
    pricehistoryid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    productid integer NOT NULL REFERENCES tbl_product(productid),
    oldretailprice numeric(12,2) NOT NULL,
    newretailprice numeric(12,2) NOT NULL,
    oldcost numeric(12,4) NULL,
    newcost numeric(12,4) NULL,
    changedbyemployeeid integer NULL REFERENCES tbl_employee(employeeid),
    changereason varchar(255) NULL,
    effectivedate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tbl_inventoryadjustment (
    adjustmentid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    adjustmentnumber varchar(30) NOT NULL UNIQUE,
    storeid integer NOT NULL REFERENCES tbl_store(storeid),
    employeeid integer NOT NULL REFERENCES tbl_employee(employeeid),
    adjustmentdate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    adjustmenttype smallint NOT NULL DEFAULT 1,
    reason varchar(500) NULL,
    approvedbyemployeeid integer NULL REFERENCES tbl_employee(employeeid),
    status smallint NOT NULL DEFAULT 1,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL
);

CREATE TABLE tbl_inventoryadjustmentitem (
    adjustmentitemid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    adjustmentid integer NOT NULL REFERENCES tbl_inventoryadjustment(adjustmentid),
    productid integer NOT NULL REFERENCES tbl_product(productid),
    quantitybefore integer NOT NULL DEFAULT 0,
    quantityafter integer NOT NULL DEFAULT 0,
    quantitydifference integer NOT NULL DEFAULT 0,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tbl_giftcard (
    giftcardid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cardnumber varchar(30) NOT NULL UNIQUE,
    pin varchar(10) NULL,
    originalbalance numeric(10,2) NOT NULL DEFAULT 0.00,
    currentbalance numeric(10,2) NOT NULL DEFAULT 0.00,
    purchasedatstoreid integer NULL REFERENCES tbl_store(storeid),
    purchasedbycustomerid integer NULL REFERENCES tbl_customer(customerid),
    purchasedate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expirationdate date NULL,
    status smallint NOT NULL DEFAULT 1,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL
);

CREATE TABLE tbl_auditlog (
    auditlogid bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tablename varchar(100) NOT NULL,
    recordid integer NOT NULL,
    action varchar(10) NOT NULL,
    oldvalues text NULL,
    newvalues text NULL,
    employeeid integer NULL,
    ipaddress varchar(45) NULL,
    workstation varchar(100) NULL,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tbl_systemconfig (
    configid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    configkey varchar(100) NOT NULL UNIQUE,
    configvalue varchar(500) NOT NULL,
    configgroup varchar(50) NULL,
    datatype varchar(20) NOT NULL DEFAULT 'STRING',
    description varchar(255) NULL,
    iseditable boolean NOT NULL DEFAULT TRUE,
    modifieddate timestamp NULL
);

CREATE TABLE tbl_taxrule (
    taxruleid integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    rulename varchar(100) NOT NULL,
    regionid integer NOT NULL REFERENCES tbl_region(regionid),
    categoryid integer NULL REFERENCES tbl_category(categoryid),
    taxrate numeric(5,4) NOT NULL DEFAULT 0.0000,
    priority integer NOT NULL DEFAULT 0,
    startdate date NOT NULL,
    enddate date NULL,
    isactive boolean NOT NULL DEFAULT TRUE,
    createddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifieddate timestamp NULL
);

CREATE INDEX IF NOT EXISTS ix_product_sku ON tbl_product(sku);
CREATE INDEX IF NOT EXISTS ix_product_upc ON tbl_product(upc);
CREATE INDEX IF NOT EXISTS ix_product_category ON tbl_product(categoryid);
CREATE INDEX IF NOT EXISTS ix_product_vendor ON tbl_product(vendorid);
CREATE INDEX IF NOT EXISTS ix_product_status ON tbl_product(status);
CREATE INDEX IF NOT EXISTS ix_inventory_productstore ON tbl_inventory(productid, storeid);
CREATE INDEX IF NOT EXISTS ix_transaction_date ON tbl_transaction(transactiondate);
CREATE INDEX IF NOT EXISTS ix_transaction_store ON tbl_transaction(storeid);
CREATE INDEX IF NOT EXISTS ix_transaction_customer ON tbl_transaction(customerid);
CREATE INDEX IF NOT EXISTS ix_transaction_employee ON tbl_transaction(employeeid);
CREATE INDEX IF NOT EXISTS ix_transactionitem_transaction ON tbl_transactionitem(transactionid);
CREATE INDEX IF NOT EXISTS ix_transactionitem_product ON tbl_transactionitem(productid);
CREATE INDEX IF NOT EXISTS ix_customer_number ON tbl_customer(customernumber);
CREATE INDEX IF NOT EXISTS ix_customer_email ON tbl_customer(email);
CREATE INDEX IF NOT EXISTS ix_customer_lastname ON tbl_customer(lastname);
CREATE INDEX IF NOT EXISTS ix_employee_number ON tbl_employee(employeenumber);
CREATE INDEX IF NOT EXISTS ix_employee_store ON tbl_employee(storeid);
CREATE INDEX IF NOT EXISTS ix_purchaseorder_vendor ON tbl_purchaseorder(vendorid);
CREATE INDEX IF NOT EXISTS ix_purchaseorder_store ON tbl_purchaseorder(storeid);
CREATE INDEX IF NOT EXISTS ix_auditlog_table ON tbl_auditlog(tablename, recordid);
CREATE INDEX IF NOT EXISTS ix_auditlog_date ON tbl_auditlog(createddate);

INSERT INTO tbl_systemconfig (configkey, configvalue, configgroup, datatype) VALUES
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
