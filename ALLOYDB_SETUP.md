# AlloyDB for PostgreSQL migration guide

This repository now includes a PostgreSQL driver dependency and connection-pool support for a PostgreSQL-compatible JDBC URL.

## 1. Create the AlloyDB instance in GCP

### Prerequisites
- A GCP project with billing enabled.
- Enable the AlloyDB API.
- A VPC network with private service access or a public IP plan if you are testing.

### Create the cluster
1. Open the Google Cloud Console and navigate to AlloyDB.
2. Create a cluster with:
   - Cluster type: Regional or Zonal depending on your HA needs.
   - Network: choose your VPC.
   - Primary region and zone.
3. Create an instance inside the cluster.
4. Create a database and a database user for the application.

### Networking
- For production, prefer private IP and private service access.
- For local testing, a public IP connection can be used temporarily.
- Ensure firewall rules allow the application to reach the instance on the correct port.

### Connection details
Collect:
- host
- port (typically 5432)
- database name
- username
- password

## 2. Configure the application

Update the database properties in src/main/resources/database.properties with values like:

```properties
db.driver=org.postgresql.Driver
db.url=jdbc:postgresql://<host>:5432/<database>
db.username=<username>
db.password=<password>
db.pool.initial=5
db.pool.max=20
db.connection.timeout=30000
db.validation.timeout=5000
```

## 3. Prepare the schema

The SQL Server-specific schema in sql/schema.sql must be translated to PostgreSQL syntax before use. A starter file is available in sql/alloydb-postgres-migration.sql.

## 4. Build and test

```bash
mvn clean package
```

Then run the application and verify the connection pool initializes successfully.
