
SET FOREIGN_KEY_CHECKS = 0;

-- =====================================================================
-- ROLE (10)
-- =====================================================================
INSERT IGNORE INTO role (id, name)
VALUES
    (1, 'ADMIN'),
    (2, 'STAFF'),
    (3, 'CUSTOMER');


-- =====================================================================
-- USER (27) — 1 admin, 2 manager, 12 staff (EMPLOYEE), 12 customer
-- =====================================================================
INSERT IGNORE INTO user
(id, email, phone, password_hash, role_id, is_active, created_at)
VALUES
    (1, 'admin@gmail.com',     '0900000001', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 1, true,  DATE_SUB(NOW(), INTERVAL 400 DAY)),
    (2, 'manager1@gmail.com',  '0900000002', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true,  DATE_SUB(NOW(), INTERVAL 350 DAY)),
    (3, 'manager2@gmail.com',  '0900000003', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, false, DATE_SUB(NOW(), INTERVAL 340 DAY)),
    (4,  'staff1@gmail.com',  '0900001001', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 200 DAY)),
    (5,  'staff2@gmail.com',  '0900001002', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 195 DAY)),
    (6,  'staff3@gmail.com',  '0900001003', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 190 DAY)),
    (7,  'staff4@gmail.com',  '0900001004', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 185 DAY)),
    (8,  'staff5@gmail.com',  '0900001005', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 180 DAY)),
    (9,  'staff6@gmail.com',  '0900001006', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 175 DAY)),
    (10, 'staff7@gmail.com',  '0900001007', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 170 DAY)),
    (11, 'staff8@gmail.com',  '0900001008', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 165 DAY)),
    (12, 'staff9@gmail.com',  '0900001009', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 160 DAY)),
    (13, 'staff10@gmail.com', '0900001010', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 155 DAY)),
    (14, 'staff11@gmail.com', '0900001011', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 150 DAY)),
    (15, 'staff12@gmail.com', '0900001012', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 145 DAY)),
    (16, 'customer1@gmail.com',  '0900002001', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 120 DAY)),
    (17, 'customer2@gmail.com',  '0900002002', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 110 DAY)),
    (18, 'customer3@gmail.com',  '0900002003', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 100 DAY)),
    (19, 'customer4@gmail.com',  '0900002004', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 90 DAY)),
    (20, 'customer5@gmail.com',  '0900002005', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 80 DAY)),
    (21, 'customer6@gmail.com',  '0900002006', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 70 DAY)),
    (22, 'customer7@gmail.com',  '0900002007', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 60 DAY)),
    (23, 'customer8@gmail.com',  '0900002008', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 50 DAY)),
    (24, 'customer9@gmail.com',  '0900002009', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 40 DAY)),
    (25, 'customer10@gmail.com', '0900002010', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 30 DAY)),
    (26, 'customer11@gmail.com', '0900002011', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 20 DAY)),
    (27, 'customer12@gmail.com', '0900002012', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 10 DAY));

