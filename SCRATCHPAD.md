# For running MS SQL locally via Docker

docker run -e "ACCEPT_EULA=1" \
           -e "MSSQL_SA_PASSWORD=Temp*3fW+eT" \
           -p 1433:1433 \
           --name sqledge \
           -d \
           mcr.microsoft.com/azure-sql-edge

# Logs

docker logs sqledge

# Create a DB

docker exec -it sqledge /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P "Temp*3fW+eT"

set up mssql extension via VS Code

CREATE LOGIN java_user WITH PASSWORD = 'Test$35&3*';
GO

USE retail;
GO

-- Create a user in the database mapped to the server login
CREATE USER java_user FOR LOGIN java_user;
GO

-- Grant database owner permissions to the user
ALTER ROLE db_owner ADD MEMBER java_user;
GO