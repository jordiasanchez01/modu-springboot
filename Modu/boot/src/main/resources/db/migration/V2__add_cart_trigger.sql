CREATE OR REPLACE FUNCTION update_cart_timestamp()
    RETURNS TRIGGER AS $func$
BEGIN
    UPDATE cart
    SET updated_at = CURRENT_TIMESTAMP
    WHERE id = COALESCE(NEW.cart_id, OLD.cart_id);
    RETURN NEW;
END;
$func$ LANGUAGE plpgsql;

CREATE TRIGGER cart_item_updated
    AFTER INSERT OR UPDATE OR DELETE ON cart_item
    FOR EACH ROW
EXECUTE FUNCTION update_cart_timestamp();