-- =====================================================================
-- REFRESH TOKEN (10)
-- =====================================================================
INSERT IGNORE INTO refresh_token
(id, user_id, token, expiry_date, created_at)
VALUES
    (1,  1,  'rt-admin-0000000000000001',  DATE_ADD(NOW(), INTERVAL 30 DAY), NOW()),
    (2,  2,  'rt-manager-000000000000002', DATE_ADD(NOW(), INTERVAL 30 DAY), NOW()),
    (3,  4,  'rt-staff-0000000000000003',  DATE_ADD(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (4,  5,  'rt-staff-0000000000000004',  DATE_ADD(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (5,  16, 'rt-cust-00000000000000005',  DATE_ADD(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
    (6,  17, 'rt-cust-00000000000000006',  DATE_ADD(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
    (7,  18, 'rt-cust-00000000000000007',  DATE_ADD(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY)),
    (8,  19, 'rt-cust-00000000000000008',  DATE_ADD(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY)),
    (9,  20, 'rt-cust-00000000000000009',  DATE_SUB(NOW(), INTERVAL 5 DAY),  DATE_SUB(NOW(), INTERVAL 35 DAY)),
    (10, 21, 'rt-cust-00000000000000010',  DATE_SUB(NOW(), INTERVAL 3 DAY),  DATE_SUB(NOW(), INTERVAL 33 DAY));

-- =====================================================================
-- PROVINCE (10)
-- =====================================================================
INSERT IGNORE INTO province (id, province_name)
VALUES
    (1, 'Tien Giang'),
    (2, 'Ho Chi Minh'),
    (3, 'Ha Noi'),
    (4, 'Da Nang'),
    (5, 'Can Tho'),
    (6, 'Binh Duong'),
    (7, 'Dong Nai'),
    (8, 'Khanh Hoa'),
    (9, 'Hai Phong'),
    (10, 'Lam Dong');

-- =====================================================================
-- COMMUNE (12)
-- =====================================================================
INSERT IGNORE INTO commune (id, commune_name, province_id)
VALUES
    (1,  'My Tho',    1),
    (2,  'Thu Duc',   2),
    (3,  'Cau Giay',  3),
    (4,  'Hai Chau',  4),
    (5,  'Ninh Kieu', 5),
    (6,  'Thuan An',  6),
    (7,  'Bien Hoa',  7),
    (8,  'Nha Trang', 8),
    (9,  'Hong Bang', 9),
    (10, 'Da Lat',    10),
    (11, 'Go Vap',    2),
    (12, 'Ba Dinh',   3);

-- =====================================================================
-- STATION (10)
-- =====================================================================
INSERT IGNORE INTO station
(id, station_name, address, commune_id, is_operating, max_wash_capacity, is_deleted)
VALUES
    (1,  'AutoWash My Tho',       '123 Ap Bac, My Tho',              1,  true,  20, false),
    (2,  'AutoWash HCM Thu Duc',  '456 Vo Van Ngan, Thu Duc, HCM',   2,  true,  30, false),
    (3,  'AutoWash Ha Noi',       '12 Cau Giay, Ha Noi',             3,  true,  25, false),
    (4,  'AutoWash Da Nang',      '78 Nguyen Van Linh, Da Nang',     4,  true,  20, false),
    (5,  'AutoWash Can Tho',      '34 Mau Than, Ninh Kieu, Can Tho', 5,  true,  18, false),
    (6,  'AutoWash Binh Duong',   '56 Le Hong Phong, Thuan An',      6,  true,  22, false),
    (7,  'AutoWash Dong Nai',     '9 Pham Van Thuan, Bien Hoa',      7,  true,  20, false),
    (8,  'AutoWash Nha Trang',    '21 Tran Phu, Nha Trang',          8,  true,  15, false),
    (9,  'AutoWash Hai Phong',    '88 Lach Tray, Hong Bang',         9,  true,  18, false),
    (10, 'AutoWash Da Lat',       '5 Tran Phu, Da Lat',              10, false, 12, false);

-- =====================================================================
-- WASH LANE (15)
-- =====================================================================
INSERT IGNORE INTO wash_lane
(id, station_id, lane_name, status, booking_walkin_ratio, is_deleted)
VALUES
    (1,  1,  'Lane 1', 'ACTIVE',   3, false),
    (2,  1,  'Lane 2', 'ACTIVE',   3, false),
    (3,  2,  'Lane 1', 'ACTIVE',   3, false),
    (4,  2,  'Lane 2', 'ACTIVE',   3, false),
    (5,  2,  'Lane 3', 'INACTIVE', 3, false),
    (6,  3,  'Lane 1', 'ACTIVE',   3, false),
    (7,  3,  'Lane 2', 'ACTIVE',   2, false),
    (8,  4,  'Lane 1', 'ACTIVE',   3, false),
    (9,  5,  'Lane 1', 'ACTIVE',   3, false),
    (10, 6,  'Lane 1', 'ACTIVE',   3, false),
    (11, 6,  'Lane 2', 'ACTIVE',   4, false),
    (12, 7,  'Lane 1', 'ACTIVE',   3, false),
    (13, 8,  'Lane 1', 'ACTIVE',   3, false),
    (14, 9,  'Lane 1', 'ACTIVE',   3, false),
    (15, 10, 'Lane 1', 'INACTIVE', 3, false);

-- =====================================================================
-- STAFF (12)
-- =====================================================================
INSERT IGNORE INTO staff
(id, user_id, station_id, first_name, last_name)
VALUES
    (1,  4,  1,  'An',    'Nguyen'),
    (2,  5,  1,  'Binh',  'Tran'),
    (3,  6,  2,  'Chi',   'Le'),
    (4,  7,  2,  'Dung',  'Pham'),
    (5,  8,  3,  'Em',    'Vo'),
    (6,  9,  4,  'Phuc',  'Dang'),
    (7,  10, 5,  'Giang', 'Bui'),
    (8,  11, 6,  'Hieu',  'Do'),
    (9,  12, 7,  'Ich',   'Ho'),
    (10, 13, 8,  'Khang', 'Ngo'),
    (11, 14, 9,  'Loan',  'Ly'),
    (12, 15, 10, 'Minh',  'Vu');

-- =====================================================================
-- CUSTOMER TIER (10)
-- =====================================================================
INSERT IGNORE INTO customer_tier
(id, tier_name, min_points, booking_window_days, point_multiple, retention_target_amount)
VALUES
    (1,  'BRONZE',    0,     3,  1.0, 0),
    (2,  'SILVER',    500,   7,  1.2, 1500000),
    (3,  'GOLD',      1000,  14, 1.5, 3000000),
    (4,  'PLATINUM',  2000,  21, 1.8, 5000000),
    (5,  'DIAMOND',   3500,  30, 2.0, 7000000),
    (6,  'TITANIUM',  5000,  30, 2.2, 9000000),
    (7,  'RUBY',      7000,  45, 2.5, 12000000),
    (8,  'SAPPHIRE',  9000,  45, 2.8, 15000000),
    (9,  'EMERALD',   12000, 60, 3.0, 20000000),
    (10, 'LEGEND',    15000, 60, 3.5, 25000000);

-- =====================================================================
-- TIER BENEFIT (12)
-- =====================================================================
INSERT IGNORE INTO tier_benefit
(id, customer_tier_id, benefit_description)
VALUES
    (1,  1,  'Tich diem co ban x1 moi luot rua xe'),
    (2,  2,  'Uu tien dat lich truoc 7 ngay'),
    (3,  3,  'Giam 5% phi dich vu addon'),
    (4,  3,  'Tich diem x1.5 moi luot'),
    (5,  4,  'Mien phi 1 luot danh bong moi quy'),
    (6,  5,  'Ho tro duong day nong rieng'),
    (7,  6,  'Uu dai dac biet vao sinh nhat'),
    (8,  7,  'Mien phi doi xe trong gia dinh khong gioi han'),
    (9,  8,  'Uu tien lan rua VIP'),
    (10, 9,  'Tang 1 goi Premium Wash moi nam'),
    (11, 10, 'Quan ly cham soc khach hang rieng'),
    (12, 2,  'Email thong bao khuyen mai som');

-- =====================================================================
-- CUSTOMER (12)
-- =====================================================================
INSERT IGNORE INTO customer
(id, user_id, first_name, last_name, birthday, customer_tier_id, violation_count, restricted_until)
VALUES
    (1,  16, 'Phong', 'Huynh', '1990-01-15', 1,  0, NULL),
    (2,  17, 'Nam',   'Tran',  '1988-03-22', 2,  0, NULL),
    (3,  18, 'Linh',  'Le',    '1995-07-09', 3,  0, NULL),
    (4,  19, 'Mai',   'Nguyen','1992-11-30', 4,  0, NULL),
    (5,  20, 'Hoa',   'Pham',  '1985-05-18', 5,  1, NULL),
    (6,  21, 'Duc',   'Vo',    '1998-09-02', 6,  0, NULL),
    (7,  22, 'Thao',  'Dang',  '1993-12-25', 7,  0, NULL),
    (8,  23, 'Quang', 'Bui',   '1991-02-14', 8,  0, NULL),
    (9,  24, 'Yen',   'Do',    '1996-06-08', 9,  0, NULL),
    (10, 25, 'Khanh', 'Ho',    '1989-08-19', 10, 0, NULL),
    (11, 26, 'Trang', 'Ngo',   '1994-04-27', 2,  0, NULL),
    (12, 27, 'Hung',  'Ly',    '1997-10-11', 3,  0, NULL);

-- =====================================================================
-- VEHICLE (15)
-- =====================================================================
INSERT IGNORE INTO vehicle
(id, customer_id, license_plate, brand_name, color, violation_count, restricted_until, is_deleted)
VALUES
    (1,  1,  '51A-11111', 'Toyota',   'White',  0, NULL, false),
    (2,  2,  '51B-22222', 'Honda',    'Blue',   0, NULL, false),
    (3,  3,  '51C-33333', 'Mazda',    'Black',  0, NULL, false),
    (4,  4,  '30A-44444', 'Ford',     'Red',    0, NULL, false),
    (5,  5,  '30B-55555', 'Kia',      'Silver', 1, DATE_ADD(NOW(), INTERVAL 5 DAY), false),
    (6,  6,  '43A-66666', 'Hyundai',  'Grey',   0, NULL, false),
    (7,  7,  '43B-77777', 'VinFast',  'White',  0, NULL, false),
    (8,  8,  '92A-88888', 'Suzuki',   'Blue',   0, NULL, false),
    (9,  9,  '92B-99999', 'Mitsubishi','Black', 0, NULL, false),
    (10, 10, '65A-10101', 'Nissan',   'Red',    0, NULL, false),
    (11, 11, '15A-11211', 'Audi',     'Black',  0, NULL, false),
    (12, 12, '16A-12121', 'Mercedes', 'White',  0, NULL, false),
    (13, 1,  '51A-13131', 'Honda',    'Green',  0, NULL, false),
    (14, 2,  '51B-14141', 'Toyota',   'Yellow', 0, NULL, false),
    (15, 3,  '51C-15151', 'Kia',      'Orange', 0, NULL, false);

-- =====================================================================
-- FAMILY GROUP (10)
-- =====================================================================
INSERT IGNORE INTO family_group
(id, group_name, owner_customer_id, created_at, is_deleted)
VALUES
    (1,  'Gia dinh Huynh', 1,  DATE_SUB(NOW(), INTERVAL 100 DAY), false),
    (2,  'Gia dinh Tran',  2,  DATE_SUB(NOW(), INTERVAL 95 DAY),  false),
    (3,  'Gia dinh Le',    3,  DATE_SUB(NOW(), INTERVAL 90 DAY),  false),
    (4,  'Gia dinh Nguyen',4,  DATE_SUB(NOW(), INTERVAL 85 DAY),  false),
    (5,  'Gia dinh Pham',  5,  DATE_SUB(NOW(), INTERVAL 80 DAY),  false),
    (6,  'Gia dinh Vo',    6,  DATE_SUB(NOW(), INTERVAL 75 DAY),  false),
    (7,  'Gia dinh Dang',  7,  DATE_SUB(NOW(), INTERVAL 70 DAY),  false),
    (8,  'Gia dinh Bui',   8,  DATE_SUB(NOW(), INTERVAL 65 DAY),  false),
    (9,  'Gia dinh Do',    9,  DATE_SUB(NOW(), INTERVAL 60 DAY),  false),
    (10, 'Gia dinh Ho',    10, DATE_SUB(NOW(), INTERVAL 55 DAY),  false);

-- =====================================================================
-- FAMILY MEMBER (12)
-- =====================================================================
INSERT IGNORE INTO family_member
(id, family_group_id, customer_id, vehicle_id, vehicle_change_count, vehicle_change_window_start)
VALUES
    (1,  1,  1,  1,  0, NULL),
    (2,  2,  2,  2,  1, DATE_SUB(NOW(), INTERVAL 10 DAY)),
    (3,  3,  3,  3,  0, NULL),
    (4,  4,  4,  4,  0, NULL),
    (5,  5,  5,  5,  2, DATE_SUB(NOW(), INTERVAL 15 DAY)),
    (6,  6,  6,  6,  0, NULL),
    (7,  7,  7,  7,  0, NULL),
    (8,  8,  8,  8,  0, NULL),
    (9,  9,  9,  9,  0, NULL),
    (10, 10, 10, 10, 0, NULL),
    (11, 1,  11, 11, 0, NULL),
    (12, 2,  12, 12, 0, NULL);

-- =====================================================================
-- TIER RETENTION (12)
-- =====================================================================
INSERT IGNORE INTO tier_retention
(id, customer_id, customer_tier_id, target_amount, current_amount, start_date, end_date, status, evaluated_at)
VALUES
    (1,  1,  1,  1500000, 600000,  DATE_SUB(CURDATE(), INTERVAL 60 DAY), DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'IN_PROGRESS', NULL),
    (2,  2,  2,  3000000, 1200000, DATE_SUB(CURDATE(), INTERVAL 60 DAY), DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'IN_PROGRESS', NULL),
    (3,  3,  3,  5000000, 2000000, DATE_SUB(CURDATE(), INTERVAL 60 DAY), DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'IN_PROGRESS', NULL),
    (4,  4,  4,  7000000, 3500000, DATE_SUB(CURDATE(), INTERVAL 60 DAY), DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'IN_PROGRESS', NULL),
    (5,  5,  5,  9000000, 4000000, DATE_SUB(CURDATE(), INTERVAL 60 DAY), DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'IN_PROGRESS', NULL),
    (6,  6,  6,  9000000, 5000000, DATE_SUB(CURDATE(), INTERVAL 60 DAY), DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'IN_PROGRESS', NULL),
    (7,  7,  7,  12000000, 12500000, DATE_SUB(CURDATE(), INTERVAL 90 DAY), DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'ACHIEVED', DATE_SUB(NOW(), INTERVAL 5 DAY)),
    (8,  8,  8,  15000000, 15200000, DATE_SUB(CURDATE(), INTERVAL 90 DAY), DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'ACHIEVED', DATE_SUB(NOW(), INTERVAL 5 DAY)),
    (9,  9,  9,  20000000, 20500000, DATE_SUB(CURDATE(), INTERVAL 90 DAY), DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'ACHIEVED', DATE_SUB(NOW(), INTERVAL 5 DAY)),
    (10, 10, 10, 25000000, 9000000,  DATE_SUB(CURDATE(), INTERVAL 90 DAY), DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'FAILED',   DATE_SUB(NOW(), INTERVAL 5 DAY)),
    (11, 11, 2,  3000000, 800000,   DATE_SUB(CURDATE(), INTERVAL 90 DAY), DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'FAILED',   DATE_SUB(NOW(), INTERVAL 5 DAY)),
    (12, 12, 3,  5000000, 1000000,  DATE_SUB(CURDATE(), INTERVAL 90 DAY), DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'FAILED',   DATE_SUB(NOW(), INTERVAL 5 DAY));

-- =====================================================================
-- LOYALTY POINT BALANCE (12) — one row per customer
-- =====================================================================
INSERT IGNORE INTO loyalty_point_balance
(customer_id, total_points, accumulated_points)
VALUES
    (1,  320,  820),
    (2,  450,  950),
    (3,  610,  1310),
    (4,  280,  680),
    (5,  900,  2100),
    (6,  150,  350),
    (7,  1200, 2600),
    (8,  980,  2300),
    (9,  1500, 3200),
    (10, 75,   175),
    (11, 200,  500),
    (12, 340,  840);

-- =====================================================================
-- SERVICE CATEGORY (10)
-- =====================================================================
INSERT IGNORE INTO service_category
(id, category_name, description)
VALUES
    (1,  'Basic Wash',         'Rua xe co ban ben ngoai'),
    (2,  'Premium Wash',       'Rua xe cao cap, lam sach toan dien'),
    (3,  'Interior Detailing', 'Lam sach va cham soc noi that'),
    (4,  'Exterior Detailing', 'Cham soc chi tiet ben ngoai xe'),
    (5,  'Engine Wash',        've sinh khoang dong co'),
    (6,  'Undercarriage Wash', 've sinh gam xe'),
    (7,  'Wax & Polish',       'Danh bong va phu sap bao ve son'),
    (8,  'Ceramic Coating',    'Phu ceramic bao ve lau dai'),
    (9,  'Pet Care Wash',      've sinh khu mui long thu cung'),
    (10, 'Express Wash',       'Rua xe nhanh trong 15 phut');

-- =====================================================================
-- ADDON SERVICE (12)
-- =====================================================================
INSERT IGNORE INTO addon_service
(id, name, price, duration_minutes, service_category_id, is_deleted)
VALUES
    (1,  'Vacuum',                 50000,  20, 1, false),
    (2,  'Polish',                 150000, 40, 7, false),
    (3,  'Wax Coating',            180000, 30, 7, false),
    (4,  'Tire Shine',             40000,  10, 4, false),
    (5,  'Interior Shampoo',       120000, 35, 3, false),
    (6,  'Engine Degrease',        100000, 25, 5, false),
    (7,  'Pet Hair Removal',       90000,  20, 9, false),
    (8,  'Air Freshener',          20000,  5,  1, false),
    (9,  'Headlight Restoration',  130000, 30, 4, false),
    (10, 'Ceramic Spray',          220000, 30, 8, false),
    (11, 'Leather Conditioning',   140000, 25, 3, false),
    (12, 'Underbody Rust-proofing',160000, 30, 6, false);

-- =====================================================================
-- SERVICE PACKAGE (12) — required_slot = number of 15-minute slots
-- =====================================================================
INSERT IGNORE INTO service_package
(id, service_category_id, name, base_price, description, required_slot, is_deleted)
VALUES
    (1,  1,  'Normal Wash',                  100000, 'Rua xe co ban ben ngoai',          1, false),
    (2,  2,  'Premium Wash',                 300000, 'Rua xe cao cap toan dien',         2, false),
    (3,  3,  'Interior Detailing Standard',  250000, 'Lam sach noi that co ban',         2, false),
    (4,  3,  'Interior Detailing Deep',      400000, 'Lam sach noi that chuyen sau',     3, false),
    (5,  4,  'Exterior Detailing',           220000, 'Cham soc chi tiet ben ngoai',      2, false),
    (6,  5,  'Engine Wash',                  150000, 've sinh khoang dong co',           1, false),
    (7,  6,  'Undercarriage Wash',           120000, 've sinh gam xe',                   1, false),
    (8,  7,  'Wax & Polish',                 280000, 'Danh bong va phu sap',             2, false),
    (9,  8,  'Ceramic Coating',              600000, 'Phu ceramic bao ve lau dai',       4, false),
    (10, 9,  'Pet Care Wash',                180000, 've sinh khu mui long thu cung',    2, false),
    (11, 10, 'Express Wash',                 70000,  'Rua xe nhanh',                     1, false),
    (12, 2,  'Premium Wash Deluxe',          350000, 'Premium Wash kem uu dai',          2, false);

-- =====================================================================
-- PACKAGE ADDON MAPPING (15)
-- =====================================================================
INSERT IGNORE INTO package_addon_mapping
(service_package_id, addon_service_id)
VALUES
    (1, 1), (1, 8),
    (2, 2), (2, 3),
    (3, 5), (3, 11),
    (4, 5),
    (5, 4), (5, 9),
    (6, 6),
    (7, 12),
    (8, 2),
    (9, 3),
    (10, 7),
    (12, 3);

-- =====================================================================
-- SUBSCRIPTION PLAN (10)
-- =====================================================================
INSERT IGNORE INTO subscription_plan
(id, service_package_id, service_category_id, plan_name, duration_days, price, plan_type, max_vehicle_count, description, is_deleted)
VALUES
    (1,  1, 1, 'Unlimited Basic Monthly',     30,  500000,   'UNLIMITED', 1, 'Rua xe khong gioi han trong 1 thang', false),
    (2,  2, 2, 'Unlimited Premium Monthly',   30,  900000,   'UNLIMITED', 1, 'Rua xe cao cap khong gioi han trong 1 thang', false),
    (3,  1, 1, 'Unlimited Basic Quarterly',   90,  1350000,  'UNLIMITED', 1, 'Rua xe khong gioi han trong 3 thang', false),
    (4,  2, 2, 'Unlimited Premium Quarterly', 90,  2400000,  'UNLIMITED', 1, 'Rua xe cao cap khong gioi han trong 3 thang', false),
    (5,  2, 2, 'Unlimited Premium Yearly',    365, 8500000,  'UNLIMITED', 1, 'Rua xe cao cap khong gioi han trong 1 nam', false),
    (6,  1, 1, 'Family Basic Monthly',        30,  1200000,  'FAMILY',    3, 'Rua xe khong gioi han cho ca gia dinh, 1 thang', false),
    (7,  2, 2, 'Family Premium Monthly',      30,  2000000,  'FAMILY',    3, 'Rua xe cao cap cho ca gia dinh, 1 thang', false),
    (8,  1, 1, 'Family Basic Quarterly',      90,  3200000,  'FAMILY',    4, 'Rua xe khong gioi han cho ca gia dinh, 3 thang', false),
    (9,  2, 2, 'Family Premium Quarterly',    90,  5400000,  'FAMILY',    4, 'Rua xe cao cap cho ca gia dinh, 3 thang', false),
    (10, 2, 2, 'Family Premium Yearly',       365, 18000000, 'FAMILY',    5, 'Rua xe cao cap cho ca gia dinh, 1 nam', false);

-- =====================================================================
-- UNLIMIT SUBSCRIPTION (10)
-- =====================================================================
INSERT IGNORE INTO unlimit_subscription
(id, customer_id, vehicle_id, subscription_plan_id, last_vehicle_change_at, start_date, end_date, status, canceled_at)
VALUES
    (1,  1,  1,  1, NULL, DATE_SUB(CURDATE(), INTERVAL 10 DAY),  DATE_ADD(CURDATE(), INTERVAL 20 DAY),  'ACTIVE',    NULL),
    (2,  2,  2,  2, NULL, DATE_SUB(CURDATE(), INTERVAL 5 DAY),   DATE_ADD(CURDATE(), INTERVAL 25 DAY),  'ACTIVE',    NULL),
    (3,  3,  3,  3, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(CURDATE(), INTERVAL 20 DAY),  DATE_ADD(CURDATE(), INTERVAL 70 DAY),  'ACTIVE',    NULL),
    (4,  4,  4,  4, NULL, DATE_SUB(CURDATE(), INTERVAL 15 DAY),  DATE_ADD(CURDATE(), INTERVAL 75 DAY),  'ACTIVE',    NULL),
    (5,  5,  5,  5, NULL, DATE_SUB(CURDATE(), INTERVAL 100 DAY), DATE_ADD(CURDATE(), INTERVAL 265 DAY), 'ACTIVE',    NULL),
    (6,  6,  6,  1, NULL, DATE_SUB(CURDATE(), INTERVAL 60 DAY),  DATE_SUB(CURDATE(), INTERVAL 30 DAY),  'EXPIRED',   NULL),
    (7,  7,  7,  2, NULL, DATE_SUB(CURDATE(), INTERVAL 90 DAY),  DATE_SUB(CURDATE(), INTERVAL 60 DAY),  'EXPIRED',   NULL),
    (8,  8,  8,  3, DATE_SUB(NOW(), INTERVAL 50 DAY), DATE_SUB(CURDATE(), INTERVAL 120 DAY), DATE_SUB(CURDATE(), INTERVAL 30 DAY),  'CANCELLED', DATE_SUB(NOW(), INTERVAL 40 DAY)),
    (9,  9,  9,  4, NULL, DATE_SUB(CURDATE(), INTERVAL 100 DAY), DATE_SUB(CURDATE(), INTERVAL 10 DAY),  'CANCELLED', DATE_SUB(NOW(), INTERVAL 50 DAY)),
    (10, 10, 10, 5, NULL, DATE_SUB(CURDATE(), INTERVAL 400 DAY), DATE_SUB(CURDATE(), INTERVAL 35 DAY),  'EXPIRED',   NULL);

-- =====================================================================
-- FAMILY SUBSCRIPTION (10)
-- =====================================================================
INSERT IGNORE INTO family_subscription
(id, family_group_id, subscription_plan_id, start_date, end_date, status, canceled_at)
VALUES
    (1,  1,  6,  DATE_SUB(CURDATE(), INTERVAL 10 DAY),  DATE_ADD(CURDATE(), INTERVAL 20 DAY),  'ACTIVE',    NULL),
    (2,  2,  7,  DATE_SUB(CURDATE(), INTERVAL 5 DAY),   DATE_ADD(CURDATE(), INTERVAL 25 DAY),  'ACTIVE',    NULL),
    (3,  3,  8,  DATE_SUB(CURDATE(), INTERVAL 20 DAY),  DATE_ADD(CURDATE(), INTERVAL 70 DAY),  'ACTIVE',    NULL),
    (4,  4,  9,  DATE_SUB(CURDATE(), INTERVAL 15 DAY),  DATE_ADD(CURDATE(), INTERVAL 75 DAY),  'ACTIVE',    NULL),
    (5,  5,  10, DATE_SUB(CURDATE(), INTERVAL 100 DAY), DATE_ADD(CURDATE(), INTERVAL 265 DAY), 'ACTIVE',    NULL),
    (6,  6,  6,  DATE_SUB(CURDATE(), INTERVAL 60 DAY),  DATE_SUB(CURDATE(), INTERVAL 30 DAY),  'EXPIRED',   NULL),
    (7,  7,  7,  DATE_SUB(CURDATE(), INTERVAL 90 DAY),  DATE_SUB(CURDATE(), INTERVAL 60 DAY),  'EXPIRED',   NULL),
    (8,  8,  8,  DATE_SUB(CURDATE(), INTERVAL 120 DAY), DATE_SUB(CURDATE(), INTERVAL 30 DAY),  'CANCELLED', DATE_SUB(NOW(), INTERVAL 40 DAY)),
    (9,  9,  9,  DATE_SUB(CURDATE(), INTERVAL 100 DAY), DATE_SUB(CURDATE(), INTERVAL 10 DAY),  'CANCELLED', DATE_SUB(NOW(), INTERVAL 50 DAY)),
    (10, 10, 10, DATE_SUB(CURDATE(), INTERVAL 400 DAY), DATE_SUB(CURDATE(), INTERVAL 35 DAY),  'EXPIRED',   NULL);

-- =====================================================================
-- SUBSCRIPTION INVOICE (12)
-- =====================================================================
INSERT IGNORE INTO subscription_invoice
(id, customer_id, unlimit_subscription_id, family_subscription_id, plan_price, status, created_at, paid_at)
VALUES
    (1,  1,  1,    NULL, 500000,   'PAID',    DATE_SUB(NOW(), INTERVAL 10 DAY),  DATE_SUB(NOW(), INTERVAL 10 DAY)),
    (2,  2,  2,    NULL, 900000,   'PAID',    DATE_SUB(NOW(), INTERVAL 5 DAY),   DATE_SUB(NOW(), INTERVAL 5 DAY)),
    (3,  3,  3,    NULL, 1350000,  'PAID',    DATE_SUB(NOW(), INTERVAL 20 DAY),  DATE_SUB(NOW(), INTERVAL 20 DAY)),
    (4,  4,  4,    NULL, 2400000,  'PAID',    DATE_SUB(NOW(), INTERVAL 15 DAY),  DATE_SUB(NOW(), INTERVAL 15 DAY)),
    (5,  5,  5,    NULL, 8500000,  'PAID',    DATE_SUB(NOW(), INTERVAL 100 DAY), DATE_SUB(NOW(), INTERVAL 100 DAY)),
    (6,  6,  6,    NULL, 500000,   'PAID',    DATE_SUB(NOW(), INTERVAL 60 DAY),  DATE_SUB(NOW(), INTERVAL 60 DAY)),
    (7,  8,  8,    NULL, 1350000,  'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY),   NULL),
    (8,  1,  NULL, 1,    1200000,  'PAID',    DATE_SUB(NOW(), INTERVAL 10 DAY),  DATE_SUB(NOW(), INTERVAL 10 DAY)),
    (9,  2,  NULL, 2,    2000000,  'PAID',    DATE_SUB(NOW(), INTERVAL 5 DAY),   DATE_SUB(NOW(), INTERVAL 5 DAY)),
    (10, 3,  NULL, 3,    3200000,  'PAID',    DATE_SUB(NOW(), INTERVAL 20 DAY),  DATE_SUB(NOW(), INTERVAL 20 DAY)),
    (11, 4,  NULL, 4,    5400000,  'PAID',    DATE_SUB(NOW(), INTERVAL 15 DAY),  DATE_SUB(NOW(), INTERVAL 15 DAY)),
    (12, 5,  NULL, 5,    18000000, 'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY),   NULL);

-- =====================================================================
-- PROMOTION (10)
-- =====================================================================
INSERT IGNORE INTO promotion
(id, title, description, start_date, end_date, status, created_at)
VALUES
    (1,  'Khuyen mai mua he',                'Giam gia cac goi rua xe mua he', DATE_SUB(CURDATE(), INTERVAL 30 DAY),  DATE_ADD(CURDATE(), INTERVAL 15 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 30 DAY)),
    (2,  'Giam gia cuoi tuan',                'Uu dai cuoi tuan cho khach hang', DATE_SUB(CURDATE(), INTERVAL 10 DAY),  DATE_ADD(CURDATE(), INTERVAL 50 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 10 DAY)),
    (3,  'Uu dai khach hang moi',             'Danh cho khach hang lan dau su dung', DATE_SUB(CURDATE(), INTERVAL 5 DAY),   DATE_ADD(CURDATE(), INTERVAL 25 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 5 DAY)),
    (4,  'Flash Sale Tet',                    'Khuyen mai dip Tet', DATE_SUB(CURDATE(), INTERVAL 200 DAY), DATE_SUB(CURDATE(), INTERVAL 180 DAY), 'EXPIRED', DATE_SUB(NOW(), INTERVAL 200 DAY)),
    (5,  'Sinh nhat cong ty',                  'Ky niem thanh lap cong ty', DATE_SUB(CURDATE(), INTERVAL 100 DAY), DATE_SUB(CURDATE(), INTERVAL 90 DAY),  'EXPIRED', DATE_SUB(NOW(), INTERVAL 100 DAY)),
    (6,  'Mung khai truong chi nhanh moi',     'Khuyen mai khai truong', DATE_SUB(CURDATE(), INTERVAL 2 DAY),   DATE_ADD(CURDATE(), INTERVAL 40 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 2 DAY)),
    (7,  'Black Friday',                       'Sale lon nhat nam', DATE_SUB(CURDATE(), INTERVAL 220 DAY), DATE_SUB(CURDATE(), INTERVAL 218 DAY), 'EXPIRED', DATE_SUB(NOW(), INTERVAL 220 DAY)),
    (8,  'Tich diem gap doi',                  'Nhan diem x2 cho moi luot rua xe', DATE_SUB(CURDATE(), INTERVAL 15 DAY),  DATE_ADD(CURDATE(), INTERVAL 10 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 15 DAY)),
    (9,  'Uu dai mua mua',                     'Khuyen mai mua mua', DATE_SUB(CURDATE(), INTERVAL 60 DAY),  DATE_SUB(CURDATE(), INTERVAL 30 DAY),  'EXPIRED', DATE_SUB(NOW(), INTERVAL 60 DAY)),
    (10, 'Combo gia dinh',                     'Uu dai cho goi gia dinh', DATE_SUB(CURDATE(), INTERVAL 1 DAY),   DATE_ADD(CURDATE(), INTERVAL 60 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 1 DAY));

-- =====================================================================
-- PROMOTION TARGET (10)
-- =====================================================================
INSERT IGNORE INTO promotion_target
(id, target_name, target_code, description)
VALUES
    (1,  'All Customers',          'ALL',     'Ap dung cho tat ca khach hang'),
    (2,  'New Customer',           'NEW',     'Khach hang moi'),
    (3,  'VIP Customer',           'VIP',     'Khach hang VIP'),
    (4,  'Bronze Tier',            'BRONZE',  'Khach hang hang Bronze'),
    (5,  'Silver Tier',            'SILVER',  'Khach hang hang Silver'),
    (6,  'Gold Tier',              'GOLD',    'Khach hang hang Gold'),
    (7,  'First Time Booking',     'FIRST',   'Lan dau dat lich'),
    (8,  'Returning Customer',     'RETURN',  'Khach hang quay lai'),
    (9,  'Birthday Month',         'BDAY',    'Khach hang co sinh nhat trong thang'),
    (10, 'Referral Program',       'REF',     'Khach hang gioi thieu ban be');

-- =====================================================================
-- PROMOTION TARGET MAPPING (15)
-- =====================================================================
INSERT IGNORE INTO promotion_target_mapping
(promotion_id, promotion_target_id)
VALUES
    (1, 1), (1, 2),
    (2, 1),
    (3, 2), (3, 7),
    (4, 1),
    (5, 3),
    (6, 1), (6, 8),
    (7, 1),
    (8, 4), (8, 5), (8, 6),
    (9, 1),
    (10, 1);

-- =====================================================================
-- VOUCHER (12)
-- =====================================================================
INSERT IGNORE INTO voucher
(id, promotion_id, voucher_code, max_discount_amount, min_order_value, usage_limit, used_count, expiry_date, status, start_date, reusable, discount_percentage, created_at)
VALUES
    (1,  1,    'SUMMER10',  50000,  100000, 200, 35,  DATE_ADD(NOW(), INTERVAL 15 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 30 DAY), true,  10, DATE_SUB(NOW(), INTERVAL 30 DAY)),
    (2,  2,    'WEEKEND15', 60000,  150000, 150, 40,  DATE_ADD(NOW(), INTERVAL 50 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 10 DAY), true,  15, DATE_SUB(NOW(), INTERVAL 10 DAY)),
    (3,  3,    'NEWCUS20',  40000,  0,      500, 120, DATE_ADD(NOW(), INTERVAL 25 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 5 DAY),  false, 20, DATE_SUB(NOW(), INTERVAL 5 DAY)),
    (4,  4,    'TETSALE',   100000, 200000, 100, 100, DATE_SUB(NOW(), INTERVAL 180 DAY), 'EXPIRED', DATE_SUB(NOW(), INTERVAL 200 DAY), false, 25, DATE_SUB(NOW(), INTERVAL 200 DAY)),
    (5,  5,    'BDAY2025',  80000,  0,      50,  50,  DATE_SUB(NOW(), INTERVAL 90 DAY), 'USED_UP', DATE_SUB(NOW(), INTERVAL 100 DAY), false, 30, DATE_SUB(NOW(), INTERVAL 100 DAY)),
    (6,  6,    'GRANDOPEN', 70000,  100000, 300, 60,  DATE_ADD(NOW(), INTERVAL 40 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 2 DAY),  true,  20, DATE_SUB(NOW(), INTERVAL 2 DAY)),
    (7,  7,    'BLACKFRI',  150000, 300000, 80,  80,  DATE_SUB(NOW(), INTERVAL 218 DAY), 'EXPIRED', DATE_SUB(NOW(), INTERVAL 220 DAY), false, 35, DATE_SUB(NOW(), INTERVAL 220 DAY)),
    (8,  8,    'DOUBLEPT',  NULL,   0,      1000, 230, DATE_ADD(NOW(), INTERVAL 10 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 15 DAY), true,  NULL, DATE_SUB(NOW(), INTERVAL 15 DAY)),
    (9,  9,    'RAINY10',   30000,  50000,  120, 120, DATE_SUB(NOW(), INTERVAL 30 DAY), 'EXPIRED', DATE_SUB(NOW(), INTERVAL 60 DAY), false, 10, DATE_SUB(NOW(), INTERVAL 60 DAY)),
    (10, 10,   'FAMILY5',   45000,  100000, 200, 18,  DATE_ADD(NOW(), INTERVAL 60 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 1 DAY),  true,  5,  DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (11, NULL, 'WELCOME50', 50000,  0,      1000, 5,   DATE_ADD(NOW(), INTERVAL 90 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 3 DAY),  false, 50, DATE_SUB(NOW(), INTERVAL 3 DAY)),
    (12, NULL, 'VIP100',    100000, 500000, 30,  4,   DATE_ADD(NOW(), INTERVAL 45 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 7 DAY),  true,  NULL, DATE_SUB(NOW(), INTERVAL 7 DAY));

-- =====================================================================
-- VOUCHER USAGE (15)
-- =====================================================================
INSERT IGNORE INTO voucher_usage
(id, voucher_id, customer_id, booking_id, used_at, status)
VALUES
    (1,  1,  1,  NULL, DATE_SUB(NOW(), INTERVAL 25 DAY), 'USED'),
    (2,  1,  2,  NULL, DATE_SUB(NOW(), INTERVAL 20 DAY), 'USED'),
    (3,  2,  3,  NULL, DATE_SUB(NOW(), INTERVAL 8 DAY),  'USED'),
    (4,  3,  4,  NULL, DATE_SUB(NOW(), INTERVAL 4 DAY),  'USED'),
    (5,  4,  5,  NULL, DATE_SUB(NOW(), INTERVAL 190 DAY),'USED'),
    (6,  5,  6,  NULL, DATE_SUB(NOW(), INTERVAL 95 DAY), 'USED'),
    (7,  6,  7,  NULL, DATE_SUB(NOW(), INTERVAL 1 DAY),  'USED'),
    (8,  7,  8,  NULL, DATE_SUB(NOW(), INTERVAL 219 DAY),'USED'),
    (9,  8,  9,  NULL, DATE_SUB(NOW(), INTERVAL 10 DAY), 'USED'),
    (10, 9,  10, NULL, DATE_SUB(NOW(), INTERVAL 45 DAY), 'USED'),
    (11, 10, 11, NULL, DATE_SUB(NOW(), INTERVAL 1 DAY),  'USED'),
    (12, 11, 12, NULL, DATE_SUB(NOW(), INTERVAL 2 DAY),  'USED'),
    (13, 2,  1,  NULL, DATE_SUB(NOW(), INTERVAL 6 DAY),  'USED'),
    (14, 3,  2,  NULL, DATE_SUB(NOW(), INTERVAL 3 DAY),  'CANCELLED'),
    (15, 12, 3,  NULL, DATE_SUB(NOW(), INTERVAL 5 DAY),  'CANCELLED');

-- =====================================================================
-- BOOKING (20)
-- appointment_date is always relative to CURDATE()/NOW() so "upcoming"
-- bookings stay in the future and "past" bookings stay in the past no
-- matter when this script runs. Nullable timestamps that have not
-- happened yet use NULL instead of a fixed placeholder date.
-- PENDING/CONFIRMED (future): 1,2,3,4,5
-- CHECKED_IN/WASHING (today): 6,7,8,9,10
-- PAID (past): 11,12,13,14   CANCELLED (past): 15,16,17   NO_SHOW (past): 18,19,20
-- =====================================================================
INSERT IGNORE INTO booking
(id, customer_id, vehicle_id, service_package_id,
 appointment_date, status, booking_type, check_in_employee_id,
 created_at, check_in_at, check_out_at, canceled_at,
 is_deposit_paid,
 total_service_amount, total_addon_amount, total_amount,
 voucher_discount_amount, point_discount_amount)
VALUES
    (1,  1,  1,  1,  DATE_ADD(CURDATE(), INTERVAL 3 DAY), 'CONFIRMED', 'ONLINE',  NULL, DATE_SUB(NOW(), INTERVAL 2 DAY), NULL, NULL, NULL, true,  100000, 70000,  170000, 0,     0),
    (2,  2,  2,  2,  DATE_ADD(CURDATE(), INTERVAL 5 DAY), 'CONFIRMED', 'ONLINE',  NULL, DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, NULL, NULL, true,  300000, 330000, 630000, 30000, 0),
    (3,  3,  3,  1,  DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'PENDING',   'ONLINE',  NULL, NOW(),                           NULL, NULL, NULL, false, 100000, 0,      100000, 0,     0),
    (4,  5,  5,  6,  DATE_ADD(CURDATE(), INTERVAL 7 DAY), 'CONFIRMED', 'WALK_IN', NULL, DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, NULL, NULL, true,  150000, 0,      150000, 0,     0),
    (5,  6,  6,  1,  DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'PENDING',   'ONLINE',  NULL, NOW(),                           NULL, NULL, NULL, false, 100000, 0,      100000, 0,     0),

    (6,  7,  7,  2,  CURDATE(), 'CHECKED_IN', 'ONLINE',  3, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 1 HOUR),    NULL, NULL, true, 300000, 120000, 420000, 15000, 0),
    (7,  8,  8,  1,  CURDATE(), 'CHECKED_IN', 'WALK_IN', 1, NOW(),                           DATE_SUB(NOW(), INTERVAL 30 MINUTE), NULL, NULL, true, 100000, 0,      100000, 0,     0),
    (8,  9,  9,  5,  CURDATE(), 'CHECKED_IN', 'ONLINE',  5, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 45 MINUTE), NULL, NULL, true, 220000, 40000,  260000, 0,     0),
    (9,  10, 10, 1,  CURDATE(), 'WASHING',    'ONLINE',  2, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 1 HOUR),    NULL, NULL, true, 100000, 90000,  190000, 0,     5000),
    (10, 11, 11, 6,  CURDATE(), 'WASHING',    'WALK_IN', 6, NOW(),                           DATE_SUB(NOW(), INTERVAL 40 MINUTE), NULL, NULL, true, 150000, 0,      150000, 0,     0),

    (11, 1,  1,  1,  DATE_SUB(CURDATE(), INTERVAL 10 DAY), 'PAID', 'ONLINE',  1, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 240 HOUR), DATE_SUB(NOW(), INTERVAL 239 HOUR), NULL, true, 100000, 50000,  150000, 0,     0),
    (12, 2,  2,  2,  DATE_SUB(CURDATE(), INTERVAL 7 DAY),  'PAID', 'ONLINE',  3, DATE_SUB(NOW(), INTERVAL 9 DAY),  DATE_SUB(NOW(), INTERVAL 168 HOUR), DATE_SUB(NOW(), INTERVAL 166 HOUR), NULL, true, 300000, 150000, 430000, 20000, 0),
    (13, 3,  3,  1,  DATE_SUB(CURDATE(), INTERVAL 15 DAY), 'PAID', 'WALK_IN', 5, DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 360 HOUR), DATE_SUB(NOW(), INTERVAL 359 HOUR), NULL, true, 100000, 20000,  120000, 0,     0),
    (14, 12, 12, 10, DATE_SUB(CURDATE(), INTERVAL 20 DAY), 'PAID', 'ONLINE',  8, DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 480 HOUR), DATE_SUB(NOW(), INTERVAL 478 HOUR), NULL, true, 180000, 220000, 400000, 0,     0),

    (15, 4,  4,  1,  DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'CANCELLED', 'ONLINE',  NULL, DATE_SUB(NOW(), INTERVAL 8 DAY), NULL, NULL, DATE_SUB(NOW(), INTERVAL 6 DAY), true,  100000, 0,      100000, 0, 0),
    (16, 5,  5,  2,  DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'CANCELLED', 'ONLINE',  NULL, DATE_SUB(NOW(), INTERVAL 5 DAY), NULL, NULL, DATE_SUB(NOW(), INTERVAL 4 DAY), false, 300000, 150000, 450000, 0, 0),
    (17, 6,  6,  1,  DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'CANCELLED', 'WALK_IN', NULL, DATE_SUB(NOW(), INTERVAL 3 DAY), NULL, NULL, DATE_SUB(NOW(), INTERVAL 3 DAY), false, 100000, 0,      100000, 0, 0),

    (18, 7,  7,  1,  DATE_SUB(CURDATE(), INTERVAL 9 DAY), 'NO_SHOW', 'ONLINE',  NULL, DATE_SUB(NOW(), INTERVAL 11 DAY), NULL, NULL, NULL, true, 100000, 0,      100000, 0, 0),
    (19, 8,  8,  2,  DATE_SUB(CURDATE(), INTERVAL 6 DAY), 'NO_SHOW', 'ONLINE',  NULL, DATE_SUB(NOW(), INTERVAL 8 DAY),  NULL, NULL, NULL, true, 300000, 330000, 630000, 0, 0),
    (20, 9,  9,  1,  DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'NO_SHOW', 'WALK_IN', NULL, DATE_SUB(NOW(), INTERVAL 6 DAY),  NULL, NULL, NULL, true, 100000, 0,      100000, 0, 0);

-- =====================================================================
-- BOOKING ADDON (15)
-- =====================================================================
INSERT IGNORE INTO booking_addon
(id, booking_id, addon_service_id, price)
VALUES
    (1,  1,  1, 50000),
    (2,  1,  8, 20000),
    (3,  2,  2, 150000),
    (4,  2,  3, 180000),
    (5,  6,  5, 120000),
    (6,  8,  4, 40000),
    (7,  9,  7, 90000),
    (8,  11, 1, 50000),
    (9,  12, 2, 150000),
    (10, 13, 8, 20000),
    (11, 14, 7, 90000),
    (12, 14, 9, 130000),
    (13, 16, 2, 150000),
    (14, 19, 2, 150000),
    (15, 19, 3, 180000);

-- =====================================================================
-- BOOKING SLOT (30) — every row spans exactly 15 minutes. Multi-slot
-- bookings (required_slot >= 2) get back-to-back contiguous rows so
-- SlotAvailabilityEngine.isContinuous() can match them.
-- =====================================================================
INSERT IGNORE INTO booking_slot
(id, station_id, start_time, end_time, max_capacity, date, booked_count, status)
VALUES
    -- booking 1 (station1, +3d, 1 slot)
    (1,  1, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 3 DAY), 1, 'AVAILABLE'),
    -- booking 2 (station2, +5d, 2 slots)
    (2,  2, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 5 DAY), 1, 'AVAILABLE'),
    (3,  2, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 5 DAY), 1, 'AVAILABLE'),
    -- booking 3 (station3, +2d, 1 slot)
    (4,  3, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 1, 'AVAILABLE'),
    -- booking 4 (station4, +7d, 1 slot)
    (5,  4, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 7 DAY), 1, 'AVAILABLE'),
    -- booking 5 (station1, +1d, 1 slot)
    (6,  1, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 1, 'AVAILABLE'),
    -- booking 6 (station2, today, 2 slots)
    (7,  2, '08:00', '08:15', 5, CURDATE(), 1, 'AVAILABLE'),
    (8,  2, '08:15', '08:30', 5, CURDATE(), 1, 'AVAILABLE'),
    -- booking 7 (station1, today, 1 slot)
    (9,  1, '08:00', '08:15', 5, CURDATE(), 1, 'AVAILABLE'),
    -- booking 8 (station3, today, 2 slots)
    (10, 3, '08:00', '08:15', 5, CURDATE(), 1, 'AVAILABLE'),
    (11, 3, '08:15', '08:30', 5, CURDATE(), 1, 'AVAILABLE'),
    -- booking 9 (station1, today, 1 slot)
    (12, 1, '08:15', '08:30', 5, CURDATE(), 1, 'AVAILABLE'),
    -- booking 10 (station4, today, 1 slot)
    (13, 4, '08:00', '08:15', 5, CURDATE(), 1, 'AVAILABLE'),
    -- booking 11 (station1, -10d, 1 slot, COMPLETED)
    (14, 1, '08:00', '08:15', 5, DATE_SUB(CURDATE(), INTERVAL 10 DAY), 1, 'COMPLETED'),
    -- booking 12 (station2, -7d, 2 slots, COMPLETED)
    (15, 2, '08:00', '08:15', 5, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 1, 'COMPLETED'),
    (16, 2, '08:15', '08:30', 5, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 1, 'COMPLETED'),
    -- booking 13 (station3, -15d, 1 slot, COMPLETED)
    (17, 3, '08:00', '08:15', 5, DATE_SUB(CURDATE(), INTERVAL 15 DAY), 1, 'COMPLETED'),
    -- booking 14 (station6, -20d, 2 slots, COMPLETED)
    (18, 6, '08:00', '08:15', 5, DATE_SUB(CURDATE(), INTERVAL 20 DAY), 1, 'COMPLETED'),
    (19, 6, '08:15', '08:30', 5, DATE_SUB(CURDATE(), INTERVAL 20 DAY), 1, 'COMPLETED'),
    -- booking 15 (station1, -5d, 1 slot, freed by cancellation)
    (20, 1, '08:00', '08:15', 5, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 0, 'AVAILABLE'),
    -- booking 16 (station2, -3d, 2 slots, freed by cancellation)
    (21, 2, '08:00', '08:15', 5, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 0, 'AVAILABLE'),
    (22, 2, '08:15', '08:30', 5, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 0, 'AVAILABLE'),
    -- booking 17 (station3, -2d, 1 slot, freed by cancellation)
    (23, 3, '08:00', '08:15', 5, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    -- booking 18 (station1, -9d, 1 slot, NO_SHOW => COMPLETED)
    (24, 1, '08:00', '08:15', 5, DATE_SUB(CURDATE(), INTERVAL 9 DAY), 1, 'COMPLETED'),
    -- booking 19 (station2, -6d, 2 slots, NO_SHOW => COMPLETED)
    (25, 2, '08:00', '08:15', 5, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 1, 'COMPLETED'),
    (26, 2, '08:15', '08:30', 5, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 1, 'COMPLETED'),
    -- booking 20 (station3, -4d, 1 slot, NO_SHOW => COMPLETED)
    (27, 3, '08:00', '08:15', 5, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 1, 'COMPLETED'),
    -- spare unbooked AVAILABLE slots for browsing
    (28, 5, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 4 DAY), 0, 'AVAILABLE'),
    (29, 5, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 4 DAY), 0, 'AVAILABLE'),
    (30, 7, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 6 DAY), 0, 'AVAILABLE');

