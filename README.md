# RetailCore

RetailCore is a Java 8 backend for retail store and point-of-sale operations. It uses Microsoft SQL Server for persistence and exposes its functionality through Java service and application classes. The repository does not currently contain a web API, desktop UI, or automated test suite.

## What the application does

The application supports the core retail workflow:

- Product lookup by SKU or UPC
- Customer lookup by customer number or email
- Employee authentication by store and PIN
- Sales transaction processing across products, inventory, customers, promotions, payments, and gift cards
- Transaction voiding
- Inventory adjustments and shipment receiving
- Low-stock and out-of-stock queries
- Store, employee, customer, vendor, revenue, and inventory reports
- Audit logging for inventory changes

During a sale, `TransactionService` validates stock, determines sale pricing, applies eligible promotions, calculates tax and totals, validates tender, persists the transaction, and updates related inventory, loyalty, promotion, and gift-card data.

## Architecture

```text
RetailCoreApplication
          |
          v
       Services
  TransactionService
  InventoryService
    ReportService
          |
          v
         DAOs
  ProductDAO, CustomerDAO, InventoryDAO,
  TransactionDAO, StoreDAO, and others
          |
          v
   Lightweight ORM/JDBC layer
  QueryBuilder, BaseDAO, ResultMapper,
  ConnectionPool, TransactionManager
          |
          v
      SQL Server
       RetailCoreDB
```

### Main packages

| Package | Responsibility |
| --- | --- |
| `com.retailcore` | Application façade and entry point |
| `com.retailcore.service` | Business workflows and calculations |
| `com.retailcore.dao` | SQL Server data access for each business domain |
| `com.retailcore.entity` | Domain/entity objects mapped to database tables |
| `com.retailcore.orm` | Generic CRUD, query building, result mapping, pooling, and transactions |
| `com.retailcore.config` | Database property loading and pool initialization |

The application uses a custom JDBC-based persistence layer rather than Spring or another external ORM. Database operations use a connection pool, and multi-step operations use `TransactionManager` with thread-local transactions.

## Prerequisites

- Java 8 JDK
- Maven 3.x
- Microsoft SQL Server reachable at the configured host and port
- A SQL Server login with permission to create and modify `RetailCoreDB`

The Maven build uses Java source and target level 8 and the Microsoft SQL Server JDBC driver variant `12.4.2.jre8`.

Verify the local tools:

```bash
java -version
mvn -version
```

## Database setup

The database schema is in [`sql/schema.sql`](sql/schema.sql). It creates the `RetailCoreDB` database, all application tables, indexes, and default system configuration values.

Run the script using SQL Server Management Studio, Azure Data Studio, or `sqlcmd`. For example:

```bash
sqlcmd -S localhost,1433 -U sa -P '<sql-server-password>' -i sql/schema.sql
```

> **Warning:** `sql/schema.sql` drops `RetailCoreDB` if it already exists. Do not run it against a database containing data you need to preserve.

The script contains `GO` batch separators, so use a SQL Server-aware client such as SSMS, Azure Data Studio, or `sqlcmd`.

## Configuration

Database settings are read from [`src/main/resources/database.properties`](src/main/resources/database.properties). The important properties are:

```properties
db.url=jdbc:sqlserver://localhost:1433;databaseName=RetailCoreDB;encrypt=false;trustServerCertificate=true
db.username=sa
db.password=<sql-server-password>
db.pool.initial=5
db.pool.max=20
db.connection.timeout=30000
db.validation.timeout=5000
```

Update the URL, username, and password for the local SQL Server instance before running the application. Do not commit real production credentials to source control.

`DatabaseConfig` first loads the classpath resource above and then falls back to `config/database.properties` if the resource is unavailable.

## Build

From the repository root:

```bash
mvn clean package
```

The packaged JAR is created under `target/`.

## Run

After configuring SQL Server and the database properties, run the packaged application with:

```bash
java -jar target/retail-legacy-1.0.0.jar
```

The configured main class is `com.retailcore.RetailCoreApplication`. On startup it initializes the database connection pool and prints messages confirming that the application is ready. The current `main` method does not start a server or interactive terminal; it exits after initialization, while the application façade and service classes are intended to be called by another application or integration layer.

## Using the application from Java

The façade can be embedded in another Java program:

```java
RetailCoreApplication app = new RetailCoreApplication();
try {
    app.startup();

    Product product = app.lookupProduct("SKU-001");
    Employee employee = app.authenticateEmployee(1, "1234");

    // Build transaction item/payment requests and call:
    // app.processNewTransaction(...);
} finally {
    app.shutdown();
}
```

For production integrations, use the service and façade APIs as the application boundary and ensure that shutdown is called so pooled database connections are released.

## Project layout

```text
src/main/java/com/retailcore/
├── RetailCoreApplication.java
├── config/       Database configuration
├── dao/          Data access objects
├── entity/       Domain models
├── orm/          JDBC/ORM support
└── service/      Business services
src/main/resources/
└── database.properties
sql/
└── schema.sql
pom.xml
```

## Known limitations

- No REST or other network API is included.
- No user interface or command-line transaction workflow is included.
- No test source tree is currently present.
- The schema and runtime code currently use a fixed SQL Server-oriented configuration, including a default tax calculation in transaction processing.
