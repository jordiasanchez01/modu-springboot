UPDATE cart SET user_id = 100000000000000 + user_id;
UPDATE orders SET user_id = 100000000000000 + user_id;

ALTER TABLE cart RENAME COLUMN user_id TO device_id;
ALTER TABLE cart ALTER COLUMN device_id TYPE VARCHAR(15);

ALTER TABLE orders RENAME COLUMN user_id TO device_id;
ALTER TABLE orders ALTER COLUMN device_id TYPE VARCHAR(15);