-- =====================================================================
-- BOOKING SLOT ALLOCATION (27) — (booking_id, booking_slot_id)
-- =====================================================================
INSERT IGNORE INTO booking_slot_allocation
(booking_id, booking_slot_id)
VALUES
    (1,  1),
    (2,  2),  (2,  3),
    (3,  4),
    (4,  5),
    (5,  6),
    (6,  7),  (6,  8),
    (7,  9),
    (8,  10), (8,  11),
    (9,  12),
    (10, 13),
    (11, 14),
    (12, 15), (12, 16),
    (13, 17),
    (14, 18), (14, 19),
    (15, 20),
    (16, 21), (16, 22),
    (17, 23),
    (18, 24),
    (19, 25), (19, 26),
    (20, 27);

-- =====================================================================
-- LOYALTY POINT TRANSACTION (15)
-- =====================================================================
INSERT IGNORE INTO loyalty_point_transaction
(id, customer_id, booking_id, transaction_type, points, balance_after, created_at)
VALUES
    (1,  1,  11,   'EARN',   150,  320,  DATE_SUB(NOW(), INTERVAL 12 DAY)),
    (2,  2,  12,   'EARN',   300,  450,  DATE_SUB(NOW(), INTERVAL 9 DAY)),
    (3,  3,  13,   'EARN',   100,  610,  DATE_SUB(NOW(), INTERVAL 16 DAY)),
    (4,  12, 14,   'EARN',   400,  340,  DATE_SUB(NOW(), INTERVAL 22 DAY)),
    (5,  1,  NULL, 'REDEEM', -50,  270,  DATE_SUB(NOW(), INTERVAL 5 DAY)),
    (6,  4,  NULL, 'EARN',   80,   280,  DATE_SUB(NOW(), INTERVAL 30 DAY)),
    (7,  5,  NULL, 'EARN',   200,  900,  DATE_SUB(NOW(), INTERVAL 25 DAY)),
    (8,  6,  NULL, 'EARN',   60,   150,  DATE_SUB(NOW(), INTERVAL 20 DAY)),
    (9,  7,  NULL, 'EARN',   250,  1200, DATE_SUB(NOW(), INTERVAL 18 DAY)),
    (10, 8,  NULL, 'REDEEM', -100, 980,  DATE_SUB(NOW(), INTERVAL 15 DAY)),
    (11, 9,  NULL, 'EARN',   300,  1500, DATE_SUB(NOW(), INTERVAL 12 DAY)),
    (12, 10, NULL, 'EARN',   75,   75,   DATE_SUB(NOW(), INTERVAL 10 DAY)),
    (13, 11, NULL, 'EARN',   200,  200,  DATE_SUB(NOW(), INTERVAL 8 DAY)),
    (14, 10, 9,    'REDEEM', -50,  25,   DATE_SUB(NOW(), INTERVAL 1 HOUR)),
    (15, 12, NULL, 'EARN',   120,  460,  DATE_SUB(NOW(), INTERVAL 3 DAY));

