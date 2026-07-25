-- PostgreSQL-compatible migration notes for AlloyDB
-- This file is a starting point for translating the SQL Server DDL.

-- Example type mappings:
-- SQL Server: DATETIME -> PostgreSQL: TIMESTAMP
-- SQL Server: BIT -> PostgreSQL: BOOLEAN
-- SQL Server: IDENTITY -> PostgreSQL: GENERATED ALWAYS AS IDENTITY

-- Recommended initial steps:
-- 1. Create the database and schema.
-- 2. Recreate tables with PostgreSQL-compatible syntax.
-- 3. Recreate indexes and foreign keys.
-- 4. Load seed data.

CREATE SCHEMA IF NOT EXISTS retail;

-- Example table skeleton:
-- CREATE TABLE retail.tbl_region (
--   region_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
--   region_name VARCHAR(100) NOT NULL,
--   region_code VARCHAR(10) NOT NULL UNIQUE,
--   country_code VARCHAR(5) NOT NULL DEFAULT 'US',
--   tax_rate NUMERIC(5,4) NOT NULL DEFAULT 0.0000,
--   is_active BOOLEAN NOT NULL DEFAULT TRUE,
--   created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--   modified_date TIMESTAMP NULL
-- );
