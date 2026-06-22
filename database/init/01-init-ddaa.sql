-- Inicialización mínima para la base SQL Server local.
-- Hibernate crea las tablas de negocio y el backend carga datos de muestra.

IF DB_ID(N'ddaa') IS NULL
BEGIN
    CREATE DATABASE [ddaa];
END
GO

USE [master];
GO

IF NOT EXISTS (SELECT 1 FROM sys.sql_logins WHERE name = N'ddaa_user')
BEGIN
    CREATE LOGIN [ddaa_user]
    WITH PASSWORD = N'DdaaUser2026!', CHECK_POLICY = OFF, CHECK_EXPIRATION = OFF;
END
GO

USE [ddaa];
GO

IF NOT EXISTS (SELECT 1 FROM sys.database_principals WHERE name = N'ddaa_user')
BEGIN
    CREATE USER [ddaa_user] FOR LOGIN [ddaa_user];
END
GO

ALTER ROLE db_owner ADD MEMBER [ddaa_user];
GO