-- =====================================================================
-- BOOKING INVOICE (12)
-- =====================================================================
INSERT IGNORE INTO booking_invoice
(id, booking_id, customer_id, raw_amount, discount_amount, final_amount, status, voucher_discount, point_discount, service_amount, addon_amount, created_at, paid_at)
VALUES
    (1,  11, 1,  150000, 0,     150000, 'PAID',      0,     0,    100000, 50000,  DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 239 HOUR)),
    (2,  12, 2,  450000, 20000, 430000, 'PAID',      20000, 0,    300000, 150000, DATE_SUB(NOW(), INTERVAL 9 DAY),  DATE_SUB(NOW(), INTERVAL 166 HOUR)),
    (3,  13, 3,  120000, 0,     120000, 'PAID',      0,     0,    100000, 20000,  DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 359 HOUR)),
    (4,  14, 12, 400000, 0,     400000, 'PAID',      0,     0,    180000, 220000, DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 478 HOUR)),
    (5,  6,  7,  420000, 15000, 405000, 'PENDING',   15000, 0,    300000, 120000, DATE_SUB(NOW(), INTERVAL 3 DAY),  NULL),
    (6,  7,  8,  100000, 0,     100000, 'PENDING',   0,     0,    100000, 0,      NOW(),                            NULL),
    (7,  8,  9,  260000, 0,     260000, 'PENDING',   0,     0,    220000, 40000,  DATE_SUB(NOW(), INTERVAL 2 DAY),  NULL),
    (8,  9,  10, 190000, 5000,  185000, 'PENDING',   0,     5000, 100000, 90000,  DATE_SUB(NOW(), INTERVAL 2 DAY),  NULL),
    (9,  10, 11, 150000, 0,     150000, 'PENDING',   0,     0,    150000, 0,      NOW(),                            NULL),
    (10, 15, 4,  100000, 0,     100000, 'CANCELLED', 0,     0,    100000, 0,      DATE_SUB(NOW(), INTERVAL 8 DAY),  NULL),
    (11, 16, 5,  450000, 0,     450000, 'CANCELLED', 0,     0,    300000, 150000, DATE_SUB(NOW(), INTERVAL 5 DAY),  NULL),
    (12, 17, 6,  100000, 0,     100000, 'CANCELLED', 0,     0,    100000, 0,      DATE_SUB(NOW(), INTERVAL 3 DAY),  NULL);

