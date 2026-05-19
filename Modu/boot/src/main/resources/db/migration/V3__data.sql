INSERT INTO product (name, description, image_url, price, active)
SELECT *
FROM (
         VALUES
             ('Classic Denim Jacket', 'A timeless blue denim jacket with a relaxed fit, designed for everyday comfort and effortless layering. Features durable stitching, classic button closures, and versatile styling that pairs perfectly with casual outfits throughout every season.', 'https://images.unsplash.com/photo-1544642899-f0d6e5f6ed6f?auto=format&fit=crop&w=800&q=80', 79.99, TRUE),

             ('Black Leather Jacket', 'Premium black leather jacket with stylish zipper details and a modern tailored fit. Crafted for durability and comfort, this jacket adds a bold, refined edge to both casual streetwear and evening outfits.', 'https://images.unsplash.com/photo-1563183939-592da2dbb596?auto=format&fit=crop&w=800&q=80', 149.99, TRUE),

             ('Slim Fit Jeans', 'Dark blue slim fit jeans made with flexible stretch fabric for all-day comfort and movement. Designed with a sleek modern silhouette, these jeans work well with sneakers, boots, or smart casual attire.', 'https://images.unsplash.com/photo-1614495039368-525273956716?auto=format&fit=crop&w=800&q=80', 59.99, TRUE),

             ('Casual Polo Shirt', 'Soft cotton polo shirt perfect for everyday wear, offering a clean and comfortable look. Features a breathable fabric blend, classic collar, and versatile design suitable for work, weekends, or relaxed outings.', 'https://images.unsplash.com/photo-1563649685437-a79731028cd1?auto=format&fit=crop&w=800&q=80', 29.99, TRUE),

             ('White T-Shirt', 'Basic white t-shirt made from high-quality organic cotton with a lightweight and breathable feel. Ideal for layering or wearing on its own, this wardrobe essential delivers comfort, simplicity, and timeless casual style.', 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=800&q=80', 19.99, TRUE),

             ('Hooded Sweatshirt', 'Comfortable hooded sweatshirt featuring a spacious front pocket and soft interior lining for extra warmth. Designed for relaxed everyday wear, it combines casual style with cozy comfort during cooler weather conditions.', 'https://images.unsplash.com/photo-1567894173760-22aff4400f21?auto=format&fit=crop&w=800&q=80', 49.99, TRUE),

             ('Chino Pants', 'Slim fit chino pants designed for a polished smart casual appearance with lasting comfort. Made from durable yet lightweight fabric, these versatile pants transition easily from office settings to casual weekend occasions.', 'https://images.unsplash.com/photo-1581382575275-97901c2635b7?auto=format&fit=crop&w=800&q=80', 54.99, TRUE),

             ('Winter Parka', 'Warm insulated parka with a faux fur trimmed hood, built to handle cold winter temperatures. Features protective outer fabric, practical storage pockets, and a comfortable fit ideal for outdoor wear and travel.', 'https://images.unsplash.com/photo-1514564652994-565a3bf3a25b?auto=format&fit=crop&w=800&q=80', 129.99, TRUE),

             ('Summer Shorts', 'Lightweight cotton shorts designed to keep you cool and comfortable during hot summer days. Featuring a relaxed fit and breathable fabric, these shorts are perfect for vacations, beach trips, or casual outdoor activities.', 'https://images.unsplash.com/photo-1685539855961-9045adffe039?auto=format&fit=crop&w=800&q=80', 24.99, TRUE),

             ('Formal Blazer', 'Elegant formal blazer tailored for professional business attire and special occasions. Designed with clean lines and premium materials, it provides a sophisticated appearance while remaining comfortable for extended daily wear.', 'https://images.unsplash.com/photo-1593030942428-a5451dca4b42?auto=format&fit=crop&w=800&q=80', 119.99, TRUE),

             ('Checked Shirt', 'Long sleeve checked shirt crafted from soft flannel fabric for warmth and comfort. Its timeless plaid pattern and relaxed fit make it an ideal choice for casual layering during cooler seasons and weekend outings.', 'https://images.unsplash.com/photo-1602038704137-439ad0e51044?auto=format&fit=crop&w=800&q=80', 39.99, TRUE),

             ('Sports Jacket', 'Lightweight sports jacket created for outdoor activities, travel, and active lifestyles. Designed with breathable materials and a flexible fit, it offers reliable comfort and mobility while maintaining a clean athletic appearance.', 'https://images.unsplash.com/photo-1581961462133-ca8618e89ed3?auto=format&fit=crop&w=800&q=80', 69.99, TRUE),

             ('Cargo Pants', 'Durable cargo pants featuring multiple utility pockets and a rugged yet comfortable design. Built for practicality and everyday versatility, these pants are suitable for outdoor adventures, travel, or relaxed casual styling.', 'https://images.unsplash.com/flagged/photo-1572372406879-e7c93ff299fa?auto=format&fit=crop&w=800&q=80', 64.99, TRUE),

             ('Knitted Sweater', 'Warm knitted sweater designed to provide comfort and insulation during colder weather conditions. Made with soft textured fabric and a classic fit, it pairs effortlessly with jeans, trousers, or layered winter outfits.', 'https://images.unsplash.com/photo-1576110598658-096ae24cdb97?auto=format&fit=crop&w=800&q=80', 59.99, TRUE),

             ('Denim Shorts', 'Casual denim shorts with a stylish frayed hem and comfortable everyday fit. Crafted from durable denim fabric, these shorts bring a relaxed summer vibe that works perfectly with t-shirts, tanks, or lightweight shirts.', 'https://images.unsplash.com/photo-1596460700790-d079c2c8ade6?auto=format&fit=crop&w=800&q=80', 34.99, TRUE),

             ('Track Pants', 'Comfortable track pants designed for workouts, running, and relaxed casual wear. Featuring lightweight fabric and an adjustable fit, they provide flexibility, breathability, and all-day comfort during active routines or downtime.', 'https://images.unsplash.com/photo-1513378628213-b8f36d8c2878?auto=format&fit=crop&w=800&q=80', 44.99, TRUE),

             ('Bomber Jacket', 'Trendy bomber jacket with ribbed cuffs and a modern streetwear-inspired silhouette. Combining lightweight comfort with versatile styling, this jacket works well for casual evenings, transitional weather, and everyday fashion looks.', 'https://images.unsplash.com/photo-1549399239-fb3c102d3d71?auto=format&fit=crop&w=800&q=80', 89.99, TRUE),

             ('Graphic T-Shirt', 'Modern graphic t-shirt featuring a bold printed design and soft breathable fabric for daily comfort. Perfect for casual outfits, this versatile tee adds personality and contemporary style to your everyday wardrobe collection.', 'https://images.unsplash.com/photo-1584636633449-6135be6c4169?auto=format&fit=crop&w=800&q=80', 22.99, TRUE),

             ('Linen Shirt', 'Breathable linen shirt created for warm summer weather and relaxed casual styling. Its lightweight natural fabric provides exceptional airflow and comfort, making it ideal for holidays, outdoor dining, and sunny daytime occasions.', 'https://images.unsplash.com/photo-1558436223-247523d35637?auto=format&fit=crop&w=800&q=80', 45.99, TRUE),

             ('Wool Coat', 'Elegant wool coat designed for the winter season with a refined and timeless appearance. Crafted from warm premium materials, it delivers both comfort and sophistication for formal events, workdays, and cold-weather outings.', 'https://images.unsplash.com/photo-1661110546899-732bffb4cb85?auto=format&fit=crop&w=800&q=80', 179.99, TRUE),

             -- T-shirts
             ('Striped Crew Neck Tee', 'Relaxed cotton tee with horizontal stripes, a classic nautical vibe, and a comfortable everyday fit perfect for layering or wearing alone during warmer seasons.', 'https://images.unsplash.com/photo-1523381210434-271e8be1f52b?auto=format&fit=crop&w=800&q=80', 24.99, TRUE),
             ('Oversized V-Neck Tee', 'Loose-fit V-neck t-shirt in washed cotton with a lived-in feel and dropped shoulders. Ideal for relaxed streetwear looks that blend comfort with effortless modern style.', 'https://images.unsplash.com/photo-1583743814966-8936f5b7be1a?auto=format&fit=crop&w=800&q=80', 27.99, TRUE),
             ('Muscle Fit Tank Top', 'Body-hugging tank top crafted from stretch jersey fabric, designed to highlight physique while providing breathable comfort during workouts and hot weather outings.', 'https://images.unsplash.com/photo-1503341455253-b2e723bb3dbb?auto=format&fit=crop&w=800&q=80', 18.99, TRUE),
             ('Henley Long Sleeve', 'Classic henley with a buttoned placket and soft ribbed cuffs. Made from lightweight cotton blend for comfortable year-round layering under jackets or wearing solo.', 'https://images.unsplash.com/photo-1618354691373-d851c5c3a990?auto=format&fit=crop&w=800&q=80', 34.99, TRUE),
             ('Tie-Dye Festival Tee', 'Vibrant hand-dyed t-shirt with unique swirl patterns. Each piece varies slightly, making it a one-of-a-kind addition to your casual wardrobe for festivals and weekends.', 'https://images.unsplash.com/photo-1529374255404-311a2a4f1fd9?auto=format&fit=crop&w=800&q=80', 29.99, TRUE),

             -- Shirts
            ('Oxford Button-Down', 'Crisp Oxford weave button-down shirt with a structured collar that holds its shape. A wardrobe staple that bridges casual and smart dress codes with effortless versatility.', 'https://images.unsplash.com/photo-1598033129183-c4f50c736c10?auto=format&fit=crop&w=800&q=80', 49.99, TRUE),
            ('Denim Western Shirt', 'Snap-button denim shirt with pointed yoke detailing and a slightly faded wash. Channels vintage Americana style while remaining comfortable enough for daily casual wear.', 'https://images.unsplash.com/photo-1589310243389-96a5483213a8?auto=format&fit=crop&w=800&q=80', 54.99, TRUE),
            ('Hawaiian Print Shirt', 'Bold tropical print short-sleeve shirt made from lightweight viscose fabric. Perfect for vacations, beach days, and any occasion that calls for relaxed statement dressing.', 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?auto=format&fit=crop&w=800&q=80', 39.99, TRUE),

            -- Jersey
            ('Crewneck Fleece Sweatshirt', 'Heavyweight fleece sweatshirt with ribbed trims and a roomy fit for ultimate comfort. Brushed interior keeps you warm while maintaining a clean minimal exterior appearance.', 'https://images.unsplash.com/photo-1556821840-3a63f95609a7?auto=format&fit=crop&w=800&q=80', 54.99, TRUE),
            ('Zip-Up Hoodie', 'Full-zip hooded sweatshirt with split kangaroo pockets and metal zipper hardware. Versatile layering piece that works equally well at the gym, on errands, or at home.', 'https://images.unsplash.com/photo-1611312449408-fcece27cdbb7?auto=format&fit=crop&w=800&q=80', 59.99, TRUE),
            ('Cable Knit Cardigan', 'Chunky cable knit cardigan with wooden toggle buttons and a shawl collar. Adds texture and warmth to autumn and winter outfits with a cozy heritage-inspired aesthetic.', 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea?auto=format&fit=crop&w=800&q=80', 74.99, TRUE),
            ('Turtleneck Sweater', 'Fine-gauge merino wool turtleneck with a slim fit and soft hand feel. An elevated essential that pairs seamlessly with blazers, coats, or worn as a standalone layering piece.', 'https://images.unsplash.com/photo-1638643391904-9b551ba91eaa?auto=format&fit=crop&w=800&q=80', 69.99, TRUE),

            -- Jackets and coats
            ('Quilted Puffer Vest', 'Lightweight quilted vest with synthetic insulation and a stand-up collar. Provides core warmth without restricting arm movement, ideal for transitional weather and outdoor activities.', 'https://images.unsplash.com/photo-1608063615781-e2ef8c73d114?auto=format&fit=crop&w=800&q=80', 64.99, TRUE),
            ('Corduroy Trucker Jacket', 'Retro-inspired corduroy jacket with a sherpa-lined collar and classic trucker silhouette. Delivers a warm vintage look that pairs perfectly with denim and workwear staples.', 'https://images.unsplash.com/photo-1551028719-00167b16eac5?auto=format&fit=crop&w=800&q=80', 89.99, TRUE),
            ('Trench Coat', 'Water-resistant trench coat with a double-breasted front and adjustable belt. A sophisticated outerwear classic designed for rainy commutes and polished seasonal layering.', 'https://images.unsplash.com/photo-1592878904946-b3cd8ae243d0?auto=format&fit=crop&w=800&q=80', 159.99, TRUE),
            ('Windbreaker Jacket', 'Ultra-light packable windbreaker with a hood and elastic cuffs. Folds into its own pocket for easy storage, making it an essential travel companion for unpredictable weather.', 'https://images.unsplash.com/photo-1545594861-3bef43ff2fc8?auto=format&fit=crop&w=800&q=80', 49.99, TRUE),

            -- Trousers
            ('Relaxed Fit Joggers', 'Soft French terry joggers with tapered legs and an elastic drawstring waist. Designed for comfort-first days when you want to look put together without sacrificing ease.', 'https://images.unsplash.com/photo-1552902865-b72c031ac5ea?auto=format&fit=crop&w=800&q=80', 42.99, TRUE),
            ('Tailored Dress Pants', 'Slim-cut dress pants with pressed creases and a hidden clasp closure. Made from wrinkle-resistant fabric that maintains a sharp professional look throughout the entire workday.', 'https://images.unsplash.com/photo-1594938298603-c8148c4dae35?auto=format&fit=crop&w=800&q=80', 74.99, TRUE),
            ('Wide Leg Trousers', 'Flowing wide-leg trousers with a high waist and pleated front. Offers a relaxed silhouette that balances comfort with contemporary fashion-forward styling for any occasion.', 'https://images.unsplash.com/photo-1506629082955-511b1aa562c8?auto=format&fit=crop&w=800&q=80', 59.99, TRUE),
            ('Ripped Skinny Jeans', 'Distressed skinny jeans with strategic rip detailing and raw hem finish. Made from stretch denim for a body-hugging fit that adds an edgy streetwear element to any outfit.', 'https://images.unsplash.com/photo-1541099649105-f69ad21f3246?auto=format&fit=crop&w=800&q=80', 49.99, TRUE),
            ('Corduroy Pants', 'Classic corduroy trousers with a straight leg and warm textured finish. A seasonal essential that adds visual interest and retro charm to smart casual autumn and winter looks.', 'https://images.unsplash.com/photo-1624378439575-d8705ad7ae80?auto=format&fit=crop&w=800&q=80', 54.99, TRUE),

            -- Shorts
            ('Athletic Running Shorts', 'Lightweight performance shorts with a built-in mesh liner and moisture-wicking fabric. Engineered for freedom of movement during runs, training sessions, and active lifestyles.', 'https://images.unsplash.com/photo-1591195853828-11db59a44f6b?auto=format&fit=crop&w=800&q=80', 29.99, TRUE),
            ('Chino Bermuda Shorts', 'Knee-length chino shorts with a clean tailored appearance and comfortable stretch fabric. Perfect for warm-weather outings that call for a polished yet relaxed dress code.', 'https://images.unsplash.com/photo-1565084888279-aca607ecce0c?auto=format&fit=crop&w=800&q=80', 34.99, TRUE),
            ('Swim Trunks', 'Quick-dry swim trunks with an elastic waistband and internal mesh briefs. Bold colour-block design adds style to beach days, pool parties, and seaside holiday wardrobes.', 'https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?auto=format&fit=crop&w=800&q=80', 27.99, TRUE),

            -- Footware
            ('Canvas Low-Top Sneakers', 'Minimalist canvas sneakers with vulcanised rubber soles and cotton laces. A timeless everyday shoe that complements casual outfits from jeans and tees to shorts and polos.', 'https://images.unsplash.com/photo-1525966222134-fcfa99b8ae77?auto=format&fit=crop&w=800&q=80', 44.99, TRUE),
            ('Leather Chelsea Boots', 'Classic leather Chelsea boots with elasticated side panels and a pull tab. Sleek ankle-height design transitions smoothly from smart office wear to evening social occasions.', 'https://images.unsplash.com/photo-1638247025967-b4e38f787b76?auto=format&fit=crop&w=800&q=80', 129.99, TRUE),
            ('Running Performance Shoes', 'Engineered mesh running shoes with responsive cushioning and a lightweight sole unit. Designed for daily training with reliable support, grip, and breathability mile after mile.', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=800&q=80', 99.99, TRUE),
            ('Suede Desert Boots', 'Soft suede desert boots with crepe rubber soles and a clean two-eyelet lace design. A versatile casual boot that pairs naturally with chinos, denim, and relaxed tailoring.', 'https://images.unsplash.com/photo-1608256246200-53e635b5b65f?auto=format&fit=crop&w=800&q=80', 89.99, TRUE),
            ('Slide Sandals', 'Contoured footbed slide sandals with a single strap and textured grip sole. Easy on-off design makes them ideal for post-workout recovery, poolside lounging, and casual errands.', 'https://images.unsplash.com/photo-1603487742131-4160ec999306?auto=format&fit=crop&w=800&q=80', 19.99, TRUE),

            -- Accesories
            ('Leather Belt', 'Full-grain leather belt with a brushed metal buckle and clean stitching along the edges. A durable everyday accessory that adds a finishing touch to both casual and smart outfits.', 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?auto=format&fit=crop&w=800&q=80', 34.99, TRUE),
            ('Cotton Baseball Cap', 'Unstructured six-panel baseball cap in washed cotton with an adjustable metal clasp. Provides sun protection and relaxed style for outdoor activities and casual everyday wear.', 'https://images.unsplash.com/photo-1588850561407-ed78c334e67a?auto=format&fit=crop&w=800&q=80', 19.99, TRUE),
            ('Knitted Beanie', 'Ribbed knit beanie in soft acrylic yarn with a fold-over cuff. A cold-weather staple that keeps you warm while adding a casual finishing touch to winter and autumn outfits.', 'https://images.unsplash.com/photo-1576871337632-b9aef4c17ab9?auto=format&fit=crop&w=800&q=80', 14.99, TRUE),
            ('Aviator Sunglasses', 'Classic aviator-frame sunglasses with polarised lenses and lightweight metal construction. Provides full UV protection while delivering a timeless look that suits every face shape.', 'https://images.unsplash.com/photo-1511499767150-a48a237f0083?auto=format&fit=crop&w=800&q=80', 29.99, TRUE),
            ('Canvas Tote Bag', 'Sturdy canvas tote bag with reinforced handles and an internal zip pocket. Spacious enough for daily essentials, gym gear, or groceries with a clean minimalist design aesthetic.', 'https://images.unsplash.com/photo-1544816155-12df9643f363?auto=format&fit=crop&w=800&q=80', 24.99, TRUE),
            ('Leather Backpack', 'Premium leather backpack with padded shoulder straps and a laptop compartment. Combines professional polish with practical everyday functionality for commuters and travellers alike.', 'https://images.unsplash.com/photo-1548036328-c9fa89d128fa?auto=format&fit=crop&w=800&q=80', 119.99, TRUE),
            ('Wool Scarf', 'Soft lambswool scarf with fringed ends and a generous length for wrapping. Adds warmth and texture to winter coats and jackets while keeping your neckline protected from cold winds.', 'https://images.unsplash.com/photo-1520903920243-00d872a2d1c9?auto=format&fit=crop&w=800&q=80', 29.99, TRUE),

            -- Limited / Vintage
            ('Retro Varsity Jacket', 'Wool-body varsity jacket with faux leather sleeves and embroidered chenille patches. A nostalgic statement piece inspired by vintage American collegiate sportswear from the 1960s.', 'https://images.unsplash.com/photo-1559551409-dadc959f76b8?auto=format&fit=crop&w=800&q=80', 109.99, TRUE),
            ('Vintage Wash Denim Overalls', 'Relaxed-fit denim overalls with adjustable straps and a vintage acid-wash finish. A playful throwback silhouette that brings retro character to modern casual weekend wardrobes.', 'https://images.unsplash.com/photo-1565084888279-aca607ecce0c?auto=format&fit=crop&w=800&q=80', 79.99, TRUE),
            ('Limited Edition Embroidered Hoodie', 'Heavy-weight hoodie featuring exclusive hand-embroidered artwork on the back panel. Produced in a small batch run, this piece blends streetwear comfort with collectible artisan craftsmanship.', 'https://images.unsplash.com/photo-1556821840-3a63f95609a7?auto=format&fit=crop&w=800&q=80', 89.99, TRUE),

            -- Inactive products
            ('Discontinued Camp Collar Shirt', 'Breezy camp collar shirt with a retro boxy cut. This style has been discontinued but remains a favourite among vintage fashion enthusiasts seeking relaxed warm-weather silhouettes.', 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?auto=format&fit=crop&w=800&q=80', 42.99, FALSE),
            ('Clearance Puffer Jacket', 'End-of-season puffer jacket with heavyweight synthetic fill and a detachable hood. Available at reduced price while stock lasts, ideal for shoppers looking for winter outerwear deals.', 'https://images.unsplash.com/photo-1608063615781-e2ef8c73d114?auto=format&fit=crop&w=800&q=80', 69.99, FALSE)
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
             (3,5),
             (4, 1),   -- Casual Polo Shirt -> Streetwear
             (5, 1),   -- White T-Shirt -> Streetwear
             (6, 1),   -- Hooded Sweatshirt -> Streetwear
             (7, 1),   -- Chino Pants -> Streetwear
             (8, 1),   -- Winter Parka -> Streetwear
             (9, 1),   -- Summer Shorts -> Streetwear
             (10, 1),  -- Formal Blazer -> Streetwear
             (11, 1),  -- Checked Shirt -> Streetwear
             (11, 6),  -- Checked Shirt -> Vintage
             (12, 2),  -- Sports Jacket -> Activewear
             (13, 1),  -- Cargo Pants -> Streetwear
             (14, 1),  -- Knitted Sweater -> Streetwear
             (15, 1),  -- Denim Shorts -> Streetwear
             (16, 2),  -- Track Pants -> Activewear
             (17, 1),  -- Bomber Jacket -> Streetwear
             (18, 1),  -- Graphic T-Shirt -> Streetwear
             (19, 1),  -- Linen Shirt -> Streetwear
             (20, 1),  -- Wool Coat -> Streetwear
             (21, 1),  -- Striped Crew Neck Tee -> Streetwear
             (22, 1),  -- Oversized V-Neck Tee -> Streetwear
             (23, 2),  -- Muscle Fit Tank Top -> Activewear
             (24, 1),  -- Henley Long Sleeve -> Streetwear
             (25, 1),  -- Tie-Dye Festival Tee -> Streetwear
             (25, 5),  -- Tie-Dye Festival Tee -> Limited Edition
             (26, 1),  -- Oxford Button-Down -> Streetwear
             (27, 1),  -- Denim Western Shirt -> Streetwear
             (27, 6),  -- Denim Western Shirt -> Vintage
             (28, 1),  -- Hawaiian Print Shirt -> Streetwear
             (29, 1),  -- Crewneck Fleece Sweatshirt -> Streetwear
             (30, 1),  -- Zip-Up Hoodie -> Streetwear
             (30, 2),  -- Zip-Up Hoodie -> Activewear
             (31, 1),  -- Cable Knit Cardigan -> Streetwear
             (31, 6),  -- Cable Knit Cardigan -> Vintage
             (32, 1),  -- Turtleneck Sweater -> Streetwear
             (33, 2),  -- Quilted Puffer Vest -> Activewear
             (34, 6),  -- Corduroy Trucker Jacket -> Vintage
             (34, 1),  -- Corduroy Trucker Jacket -> Streetwear
             (35, 1),  -- Trench Coat -> Streetwear
             (36, 2),  -- Windbreaker Jacket -> Activewear
             (37, 2),  -- Relaxed Fit Joggers -> Activewear
             (37, 1),  -- Relaxed Fit Joggers -> Streetwear
             (38, 1),  -- Tailored Dress Pants -> Streetwear
             (39, 1),  -- Wide Leg Trousers -> Streetwear
             (40, 1),  -- Ripped Skinny Jeans -> Streetwear
             (41, 6),  -- Corduroy Pants -> Vintage
             (41, 1),  -- Corduroy Pants -> Streetwear
             (42, 2),  -- Athletic Running Shorts -> Activewear
             (43, 1),  -- Chino Bermuda Shorts -> Streetwear
             (44, 2),  -- Swim Trunks -> Activewear
             (45, 4),  -- Canvas Low-Top Sneakers -> Footwear
             (45, 1),  -- Canvas Low-Top Sneakers -> Streetwear
             (46, 4),  -- Leather Chelsea Boots -> Footwear
             (47, 4),  -- Running Performance Shoes -> Footwear
             (47, 2),  -- Running Performance Shoes -> Activewear
             (48, 4),  -- Suede Desert Boots -> Footwear
             (48, 6),  -- Suede Desert Boots -> Vintage
             (49, 4),  -- Slide Sandals -> Footwear
             (50, 3),  -- Leather Belt -> Accesories
             (51, 3),  -- Cotton Baseball Cap -> Accesories
             (51, 1),  -- Cotton Baseball Cap -> Streetwear
             (52, 3),  -- Knitted Beanie -> Accesories
             (53, 3),  -- Aviator Sunglasses -> Accesories
             (54, 3),  -- Canvas Tote Bag -> Accesories
             (55, 3),  -- Leather Backpack -> Accesories
             (56, 3),  -- Wool Scarf -> Accesories
             (57, 5),  -- Retro Varsity Jacket -> Limited Edition
             (57, 6),  -- Retro Varsity Jacket -> Vintage
             (58, 6),  -- Vintage Wash Denim Overalls -> Vintage
             (59, 5),  -- Limited Edition Embroidered Hoodie -> Limited Edition
             (59, 1),  -- Limited Edition Embroidered Hoodie -> Streetwear
             (60, 1),  -- Discontinued Camp Collar Shirt -> Streetwear (inactive)
             (60, 6),   -- Discontinued Camp Collar Shirt -> Vintage (inactive)
             (61, 1),  -- Clearance Puffer Jacket -> Streetwear (inactive)
             (61, 2)   -- Clearance Puffer Jacket -> Activewear (inactive)
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
             ('20_L_BLUE', 'L', 'BLUE', 10, TRUE, 20),
             ('21_S_BLACK', 'S', 'BLACK', 10, TRUE, 21),
             ('21_M_BLACK', 'M', 'BLACK', 10, TRUE, 21),
             ('21_L_BLACK', 'L', 'BLACK', 10, TRUE, 21),
             ('21_S_WHITE', 'S', 'WHITE', 10, TRUE, 21),
             ('21_M_WHITE', 'M', 'WHITE', 10, TRUE, 21),
             ('21_L_WHITE', 'L', 'WHITE', 10, TRUE, 21),
             ('21_S_BLUE', 'S', 'BLUE', 10, TRUE, 21),
             ('21_M_BLUE', 'M', 'BLUE', 10, TRUE, 21),
             ('21_L_BLUE', 'L', 'BLUE', 10, TRUE, 21),

             ('22_S_BLACK', 'S', 'BLACK', 10, TRUE, 22),
             ('22_M_BLACK', 'M', 'BLACK', 10, TRUE, 22),
             ('22_L_BLACK', 'L', 'BLACK', 10, TRUE, 22),
             ('22_S_WHITE', 'S', 'WHITE', 10, TRUE, 22),
             ('22_M_WHITE', 'M', 'WHITE', 10, TRUE, 22),
             ('22_L_WHITE', 'L', 'WHITE', 10, TRUE, 22),
             ('22_S_BLUE', 'S', 'BLUE', 10, TRUE, 22),
             ('22_M_BLUE', 'M', 'BLUE', 10, TRUE, 22),
             ('22_L_BLUE', 'L', 'BLUE', 10, TRUE, 22),

             ('23_S_BLACK', 'S', 'BLACK', 10, TRUE, 23),
             ('23_M_BLACK', 'M', 'BLACK', 10, TRUE, 23),
             ('23_L_BLACK', 'L', 'BLACK', 10, TRUE, 23),
             ('23_S_WHITE', 'S', 'WHITE', 10, TRUE, 23),
             ('23_M_WHITE', 'M', 'WHITE', 10, TRUE, 23),
             ('23_L_WHITE', 'L', 'WHITE', 10, TRUE, 23),
             ('23_S_BLUE', 'S', 'BLUE', 10, TRUE, 23),
             ('23_M_BLUE', 'M', 'BLUE', 10, TRUE, 23),
             ('23_L_BLUE', 'L', 'BLUE', 10, TRUE, 23),

             ('24_S_BLACK', 'S', 'BLACK', 10, TRUE, 24),
             ('24_M_BLACK', 'M', 'BLACK', 10, TRUE, 24),
             ('24_L_BLACK', 'L', 'BLACK', 10, TRUE, 24),
             ('24_S_WHITE', 'S', 'WHITE', 10, TRUE, 24),
             ('24_M_WHITE', 'M', 'WHITE', 10, TRUE, 24),
             ('24_L_WHITE', 'L', 'WHITE', 10, TRUE, 24),
             ('24_S_BLUE', 'S', 'BLUE', 10, TRUE, 24),
             ('24_M_BLUE', 'M', 'BLUE', 10, TRUE, 24),
             ('24_L_BLUE', 'L', 'BLUE', 10, TRUE, 24),

             ('25_S_BLACK', 'S', 'BLACK', 10, TRUE, 25),
             ('25_M_BLACK', 'M', 'BLACK', 10, TRUE, 25),
             ('25_L_BLACK', 'L', 'BLACK', 10, TRUE, 25),
             ('25_S_WHITE', 'S', 'WHITE', 10, TRUE, 25),
             ('25_M_WHITE', 'M', 'WHITE', 10, TRUE, 25),
             ('25_L_WHITE', 'L', 'WHITE', 10, TRUE, 25),
             ('25_S_BLUE', 'S', 'BLUE', 10, TRUE, 25),
             ('25_M_BLUE', 'M', 'BLUE', 10, TRUE, 25),
             ('25_L_BLUE', 'L', 'BLUE', 10, TRUE, 25),

             ('26_XS_BLACK', 'XS', 'BLACK', 10, TRUE, 26),
             ('26_S_BLACK', 'S', 'BLACK', 10, TRUE, 26),
             ('26_M_BLACK', 'M', 'BLACK', 10, TRUE, 26),
             ('26_L_BLACK', 'L', 'BLACK', 10, TRUE, 26),
             ('26_XL_BLACK', 'XL', 'BLACK', 10, TRUE, 26),
             ('26_XS_WHITE', 'XS', 'WHITE', 10, TRUE, 26),
             ('26_S_WHITE', 'S', 'WHITE', 10, TRUE, 26),
             ('26_M_WHITE', 'M', 'WHITE', 10, TRUE, 26),
             ('26_L_WHITE', 'L', 'WHITE', 10, TRUE, 26),
             ('26_XL_WHITE', 'XL', 'WHITE', 10, TRUE, 26),
             ('26_XS_BLUE', 'XS', 'BLUE', 10, TRUE, 26),
             ('26_S_BLUE', 'S', 'BLUE', 10, TRUE, 26),
             ('26_M_BLUE', 'M', 'BLUE', 10, TRUE, 26),
             ('26_L_BLUE', 'L', 'BLUE', 10, TRUE, 26),
             ('26_XL_BLUE', 'XL', 'BLUE', 10, TRUE, 26),

             ('27_XS_BLACK', 'XS', 'BLACK', 10, TRUE, 27),
             ('27_S_BLACK', 'S', 'BLACK', 10, TRUE, 27),
             ('27_M_BLACK', 'M', 'BLACK', 10, TRUE, 27),
             ('27_L_BLACK', 'L', 'BLACK', 10, TRUE, 27),
             ('27_XL_BLACK', 'XL', 'BLACK', 10, TRUE, 27),
             ('27_XS_WHITE', 'XS', 'WHITE', 10, TRUE, 27),
             ('27_S_WHITE', 'S', 'WHITE', 10, TRUE, 27),
             ('27_M_WHITE', 'M', 'WHITE', 10, TRUE, 27),
             ('27_L_WHITE', 'L', 'WHITE', 10, TRUE, 27),
             ('27_XL_WHITE', 'XL', 'WHITE', 10, TRUE, 27),
             ('27_XS_BLUE', 'XS', 'BLUE', 10, TRUE, 27),
             ('27_S_BLUE', 'S', 'BLUE', 10, TRUE, 27),
             ('27_M_BLUE', 'M', 'BLUE', 10, TRUE, 27),
             ('27_L_BLUE', 'L', 'BLUE', 10, TRUE, 27),
             ('27_XL_BLUE', 'XL', 'BLUE', 10, TRUE, 27),

             ('28_XS_BLACK', 'XS', 'BLACK', 10, TRUE, 28),
             ('28_S_BLACK', 'S', 'BLACK', 10, TRUE, 28),
             ('28_M_BLACK', 'M', 'BLACK', 10, TRUE, 28),
             ('28_L_BLACK', 'L', 'BLACK', 10, TRUE, 28),
             ('28_XL_BLACK', 'XL', 'BLACK', 10, TRUE, 28),
             ('28_XS_WHITE', 'XS', 'WHITE', 10, TRUE, 28),
             ('28_S_WHITE', 'S', 'WHITE', 10, TRUE, 28),
             ('28_M_WHITE', 'M', 'WHITE', 10, TRUE, 28),
             ('28_L_WHITE', 'L', 'WHITE', 10, TRUE, 28),
             ('28_XL_WHITE', 'XL', 'WHITE', 10, TRUE, 28),
             ('28_XS_BLUE', 'XS', 'BLUE', 10, TRUE, 28),
             ('28_S_BLUE', 'S', 'BLUE', 10, TRUE, 28),
             ('28_M_BLUE', 'M', 'BLUE', 10, TRUE, 28),
             ('28_L_BLUE', 'L', 'BLUE', 10, TRUE, 28),
             ('28_XL_BLUE', 'XL', 'BLUE', 10, TRUE, 28),

             ('29_S_BLACK', 'S', 'BLACK', 10, TRUE, 29),
             ('29_M_BLACK', 'M', 'BLACK', 10, TRUE, 29),
             ('29_L_BLACK', 'L', 'BLACK', 10, TRUE, 29),
             ('29_S_WHITE', 'S', 'WHITE', 10, TRUE, 29),
             ('29_M_WHITE', 'M', 'WHITE', 10, TRUE, 29),
             ('29_L_WHITE', 'L', 'WHITE', 10, TRUE, 29),
             ('29_S_BLUE', 'S', 'BLUE', 10, TRUE, 29),
             ('29_M_BLUE', 'M', 'BLUE', 10, TRUE, 29),
             ('29_L_BLUE', 'L', 'BLUE', 10, TRUE, 29),

             ('30_S_BLACK', 'S', 'BLACK', 10, TRUE, 30),
             ('30_M_BLACK', 'M', 'BLACK', 10, TRUE, 30),
             ('30_L_BLACK', 'L', 'BLACK', 10, TRUE, 30),
             ('30_S_WHITE', 'S', 'WHITE', 10, TRUE, 30),
             ('30_M_WHITE', 'M', 'WHITE', 10, TRUE, 30),
             ('30_L_WHITE', 'L', 'WHITE', 10, TRUE, 30),
             ('30_S_BLUE', 'S', 'BLUE', 10, TRUE, 30),
             ('30_M_BLUE', 'M', 'BLUE', 10, TRUE, 30),
             ('30_L_BLUE', 'L', 'BLUE', 10, TRUE, 30),

             ('31_S_BLACK', 'S', 'BLACK', 10, TRUE, 31),
             ('31_M_BLACK', 'M', 'BLACK', 10, TRUE, 31),
             ('31_L_BLACK', 'L', 'BLACK', 10, TRUE, 31),
             ('31_S_WHITE', 'S', 'WHITE', 10, TRUE, 31),
             ('31_M_WHITE', 'M', 'WHITE', 10, TRUE, 31),
             ('31_L_WHITE', 'L', 'WHITE', 10, TRUE, 31),
             ('31_S_BLUE', 'S', 'BLUE', 10, TRUE, 31),
             ('31_M_BLUE', 'M', 'BLUE', 10, TRUE, 31),
             ('31_L_BLUE', 'L', 'BLUE', 10, TRUE, 31),

             ('32_S_BLACK', 'S', 'BLACK', 10, TRUE, 32),
             ('32_M_BLACK', 'M', 'BLACK', 10, TRUE, 32),
             ('32_L_BLACK', 'L', 'BLACK', 10, TRUE, 32),
             ('32_S_WHITE', 'S', 'WHITE', 10, TRUE, 32),
             ('32_M_WHITE', 'M', 'WHITE', 10, TRUE, 32),
             ('32_L_WHITE', 'L', 'WHITE', 10, TRUE, 32),
             ('32_S_BLUE', 'S', 'BLUE', 10, TRUE, 32),
             ('32_M_BLUE', 'M', 'BLUE', 10, TRUE, 32),
             ('32_L_BLUE', 'L', 'BLUE', 10, TRUE, 32),

             ('33_S_BLACK', 'S', 'BLACK', 10, TRUE, 33),
             ('33_M_BLACK', 'M', 'BLACK', 10, TRUE, 33),
             ('33_L_BLACK', 'L', 'BLACK', 10, TRUE, 33),
             ('33_S_WHITE', 'S', 'WHITE', 10, TRUE, 33),
             ('33_M_WHITE', 'M', 'WHITE', 10, TRUE, 33),
             ('33_L_WHITE', 'L', 'WHITE', 10, TRUE, 33),
             ('33_S_BLUE', 'S', 'BLUE', 10, TRUE, 33),
             ('33_M_BLUE', 'M', 'BLUE', 10, TRUE, 33),
             ('33_L_BLUE', 'L', 'BLUE', 10, TRUE, 33),

             ('34_S_BLACK', 'S', 'BLACK', 10, TRUE, 34),
             ('34_M_BLACK', 'M', 'BLACK', 10, TRUE, 34),
             ('34_L_BLACK', 'L', 'BLACK', 10, TRUE, 34),
             ('34_S_WHITE', 'S', 'WHITE', 10, TRUE, 34),
             ('34_M_WHITE', 'M', 'WHITE', 10, TRUE, 34),
             ('34_L_WHITE', 'L', 'WHITE', 10, TRUE, 34),
             ('34_S_BLUE', 'S', 'BLUE', 10, TRUE, 34),
             ('34_M_BLUE', 'M', 'BLUE', 10, TRUE, 34),
             ('34_L_BLUE', 'L', 'BLUE', 10, TRUE, 34),

             ('35_S_BLACK', 'S', 'BLACK', 10, TRUE, 35),
             ('35_M_BLACK', 'M', 'BLACK', 10, TRUE, 35),
             ('35_L_BLACK', 'L', 'BLACK', 10, TRUE, 35),
             ('35_S_WHITE', 'S', 'WHITE', 10, TRUE, 35),
             ('35_M_WHITE', 'M', 'WHITE', 10, TRUE, 35),
             ('35_L_WHITE', 'L', 'WHITE', 10, TRUE, 35),
             ('35_S_BLUE', 'S', 'BLUE', 10, TRUE, 35),
             ('35_M_BLUE', 'M', 'BLUE', 10, TRUE, 35),
             ('35_L_BLUE', 'L', 'BLUE', 10, TRUE, 35),

             ('36_S_BLACK', 'S', 'BLACK', 10, TRUE, 36),
             ('36_M_BLACK', 'M', 'BLACK', 10, TRUE, 36),
             ('36_L_BLACK', 'L', 'BLACK', 10, TRUE, 36),
             ('36_S_WHITE', 'S', 'WHITE', 10, TRUE, 36),
             ('36_M_WHITE', 'M', 'WHITE', 10, TRUE, 36),
             ('36_L_WHITE', 'L', 'WHITE', 10, TRUE, 36),
             ('36_S_BLUE', 'S', 'BLUE', 10, TRUE, 36),
             ('36_M_BLUE', 'M', 'BLUE', 10, TRUE, 36),
             ('36_L_BLUE', 'L', 'BLUE', 10, TRUE, 36),

             ('37_S_BLACK', 'S', 'BLACK', 10, TRUE, 37),
             ('37_M_BLACK', 'M', 'BLACK', 10, TRUE, 37),
             ('37_L_BLACK', 'L', 'BLACK', 10, TRUE, 37),
             ('37_S_WHITE', 'S', 'WHITE', 10, TRUE, 37),
             ('37_M_WHITE', 'M', 'WHITE', 10, TRUE, 37),
             ('37_L_WHITE', 'L', 'WHITE', 10, TRUE, 37),
             ('37_S_BLUE', 'S', 'BLUE', 10, TRUE, 37),
             ('37_M_BLUE', 'M', 'BLUE', 10, TRUE, 37),
             ('37_L_BLUE', 'L', 'BLUE', 10, TRUE, 37),

             ('38_S_BLACK', 'S', 'BLACK', 10, TRUE, 38),
             ('38_M_BLACK', 'M', 'BLACK', 10, TRUE, 38),
             ('38_L_BLACK', 'L', 'BLACK', 10, TRUE, 38),
             ('38_S_WHITE', 'S', 'WHITE', 10, TRUE, 38),
             ('38_M_WHITE', 'M', 'WHITE', 10, TRUE, 38),
             ('38_L_WHITE', 'L', 'WHITE', 10, TRUE, 38),
             ('38_S_BLUE', 'S', 'BLUE', 10, TRUE, 38),
             ('38_M_BLUE', 'M', 'BLUE', 10, TRUE, 38),
             ('38_L_BLUE', 'L', 'BLUE', 10, TRUE, 38),

             ('39_S_BLACK', 'S', 'BLACK', 10, TRUE, 39),
             ('39_M_BLACK', 'M', 'BLACK', 10, TRUE, 39),
             ('39_L_BLACK', 'L', 'BLACK', 10, TRUE, 39),
             ('39_S_WHITE', 'S', 'WHITE', 10, TRUE, 39),
             ('39_M_WHITE', 'M', 'WHITE', 10, TRUE, 39),
             ('39_L_WHITE', 'L', 'WHITE', 10, TRUE, 39),
             ('39_S_BLUE', 'S', 'BLUE', 10, TRUE, 39),
             ('39_M_BLUE', 'M', 'BLUE', 10, TRUE, 39),
             ('39_L_BLUE', 'L', 'BLUE', 10, TRUE, 39),

             ('40_S_BLACK', 'S', 'BLACK', 0, TRUE, 40),
             ('40_M_BLACK', 'M', 'BLACK', 10, TRUE, 40),
             ('40_L_BLACK', 'L', 'BLACK', 10, TRUE, 40),
             ('40_S_WHITE', 'S', 'WHITE', 0, TRUE, 40),
             ('40_M_WHITE', 'M', 'WHITE', 0, TRUE, 40),
             ('40_L_WHITE', 'L', 'WHITE', 10, TRUE, 40),
             ('40_S_BLUE', 'S', 'BLUE', 0, TRUE, 40),
             ('40_M_BLUE', 'M', 'BLUE', 10, TRUE, 40),
             ('40_L_BLUE', 'L', 'BLUE', 10, TRUE, 40),

             ('41_S_BLACK', 'S', 'BLACK', 0, TRUE, 41),
             ('41_M_BLACK', 'M', 'BLACK', 10, TRUE, 41),
             ('41_L_BLACK', 'L', 'BLACK', 10, TRUE, 41),
             ('41_S_WHITE', 'S', 'WHITE', 0, TRUE, 41),
             ('41_M_WHITE', 'M', 'WHITE', 0, TRUE, 41),
             ('41_L_WHITE', 'L', 'WHITE', 10, TRUE, 41),
             ('41_S_BLUE', 'S', 'BLUE', 0, TRUE, 41),
             ('41_M_BLUE', 'M', 'BLUE', 10, TRUE, 41),
             ('41_L_BLUE', 'L', 'BLUE', 10, TRUE, 41),

             ('42_S_BLACK', 'S', 'BLACK', 0, TRUE, 42),
             ('42_M_BLACK', 'M', 'BLACK', 10, TRUE, 42),
             ('42_L_BLACK', 'L', 'BLACK', 10, TRUE, 42),
             ('42_S_WHITE', 'S', 'WHITE', 0, TRUE, 42),
             ('42_M_WHITE', 'M', 'WHITE', 0, TRUE, 42),
             ('42_L_WHITE', 'L', 'WHITE', 10, TRUE, 42),
             ('42_S_BLUE', 'S', 'BLUE', 0, TRUE, 42),
             ('42_M_BLUE', 'M', 'BLUE', 10, TRUE, 42),
             ('42_L_BLUE', 'L', 'BLUE', 10, TRUE, 42),

             ('43_S_BLACK', 'S', 'BLACK', 0, TRUE, 43),
             ('43_M_BLACK', 'M', 'BLACK', 10, TRUE, 43),
             ('43_L_BLACK', 'L', 'BLACK', 10, TRUE, 43),
             ('43_S_WHITE', 'S', 'WHITE', 0, TRUE, 43),
             ('43_M_WHITE', 'M', 'WHITE', 0, TRUE, 43),
             ('43_L_WHITE', 'L', 'WHITE', 10, TRUE, 43),
             ('43_S_BLUE', 'S', 'BLUE', 0, TRUE, 43),
             ('43_M_BLUE', 'M', 'BLUE', 10, TRUE, 43),
             ('43_L_BLUE', 'L', 'BLUE', 10, TRUE, 43),

             ('44_S_BLACK', 'S', 'BLACK', 0, TRUE, 44),
             ('44_M_BLACK', 'M', 'BLACK', 10, TRUE, 44),
             ('44_L_BLACK', 'L', 'BLACK', 10, TRUE, 44),
             ('44_S_WHITE', 'S', 'WHITE', 0, TRUE, 44),
             ('44_M_WHITE', 'M', 'WHITE', 0, TRUE, 44),
             ('44_L_WHITE', 'L', 'WHITE', 10, TRUE, 44),
             ('44_S_BLUE', 'S', 'BLUE', 0, TRUE, 44),
             ('44_M_BLUE', 'M', 'BLUE', 10, TRUE, 44),
             ('44_L_BLUE', 'L', 'BLUE', 10, TRUE, 44),

             ('45_39_BLACK', '39', 'BLACK', 10, TRUE, 45),
             ('45_40_BLACK', '40', 'BLACK', 10, TRUE, 45),
             ('45_41_BLACK', '41', 'BLACK', 10, TRUE, 45),
             ('45_39_WHITE', '39', 'WHITE', 10, TRUE, 45),
             ('45_40_WHITE', '40', 'WHITE', 10, TRUE, 45),
             ('45_41_WHITE', '41', 'WHITE', 10, TRUE, 45),
             ('45_39_BLUE', '39', 'BLUE', 10, TRUE, 45),
             ('45_40_BLUE', '40', 'BLUE', 10, TRUE, 45),
             ('45_41_BLUE', '41', 'BLUE', 10, TRUE, 45),

             ('46_39_BLACK', '39', 'BLACK', 10, TRUE, 46),
             ('46_40_BLACK', '40', 'BLACK', 10, TRUE, 46),
             ('46_41_BLACK', '41', 'BLACK', 10, TRUE, 46),
             ('46_39_WHITE', '39', 'WHITE', 10, TRUE, 46),
             ('46_40_WHITE', '40', 'WHITE', 10, TRUE, 46),
             ('46_41_WHITE', '41', 'WHITE', 10, TRUE, 46),
             ('46_39_BLUE', '39', 'BLUE', 10, TRUE, 46),
             ('46_40_BLUE', '40', 'BLUE', 10, TRUE, 46),
             ('46_41_BLUE', '41', 'BLUE', 10, TRUE, 46),

             ('47_39_BLACK', '39', 'BLACK', 10, TRUE, 47),
             ('47_40_BLACK', '40', 'BLACK', 10, TRUE, 47),
             ('47_41_BLACK', '41', 'BLACK', 10, TRUE, 47),
             ('47_39_WHITE', '39', 'WHITE', 10, TRUE, 47),
             ('47_40_WHITE', '40', 'WHITE', 10, TRUE, 47),
             ('47_41_WHITE', '41', 'WHITE', 10, TRUE, 47),
             ('47_39_BLUE', '39', 'BLUE', 10, TRUE, 47),
             ('47_40_BLUE', '40', 'BLUE', 10, TRUE, 47),
             ('47_41_BLUE', '41', 'BLUE', 10, TRUE, 47),

             ('48_39_BLACK', '39', 'BLACK', 10, TRUE, 48),
             ('48_40_BLACK', '40', 'BLACK', 10, TRUE, 48),
             ('48_41_BLACK', '41', 'BLACK', 10, TRUE, 48),
             ('48_39_WHITE', '39', 'WHITE', 10, TRUE, 48),
             ('48_40_WHITE', '40', 'WHITE', 10, TRUE, 48),
             ('48_41_WHITE', '41', 'WHITE', 10, TRUE, 48),
             ('48_39_BLUE', '39', 'BLUE', 10, TRUE, 48),
             ('48_40_BLUE', '40', 'BLUE', 10, TRUE, 48),
             ('48_41_BLUE', '41', 'BLUE', 10, TRUE, 48),

             ('49_39_BLACK', '39', 'BLACK', 10, TRUE, 49),
             ('49_40_BLACK', '40', 'BLACK', 10, TRUE, 49),
             ('49_41_BLACK', '41', 'BLACK', 10, TRUE, 49),
             ('49_39_WHITE', '39', 'WHITE', 10, TRUE, 49),
             ('49_40_WHITE', '40', 'WHITE', 10, TRUE, 49),
             ('49_41_WHITE', '41', 'WHITE', 10, TRUE, 49),
             ('49_39_BLUE', '39', 'BLUE', 10, TRUE, 49),
             ('49_40_BLUE', '40', 'BLUE', 10, TRUE, 49),
             ('49_41_BLUE', '41', 'BLUE', 10, TRUE, 49),

             ('50_UNIVERSAL SIZE_BLACK', 'UNIVERSAL SIZE', 'BLACK', 10, TRUE, 50),
             ('50_UNIVERSAL SIZE_WHITE', 'UNIVERSAL SIZE', 'WHITE', 10, TRUE, 50),
             ('50_UNIVERSAL SIZE_BLUE', 'UNIVERSAL SIZE', 'BLUE', 10, TRUE, 50),

             ('51_UNIVERSAL SIZE_BLACK', 'UNIVERSAL SIZE', 'BLACK', 10, TRUE, 51),
             ('51_UNIVERSAL SIZE_WHITE', 'UNIVERSAL SIZE', 'WHITE', 10, TRUE, 51),
             ('51_UNIVERSAL SIZE_BLUE', 'UNIVERSAL SIZE', 'BLUE', 10, TRUE, 51),

             ('52_UNIVERSAL SIZE_BLACK', 'UNIVERSAL SIZE', 'BLACK', 10, TRUE, 52),
             ('52_UNIVERSAL SIZE_WHITE', 'UNIVERSAL SIZE', 'WHITE', 10, TRUE, 52),
             ('52_UNIVERSAL SIZE_BLUE', 'UNIVERSAL SIZE', 'BLUE', 10, TRUE, 52),

             ('53_UNIVERSAL SIZE_BLACK', 'UNIVERSAL SIZE', 'BLACK', 10, TRUE, 53),
             ('53_UNIVERSAL SIZE_WHITE', 'UNIVERSAL SIZE', 'WHITE', 10, TRUE, 53),
             ('53_UNIVERSAL SIZE_BLUE', 'UNIVERSAL SIZE', 'BLUE', 10, TRUE, 53),

             ('54_UNIVERSAL SIZE_BLACK', 'UNIVERSAL SIZE', 'BLACK', 10, TRUE, 54),
             ('54_UNIVERSAL SIZE_WHITE', 'UNIVERSAL SIZE', 'WHITE', 10, TRUE, 54),
             ('54_UNIVERSAL SIZE_BLUE', 'UNIVERSAL SIZE', 'BLUE', 10, TRUE, 54),

             ('55_UNIVERSAL SIZE_BLACK', 'UNIVERSAL SIZE', 'BLACK', 10, TRUE, 55),
             ('55_UNIVERSAL SIZE_WHITE', 'UNIVERSAL SIZE', 'WHITE', 10, TRUE, 55),
             ('55_UNIVERSAL SIZE_BLUE', 'UNIVERSAL SIZE', 'BLUE', 10, TRUE, 55),

             ('56_UNIVERSAL SIZE_BLACK', 'UNIVERSAL SIZE', 'BLACK', 10, TRUE, 56),
             ('56_UNIVERSAL SIZE_WHITE', 'UNIVERSAL SIZE', 'WHITE', 10, TRUE, 56),
             ('56_UNIVERSAL SIZE_BLUE', 'UNIVERSAL SIZE', 'BLUE', 10, TRUE, 56),

             ('57_S_BLACK', 'S', 'BLACK', 0, TRUE, 57),
             ('57_M_BLACK', 'M', 'BLACK', 10, TRUE, 57),
             ('57_L_BLACK', 'L', 'BLACK', 10, TRUE, 57),
             ('57_S_WHITE', 'S', 'WHITE', 0, TRUE, 57),
             ('57_M_WHITE', 'M', 'WHITE', 0, TRUE, 57),
             ('57_L_WHITE', 'L', 'WHITE', 10, TRUE, 57),
             ('57_S_BLUE', 'S', 'BLUE', 0, TRUE, 57),
             ('57_M_BLUE', 'M', 'BLUE', 10, TRUE, 57),
             ('57_L_BLUE', 'L', 'BLUE', 10, TRUE, 57),

             ('58_S_BLACK', 'S', 'BLACK', 0, TRUE, 58),
             ('58_M_BLACK', 'M', 'BLACK', 10, TRUE, 58),
             ('58_L_BLACK', 'L', 'BLACK', 10, TRUE, 58),
             ('58_S_WHITE', 'S', 'WHITE', 0, TRUE, 58),
             ('58_M_WHITE', 'M', 'WHITE', 0, TRUE, 58),
             ('58_L_WHITE', 'L', 'WHITE', 10, TRUE, 58),
             ('58_S_BLUE', 'S', 'BLUE', 0, TRUE, 58),
             ('58_M_BLUE', 'M', 'BLUE', 10, TRUE, 58),
             ('58_L_BLUE', 'L', 'BLUE', 10, TRUE, 58),

             ('59_S_BLACK', 'S', 'BLACK', 0, TRUE, 59),
             ('59_M_BLACK', 'M', 'BLACK', 10, TRUE, 59),
             ('59_L_BLACK', 'L', 'BLACK', 10, TRUE, 59),
             ('59_S_WHITE', 'S', 'WHITE', 0, TRUE, 59),
             ('59_M_WHITE', 'M', 'WHITE', 0, TRUE, 59),
             ('59_L_WHITE', 'L', 'WHITE', 10, TRUE, 59),
             ('59_S_BLUE', 'S', 'BLUE', 0, TRUE, 59),
             ('59_M_BLUE', 'M', 'BLUE', 10, TRUE, 59),
             ('59_L_BLUE', 'L', 'BLUE', 10, TRUE, 59),

             ('60_S_BLACK', 'S', 'BLACK', 0, FALSE, 60),
             ('60_M_BLACK', 'M', 'BLACK', 10, FALSE, 60),
             ('60_L_BLACK', 'L', 'BLACK', 10, FALSE, 60),
             ('60_S_WHITE', 'S', 'WHITE', 0, FALSE, 60),
             ('60_M_WHITE', 'M', 'WHITE', 0, FALSE, 60),
             ('60_L_WHITE', 'L', 'WHITE', 10, FALSE, 60),
             ('60_S_BLUE', 'S', 'BLUE', 0, FALSE, 60),
             ('60_M_BLUE', 'M', 'BLUE', 10, FALSE, 60),
             ('60_L_BLUE', 'L', 'BLUE', 10, FALSE, 60),

             ('61_S_BLACK', 'S', 'BLACK', 0, FALSE, 61),
             ('61_M_BLACK', 'M', 'BLACK', 10, FALSE, 61),
             ('61_L_BLACK', 'L', 'BLACK', 10, FALSE, 61),
             ('61_S_WHITE', 'S', 'WHITE', 0, FALSE, 61),
             ('61_M_WHITE', 'M', 'WHITE', 0, FALSE, 61),
             ('61_L_WHITE', 'L', 'WHITE', 10, FALSE, 61),
             ('61_S_BLUE', 'S', 'BLUE', 0, FALSE, 61),
             ('61_M_BLUE', 'M', 'BLUE', 10, FALSE, 61),
             ('61_L_BLUE', 'L', 'BLUE', 10, FALSE, 61)

     ) AS v(name, size, color, stock, active, product_id)
WHERE NOT EXISTS (SELECT 1 FROM product_variant);


INSERT INTO cart (user_id)
SELECT *
FROM (
         VALUES
             (1),
             (2),
             (3),  -- cart with many items
             (4),  -- empty cart
             (5)   -- cart with one item
     ) AS v(user_id)
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
             (3,2,88,119.99, 359.97),
             (1, 3, 1,  79.99,  79.99),   -- Classic Denim Jacket S BLACK
             (2, 3, 10, 149.99, 299.98),  -- Black Leather Jacket S BLACK
             (1, 3, 28, 29.99,  29.99),   -- Casual Polo Shirt S BLACK
             (3, 3, 37, 19.99,  59.97),   -- White T-Shirt S BLACK
             (1, 3, 55, 49.99,  49.99),   -- Chino Pants S BLUE
             (1, 5, 73, 64.99,  64.99)    -- Cargo Pants S BLUE
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