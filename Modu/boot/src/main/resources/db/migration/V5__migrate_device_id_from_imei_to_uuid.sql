-- Step 1: add a new temporary UUID column
ALTER TABLE cart ADD COLUMN device_id_uuid UUID;
ALTER TABLE orders ADD COLUMN device_id_uuid UUID;

-- Step 2: generate a UUID for each existing IMEI
-- we can't recover the original UUIDs, so we generate new ones per unique IMEI
UPDATE cart SET device_id_uuid = gen_random_uuid();
UPDATE orders SET device_id_uuid = gen_random_uuid();

-- Step 3: drop the old VARCHAR IMEI column
ALTER TABLE cart DROP COLUMN device_id;
ALTER TABLE orders DROP COLUMN device_id;

-- Step 4: rename the new column to device_id
ALTER TABLE cart RENAME COLUMN device_id_uuid TO device_id;
ALTER TABLE orders RENAME COLUMN device_id_uuid TO device_id;

-- Step 5: add constraints
ALTER TABLE cart ALTER COLUMN device_id SET NOT NULL;
ALTER TABLE cart ADD CONSTRAINT cart_device_id_unique UNIQUE (device_id);

ALTER TABLE orders ALTER COLUMN device_id SET NOT NULL;