-- =====================================================================
-- PAYMENT (15)
-- =====================================================================
INSERT IGNORE INTO payment
(id, booking_invoice_id, subscription_invoice_id, payment_method, amount, transaction_code, payment_status, paid_at, received_amount, payment_type)
VALUES
    (1,  1,    NULL, 'CASH',  150000,  NULL,            'SUCCESS', DATE_SUB(NOW(), INTERVAL 239 HOUR), 150000, 'PAYMENT'),
    (2,  2,    NULL, 'MOMO',  430000,  'TXN-0000000002', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 166 HOUR), 430000, 'PAYMENT'),
    (3,  3,    NULL, 'CASH',  120000,  NULL,            'SUCCESS', DATE_SUB(NOW(), INTERVAL 359 HOUR), 200000, 'PAYMENT'),
    (4,  4,    NULL, 'VNPAY', 400000,  'TXN-0000000004', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 478 HOUR), 400000, 'PAYMENT'),
    (5,  10,   NULL, 'CASH',  20000,   NULL,            'SUCCESS', DATE_SUB(NOW(), INTERVAL 6 DAY),    20000,  'REFUND'),
    (6,  NULL, 1,    'MOMO',  500000,  'TXN-0000000006', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 10 DAY),   500000, 'PAYMENT'),
    (7,  NULL, 2,    'CASH',  900000,  NULL,            'SUCCESS', DATE_SUB(NOW(), INTERVAL 5 DAY),    900000, 'PAYMENT'),
    (8,  NULL, 3,    'VNPAY', 1350000, 'TXN-0000000008', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 20 DAY),   1350000,'PAYMENT'),
    (9,  NULL, 4,    'MOMO',  2400000, 'TXN-0000000009', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 15 DAY),   2400000,'PAYMENT'),
    (10, NULL, 5,    'CASH',  8500000, NULL,            'SUCCESS', DATE_SUB(NOW(), INTERVAL 100 DAY),  8500000,'PAYMENT'),
    (11, NULL, 6,    'MOMO',  500000,  'TXN-0000000011', 'FAILED',  DATE_SUB(NOW(), INTERVAL 60 DAY),   0,      'PAYMENT'),
    (12, NULL, 8,    'CASH',  1200000, NULL,            'SUCCESS', DATE_SUB(NOW(), INTERVAL 10 DAY),   1200000,'PAYMENT'),
    (13, NULL, 9,    'VNPAY', 2000000, 'TXN-0000000013', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 5 DAY),    2000000,'PAYMENT'),
    (14, NULL, 10,   'CASH',  3200000, NULL,            'SUCCESS', DATE_SUB(NOW(), INTERVAL 20 DAY),   3200000,'PAYMENT'),
    (15, NULL, 11,   'MOMO',  5400000, 'TXN-0000000015', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 15 DAY),   5400000,'PAYMENT');

