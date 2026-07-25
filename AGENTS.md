# Repository Guidelines

## Project Structure & Module Organization

RetailCore is a Java 8 SQL Server backend using a small JDBC-based ORM:

- `src/main/java/com/retailcore/entity/` contains table-mapped domain objects.
- `src/main/java/com/retailcore/dao/` contains persistence queries and writes.
- `src/main/java/com/retailcore/service/` contains transaction, inventory, and reporting workflows.
- `src/main/java/com/retailcore/orm/` contains connection pooling, metadata mapping, and transactions.
- `src/main/resources/database.properties` contains local database configuration.
- `sql/schema.sql` creates the database and tables; `sql/seed.sql` adds development data.
- No automated test tree currently exists; add tests under `src/test/java/` when introducing them.

## Build, Test, and Development Commands

```bash
mvn clean package       # Compile, test, and create the executable shaded JAR
mvn test                # Run the test suite (currently no tests are defined)
java -jar target/retail-legacy-1.0.0.jar
```

Run `sql/schema.sql` first in SQL Server, then `sql/seed.sql` for local data. Configure the connection before starting the JAR. The schema script is destructive and must not be used against data that must be preserved.

## Coding Style & Naming Conventions

Use four-space indentation, Java 8-compatible syntax, and descriptive names. Classes use `PascalCase`; methods and fields use `camelCase`; database columns and tables retain the existing SQL Server naming convention. Keep entity annotations synchronized with schema columns. Use prepared statements and existing DAO/transaction abstractions for database access.

## Testing Guidelines

There is no configured framework or coverage threshold. New behavior should include focused JUnit tests under `src/test/java/`, with names such as `TransactionServiceTest`. Database-dependent tests should use an isolated SQL Server database and must not rely on production credentials.

## Commit & Pull Request Guidelines

Existing commits use short imperative descriptions (for example, `Added ...`). Keep commits focused and explain schema or configuration changes clearly. Pull requests should describe behavior changes, database impact, setup/migration steps, validation commands, and any security considerations. Include screenshots only when user-facing tooling is added.

## Security & Configuration

Never commit real passwords or production connection strings. Prefer environment-specific configuration outside source control, use a least-privilege SQL Server login, and rotate credentials if they have been exposed.
