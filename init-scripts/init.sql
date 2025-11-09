CREATE DATABASE smart-parking-system-db
CREATE USER admin_user WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE smart-parking-system-db TO admin_user;
ALTER DATABASE smart-parking-system-db SET TIMEZONE TO 'Europe/Kyiv';