-- =====================================================================
-- REVIEW (12)
-- =====================================================================
INSERT IGNORE INTO review
(id, customer_id, booking_id, rating_stars, comment, created_at, updated_at, is_deleted)
VALUES
    (1,  1,  11,   5, 'Xe sach bong, nhan vien than thien, rat hai long!',         DATE_SUB(NOW(), INTERVAL 239 HOUR), NULL, false),
    (2,  2,  12,   4, 'Dich vu tot, thoi gian hoi lau nhung chap nhan duoc.',      DATE_SUB(NOW(), INTERVAL 166 HOUR), DATE_SUB(NOW(), INTERVAL 160 HOUR), false),
    (3,  3,  13,   5, 'Nhanh va chat luong, se quay lai lan sau.',                DATE_SUB(NOW(), INTERVAL 359 HOUR), NULL, false),
    (4,  12, 14,   4, 'Cham soc thu cung tot, xe sach.',                          DATE_SUB(NOW(), INTERVAL 478 HOUR), NULL, false),
    (5,  4,  NULL, 5, 'Ung dung dat lich rat tien loi.',                         DATE_SUB(NOW(), INTERVAL 30 DAY),   NULL, false),
    (6,  5,  NULL, 3, 'Gia hoi cao so voi mat bang chung.',                      DATE_SUB(NOW(), INTERVAL 25 DAY),   NULL, false),
    (7,  6,  NULL, 4, 'Nhan vien nhiet tinh, ho tro tot.',                       DATE_SUB(NOW(), INTERVAL 20 DAY),   NULL, false),
    (8,  7,  NULL, 5, 'Se gioi thieu cho ban be.',                               DATE_SUB(NOW(), INTERVAL 18 DAY),   NULL, false),
    (9,  8,  NULL, 2, 'Cho lau hon du kien.',                                    DATE_SUB(NOW(), INTERVAL 15 DAY),   NULL, false),
    (10, 9,  NULL, 4, 'Chat luong on dinh qua nhieu lan su dung.',               DATE_SUB(NOW(), INTERVAL 12 DAY),   NULL, false),
    (11, 10, NULL, 5, 'Rat hai long voi dich vu subscription.',                  DATE_SUB(NOW(), INTERVAL 10 DAY),   NULL, false),
    (12, 11, NULL, 4, 'On, se tiep tuc su dung.',                                DATE_SUB(NOW(), INTERVAL 8 DAY),    NULL, false);

