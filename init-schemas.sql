CREATE DATABASE IF NOT EXISTS auth_schema;

CREATE DATABASE IF NOT EXISTS stock_schema;

CREATE DATABASE IF NOT EXISTS work_order_schema;

GRANT ALL PRIVILEGES ON auth_schema.* TO 'hospidata-user';

GRANT ALL PRIVILEGES ON stock_schema.* TO 'hospidata-user';

GRANT ALL PRIVILEGES ON work_order_schema.* TO 'hospidata-user';

FLUSH PRIVILEGES;