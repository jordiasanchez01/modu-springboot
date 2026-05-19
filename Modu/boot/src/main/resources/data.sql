INSERT INTO product (name, description, image_url, price, active)
SELECT *
FROM (
         VALUES
             ('Classic Denim Jacket', 'A timeless blue denim jacket with a relaxed fit.', 'https://images.unsplash.com/photo-1544642899-f0d6e5f6ed6f?auto=format&fit=crop&w=800&q=80', 79.99, TRUE),
             ('Black Leather Jacket', 'Premium black leather jacket with zipper details.', 'https://images.unsplash.com/photo-1563183939-592da2dbb596?auto=format&fit=crop&w=800&q=80', 149.99, TRUE),
             ('Slim Fit Jeans', 'Dark blue slim fit jeans with stretch fabric.', 'https://images.unsplash.com/photo-1614495039368-525273956716?auto=format&fit=crop&w=800&q=80', 59.99, TRUE),
             ('Casual Polo Shirt', 'Cotton polo shirt perfect for everyday wear.', 'https://images.unsplash.com/photo-1563649685437-a79731028cd1?auto=format&fit=crop&w=800&q=80', 29.99, TRUE),
             ('White T-Shirt', 'Basic white t-shirt made from organic cotton.', 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=800&q=80', 19.99, TRUE),
             ('Hooded Sweatshirt', 'Comfortable hoodie with front pocket.', 'https://images.unsplash.com/photo-1567894173760-22aff4400f21?auto=format&fit=crop&w=800&q=80', 49.99, TRUE),
             ('Chino Pants', 'Slim fit chino pants for a smart casual look.', 'https://images.unsplash.com/photo-1581382575275-97901c2635b7?auto=format&fit=crop&w=800&q=80', 54.99, TRUE),
             ('Winter Parka', 'Warm insulated parka with faux fur hood.', 'https://images.unsplash.com/photo-1514564652994-565a3bf3a25b?auto=format&fit=crop&w=800&q=80', 129.99, TRUE),
             ('Summer Shorts', 'Lightweight cotton shorts for hot days.', 'https://images.unsplash.com/photo-1685539855961-9045adffe039?auto=format&fit=crop&w=800&q=80', 24.99, TRUE),
             ('Formal Blazer', 'Elegant blazer suitable for business attire.', 'https://images.unsplash.com/photo-1593030942428-a5451dca4b42?auto=format&fit=crop&w=800&q=80', 119.99, TRUE),
             ('Checked Shirt', 'Long sleeve checked shirt in soft flannel.', 'https://images.unsplash.com/photo-1602038704137-439ad0e51044?auto=format&fit=crop&w=800&q=80', 39.99, TRUE),
             ('Sports Jacket', 'Lightweight sports jacket for outdoor activities.', 'https://images.unsplash.com/photo-1581961462133-ca8618e89ed3?auto=format&fit=crop&w=800&q=80', 69.99, TRUE),
             ('Cargo Pants', 'Durable cargo pants with multiple pockets.', 'https://images.unsplash.com/flagged/photo-1572372406879-e7c93ff299fa?auto=format&fit=crop&w=800&q=80', 64.99, TRUE),
             ('Knitted Sweater', 'Warm knitted sweater for cold weather.', 'https://images.unsplash.com/photo-1576110598658-096ae24cdb97?auto=format&fit=crop&w=800&q=80', 59.99, TRUE),
             ('Denim Shorts', 'Casual denim shorts with frayed hem.', 'https://images.unsplash.com/photo-1596460700790-d079c2c8ade6?auto=format&fit=crop&w=800&q=80', 34.99, TRUE),
             ('Track Pants', 'Comfortable track pants for workouts.', 'https://images.unsplash.com/photo-1513378628213-b8f36d8c2878?auto=format&fit=crop&w=800&q=80', 44.99, TRUE),
             ('Bomber Jacket', 'Trendy bomber jacket with ribbed cuffs.', 'https://images.unsplash.com/photo-1549399239-fb3c102d3d71?auto=format&fit=crop&w=800&q=80', 89.99, TRUE),
             ('Graphic T-Shirt', 'T-shirt with modern graphic print.', 'https://images.unsplash.com/photo-1584636633449-6135be6c4169?auto=format&fit=crop&w=800&q=80', 22.99, TRUE),
             ('Linen Shirt', 'Breathable linen shirt for summer.', 'https://images.unsplash.com/photo-1558436223-247523d35637?auto=format&fit=crop&w=800&q=80', 45.99, TRUE),
             ('Wool Coat', 'Elegant wool coat for winter season.', 'https://images.unsplash.com/photo-1661110546899-732bffb4cb85?auto=format&fit=crop&w=800&q=80', 179.99, TRUE)
         ) AS v(name, description, image_url, price, active)
WHERE NOT EXISTS (SELECT 1 FROM product);

INSERT INTO category (name)
SELECT *
FROM (
         VALUES
             ('Streetwear'),
             ('Activewear'),
             ('Accesories'),
             ('Footwear'),
             ('Limited Edition'),
             ('Vintage')
     ) AS v(name)
WHERE NOT EXISTS (SELECT 1 FROM category);

INSERT INTO product_categories (product_id,category_id)
SELECT *
FROM (
         VALUES
             (1,1),
             (1,6),
             (2,1),
             (2,6),
             (3,1),
             (3,5)
     ) AS v(product_id,category_id)
WHERE NOT EXISTS (SELECT 1 FROM product_categories);


/* SEED VALUES FOR PRODUCT TABLE*/
INSERT INTO product_variant (name, size, color, stock, active, product_id)
SELECT *
FROM (
         VALUES
             ('1_S_BLACK', 'S', 'BLACK', 10, TRUE, 1),
             ('1_M_BLACK', 'M', 'BLACK', 10, TRUE, 1),
             ('1_L_BLACK', 'L', 'BLACK', 10, TRUE, 1),
             ('1_S_WHITE', 'S', 'WHITE', 50, TRUE, 1),
             ('1_M_WHITE', 'M', 'WHITE', 10, TRUE, 1),
             ('1_L_WHITE', 'L', 'WHITE', 10, TRUE, 1),
             ('1_S_BLUE', 'S', 'BLUE', 10, TRUE, 1),
             ('1_M_BLUE', 'M', 'BLUE', 10, TRUE, 1),
             ('1_L_BLUE', 'L', 'BLUE', 10, TRUE, 1),

             ('2_S_BLACK', 'S', 'BLACK', 10, TRUE, 2),
             ('2_M_BLACK', 'M', 'BLACK', 10, TRUE, 2),
             ('2_L_BLACK', 'L', 'BLACK', 10, TRUE, 2),
             ('2_S_WHITE', 'S', 'WHITE', 10, TRUE, 2),
             ('2_M_WHITE', 'M', 'WHITE', 10, TRUE, 2),
             ('2_L_WHITE', 'L', 'WHITE', 10, TRUE, 2),
             ('2_S_BLUE', 'S', 'BLUE', 10, TRUE, 2),
             ('2_M_BLUE', 'M', 'BLUE', 10, TRUE, 2),
             ('2_L_BLUE', 'L', 'BLUE', 10, TRUE, 2),

             ('3_S_BLACK', 'S', 'BLACK', 10, TRUE, 3),
             ('3_M_BLACK', 'M', 'BLACK', 10, TRUE, 3),
             ('3_L_BLACK', 'L', 'BLACK', 10, TRUE, 3),
             ('3_S_WHITE', 'S', 'WHITE', 10, TRUE, 3),
             ('3_M_WHITE', 'M', 'WHITE', 10, TRUE, 3),
             ('3_L_WHITE', 'L', 'WHITE', 10, TRUE, 3),
             ('3_S_BLUE', 'S', 'BLUE', 10, TRUE, 3),
             ('3_M_BLUE', 'M', 'BLUE', 10, TRUE, 3),
             ('3_L_BLUE', 'L', 'BLUE', 10, TRUE, 3),

             ('4_S_BLACK', 'S', 'BLACK', 10, TRUE, 4),
             ('4_M_BLACK', 'M', 'BLACK', 10, TRUE, 4),
             ('4_L_BLACK', 'L', 'BLACK', 10, TRUE, 4),
             ('4_S_WHITE', 'S', 'WHITE', 10, TRUE, 4),
             ('4_M_WHITE', 'M', 'WHITE', 10, TRUE, 4),
             ('4_L_WHITE', 'L', 'WHITE', 10, TRUE, 4),
             ('4_S_BLUE', 'S', 'BLUE', 10, TRUE, 4),
             ('4_M_BLUE', 'M', 'BLUE', 10, TRUE, 4),
             ('4_L_BLUE', 'L', 'BLUE', 10, TRUE, 4),

             ('5_S_BLACK', 'S', 'BLACK', 10, TRUE, 5),
             ('5_M_BLACK', 'M', 'BLACK', 10, TRUE, 5),
             ('5_L_BLACK', 'L', 'BLACK', 10, TRUE, 5),
             ('5_S_WHITE', 'S', 'WHITE', 10, TRUE, 5),
             ('5_M_WHITE', 'M', 'WHITE', 10, TRUE, 5),
             ('5_L_WHITE', 'L', 'WHITE', 10, TRUE, 5),
             ('5_S_BLUE', 'S', 'BLUE', 10, TRUE, 5),
             ('5_M_BLUE', 'M', 'BLUE', 10, TRUE, 5),
             ('5_L_BLUE', 'L', 'BLUE', 10, TRUE, 5),

             ('6_S_BLACK', 'S', 'BLACK', 10, TRUE, 6),
             ('6_M_BLACK', 'M', 'BLACK', 10, TRUE, 6),
             ('6_L_BLACK', 'L', 'BLACK', 10, TRUE, 6),
             ('6_S_WHITE', 'S', 'WHITE', 10, TRUE, 6),
             ('6_M_WHITE', 'M', 'WHITE', 10, TRUE, 6),
             ('6_L_WHITE', 'L', 'WHITE', 10, TRUE, 6),
             ('6_S_BLUE', 'S', 'BLUE', 10, TRUE, 6),
             ('6_M_BLUE', 'M', 'BLUE', 10, TRUE, 6),
             ('6_L_BLUE', 'L', 'BLUE', 10, TRUE, 6),

             ('7_S_BLACK', 'S', 'BLACK', 10, TRUE, 7),
             ('7_M_BLACK', 'M', 'BLACK', 10, TRUE, 7),
             ('7_L_BLACK', 'L', 'BLACK', 10, TRUE, 7),
             ('7_S_WHITE', 'S', 'WHITE', 10, TRUE, 7),
             ('7_M_WHITE', 'M', 'WHITE', 10, TRUE, 7),
             ('7_L_WHITE', 'L', 'WHITE', 10, TRUE, 7),
             ('7_S_BLUE', 'S', 'BLUE', 10, TRUE, 7),
             ('7_M_BLUE', 'M', 'BLUE', 10, TRUE, 7),
             ('7_L_BLUE', 'L', 'BLUE', 10, TRUE, 7),

             ('8_S_BLACK', 'S', 'BLACK', 10, TRUE, 8),
             ('8_M_BLACK', 'M', 'BLACK', 10, TRUE, 8),
             ('8_L_BLACK', 'L', 'BLACK', 10, TRUE, 8),
             ('8_S_WHITE', 'S', 'WHITE', 10, TRUE, 8),
             ('8_M_WHITE', 'M', 'WHITE', 10, TRUE, 8),
             ('8_L_WHITE', 'L', 'WHITE', 10, TRUE, 8),
             ('8_S_BLUE', 'S', 'BLUE', 10, TRUE, 8),
             ('8_M_BLUE', 'M', 'BLUE', 10, TRUE, 8),
             ('8_L_BLUE', 'L', 'BLUE', 10, TRUE, 8),

             ('9_S_BLACK', 'S', 'BLACK', 10, TRUE, 9),
             ('9_M_BLACK', 'M', 'BLACK', 10, TRUE, 9),
             ('9_L_BLACK', 'L', 'BLACK', 10, TRUE, 9),
             ('9_S_WHITE', 'S', 'WHITE', 10, TRUE, 9),
             ('9_M_WHITE', 'M', 'WHITE', 10, TRUE, 9),
             ('9_L_WHITE', 'L', 'WHITE', 10, TRUE, 9),
             ('9_S_BLUE', 'S', 'BLUE', 10, TRUE, 9),
             ('9_M_BLUE', 'M', 'BLUE', 10, TRUE, 9),
             ('9_L_BLUE', 'L', 'BLUE', 10, TRUE, 9),

             ('10_S_BLACK', 'S', 'BLACK', 10, TRUE, 10),
             ('10_M_BLACK', 'M', 'BLACK', 10, TRUE, 10),
             ('10_L_BLACK', 'L', 'BLACK', 10, TRUE, 10),
             ('10_S_WHITE', 'S', 'WHITE', 10, TRUE, 10),
             ('10_M_WHITE', 'M', 'WHITE', 10, TRUE, 10),
             ('10_L_WHITE', 'L', 'WHITE', 10, TRUE, 10),
             ('10_S_BLUE', 'S', 'BLUE', 10, TRUE, 10),
             ('10_M_BLUE', 'M', 'BLUE', 10, TRUE, 10),
             ('10_L_BLUE', 'L', 'BLUE', 10, TRUE, 10),

             ('11_S_BLACK', 'S', 'BLACK', 10, TRUE, 11),
             ('11_M_BLACK', 'M', 'BLACK', 10, TRUE, 11),
             ('11_L_BLACK', 'L', 'BLACK', 10, TRUE, 11),
             ('11_S_WHITE', 'S', 'WHITE', 10, TRUE, 11),
             ('11_M_WHITE', 'M', 'WHITE', 10, TRUE, 11),
             ('11_L_WHITE', 'L', 'WHITE', 10, TRUE, 11),
             ('11_S_BLUE', 'S', 'BLUE', 10, TRUE, 11),
             ('11_M_BLUE', 'M', 'BLUE', 10, TRUE, 11),
             ('11_L_BLUE', 'L', 'BLUE', 10, TRUE, 11),

             ('12_S_BLACK', 'S', 'BLACK', 10, TRUE, 12),
             ('12_M_BLACK', 'M', 'BLACK', 10, TRUE, 12),
             ('12_L_BLACK', 'L', 'BLACK', 10, TRUE, 12),
             ('12_S_WHITE', 'S', 'WHITE', 10, TRUE, 12),
             ('12_M_WHITE', 'M', 'WHITE', 10, TRUE, 12),
             ('12_L_WHITE', 'L', 'WHITE', 10, TRUE, 12),
             ('12_S_BLUE', 'S', 'BLUE', 10, TRUE, 12),
             ('12_M_BLUE', 'M', 'BLUE', 10, TRUE, 12),
             ('12_L_BLUE', 'L', 'BLUE', 10, TRUE, 12),

             ('13_S_BLACK', 'S', 'BLACK', 10, TRUE, 13),
             ('13_M_BLACK', 'M', 'BLACK', 10, TRUE, 13),
             ('13_L_BLACK', 'L', 'BLACK', 10, TRUE, 13),
             ('13_S_WHITE', 'S', 'WHITE', 10, TRUE, 13),
             ('13_M_WHITE', 'M', 'WHITE', 10, TRUE, 13),
             ('13_L_WHITE', 'L', 'WHITE', 10, TRUE, 13),
             ('13_S_BLUE', 'S', 'BLUE', 10, TRUE, 13),
             ('13_M_BLUE', 'M', 'BLUE', 10, TRUE, 13),
             ('13_L_BLUE', 'L', 'BLUE', 10, TRUE, 13),

             ('14_S_BLACK', 'S', 'BLACK', 10, TRUE, 14),
             ('14_M_BLACK', 'M', 'BLACK', 10, TRUE, 14),
             ('14_L_BLACK', 'L', 'BLACK', 10, TRUE, 14),
             ('14_S_WHITE', 'S', 'WHITE', 10, TRUE, 14),
             ('14_M_WHITE', 'M', 'WHITE', 10, TRUE, 14),
             ('14_L_WHITE', 'L', 'WHITE', 10, TRUE, 14),
             ('14_S_BLUE', 'S', 'BLUE', 10, TRUE, 14),
             ('14_M_BLUE', 'M', 'BLUE', 10, TRUE, 14),
             ('14_L_BLUE', 'L', 'BLUE', 10, TRUE, 14),

             ('15_S_BLACK', 'S', 'BLACK', 10, TRUE, 15),
             ('15_M_BLACK', 'M', 'BLACK', 10, TRUE, 15),
             ('15_L_BLACK', 'L', 'BLACK', 10, TRUE, 15),
             ('15_S_WHITE', 'S', 'WHITE', 10, TRUE, 15),
             ('15_M_WHITE', 'M', 'WHITE', 10, TRUE, 15),
             ('15_L_WHITE', 'L', 'WHITE', 10, TRUE, 15),
             ('15_S_BLUE', 'S', 'BLUE', 10, TRUE, 15),
             ('15_M_BLUE', 'M', 'BLUE', 10, TRUE, 15),
             ('15_L_BLUE', 'L', 'BLUE', 10, TRUE, 15),

             ('16_S_BLACK', 'S', 'BLACK', 10, TRUE, 16),
             ('16_M_BLACK', 'M', 'BLACK', 10, TRUE, 16),
             ('16_L_BLACK', 'L', 'BLACK', 10, TRUE, 16),
             ('16_S_WHITE', 'S', 'WHITE', 10, TRUE, 16),
             ('16_M_WHITE', 'M', 'WHITE', 10, TRUE, 16),
             ('16_L_WHITE', 'L', 'WHITE', 10, TRUE, 16),
             ('16_S_BLUE', 'S', 'BLUE', 10, TRUE, 16),
             ('16_M_BLUE', 'M', 'BLUE', 10, TRUE, 16),
             ('16_L_BLUE', 'L', 'BLUE', 10, TRUE, 16),

             ('17_S_BLACK', 'S', 'BLACK', 10, TRUE, 17),
             ('17_M_BLACK', 'M', 'BLACK', 10, TRUE, 17),
             ('17_L_BLACK', 'L', 'BLACK', 10, TRUE, 17),
             ('17_S_WHITE', 'S', 'WHITE', 10, TRUE, 17),
             ('17_M_WHITE', 'M', 'WHITE', 10, TRUE, 17),
             ('17_L_WHITE', 'L', 'WHITE', 10, TRUE, 17),
             ('17_S_BLUE', 'S', 'BLUE', 10, TRUE, 17),
             ('17_M_BLUE', 'M', 'BLUE', 10, TRUE, 17),
             ('17_L_BLUE', 'L', 'BLUE', 10, TRUE, 17),

             ('18_S_BLACK', 'S', 'BLACK', 10, TRUE, 18),
             ('18_M_BLACK', 'M', 'BLACK', 10, TRUE, 18),
             ('18_L_BLACK', 'L', 'BLACK', 10, TRUE, 18),
             ('18_S_WHITE', 'S', 'WHITE', 10, TRUE, 18),
             ('18_M_WHITE', 'M', 'WHITE', 10, TRUE, 18),
             ('18_L_WHITE', 'L', 'WHITE', 10, TRUE, 18),
             ('18_S_BLUE', 'S', 'BLUE', 10, TRUE, 18),
             ('18_M_BLUE', 'M', 'BLUE', 10, TRUE, 18),
             ('18_L_BLUE', 'L', 'BLUE', 10, TRUE, 18),

             ('19_S_BLACK', 'S', 'BLACK', 10, TRUE, 19),
             ('19_M_BLACK', 'M', 'BLACK', 10, TRUE, 19),
             ('19_L_BLACK', 'L', 'BLACK', 10, TRUE, 19),
             ('19_S_WHITE', 'S', 'WHITE', 10, TRUE, 19),
             ('19_M_WHITE', 'M', 'WHITE', 10, TRUE, 19),
             ('19_L_WHITE', 'L', 'WHITE', 10, TRUE, 19),
             ('19_S_BLUE', 'S', 'BLUE', 10, TRUE, 19),
             ('19_M_BLUE', 'M', 'BLUE', 10, TRUE, 19),
             ('19_L_BLUE', 'L', 'BLUE', 10, TRUE, 19),

             ('20_S_BLACK', 'S', 'BLACK', 10, TRUE, 20),
             ('20_M_BLACK', 'M', 'BLACK', 10, TRUE, 20),
             ('20_L_BLACK', 'L', 'BLACK', 10, TRUE, 20),
             ('20_S_WHITE', 'S', 'WHITE', 10, TRUE, 20),
             ('20_M_WHITE', 'M', 'WHITE', 10, TRUE, 20),
             ('20_L_WHITE', 'L', 'WHITE', 10, TRUE, 20),
             ('20_S_BLUE', 'S', 'BLUE', 10, TRUE, 20),
             ('20_M_BLUE', 'M', 'BLUE', 10, TRUE, 20),
             ('20_L_BLUE', 'L', 'BLUE', 10, TRUE, 20)
     ) AS v(name, size, color, stock, active, product_id)
WHERE NOT EXISTS (SELECT 1 FROM product_variant);


INSERT INTO cart (user_id, total_price)
SELECT *
FROM (
         VALUES
             (1,429.94),
             (2,594.92)
     ) AS v(user_id, total_price)
WHERE NOT EXISTS (SELECT 1 FROM cart);

INSERT INTO cart_item (quantity, cart_id, product_variant_id, unit_price, total_price)
SELECT *
FROM (
         VALUES
             (1,1,10,149.99,149.99),
             (3,1,4,75.00,225.00),
             (2,1,45,19.99,39.98),
             (2,2,4,79.99,159.98),
             (3,2,81,24.99,74.97),
             (3,2,88,119.99, 359.97)
     ) AS v(quantity, cart_id, product_variant_id, unit_price, total_price)
WHERE NOT EXISTS (SELECT 1 FROM cart_item);

INSERT INTO orders (user_id, special_instructions, total_price)
SELECT *
FROM (
         VALUES
             (2,'Wrap the items in blue wrapping paper',594.92)
     ) AS v(user_id, special_instructions, total_price)
WHERE NOT EXISTS (SELECT 1 FROM orders);

INSERT INTO order_item (order_id, product_variant_id, unit_price, total_price, quantity)
VALUES
    (1, 4,  79.99,  159.98, 2),
    (1, 81, 24.99,   74.97, 3),
    (1, 88, 119.99, 359.97, 3)
ON CONFLICT (order_id, product_variant_id) DO NOTHING;