-- =====================================================================
-- NOTIFICATION (10)
-- =====================================================================
INSERT IGNORE INTO notification
(id, title, content, created_at)
VALUES
    (1,  'Booking Confirmed',          'Your booking has been confirmed',                       DATE_SUB(NOW(), INTERVAL 2 DAY)),
    (2,  'Check-in Success',           'You have successfully checked in for your booking',     DATE_SUB(NOW(), INTERVAL 3 HOUR)),
    (3,  'Promotion',                  'New promotion: up to 20% off this week!',                DATE_SUB(NOW(), INTERVAL 5 DAY)),
    (4,  'System Maintenance',         'Scheduled maintenance this weekend from 23:00 to 02:00', DATE_SUB(NOW(), INTERVAL 10 DAY)),
    (5,  'New Service Available',      'Try our new Ceramic Coating service!',                   DATE_SUB(NOW(), INTERVAL 15 DAY)),
    (6,  'Loyalty Tier Upgrade',       'Congratulations, you have been upgraded to a new tier!', DATE_SUB(NOW(), INTERVAL 20 DAY)),
    (7,  'Subscription Renewal',       'Your subscription will renew soon',                      DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (8,  'Voucher Expiring Soon',      'Your voucher will expire in 3 days',                      DATE_SUB(NOW(), INTERVAL 12 HOUR)),
    (9,  'Queue Update',               'Your turn is approaching, please be ready',              DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
    (10, 'Feedback Request',           'How was your recent visit? Leave us a review!',          DATE_SUB(NOW(), INTERVAL 7 DAY));

-- =====================================================================
-- CUSTOMER NOTIFICATION (15)
-- =====================================================================
INSERT IGNORE INTO customer_notification
(id, notification_id, customer_id, status, sent_at, read_at)
VALUES
    (1,  1,  1,  'READ',   DATE_SUB(NOW(), INTERVAL 2 DAY),  DATE_SUB(NOW(), INTERVAL 2 DAY)),
    (2,  2,  7,  'UNREAD', DATE_SUB(NOW(), INTERVAL 3 HOUR), NULL),
    (3,  3,  2,  'UNREAD', DATE_SUB(NOW(), INTERVAL 5 DAY),  NULL),
    (4,  4,  1,  'READ',   DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY)),
    (5,  5,  3,  'UNREAD', DATE_SUB(NOW(), INTERVAL 15 DAY), NULL),
    (6,  6,  4,  'READ',   DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 19 DAY)),
    (7,  7,  5,  'UNREAD', DATE_SUB(NOW(), INTERVAL 1 DAY),  NULL),
    (8,  8,  6,  'UNREAD', DATE_SUB(NOW(), INTERVAL 12 HOUR),NULL),
    (9,  9,  10, 'UNREAD', DATE_SUB(NOW(), INTERVAL 30 MINUTE), NULL),
    (10, 10, 8,  'READ',   DATE_SUB(NOW(), INTERVAL 7 DAY),  DATE_SUB(NOW(), INTERVAL 6 DAY)),
    (11, 3,  9,  'READ',   DATE_SUB(NOW(), INTERVAL 5 DAY),  DATE_SUB(NOW(), INTERVAL 4 DAY)),
    (12, 1,  11, 'UNREAD', DATE_SUB(NOW(), INTERVAL 1 DAY),  NULL),
    (13, 6,  12, 'READ',   DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 17 DAY)),
    (14, 8,  2,  'UNREAD', DATE_SUB(NOW(), INTERVAL 12 HOUR),NULL),
    (15, 9,  7,  'READ',   DATE_SUB(NOW(), INTERVAL 30 MINUTE), DATE_SUB(NOW(), INTERVAL 20 MINUTE));

