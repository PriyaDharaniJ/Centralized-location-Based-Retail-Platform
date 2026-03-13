-- ============================================================
-- CENTRALIZED LOCATION-BASED RETAIL PLATFORM - DATABASE SCHEMA
-- Dindigul District, Tamil Nadu | PostgreSQL
-- ============================================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS admins (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15),
    address TEXT,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    profile_image VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS retail_owners (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    shop_name VARCHAR(200) NOT NULL,
    shop_address TEXT NOT NULL,
    shop_category VARCHAR(100) NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    shop_image VARCHAR(500),
    gstin VARCHAR(20),
    is_open BOOLEAN DEFAULT FALSE,
    delivery_available BOOLEAN DEFAULT FALSE,
    is_approved BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    rating DECIMAL(3,2) DEFAULT 0.00,
    total_orders INT DEFAULT 0,
    total_revenue DECIMAL(15,2) DEFAULT 0.00,
    commission_rate DECIMAL(5,2) DEFAULT 5.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    icon VARCHAR(100),
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    retail_owner_id BIGINT REFERENCES retail_owners(id) ON DELETE CASCADE,
    category_id BIGINT REFERENCES categories(id),
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    original_price DECIMAL(10,2),
    quantity INT NOT NULL DEFAULT 0,
    unit VARCHAR(50) DEFAULT 'piece',
    brand VARCHAR(100),
    sku VARCHAR(100),
    image_url VARCHAR(500),
    is_available BOOLEAN DEFAULT TRUE,
    pickup_available BOOLEAN DEFAULT TRUE,
    delivery_available BOOLEAN DEFAULT TRUE,
    rating DECIMAL(3,2) DEFAULT 0.00,
    review_count INT DEFAULT 0,
    sold_count INT DEFAULT 0,
    discount_percent DECIMAL(5,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cart (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES products(id) ON DELETE CASCADE,
    quantity INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, product_id)
);

CREATE TABLE IF NOT EXISTS wishlist (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES products(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, product_id)
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    tracking_id VARCHAR(20) UNIQUE NOT NULL,
    user_id BIGINT REFERENCES users(id),
    retail_owner_id BIGINT REFERENCES retail_owners(id),
    order_type VARCHAR(20) NOT NULL CHECK (order_type IN ('DELIVERY', 'PICKUP')),
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING','ACCEPTED','REJECTED','READY_TO_PICK','OUT_FOR_DELIVERY','DELIVERED','CANCELLED')),
    subtotal DECIMAL(10,2) NOT NULL,
    delivery_charge DECIMAL(10,2) DEFAULT 0.00,
    discount DECIMAL(10,2) DEFAULT 0.00,
    total_amount DECIMAL(10,2) NOT NULL,
    commission_amount DECIMAL(10,2) DEFAULT 0.00,
    payment_method VARCHAR(30) DEFAULT 'COD',
    payment_status VARCHAR(20) DEFAULT 'PENDING',
    delivery_address TEXT,
    delivery_latitude DECIMAL(10,8),
    delivery_longitude DECIMAL(11,8),
    customer_phone VARCHAR(15),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES products(id),
    product_name VARCHAR(200) NOT NULL,
    product_image VARCHAR(500),
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL PRIMARY KEY,
    recipient_type VARCHAR(20) NOT NULL CHECK (recipient_type IN ('USER','RETAILER','ADMIN')),
    recipient_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(50),
    reference_id BIGINT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS reviews (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    product_id BIGINT REFERENCES products(id),
    order_id BIGINT REFERENCES orders(id),
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_products_retail_owner ON products(retail_owner_id);
CREATE INDEX IF NOT EXISTS idx_orders_user ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_orders_retailer ON orders(retail_owner_id);
CREATE INDEX IF NOT EXISTS idx_orders_tracking ON orders(tracking_id);
CREATE INDEX IF NOT EXISTS idx_notifications_recipient ON notifications(recipient_type, recipient_id);
CREATE INDEX IF NOT EXISTS idx_retail_location ON retail_owners(latitude, longitude);

-- Default Admin (password: admin123)
INSERT INTO admins (name, email, password, phone) VALUES
('Super Admin', 'admin@retailplatform.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTqY0XZUHS2', '9876543210')
ON CONFLICT DO NOTHING;

-- Categories
INSERT INTO categories (name, icon, description) VALUES
('Grocery', '🛒', 'Daily groceries and food items'),
('Electronics', '📱', 'Electronic gadgets and accessories'),
('Sports', '🏏', 'Sports equipment and accessories'),
('Medical', '💊', 'Medicines and health products'),
('Stationery', '📚', 'Books, pens and office supplies'),
('Hardware', '🔧', 'Tools and hardware items'),
('Clothing', '👕', 'Clothes and fashion accessories'),
('Food & Beverages', '🍕', 'Restaurants and food shops')
ON CONFLICT DO NOTHING;

-- Sample Retail Owners (password: admin123)
INSERT INTO retail_owners (name, email, password, phone, shop_name, shop_address, shop_category, latitude, longitude, is_open, delivery_available, is_approved, rating, commission_rate)
VALUES
('Rajan Kumar', 'rajan@demo.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTqY0XZUHS2', '9876543001',
 'Super Sports Dindigul', 'Palani Road, Dindigul, Tamil Nadu 624001', 'Sports',
 10.3673, 77.9803, TRUE, TRUE, TRUE, 4.5, 5.00),
('Priya Stores', 'priya@demo.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTqY0XZUHS2', '9876543002',
 'Priya Grocery Mart', 'Gandhi Nagar, Dindigul, Tamil Nadu 624001', 'Grocery',
 10.3710, 77.9750, TRUE, FALSE, TRUE, 4.2, 5.00)
ON CONFLICT DO NOTHING;

-- Sample User (password: admin123)
INSERT INTO users (name, email, password, phone, address, latitude, longitude)
VALUES ('Test User', 'user@demo.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTqY0XZUHS2', '9876543100',
        'Dindigul Town, Tamil Nadu', 10.3673, 77.9803)
ON CONFLICT DO NOTHING;

-- Sample Products
INSERT INTO products (retail_owner_id, category_id, name, description, price, original_price, quantity, brand, is_available, pickup_available, delivery_available, discount_percent, rating, sold_count)
VALUES
(1, 3, 'SS Cricket Bat', 'Professional grade cricket bat for all formats', 1800.00, 2200.00, 15, 'SS', TRUE, TRUE, TRUE, 18.18, 4.5, 32),
(1, 3, 'SG Cricket Ball', 'Leather cricket ball - set of 3', 450.00, 500.00, 50, 'SG', TRUE, TRUE, TRUE, 10.00, 4.3, 78),
(1, 3, 'Badminton Racket', 'Yonex badminton racket lightweight carbon', 1200.00, 1500.00, 20, 'Yonex', TRUE, TRUE, TRUE, 20.00, 4.6, 45),
(1, 3, 'Football', 'FIFA approved football size 5', 900.00, 1000.00, 30, 'Nike', TRUE, TRUE, TRUE, 10.00, 4.4, 23),
(2, 1, 'Basmati Rice 5kg', 'Premium aged basmati rice 5kg pack', 380.00, 420.00, 100, 'India Gate', TRUE, TRUE, FALSE, 9.52, 4.7, 150),
(2, 1, 'Toor Dal 1kg', 'Premium quality toor dal 1kg', 120.00, 140.00, 200, 'Local Brand', TRUE, TRUE, FALSE, 14.29, 4.2, 200),
(2, 1, 'Sunflower Oil 1L', 'Refined sunflower oil 1 litre', 145.00, 160.00, 80, 'Fortune', TRUE, TRUE, FALSE, 9.38, 4.0, 90)
ON CONFLICT DO NOTHING;
