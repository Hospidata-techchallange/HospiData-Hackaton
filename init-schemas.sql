CREATE DATABASE IF NOT EXISTS auth_schema;
CREATE DATABASE IF NOT EXISTS stock_schema;
CREATE DATABASE IF NOT EXISTS work_order_schema;
CREATE DATABASE IF NOT EXISTS appointment_schema;

GRANT ALL PRIVILEGES ON auth_schema.* TO 'hospidata-user';
GRANT ALL PRIVILEGES ON stock_schema.* TO 'hospidata-user';
GRANT ALL PRIVILEGES ON work_order_schema.* TO 'hospidata-user';
GRANT ALL PRIVILEGES ON appointment_schema.* TO 'hospidata-user';

CREATE USER IF NOT EXISTS 'stock-agent'@'%' IDENTIFIED BY 'password';
CREATE USER IF NOT EXISTS 'appointment-agent'@'%' IDENTIFIED BY 'password';

GRANT SELECT ON stock_schema.* TO 'stock-agent'@'%';
GRANT SELECT ON appointment_schema.* TO 'appointment-agent'@'%';

FLUSH PRIVILEGES;