-- =====================================================================
-- QUEUE TICKET (12) — today's queue
-- =====================================================================
INSERT IGNORE INTO queue_ticket
(id, station_id, booking_id, ticket_number, status, issued_at, is_booking, priority_score)
VALUES
    (1,  2, 6,    'A001', 'IN_SERVICE', DATE_SUB(NOW(), INTERVAL 70 MINUTE), true,  3),
    (2,  1, 7,    'A002', 'IN_SERVICE', DATE_SUB(NOW(), INTERVAL 35 MINUTE), true,  3),
    (3,  3, 8,    'A003', 'IN_SERVICE', DATE_SUB(NOW(), INTERVAL 50 MINUTE), true,  3),
    (4,  1, 9,    'A004', 'IN_SERVICE', DATE_SUB(NOW(), INTERVAL 65 MINUTE), true,  3),
    (5,  4, 10,   'A005', 'IN_SERVICE', DATE_SUB(NOW(), INTERVAL 45 MINUTE), true,  3),
    (6,  1, NULL, 'A006', 'WAITING',    DATE_SUB(NOW(), INTERVAL 20 MINUTE), false, 1),
    (7,  1, NULL, 'A007', 'WAITING',    DATE_SUB(NOW(), INTERVAL 10 MINUTE), false, 1),
    (8,  2, NULL, 'A008', 'WAITING',    DATE_SUB(NOW(), INTERVAL 15 MINUTE), false, 1),
    (9,  3, NULL, 'A009', 'WAITING',    DATE_SUB(NOW(), INTERVAL 5 MINUTE),  false, 1),
    (10, 4, NULL, 'A010', 'COMPLETED',  DATE_SUB(NOW(), INTERVAL 3 HOUR),    false, 1),
    (11, 1, NULL, 'A011', 'CANCELLED',  DATE_SUB(NOW(), INTERVAL 2 HOUR),    false, 1),
    (12, 2, NULL, 'A012', 'COMPLETED',  DATE_SUB(NOW(), INTERVAL 4 HOUR),    false, 1);

-- =====================================================================
-- SYSTEM SETTING (10)
-- =====================================================================
INSERT IGNORE INTO system_setting
(setting_key, setting_value, description, data_type, is_active)
VALUES
    ('DEPOSIT_PERCENT',             '30',      'Deposit percent',                                'NUMBER',  true),
    ('MAX_BOOKING_DAY',             '30',      'Maximum booking day',                            'NUMBER',  true),
    ('DEFAULT_DEPOSIT_AMOUNT',      '20000',   'Default deposit amount',                         'NUMBER',  true),
    ('CANCEL_THRESHOLD_MINUTES',    '120',     'Minutes before appointment a booking can be cancelled', 'NUMBER', true),
    ('LOYALTY_POINT_PER_VND',       '1000',    'VND spent per loyalty point earned',             'NUMBER',  true),
    ('MAX_VEHICLE_PER_FAMILY',      '5',       'Maximum vehicles allowed per family subscription','NUMBER', true),
    ('QUEUE_PRIORITY_BOOKING_WEIGHT','3',      'Priority weight given to booking-based queue tickets','NUMBER', true),
    ('SUPPORT_HOTLINE',             '1900-1234','Customer support hotline number',               'STRING',  true),
    ('MAINTENANCE_MODE',            'false',   'Whether the system is in maintenance mode',      'BOOLEAN', true),
    ('REVIEW_EDIT_WINDOW_HOURS',    '24',      'Hours a customer may edit their review after posting', 'NUMBER', true);

SET FOREIGN_KEY_CHECKS = 1;
