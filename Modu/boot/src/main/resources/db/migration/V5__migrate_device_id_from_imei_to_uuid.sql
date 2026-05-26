ALTER TABLE cart ALTER COLUMN device_id TYPE VARCHAR(32);
ALTER TABLE orders ALTER COLUMN device_id TYPE VARCHAR(32);

UPDATE cart SET device_id = '0' || device_id;
UPDATE orders SET device_id = '0' || device_id;