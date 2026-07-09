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
    (1, 'khoa@gmail.com',     '0900000001', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 1, true,  DATE_SUB(NOW(), INTERVAL 400 DAY)),
    (2, 'hai@gmail.com',  '0900000002', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true,  DATE_SUB(NOW(), INTERVAL 350 DAY)),
    (3, 'tuyet@gmail.com',  '0900000003', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, false, DATE_SUB(NOW(), INTERVAL 340 DAY)),
    (4,  'an@gmail.com',  '0900001001', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 200 DAY)),
    (5,  'binh@gmail.com',  '0900001002', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 195 DAY)),
    (6,  'chi@gmail.com',  '0900001003', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 190 DAY)),
    (7,  'dung@gmail.com',  '0900001004', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 185 DAY)),
    (8,  'em@gmail.com',  '0900001005', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 180 DAY)),
    (9,  'phuc@gmail.com',  '0900001006', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 175 DAY)),
    (28, 'tuan@gmail.com',  '0900001007', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 170 DAY)),
    (29, 'long@gmail.com',  '0900001008', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 165 DAY)),
    (12, 'ich@gmail.com',  '0900001009', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 160 DAY)),
    (13, 'khang@gmail.com', '0900001010', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 155 DAY)),
    (14, 'loan@gmail.com', '0900001011', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 150 DAY)),
    (15, 'minh@gmail.com', '0900001012', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 2, true, DATE_SUB(NOW(), INTERVAL 145 DAY)),
    (16, 'phong@gmail.com',  '0900002001', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 120 DAY)),
    (17, 'nam@gmail.com',  '0900002002', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 110 DAY)),
    (18, 'linh@gmail.com',  '0900002003', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 100 DAY)),
    (19, 'mai@gmail.com',  '0900002004', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 90 DAY)),
    (20, 'hoa@gmail.com',  '0900002005', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 80 DAY)),
    (21, 'duc@gmail.com',  '0900002006', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 70 DAY)),
    (22, 'thao@gmail.com',  '0900002007', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 60 DAY)),
    (23, 'quang@gmail.com',  '0900002008', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 50 DAY)),
    (24, 'yen@gmail.com',  '0900002009', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 40 DAY)),
    (25, 'khanh@gmail.com', '0900002010', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 30 DAY)),
    (26, 'trang@gmail.com', '0900002011', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 20 DAY)),
    (27, 'hung@gmail.com', '0900002012', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 10 DAY));


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
-- PROVINCE (5)
-- =====================================================================
INSERT IGNORE INTO province (id, province_name)
VALUES
    (1, 'Tien Giang'),
    (2, 'Ho Chi Minh'),
    (3, 'Ha Noi'),
    (4, 'Da Nang'),
    (5, 'Can Tho');

-- =====================================================================
-- COMMUNE (7) — each province has 1 or 2 communes
-- =====================================================================
INSERT IGNORE INTO commune (id, commune_name, province_id)
VALUES
    (1,  'My Tho',    1),
    (2,  'Thu Duc',   2),
    (3,  'Cau Giay',  3),
    (4,  'Hai Chau',  4),
    (5,  'Ninh Kieu', 5),
    (11, 'Go Vap',    2),
    (12, 'Ba Dinh',   3);

-- =====================================================================
-- STATION (8) — each commune has 1 or 2 stations
-- =====================================================================
INSERT IGNORE INTO station
(id, station_name, address, commune_id, is_operating, max_wash_capacity, is_deleted)
VALUES
    (9,  'AutoWash My Tho',         '123 Ap Bac, My Tho',              1,  true, 20, false),
    (2,  'AutoWash HCM Thu Duc',    '456 Vo Van Ngan, Thu Duc, HCM',   2,  true, 30, false),
    (3,  'AutoWash Ha Noi',         '12 Cau Giay, Ha Noi',             3,  true, 25, false),
    (4,  'AutoWash Da Nang',        '78 Nguyen Van Linh, Da Nang',     4,  true, 20, false),
    (5,  'AutoWash Can Tho',        '34 Mau Than, Ninh Kieu, Can Tho', 5,  true, 18, false),
    (6,  'AutoWash HCM Thu Duc 2',  '12 Kha Van Can, Thu Duc, HCM',    2,  true, 22, false),
    (7,  'AutoWash HCM Go Vap',     '34 Quang Trung, Go Vap, HCM',     11, true, 20, false),
    (8,  'AutoWash Ha Noi Ba Dinh', '7 Doi Can, Ba Dinh, Ha Noi',      12, true, 18, false),

    (1, 'AutoWash Chi nhánh Q9', 'Lê Văn Việt', TRUE, true,3, false);

-- =====================================================================
-- WASH LANE (15)
-- =====================================================================
INSERT IGNORE INTO wash_lane
(id, station_id, lane_name, status, booking_walkin_ratio, is_deleted)
VALUES
    (1,  1,  'Lane 1', 'AVAILABLE', 3, false),
    (2,  1,  'Lane 2', 'AVAILABLE', 3, false),
    (3,  2,  'Lane 1', 'AVAILABLE', 3, false),
    (4,  2,  'Lane 2', 'AVAILABLE', 3, false),
    (5,  2,  'Lane 3', 'WASHING',  3, false),
    (6,  3,  'Lane 1', 'AVAILABLE', 3, false),
    (7,  3,  'Lane 2', 'AVAILABLE', 2, false),
    (8,  4,  'Lane 1', 'AVAILABLE', 3, false),
    (9,  5,  'Lane 1', 'AVAILABLE', 3, false),
    (10, 6,  'Lane 1', 'AVAILABLE', 3, false),
    (11, 6,  'Lane 2', 'AVAILABLE', 4, false),
    (12, 7,  'Lane 1', 'AVAILABLE', 3, false),
    (13, 8,  'Lane 1', 'AVAILABLE', 3, false),
    (14, 6,  'Lane 1', 'AVAILABLE', 3, false),
    (15, 7,  'Lane 1', 'AVAILABLE',  3, false);

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
    (11, 14, 6,  'Loan',  'Ly'),
    (12, 15, 7,  'Minh',  'Vu');

-- =====================================================================
-- CUSTOMER TIER (4)
-- =====================================================================
INSERT IGNORE INTO customer_tier
(id, tier_name, min_points, booking_window_days, point_multiple, retention_target_amount)
VALUES
    (1,  'MEMBER',    0,     7,  1.0, 0),
    (2,  'SILVER',    500,   10, 1.2, 1500000),
    (3,  'GOLD',      1000,  12, 1.5, 3000000),
    (4,  'PLATINUM',  2000,  14, 1.8, 5000000);

-- =====================================================================
-- TIER BENEFIT (6)
-- =====================================================================
INSERT IGNORE INTO tier_benefit
(id, customer_tier_id, benefit_description)
VALUES
    (1,  1,  'Tich diem co ban x1 moi luot rua xe'),
    (2,  2,  'Uu tien dat lich truoc 10 ngay'),
    (3,  3,  'Giam 5% phi dich vu addon'),
    (4,  3,  'Tich diem x1.5 moi luot'),
    (5,  4,  'Mien phi 1 luot danh bong moi quy'),
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
    (5,  20, 'Hoa',   'Pham',  '1985-05-18', 1,  1, NULL),
    (6,  21, 'Duc',   'Vo',    '1998-09-02', 2,  0, NULL),
    (7,  22, 'Thao',  'Dang',  '1993-12-25', 3,  0, NULL),
    (8,  23, 'Quang', 'Bui',   '1991-02-14', 4,  0, NULL),
    (9,  24, 'Yen',   'Do',    '1996-06-08', 1,  0, NULL),
    (10, 25, 'Khanh', 'Ho',    '1989-08-19', 2,  0, NULL),
    (11, 26, 'Trang', 'Ngo',   '1994-04-27', 2,  0, NULL),
    (12, 27, 'Hung',  'Ly',    '1997-10-11', 3,  0, NULL),

    (100, 10, 'Nguyen Van', 'A', '2005-10-11', 3,  0, NULL),

    (101, 11, 'Nguyen Van', 'B', '2005-10-12', 3,  0, NULL);

-- =====================================================================
-- VEHICLE (16) — vehicle 16 has no customer_id: anonymous walk-in
-- guest with no registered account (FE shows "Guest" badge for this).
-- =====================================================================
INSERT IGNORE INTO vehicle
(id, customer_id, license_plate, brand_name, color, violation_count, restricted_until, is_deleted)
VALUES
    (1,  1,  '51A-11111', 'Toyota',   'White',  4, NULL, false),
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
    (15, 3,  '51C-15151', 'Kia',      'Orange', 0, NULL, false),

    (201, 100, '51G-111.11', 'Toyota Vios', 'Đen', 0, NULL, false),
    (202, NULL, '51G-222.22', 'Kia Morning', 'Đỏ', 4, DATE_ADD(NOW(), INTERVAL 5 DAY), false),

    (203, 101, '51G-333.33', 'Honda City', 'Trắng', 0, NULL, FALSE);


# (203, NULL, '51G-333.33', 'Honda City', 'Trắng', 0, NULL, false)

#     (203, 101, '51G-333.33', 'Honda City', 'Trắng', 0, NULL, false);

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
    (5,  5,  1,  9000000, 4000000, DATE_SUB(CURDATE(), INTERVAL 60 DAY), DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'IN_PROGRESS', NULL),
    (6,  6,  2,  9000000, 5000000, DATE_SUB(CURDATE(), INTERVAL 60 DAY), DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'IN_PROGRESS', NULL),
    (7,  7,  3,  12000000, 12500000, DATE_SUB(CURDATE(), INTERVAL 90 DAY), DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'ACHIEVED', DATE_SUB(NOW(), INTERVAL 5 DAY)),
    (8,  8,  4,  15000000, 15200000, DATE_SUB(CURDATE(), INTERVAL 90 DAY), DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'ACHIEVED', DATE_SUB(NOW(), INTERVAL 5 DAY)),
    (9,  9,  1,  20000000, 20500000, DATE_SUB(CURDATE(), INTERVAL 90 DAY), DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'ACHIEVED', DATE_SUB(NOW(), INTERVAL 5 DAY)),
    (10, 10, 2,  25000000, 9000000,  DATE_SUB(CURDATE(), INTERVAL 90 DAY), DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'FAILED',   DATE_SUB(NOW(), INTERVAL 5 DAY)),
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
-- SERVICE CATEGORY (3) — dong san pham: rua le, goi Family, goi Unlimited
-- =====================================================================
INSERT IGNORE INTO service_category
(id, category_name, description)
VALUES
    (1, 'Single Wash', 'Dich vu rua xe theo lan, thanh toan tung luot'),
    (2, 'Family',      'Dich vu thuoc goi thanh vien Family (nhieu xe)'),
    (3, 'Unlimited',   'Dich vu thuoc goi thanh vien Unlimited (khong gioi han)');

-- =====================================================================
-- ADDON SERVICE (7) — addon tuong ung 3 goi Basic/Medium/Premium (FE mock)
-- =====================================================================
INSERT IGNORE INTO addon_service
(id, name, price, duration_minutes, service_category_id, is_deleted)
VALUES
    (1, 'Exterior Foam Wash',      60000,  15, 1, false),
    (2, 'Wheel Cleaning',          40000,  15, 1, false),
    (3, 'Hand Dry',                30000,  0,  1, false),
    (4, 'Interior Vacuum',         70000,  15, 1, false),
    (5, 'Window Cleaning',         30000,  15, 1, false),
    (6, 'Ceramic Boost Spray',     150000, 15, 1, false),
    (7, 'Dashboard UV Protection', 80000,  15, 1, false),

    (50, 'Xịt Gầm Chống Rỉ', 30000.00, 15, 1, false);

-- =====================================================================
-- SERVICE PACKAGE (3) — dung dung 3 muc Basic/Medium/Premium theo FE mock.
-- required_slot = durationMinutes / 15 (Basic 15p, Medium 30p, Premium 45p)
-- =====================================================================
INSERT IGNORE INTO service_package
(id, service_category_id, name, base_price, description, required_slot, is_deleted)
VALUES
    (1, 1, 'Basic',   149000, 'Rua xe co ban: rua bot ngoai xe, lam sach mam xe va lau kho tay',                                     1, false),
    (2, 1, 'Medium',  299000, 'Lam moi toan dien tu trong ra ngoai: bao gom Basic + hut bui noi that va lau kinh',                    2, false),
    (3, 1, 'Premium', 499000, 'Cham soc va bao ve toi uu: bao gom Medium + xit ceramic boost va chong tia UV cho bang dieu khien',    3, false);

-- =====================================================================
-- PACKAGE ADDON MAPPING (15) — addon cong don theo tung muc (Basic 3,
-- Medium 5, Premium 7) dung khop danh sach addons trong FE mock
-- =====================================================================
INSERT IGNORE INTO package_addon_mapping
(service_package_id, addon_service_id)
VALUES
    (1, 1), (1, 2), (1, 3),
    (2, 1), (2, 2), (2, 3), (2, 4), (2, 5),
    (3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (3, 6), (3, 7);

-- =====================================================================
-- SUBSCRIPTION PLAN (12) — moi to hop (Unlimited/Family x Basic/Premium)
-- co du 3 ky han: 1 thang, 3 thang, 6 thang
-- =====================================================================
INSERT IGNORE INTO subscription_plan
(id, service_package_id, service_category_id, plan_name, duration_days, price, plan_type, max_vehicle_count, description, is_deleted)
VALUES
    (1,  1, 3, 'Unlimited Basic 1 Month',     30,  500000,   'UNLIMIT', 1, 'Rua xe khong gioi han trong 1 thang', false),
    (2,  3, 3, 'Unlimited Premium 1 Month',   30,  900000,   'UNLIMIT', 1, 'Rua xe cao cap khong gioi han trong 1 thang', false),
    (3,  1, 3, 'Unlimited Basic 3 Months',    90,  1350000,  'UNLIMIT', 1, 'Rua xe khong gioi han trong 3 thang', false),
    (4,  3, 3, 'Unlimited Premium 3 Months',  90,  2400000,  'UNLIMIT', 1, 'Rua xe cao cap khong gioi han trong 3 thang', false),
    (5,  3, 3, 'Unlimited Premium 6 Months',  180, 4800000,  'UNLIMIT', 1, 'Rua xe cao cap khong gioi han trong 6 thang', false),
    (6,  1, 2, 'Family Basic 1 Month',        30,  1200000,  'FAMILY',    3, 'Rua xe khong gioi han cho ca gia dinh, 1 thang', false),
    (7,  3, 2, 'Family Premium 1 Month',      30,  2000000,  'FAMILY',    3, 'Rua xe cao cap cho ca gia dinh, 1 thang', false),
    (8,  1, 2, 'Family Basic 3 Months',       90,  3200000,  'FAMILY',    4, 'Rua xe khong gioi han cho ca gia dinh, 3 thang', false),
    (9,  3, 2, 'Family Premium 3 Months',     90,  5400000,  'FAMILY',    4, 'Rua xe cao cap cho ca gia dinh, 3 thang', false),
    (10, 3, 2, 'Family Premium 6 Months',     180, 10800000, 'FAMILY',    5, 'Rua xe cao cap cho ca gia dinh, 6 thang', false),
    (11, 1, 3, 'Unlimited Basic 6 Months',    180, 2700000,  'UNLIMIT', 1, 'Rua xe khong gioi han trong 6 thang', false),
    (12, 1, 2, 'Family Basic 6 Months',       180, 6500000,  'FAMILY',    3, 'Rua xe khong gioi han cho ca gia dinh, 6 thang', false);

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
    (8,  8,  8,  3, DATE_SUB(NOW(), INTERVAL 50 DAY), DATE_SUB(CURDATE(), INTERVAL 120 DAY), DATE_SUB(CURDATE(), INTERVAL 30 DAY),  'CANCELED', DATE_SUB(NOW(), INTERVAL 40 DAY)),
    (9,  9,  9,  4, NULL, DATE_SUB(CURDATE(), INTERVAL 100 DAY), DATE_SUB(CURDATE(), INTERVAL 10 DAY),  'CANCELED', DATE_SUB(NOW(), INTERVAL 50 DAY)),
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
    (8,  8,  8,  DATE_SUB(CURDATE(), INTERVAL 120 DAY), DATE_SUB(CURDATE(), INTERVAL 30 DAY),  'CANCELED', DATE_SUB(NOW(), INTERVAL 40 DAY)),
    (9,  9,  9,  DATE_SUB(CURDATE(), INTERVAL 100 DAY), DATE_SUB(CURDATE(), INTERVAL 10 DAY),  'CANCELED', DATE_SUB(NOW(), INTERVAL 50 DAY)),
    (10, 10, 10, DATE_SUB(CURDATE(), INTERVAL 400 DAY), DATE_SUB(CURDATE(), INTERVAL 35 DAY),  'EXPIRED',   NULL);

-- =====================================================================
-- SUBSCRIPTION INVOICE (12)
-- =====================================================================
INSERT IGNORE INTO subscription_invoice
(id, customer_id, unlimit_subscription_id, family_subscription_id, plan_price, status, created_at, paid_at,type)
VALUES
    (1,  1,  1,    NULL, 500000,   'PAID',    DATE_SUB(NOW(), INTERVAL 10 DAY),  DATE_SUB(NOW(), INTERVAL 10 DAY),1),
    (2,  2,  2,    NULL, 900000,   'PAID',    DATE_SUB(NOW(), INTERVAL 5 DAY),   DATE_SUB(NOW(), INTERVAL 5 DAY),1),
    (3,  3,  3,    NULL, 1350000,  'PAID',    DATE_SUB(NOW(), INTERVAL 20 DAY),  DATE_SUB(NOW(), INTERVAL 20 DAY),1),
    (4,  4,  4,    NULL, 2400000,  'PAID',    DATE_SUB(NOW(), INTERVAL 15 DAY),  DATE_SUB(NOW(), INTERVAL 15 DAY),1),
    (5,  5,  5,    NULL, 8500000,  'PAID',    DATE_SUB(NOW(), INTERVAL 100 DAY), DATE_SUB(NOW(), INTERVAL 100 DAY),1),
    (6,  6,  6,    NULL, 500000,   'PAID',    DATE_SUB(NOW(), INTERVAL 60 DAY),  DATE_SUB(NOW(), INTERVAL 60 DAY),1),
    (7,  8,  8,    NULL, 1350000,  'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY),   NULL,1),
    (8,  1,  NULL, 1,    1200000,  'PAID',    DATE_SUB(NOW(), INTERVAL 10 DAY),  DATE_SUB(NOW(), INTERVAL 10 DAY),1),
    (9,  2,  NULL, 2,    2000000,  'PAID',    DATE_SUB(NOW(), INTERVAL 5 DAY),   DATE_SUB(NOW(), INTERVAL 5 DAY),1),
    (10, 3,  NULL, 3,    3200000,  'PAID',    DATE_SUB(NOW(), INTERVAL 20 DAY),  DATE_SUB(NOW(), INTERVAL 20 DAY),1),
    (11, 4,  NULL, 4,    5400000,  'PAID',    DATE_SUB(NOW(), INTERVAL 15 DAY),  DATE_SUB(NOW(), INTERVAL 15 DAY),1),
    (12, 5,  NULL, 5,    18000000, 'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY),   NULL,1);

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
    (4,  'Member Tier',            'MEMBER',  'Khach hang hang Member'),
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
    (8,  8,    'DOUBLEPT',  100000,   50000,      1000, 230, DATE_ADD(NOW(), INTERVAL 10 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 15 DAY), true,  10, DATE_SUB(NOW(), INTERVAL 15 DAY)),
    (9,  9,    'RAINY10',   30000,  50000,  120, 120, DATE_SUB(NOW(), INTERVAL 30 DAY), 'EXPIRED', DATE_SUB(NOW(), INTERVAL 60 DAY), false, 10, DATE_SUB(NOW(), INTERVAL 60 DAY)),
    (10, 10,   'FAMILY5',   45000,  100000, 200, 18,  DATE_ADD(NOW(), INTERVAL 60 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 1 DAY),  true,  5,  DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (11, NULL, 'WELCOME50', 50000,  10000,      1000, 5,   DATE_ADD(NOW(), INTERVAL 90 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 3 DAY),  false, 5, DATE_SUB(NOW(), INTERVAL 3 DAY)),
    (12, NULL, 'VIP100',    100000, 500000, 30,  4,   DATE_ADD(NOW(), INTERVAL 45 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 7 DAY),  true,  10, DATE_SUB(NOW(), INTERVAL 7 DAY));

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
    (14, 3,  2,  NULL, DATE_SUB(NOW(), INTERVAL 3 DAY),  'CANCELED'),
    (15, 12, 3,  NULL, DATE_SUB(NOW(), INTERVAL 5 DAY),  'CANCELED');

-- =====================================================================
-- BOOKING (25)
-- appointment_date is always relative to CURDATE()/NOW() so "upcoming"
-- bookings stay in the future and "past" bookings stay in the past no
-- matter when this script runs. Nullable timestamps that have not
-- happened yet use NULL instead of a fixed placeholder date.
-- PENDING/CONFIRMED (future): 1,2,3,4,5
-- CHECK_IN/WASHING (today, already in a wash lane): 6,7,8,9,10
-- COMPLETED (past): 11,12,13,14   CANCELED (past): 15,16,17   NO_SHOW (past): 18,19,20
-- CHECK_IN waiting in queue (today, covers FE-27-US-01 AC02-AC04):
--   21 ONLINE+deposit/GOLD (AC02), 22 WALK_IN registered/SILVER (AC04),
--   23 ONLINE+no-deposit subscription/PLATINUM (AC03),
--   24 WALK_IN anonymous guest/no customer (AC04), 25 ONLINE+deposit/MEMBER (AC02)
-- =====================================================================
INSERT IGNORE INTO booking
(id, customer_id, vehicle_id, service_package_id,
 appointment_date, status, booking_type, check_in_employee_id,
 created_at, check_in_at, check_out_at, canceled_at,
 is_deposit_paid,
 total_service_amount, total_addon_amount, total_amount,
 voucher_discount_amount, point_discount_amount)
VALUES
    (1,  1,  1,  1,  DATE_ADD(CURDATE(), INTERVAL 3 DAY), 'CONFIRMED', 'SUBSCRIPTION',  NULL, DATE_SUB(NOW(), INTERVAL 2 DAY), NULL, NULL, NULL, true,  100000, 70000,  170000, 0,     0),
    (2,  2,  2,  3,  DATE_ADD(CURDATE(), INTERVAL 5 DAY), 'CONFIRMED', 'SUBSCRIPTION',  NULL, DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, NULL, NULL, true,  300000, 330000, 630000, 30000, 0),
    (3,  3,  3,  1,  DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'PENDING',   'SUBSCRIPTION',  NULL, NOW(),                           NULL, NULL, NULL, false, 100000, 0,      100000, 0,     0),
    (4,  5,  5,  2,  DATE_ADD(CURDATE(), INTERVAL 7 DAY), 'CONFIRMED', 'WALK_IN', NULL, DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, NULL, NULL, true,  150000, 0,      150000, 0,     0),
    (5,  6,  6,  1,  DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'PENDING',   'ADVANCE',  NULL, NOW(),                           NULL, NULL, NULL, false, 100000, 0,      100000, 0,     0),

    (6,  7,  7,  3,  CURDATE(), 'WASHING', 'ADVANCE',  3, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 1 HOUR),    NULL, NULL, true, 300000, 120000, 420000, 15000, 0),
    (7,  8,  8,  1,  CURDATE(), 'WASHING', 'WALK_IN', 1, NOW(),                           DATE_SUB(NOW(), INTERVAL 30 MINUTE), NULL, NULL, true, 100000, 0,      100000, 0,     0),
    (8,  9,  9,  2,  CURDATE(), 'CHECK_IN', 'ADVANCE',  5, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 45 MINUTE), NULL, NULL, true, 220000, 40000,  260000, 0,     0),
    (9,  10, 10, 1,  CURDATE(), 'WASHING',    'ADVANCE',  2, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 1 HOUR),    NULL, NULL, true, 100000, 90000,  190000, 0,     5000),
    (10, 11, 11, 2,  CURDATE(), 'WASHING',    'WALK_IN', 6, NOW(),                           DATE_SUB(NOW(), INTERVAL 40 MINUTE), NULL, NULL, true, 150000, 0,      150000, 0,     0),

    (11, 1,  1,  1,  DATE_SUB(CURDATE(), INTERVAL 10 DAY), 'COMPLETED', 'SUBSCRIPTION',  1, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 240 HOUR), DATE_SUB(NOW(), INTERVAL 239 HOUR), NULL, true, 100000, 50000,  150000, 0,     0),
    (12, 2,  2,  3,  DATE_SUB(CURDATE(), INTERVAL 7 DAY),  'COMPLETED', 'SUBSCRIPTION',  3, DATE_SUB(NOW(), INTERVAL 9 DAY),  DATE_SUB(NOW(), INTERVAL 168 HOUR), DATE_SUB(NOW(), INTERVAL 166 HOUR), NULL, true, 300000, 150000, 430000, 20000, 0),
    (13, 3,  3,  1,  DATE_SUB(CURDATE(), INTERVAL 15 DAY), 'COMPLETED', 'WALK_IN', 5, DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 360 HOUR), DATE_SUB(NOW(), INTERVAL 359 HOUR), NULL, true, 100000, 20000,  120000, 0,     0),
    (14, 12, 12, 3,  DATE_SUB(CURDATE(), INTERVAL 20 DAY), 'COMPLETED', 'SUBSCRIPTION',  8, DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 480 HOUR), DATE_SUB(NOW(), INTERVAL 478 HOUR), NULL, true, 180000, 220000, 400000, 0,     0),

    (15, 4,  4,  1,  DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'CANCELED', 'ADVANCE',  NULL, DATE_SUB(NOW(), INTERVAL 8 DAY), NULL, NULL, DATE_SUB(NOW(), INTERVAL 6 DAY), true,  100000, 0,      100000, 0, 0),
    (16, 5,  5,  3,  DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'CANCELED', 'SUBSCRIPTION',  NULL, DATE_SUB(NOW(), INTERVAL 5 DAY), NULL, NULL, DATE_SUB(NOW(), INTERVAL 4 DAY), false, 300000, 150000, 450000, 0, 0),
    (17, 6,  6,  1,  DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'CANCELED', 'WALK_IN', NULL, DATE_SUB(NOW(), INTERVAL 3 DAY), NULL, NULL, DATE_SUB(NOW(), INTERVAL 3 DAY), false, 100000, 0,      100000, 0, 0),

    (18, 7,  7,  1,  DATE_SUB(CURDATE(), INTERVAL 9 DAY), 'NO_SHOW', 'ADVANCE',  NULL, DATE_SUB(NOW(), INTERVAL 11 DAY), NULL, NULL, NULL, true, 100000, 0,      100000, 0, 0),
    (19, 8,  8,  3,  DATE_SUB(CURDATE(), INTERVAL 6 DAY), 'NO_SHOW', 'ADVANCE',  NULL, DATE_SUB(NOW(), INTERVAL 8 DAY),  NULL, NULL, NULL, true, 300000, 330000, 630000, 0, 0),
    (20, 9,  9,  1,  DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'NO_SHOW', 'WALK_IN', NULL, DATE_SUB(NOW(), INTERVAL 6 DAY),  NULL, NULL, NULL, true, 100000, 0,      100000, 0, 0),

    (99, 101, 203, 1, CURDATE(), 'NO_SHOW', 'ADVANCE', NULL, DATE_SUB(NOW(), INTERVAL 6 DAY),  NULL, NULL, NULL, true, 100000, 0, 100000, 0, 0),
    (20, 9,  9,  1,  DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'NO_SHOW', 'WALK_IN', NULL, DATE_SUB(NOW(), INTERVAL 6 DAY),  NULL, NULL, NULL, true, 100000, 0,      100000, 0, 0);

-- =====================================================================
-- TEST CHECK-IN FLOW (28-31) — booking CONFIRMED hôm nay tại station 2 (staff3@gmail.com),
-- CHƯA check-in. Staff scan biển số + confirm để sinh queue_ticket WAITING, rồi test queue
-- management. 3 ONLINE + 1 WALK_IN, tất cả đều có booking_id (không null).
-- Slot/allocation ở mục BOOKING SLOT (40-43) bên dưới.
-- =====================================================================
INSERT IGNORE INTO booking
(id, customer_id, vehicle_id, service_package_id,
 appointment_date, status, booking_type, check_in_employee_id,
 created_at, check_in_at, check_out_at, canceled_at,
 is_deposit_paid,
 total_service_amount, total_addon_amount, total_amount,
 voucher_discount_amount, point_discount_amount)
VALUES
    (28, 7,  7,  1, CURDATE(), 'CONFIRMED', 'ADVANCE',  NULL, DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, NULL, NULL, true,  100000, 0, 100000, 0, 0),
    (29, 8,  8,  2, CURDATE(), 'CONFIRMED', 'ADVANCE',  NULL, DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, NULL, NULL, true,  150000, 0, 150000, 0, 0),
    (30, 9,  9,  3, CURDATE(), 'CONFIRMED', 'ADVANCE',  NULL, DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, NULL, NULL, false, 300000, 0, 300000, 0, 0),
    (31, 10, 10, 1, CURDATE(), 'CONFIRMED', 'WALK_IN', NULL, NOW(),                           NULL, NULL, NULL, true,  100000, 0, 100000, 0, 0);

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
    (1,  1, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 3 DAY), 2, 'AVAILABLE'),
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
    (37, 3, '08:00', '08:15', 5, CURDATE(), 1, 'AVAILABLE'),
    (38, 3, '08:15', '08:30', 5, CURDATE(), 1, 'AVAILABLE'),
    -- booking 9 (station1, today, 1 slot)
    (39, 1, '08:15', '08:30', 5, CURDATE(), 1, 'AVAILABLE'),
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
    (30, 7, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 6 DAY), 0, 'AVAILABLE'),
    -- 3rd slot for Premium (required_slot=3) bookings 2,6,12,14,16,19
    (31, 2, '08:30', '08:45', 5, DATE_ADD(CURDATE(), INTERVAL 5 DAY),  1, 'AVAILABLE'),
    (32, 2, '08:30', '08:45', 5, CURDATE(),                             1, 'AVAILABLE'),
    (33, 2, '08:30', '08:45', 5, DATE_SUB(CURDATE(), INTERVAL 7 DAY),  1, 'COMPLETED'),
    (34, 6, '08:30', '08:45', 5, DATE_SUB(CURDATE(), INTERVAL 20 DAY), 1, 'COMPLETED'),
    (35, 2, '08:30', '08:45', 5, DATE_SUB(CURDATE(), INTERVAL 3 DAY),  0, 'AVAILABLE'),
    (36, 2, '08:30', '08:45', 5, DATE_SUB(CURDATE(), INTERVAL 6 DAY),  1, 'COMPLETED'),

    (10, 1, '16:00:00', '16:15:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (11, 1, '16:15:00', '16:30:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (12, 1, '16:30:00', '16:45:00', 5, CURDATE(), 0, 'AVAILABLE');

-- =====================================================================
-- BOOKING SLOT — LICH DAY DU 29/6 -> 10/7 (12 ngay, offset 0-11 ke tu
-- CURDATE()), tat ca 9 station, 08:00 -> 20:00, moi slot 15 phut.
-- Cac (station, ngay, gio) da ton tai o block tren (vi du booking 1-20,
-- TEST CHECK-IN FLOW...) duoc bo qua de khong tao slot trung.
-- id chay tiep tu 44 (id cu lon nhat la 43).
-- =====================================================================
INSERT IGNORE INTO booking_slot
(id, station_id, start_time, end_time, max_capacity, date, booked_count, status)
VALUES
    (44, 1, '08:30', '08:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (45, 1, '08:45', '09:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (46, 1, '09:00', '09:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (47, 1, '09:15', '09:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (48, 1, '09:30', '09:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (49, 1, '09:45', '10:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (50, 1, '10:00', '10:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (51, 1, '10:15', '10:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (52, 1, '10:30', '10:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (53, 1, '10:45', '11:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (54, 1, '11:00', '11:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (55, 1, '11:15', '11:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (56, 1, '11:30', '11:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (57, 1, '11:45', '12:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (58, 1, '12:00', '12:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (59, 1, '12:15', '12:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (60, 1, '12:30', '12:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (61, 1, '12:45', '13:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (62, 1, '13:00', '13:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (63, 1, '13:15', '13:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (64, 1, '13:30', '13:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (65, 1, '13:45', '14:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (66, 1, '14:00', '14:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (67, 1, '14:15', '14:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (68, 1, '14:30', '14:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (69, 1, '14:45', '15:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (70, 1, '15:00', '15:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (71, 1, '15:15', '15:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (72, 1, '15:30', '15:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (73, 1, '15:45', '16:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (74, 1, '16:45', '17:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (75, 1, '17:00', '17:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (76, 1, '17:15', '17:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (77, 1, '17:30', '17:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (78, 1, '17:45', '18:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (79, 1, '18:00', '18:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (80, 1, '18:15', '18:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (81, 1, '18:30', '18:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (82, 1, '18:45', '19:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (83, 1, '19:00', '19:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (84, 1, '19:15', '19:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (85, 1, '19:30', '19:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (86, 1, '19:45', '20:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (5300, 1, '21:00', '21:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (5301, 1, '21:15', '21:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (5302, 1, '21:30', '21:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (5303, 1, '21:45', '22:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (5304, 1, '22:00', '22:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (5305, 1, '22:15', '22:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (5306, 1, '22:30', '22:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (5307, 1, '22:45', '23:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (5308, 1, '23:00', '23:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (5309, 1, '23:15', '23:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (5310, 1, '23:30', '23:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (87, 2, '08:45', '09:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (88, 2, '09:00', '09:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (89, 2, '09:15', '09:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (90, 2, '09:30', '09:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (91, 2, '09:45', '10:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (92, 2, '10:00', '10:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (93, 2, '10:15', '10:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (94, 2, '10:30', '10:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (95, 2, '10:45', '11:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (96, 2, '11:00', '11:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (97, 2, '11:15', '11:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (98, 2, '11:30', '11:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (99, 2, '11:45', '12:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (100, 2, '12:00', '12:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (101, 2, '12:15', '12:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (102, 2, '12:30', '12:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (103, 2, '12:45', '13:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (104, 2, '13:00', '13:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (105, 2, '13:15', '13:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (106, 2, '13:30', '13:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (107, 2, '13:45', '14:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (108, 2, '14:00', '14:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (109, 2, '14:15', '14:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (110, 2, '14:30', '14:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (111, 2, '14:45', '15:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (112, 2, '15:00', '15:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (113, 2, '15:15', '15:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (114, 2, '15:30', '15:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (115, 2, '15:45', '16:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (116, 2, '16:00', '16:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (117, 2, '16:15', '16:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (118, 2, '16:30', '16:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (119, 2, '16:45', '17:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (120, 2, '17:00', '17:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (121, 2, '17:15', '17:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (122, 2, '17:30', '17:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (123, 2, '17:45', '18:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (124, 2, '18:00', '18:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (125, 2, '18:15', '18:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (126, 2, '18:30', '18:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (127, 2, '18:45', '19:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (128, 2, '19:00', '19:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (129, 2, '19:15', '19:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (130, 2, '19:30', '19:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (131, 2, '19:45', '20:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (132, 3, '08:30', '08:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (133, 3, '08:45', '09:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (134, 3, '09:00', '09:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (135, 3, '09:15', '09:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (136, 3, '09:30', '09:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (137, 3, '09:45', '10:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (138, 3, '10:00', '10:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (139, 3, '10:15', '10:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (140, 3, '10:30', '10:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (141, 3, '10:45', '11:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (142, 3, '11:00', '11:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (143, 3, '11:15', '11:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (144, 3, '11:30', '11:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (145, 3, '11:45', '12:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (146, 3, '12:00', '12:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (147, 3, '12:15', '12:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (148, 3, '12:30', '12:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (149, 3, '12:45', '13:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (150, 3, '13:00', '13:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (151, 3, '13:15', '13:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (152, 3, '13:30', '13:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (153, 3, '13:45', '14:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (154, 3, '14:00', '14:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (155, 3, '14:15', '14:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (156, 3, '14:30', '14:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (157, 3, '14:45', '15:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (158, 3, '15:00', '15:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (159, 3, '15:15', '15:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (160, 3, '15:30', '15:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (161, 3, '15:45', '16:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (162, 3, '16:00', '16:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (163, 3, '16:15', '16:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (164, 3, '16:30', '16:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (165, 3, '16:45', '17:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (166, 3, '17:00', '17:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (167, 3, '17:15', '17:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (168, 3, '17:30', '17:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (169, 3, '17:45', '18:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (170, 3, '18:00', '18:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (171, 3, '18:15', '18:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (172, 3, '18:30', '18:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (173, 3, '18:45', '19:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (174, 3, '19:00', '19:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (175, 3, '19:15', '19:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (176, 3, '19:30', '19:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (177, 3, '19:45', '20:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (178, 4, '08:15', '08:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (179, 4, '08:30', '08:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (180, 4, '08:45', '09:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (181, 4, '09:00', '09:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (182, 4, '09:15', '09:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (183, 4, '09:30', '09:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (184, 4, '09:45', '10:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (185, 4, '10:00', '10:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (186, 4, '10:15', '10:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (187, 4, '10:30', '10:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (188, 4, '10:45', '11:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (189, 4, '11:00', '11:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (190, 4, '11:15', '11:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (191, 4, '11:30', '11:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (192, 4, '11:45', '12:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (193, 4, '12:00', '12:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (194, 4, '12:15', '12:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (195, 4, '12:30', '12:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (196, 4, '12:45', '13:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (197, 4, '13:00', '13:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (198, 4, '13:15', '13:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (199, 4, '13:30', '13:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (200, 4, '13:45', '14:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (201, 4, '14:00', '14:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (202, 4, '14:15', '14:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (203, 4, '14:30', '14:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (204, 4, '14:45', '15:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (205, 4, '15:00', '15:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (206, 4, '15:15', '15:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (207, 4, '15:30', '15:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (208, 4, '15:45', '16:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (209, 4, '16:00', '16:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (210, 4, '16:15', '16:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (211, 4, '16:30', '16:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (212, 4, '16:45', '17:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (213, 4, '17:00', '17:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (214, 4, '17:15', '17:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (215, 4, '17:30', '17:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (216, 4, '17:45', '18:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (217, 4, '18:00', '18:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (218, 4, '18:15', '18:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (219, 4, '18:30', '18:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (220, 4, '18:45', '19:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (221, 4, '19:00', '19:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (222, 4, '19:15', '19:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (223, 4, '19:30', '19:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (224, 4, '19:45', '20:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (225, 5, '08:00', '08:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (226, 5, '08:15', '08:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (227, 5, '08:30', '08:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (228, 5, '08:45', '09:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (229, 5, '09:00', '09:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (230, 5, '09:15', '09:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (231, 5, '09:30', '09:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (232, 5, '09:45', '10:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (233, 5, '10:00', '10:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (234, 5, '10:15', '10:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (235, 5, '10:30', '10:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (236, 5, '10:45', '11:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (237, 5, '11:00', '11:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (238, 5, '11:15', '11:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (239, 5, '11:30', '11:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (240, 5, '11:45', '12:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (241, 5, '12:00', '12:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (242, 5, '12:15', '12:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (243, 5, '12:30', '12:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (244, 5, '12:45', '13:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (245, 5, '13:00', '13:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (246, 5, '13:15', '13:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (247, 5, '13:30', '13:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (248, 5, '13:45', '14:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (249, 5, '14:00', '14:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (250, 5, '14:15', '14:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (251, 5, '14:30', '14:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (252, 5, '14:45', '15:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (253, 5, '15:00', '15:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (254, 5, '15:15', '15:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (255, 5, '15:30', '15:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (256, 5, '15:45', '16:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (257, 5, '16:00', '16:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (258, 5, '16:15', '16:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (259, 5, '16:30', '16:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (260, 5, '16:45', '17:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (261, 5, '17:00', '17:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (262, 5, '17:15', '17:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (263, 5, '17:30', '17:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (264, 5, '17:45', '18:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (265, 5, '18:00', '18:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (266, 5, '18:15', '18:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (267, 5, '18:30', '18:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (268, 5, '18:45', '19:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (269, 5, '19:00', '19:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (270, 5, '19:15', '19:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (271, 5, '19:30', '19:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (272, 5, '19:45', '20:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (273, 6, '08:00', '08:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (274, 6, '08:15', '08:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (275, 6, '08:30', '08:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (276, 6, '08:45', '09:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (277, 6, '09:00', '09:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (278, 6, '09:15', '09:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (279, 6, '09:30', '09:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (280, 6, '09:45', '10:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (281, 6, '10:00', '10:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (282, 6, '10:15', '10:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (283, 6, '10:30', '10:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (284, 6, '10:45', '11:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (285, 6, '11:00', '11:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (286, 6, '11:15', '11:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (287, 6, '11:30', '11:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (288, 6, '11:45', '12:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (289, 6, '12:00', '12:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (290, 6, '12:15', '12:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (291, 6, '12:30', '12:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (292, 6, '12:45', '13:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (293, 6, '13:00', '13:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (294, 6, '13:15', '13:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (295, 6, '13:30', '13:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (296, 6, '13:45', '14:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (297, 6, '14:00', '14:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (298, 6, '14:15', '14:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (299, 6, '14:30', '14:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (300, 6, '14:45', '15:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (301, 6, '15:00', '15:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (302, 6, '15:15', '15:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (303, 6, '15:30', '15:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (304, 6, '15:45', '16:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (305, 6, '16:00', '16:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (306, 6, '16:15', '16:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (307, 6, '16:30', '16:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (308, 6, '16:45', '17:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (309, 6, '17:00', '17:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (310, 6, '17:15', '17:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (311, 6, '17:30', '17:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (312, 6, '17:45', '18:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (313, 6, '18:00', '18:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (314, 6, '18:15', '18:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (315, 6, '18:30', '18:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (316, 6, '18:45', '19:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (317, 6, '19:00', '19:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (318, 6, '19:15', '19:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (319, 6, '19:30', '19:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (320, 6, '19:45', '20:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (321, 7, '08:00', '08:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (322, 7, '08:15', '08:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (323, 7, '08:30', '08:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (324, 7, '08:45', '09:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (325, 7, '09:00', '09:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (326, 7, '09:15', '09:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (327, 7, '09:30', '09:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (328, 7, '09:45', '10:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (329, 7, '10:00', '10:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (330, 7, '10:15', '10:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (331, 7, '10:30', '10:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (332, 7, '10:45', '11:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (333, 7, '11:00', '11:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (334, 7, '11:15', '11:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (335, 7, '11:30', '11:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (336, 7, '11:45', '12:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (337, 7, '12:00', '12:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (338, 7, '12:15', '12:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (339, 7, '12:30', '12:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (340, 7, '12:45', '13:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (341, 7, '13:00', '13:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (342, 7, '13:15', '13:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (343, 7, '13:30', '13:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (344, 7, '13:45', '14:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (345, 7, '14:00', '14:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (346, 7, '14:15', '14:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (347, 7, '14:30', '14:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (348, 7, '14:45', '15:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (349, 7, '15:00', '15:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (350, 7, '15:15', '15:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (351, 7, '15:30', '15:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (352, 7, '15:45', '16:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (353, 7, '16:00', '16:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (354, 7, '16:15', '16:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (355, 7, '16:30', '16:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (356, 7, '16:45', '17:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (357, 7, '17:00', '17:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (358, 7, '17:15', '17:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (359, 7, '17:30', '17:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (360, 7, '17:45', '18:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (361, 7, '18:00', '18:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (362, 7, '18:15', '18:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (363, 7, '18:30', '18:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (364, 7, '18:45', '19:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (365, 7, '19:00', '19:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (366, 7, '19:15', '19:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (367, 7, '19:30', '19:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (368, 7, '19:45', '20:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (369, 8, '08:00', '08:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (370, 8, '08:15', '08:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (371, 8, '08:30', '08:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (372, 8, '08:45', '09:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (373, 8, '09:00', '09:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (374, 8, '09:15', '09:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (375, 8, '09:30', '09:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (376, 8, '09:45', '10:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (377, 8, '10:00', '10:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (378, 8, '10:15', '10:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (379, 8, '10:30', '10:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (380, 8, '10:45', '11:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (381, 8, '11:00', '11:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (382, 8, '11:15', '11:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (383, 8, '11:30', '11:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (384, 8, '11:45', '12:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (385, 8, '12:00', '12:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (386, 8, '12:15', '12:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (387, 8, '12:30', '12:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (388, 8, '12:45', '13:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (389, 8, '13:00', '13:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (390, 8, '13:15', '13:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (391, 8, '13:30', '13:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (392, 8, '13:45', '14:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (393, 8, '14:00', '14:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (394, 8, '14:15', '14:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (395, 8, '14:30', '14:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (396, 8, '14:45', '15:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (397, 8, '15:00', '15:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (398, 8, '15:15', '15:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (399, 8, '15:30', '15:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (400, 8, '15:45', '16:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (401, 8, '16:00', '16:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (402, 8, '16:15', '16:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (403, 8, '16:30', '16:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (404, 8, '16:45', '17:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (405, 8, '17:00', '17:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (406, 8, '17:15', '17:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (407, 8, '17:30', '17:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (408, 8, '17:45', '18:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (409, 8, '18:00', '18:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (410, 8, '18:15', '18:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (411, 8, '18:30', '18:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (412, 8, '18:45', '19:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (413, 8, '19:00', '19:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (414, 8, '19:15', '19:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (415, 8, '19:30', '19:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (416, 8, '19:45', '20:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (417, 9, '08:00', '08:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (418, 9, '08:15', '08:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (419, 9, '08:30', '08:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (420, 9, '08:45', '09:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (421, 9, '09:00', '09:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (422, 9, '09:15', '09:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (423, 9, '09:30', '09:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (424, 9, '09:45', '10:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (425, 9, '10:00', '10:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (426, 9, '10:15', '10:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (427, 9, '10:30', '10:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (428, 9, '10:45', '11:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (429, 9, '11:00', '11:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (430, 9, '11:15', '11:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (431, 9, '11:30', '11:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (432, 9, '11:45', '12:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (433, 9, '12:00', '12:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (434, 9, '12:15', '12:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (435, 9, '12:30', '12:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (436, 9, '12:45', '13:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (437, 9, '13:00', '13:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (438, 9, '13:15', '13:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (439, 9, '13:30', '13:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (440, 9, '13:45', '14:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (441, 9, '14:00', '14:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (442, 9, '14:15', '14:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (443, 9, '14:30', '14:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (444, 9, '14:45', '15:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (445, 9, '15:00', '15:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (446, 9, '15:15', '15:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (447, 9, '15:30', '15:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (448, 9, '15:45', '16:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (449, 9, '16:00', '16:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (450, 9, '16:15', '16:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (451, 9, '16:30', '16:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (452, 9, '16:45', '17:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (453, 9, '17:00', '17:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (454, 9, '17:15', '17:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (455, 9, '17:30', '17:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (456, 9, '17:45', '18:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (457, 9, '18:00', '18:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (458, 9, '18:15', '18:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (459, 9, '18:30', '18:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (460, 9, '18:45', '19:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (461, 9, '19:00', '19:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (462, 9, '19:15', '19:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (463, 9, '19:30', '19:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (464, 9, '19:45', '20:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (465, 1, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (466, 1, '08:30', '08:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (467, 1, '08:45', '09:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (468, 1, '09:00', '09:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (469, 1, '09:15', '09:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (470, 1, '09:30', '09:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (471, 1, '09:45', '10:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (472, 1, '10:00', '10:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (473, 1, '10:15', '10:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (474, 1, '10:30', '10:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (475, 1, '10:45', '11:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (476, 1, '11:00', '11:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (477, 1, '11:15', '11:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (478, 1, '11:30', '11:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (479, 1, '11:45', '12:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (480, 1, '12:00', '12:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (481, 1, '12:15', '12:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (482, 1, '12:30', '12:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (483, 1, '12:45', '13:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (484, 1, '13:00', '13:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (485, 1, '13:15', '13:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (486, 1, '13:30', '13:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (487, 1, '13:45', '14:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (488, 1, '14:00', '14:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (489, 1, '14:15', '14:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (490, 1, '14:30', '14:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (491, 1, '14:45', '15:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (492, 1, '15:00', '15:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (493, 1, '15:15', '15:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (494, 1, '15:30', '15:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (495, 1, '15:45', '16:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (496, 1, '16:00', '16:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (497, 1, '16:15', '16:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (498, 1, '16:30', '16:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (499, 1, '16:45', '17:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (500, 1, '17:00', '17:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (501, 1, '17:15', '17:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (502, 1, '17:30', '17:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (503, 1, '17:45', '18:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (504, 1, '18:00', '18:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (505, 1, '18:15', '18:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (506, 1, '18:30', '18:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (507, 1, '18:45', '19:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (508, 1, '19:00', '19:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (509, 1, '19:15', '19:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (510, 1, '19:30', '19:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (511, 1, '19:45', '20:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (512, 2, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (513, 2, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (514, 2, '08:30', '08:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (515, 2, '08:45', '09:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (516, 2, '09:00', '09:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (517, 2, '09:15', '09:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (518, 2, '09:30', '09:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (519, 2, '09:45', '10:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (520, 2, '10:00', '10:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (521, 2, '10:15', '10:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (522, 2, '10:30', '10:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (523, 2, '10:45', '11:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (524, 2, '11:00', '11:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (525, 2, '11:15', '11:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (526, 2, '11:30', '11:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (527, 2, '11:45', '12:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (528, 2, '12:00', '12:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (529, 2, '12:15', '12:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (530, 2, '12:30', '12:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (531, 2, '12:45', '13:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (532, 2, '13:00', '13:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (533, 2, '13:15', '13:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (534, 2, '13:30', '13:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (535, 2, '13:45', '14:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (536, 2, '14:00', '14:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (537, 2, '14:15', '14:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (538, 2, '14:30', '14:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (539, 2, '14:45', '15:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (540, 2, '15:00', '15:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (541, 2, '15:15', '15:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (542, 2, '15:30', '15:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (543, 2, '15:45', '16:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (544, 2, '16:00', '16:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (545, 2, '16:15', '16:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (546, 2, '16:30', '16:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (547, 2, '16:45', '17:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (548, 2, '17:00', '17:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (549, 2, '17:15', '17:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (550, 2, '17:30', '17:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (551, 2, '17:45', '18:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (552, 2, '18:00', '18:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (553, 2, '18:15', '18:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (554, 2, '18:30', '18:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (555, 2, '18:45', '19:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (556, 2, '19:00', '19:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (557, 2, '19:15', '19:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (558, 2, '19:30', '19:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (559, 2, '19:45', '20:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (560, 3, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (561, 3, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (562, 3, '08:30', '08:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (563, 3, '08:45', '09:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (564, 3, '09:00', '09:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (565, 3, '09:15', '09:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (566, 3, '09:30', '09:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (567, 3, '09:45', '10:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (568, 3, '10:00', '10:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (569, 3, '10:15', '10:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (570, 3, '10:30', '10:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (571, 3, '10:45', '11:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (572, 3, '11:00', '11:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (573, 3, '11:15', '11:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (574, 3, '11:30', '11:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (575, 3, '11:45', '12:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (576, 3, '12:00', '12:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (577, 3, '12:15', '12:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (578, 3, '12:30', '12:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (579, 3, '12:45', '13:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (580, 3, '13:00', '13:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (581, 3, '13:15', '13:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (582, 3, '13:30', '13:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (583, 3, '13:45', '14:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (584, 3, '14:00', '14:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (585, 3, '14:15', '14:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (586, 3, '14:30', '14:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (587, 3, '14:45', '15:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (588, 3, '15:00', '15:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (589, 3, '15:15', '15:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (590, 3, '15:30', '15:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (591, 3, '15:45', '16:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (592, 3, '16:00', '16:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (593, 3, '16:15', '16:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (594, 3, '16:30', '16:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (595, 3, '16:45', '17:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (596, 3, '17:00', '17:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (597, 3, '17:15', '17:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (598, 3, '17:30', '17:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (599, 3, '17:45', '18:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (600, 3, '18:00', '18:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (601, 3, '18:15', '18:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (602, 3, '18:30', '18:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (603, 3, '18:45', '19:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (604, 3, '19:00', '19:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (605, 3, '19:15', '19:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (606, 3, '19:30', '19:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (607, 3, '19:45', '20:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (608, 4, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (609, 4, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (610, 4, '08:30', '08:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (611, 4, '08:45', '09:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (612, 4, '09:00', '09:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (613, 4, '09:15', '09:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (614, 4, '09:30', '09:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (615, 4, '09:45', '10:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (616, 4, '10:00', '10:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (617, 4, '10:15', '10:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (618, 4, '10:30', '10:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (619, 4, '10:45', '11:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (620, 4, '11:00', '11:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (621, 4, '11:15', '11:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (622, 4, '11:30', '11:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (623, 4, '11:45', '12:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (624, 4, '12:00', '12:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (625, 4, '12:15', '12:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (626, 4, '12:30', '12:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (627, 4, '12:45', '13:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (628, 4, '13:00', '13:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (629, 4, '13:15', '13:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (630, 4, '13:30', '13:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (631, 4, '13:45', '14:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (632, 4, '14:00', '14:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (633, 4, '14:15', '14:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (634, 4, '14:30', '14:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (635, 4, '14:45', '15:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (636, 4, '15:00', '15:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (637, 4, '15:15', '15:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (638, 4, '15:30', '15:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (639, 4, '15:45', '16:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (640, 4, '16:00', '16:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (641, 4, '16:15', '16:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (642, 4, '16:30', '16:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (643, 4, '16:45', '17:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (644, 4, '17:00', '17:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (645, 4, '17:15', '17:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (646, 4, '17:30', '17:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (647, 4, '17:45', '18:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (648, 4, '18:00', '18:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (649, 4, '18:15', '18:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (650, 4, '18:30', '18:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (651, 4, '18:45', '19:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (652, 4, '19:00', '19:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (653, 4, '19:15', '19:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (654, 4, '19:30', '19:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (655, 4, '19:45', '20:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (656, 5, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (657, 5, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (658, 5, '08:30', '08:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (659, 5, '08:45', '09:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (660, 5, '09:00', '09:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (661, 5, '09:15', '09:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (662, 5, '09:30', '09:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (663, 5, '09:45', '10:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (664, 5, '10:00', '10:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (665, 5, '10:15', '10:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (666, 5, '10:30', '10:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (667, 5, '10:45', '11:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (668, 5, '11:00', '11:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (669, 5, '11:15', '11:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (670, 5, '11:30', '11:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (671, 5, '11:45', '12:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (672, 5, '12:00', '12:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (673, 5, '12:15', '12:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (674, 5, '12:30', '12:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (675, 5, '12:45', '13:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (676, 5, '13:00', '13:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (677, 5, '13:15', '13:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (678, 5, '13:30', '13:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (679, 5, '13:45', '14:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (680, 5, '14:00', '14:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (681, 5, '14:15', '14:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (682, 5, '14:30', '14:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (683, 5, '14:45', '15:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (684, 5, '15:00', '15:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (685, 5, '15:15', '15:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (686, 5, '15:30', '15:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (687, 5, '15:45', '16:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (688, 5, '16:00', '16:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (689, 5, '16:15', '16:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (690, 5, '16:30', '16:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (691, 5, '16:45', '17:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (692, 5, '17:00', '17:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (693, 5, '17:15', '17:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (694, 5, '17:30', '17:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (695, 5, '17:45', '18:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (696, 5, '18:00', '18:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (697, 5, '18:15', '18:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (698, 5, '18:30', '18:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (699, 5, '18:45', '19:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (700, 5, '19:00', '19:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (701, 5, '19:15', '19:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (702, 5, '19:30', '19:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (703, 5, '19:45', '20:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (704, 6, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (705, 6, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (706, 6, '08:30', '08:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (707, 6, '08:45', '09:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (708, 6, '09:00', '09:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (709, 6, '09:15', '09:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (710, 6, '09:30', '09:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (711, 6, '09:45', '10:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (712, 6, '10:00', '10:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (713, 6, '10:15', '10:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (714, 6, '10:30', '10:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (715, 6, '10:45', '11:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (716, 6, '11:00', '11:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (717, 6, '11:15', '11:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (718, 6, '11:30', '11:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (719, 6, '11:45', '12:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (720, 6, '12:00', '12:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (721, 6, '12:15', '12:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (722, 6, '12:30', '12:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (723, 6, '12:45', '13:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (724, 6, '13:00', '13:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (725, 6, '13:15', '13:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (726, 6, '13:30', '13:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (727, 6, '13:45', '14:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (728, 6, '14:00', '14:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (729, 6, '14:15', '14:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (730, 6, '14:30', '14:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (731, 6, '14:45', '15:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (732, 6, '15:00', '15:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (733, 6, '15:15', '15:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (734, 6, '15:30', '15:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (735, 6, '15:45', '16:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (736, 6, '16:00', '16:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (737, 6, '16:15', '16:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (738, 6, '16:30', '16:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (739, 6, '16:45', '17:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (740, 6, '17:00', '17:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (741, 6, '17:15', '17:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (742, 6, '17:30', '17:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (743, 6, '17:45', '18:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (744, 6, '18:00', '18:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (745, 6, '18:15', '18:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (746, 6, '18:30', '18:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (747, 6, '18:45', '19:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (748, 6, '19:00', '19:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (749, 6, '19:15', '19:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (750, 6, '19:30', '19:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (751, 6, '19:45', '20:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (752, 7, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (753, 7, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (754, 7, '08:30', '08:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (755, 7, '08:45', '09:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (756, 7, '09:00', '09:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (757, 7, '09:15', '09:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (758, 7, '09:30', '09:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (759, 7, '09:45', '10:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (760, 7, '10:00', '10:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (761, 7, '10:15', '10:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (762, 7, '10:30', '10:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (763, 7, '10:45', '11:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (764, 7, '11:00', '11:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (765, 7, '11:15', '11:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (766, 7, '11:30', '11:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (767, 7, '11:45', '12:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (768, 7, '12:00', '12:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (769, 7, '12:15', '12:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (770, 7, '12:30', '12:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (771, 7, '12:45', '13:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (772, 7, '13:00', '13:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (773, 7, '13:15', '13:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (774, 7, '13:30', '13:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (775, 7, '13:45', '14:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (776, 7, '14:00', '14:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (777, 7, '14:15', '14:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (778, 7, '14:30', '14:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (779, 7, '14:45', '15:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (780, 7, '15:00', '15:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (781, 7, '15:15', '15:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (782, 7, '15:30', '15:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (783, 7, '15:45', '16:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (784, 7, '16:00', '16:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (785, 7, '16:15', '16:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (786, 7, '16:30', '16:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (787, 7, '16:45', '17:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (788, 7, '17:00', '17:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (789, 7, '17:15', '17:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (790, 7, '17:30', '17:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (791, 7, '17:45', '18:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (792, 7, '18:00', '18:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (793, 7, '18:15', '18:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (794, 7, '18:30', '18:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (795, 7, '18:45', '19:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (796, 7, '19:00', '19:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (797, 7, '19:15', '19:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (798, 7, '19:30', '19:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (799, 7, '19:45', '20:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (800, 8, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (801, 8, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (802, 8, '08:30', '08:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (803, 8, '08:45', '09:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (804, 8, '09:00', '09:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (805, 8, '09:15', '09:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (806, 8, '09:30', '09:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (807, 8, '09:45', '10:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (808, 8, '10:00', '10:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (809, 8, '10:15', '10:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (810, 8, '10:30', '10:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (811, 8, '10:45', '11:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (812, 8, '11:00', '11:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (813, 8, '11:15', '11:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (814, 8, '11:30', '11:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (815, 8, '11:45', '12:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (816, 8, '12:00', '12:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (817, 8, '12:15', '12:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (818, 8, '12:30', '12:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (819, 8, '12:45', '13:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (820, 8, '13:00', '13:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (821, 8, '13:15', '13:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (822, 8, '13:30', '13:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (823, 8, '13:45', '14:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (824, 8, '14:00', '14:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (825, 8, '14:15', '14:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (826, 8, '14:30', '14:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (827, 8, '14:45', '15:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (828, 8, '15:00', '15:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (829, 8, '15:15', '15:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (830, 8, '15:30', '15:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (831, 8, '15:45', '16:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (832, 8, '16:00', '16:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (833, 8, '16:15', '16:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (834, 8, '16:30', '16:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (835, 8, '16:45', '17:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (836, 8, '17:00', '17:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (837, 8, '17:15', '17:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (838, 8, '17:30', '17:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (839, 8, '17:45', '18:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (840, 8, '18:00', '18:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (841, 8, '18:15', '18:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (842, 8, '18:30', '18:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (843, 8, '18:45', '19:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (844, 8, '19:00', '19:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (845, 8, '19:15', '19:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (846, 8, '19:30', '19:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (847, 8, '19:45', '20:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (848, 9, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (849, 9, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (850, 9, '08:30', '08:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (851, 9, '08:45', '09:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (852, 9, '09:00', '09:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (853, 9, '09:15', '09:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (854, 9, '09:30', '09:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (855, 9, '09:45', '10:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (856, 9, '10:00', '10:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (857, 9, '10:15', '10:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (858, 9, '10:30', '10:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (859, 9, '10:45', '11:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (860, 9, '11:00', '11:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (861, 9, '11:15', '11:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (862, 9, '11:30', '11:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (863, 9, '11:45', '12:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (864, 9, '12:00', '12:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (865, 9, '12:15', '12:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (866, 9, '12:30', '12:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (867, 9, '12:45', '13:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (868, 9, '13:00', '13:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (869, 9, '13:15', '13:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (870, 9, '13:30', '13:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (871, 9, '13:45', '14:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (872, 9, '14:00', '14:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (873, 9, '14:15', '14:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (874, 9, '14:30', '14:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (875, 9, '14:45', '15:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (876, 9, '15:00', '15:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (877, 9, '15:15', '15:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (878, 9, '15:30', '15:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (879, 9, '15:45', '16:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (880, 9, '16:00', '16:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (881, 9, '16:15', '16:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (882, 9, '16:30', '16:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (883, 9, '16:45', '17:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (884, 9, '17:00', '17:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (885, 9, '17:15', '17:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (886, 9, '17:30', '17:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (887, 9, '17:45', '18:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (888, 9, '18:00', '18:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (889, 9, '18:15', '18:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (890, 9, '18:30', '18:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (891, 9, '18:45', '19:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (892, 9, '19:00', '19:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (893, 9, '19:15', '19:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (894, 9, '19:30', '19:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (895, 9, '19:45', '20:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (896, 1, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (897, 1, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (898, 1, '08:30', '08:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (899, 1, '08:45', '09:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (900, 1, '09:00', '09:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (901, 1, '09:15', '09:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (902, 1, '09:30', '09:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (903, 1, '09:45', '10:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (904, 1, '10:00', '10:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (905, 1, '10:15', '10:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (906, 1, '10:30', '10:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (907, 1, '10:45', '11:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (908, 1, '11:00', '11:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (909, 1, '11:15', '11:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (910, 1, '11:30', '11:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (911, 1, '11:45', '12:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (912, 1, '12:00', '12:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (913, 1, '12:15', '12:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (914, 1, '12:30', '12:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (915, 1, '12:45', '13:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (916, 1, '13:00', '13:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (917, 1, '13:15', '13:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (918, 1, '13:30', '13:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (919, 1, '13:45', '14:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (920, 1, '14:00', '14:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (921, 1, '14:15', '14:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (922, 1, '14:30', '14:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (923, 1, '14:45', '15:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (924, 1, '15:00', '15:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (925, 1, '15:15', '15:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (926, 1, '15:30', '15:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (927, 1, '15:45', '16:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (928, 1, '16:00', '16:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (929, 1, '16:15', '16:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (930, 1, '16:30', '16:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (931, 1, '16:45', '17:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (932, 1, '17:00', '17:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (933, 1, '17:15', '17:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (934, 1, '17:30', '17:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (935, 1, '17:45', '18:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (936, 1, '18:00', '18:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (937, 1, '18:15', '18:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (938, 1, '18:30', '18:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (939, 1, '18:45', '19:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (940, 1, '19:00', '19:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (941, 1, '19:15', '19:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (942, 1, '19:30', '19:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (943, 1, '19:45', '20:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (944, 2, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (945, 2, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (946, 2, '08:30', '08:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (947, 2, '08:45', '09:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (948, 2, '09:00', '09:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (949, 2, '09:15', '09:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (950, 2, '09:30', '09:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (951, 2, '09:45', '10:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (952, 2, '10:00', '10:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (953, 2, '10:15', '10:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (954, 2, '10:30', '10:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (955, 2, '10:45', '11:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (956, 2, '11:00', '11:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (957, 2, '11:15', '11:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (958, 2, '11:30', '11:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (959, 2, '11:45', '12:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (960, 2, '12:00', '12:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (961, 2, '12:15', '12:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (962, 2, '12:30', '12:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (963, 2, '12:45', '13:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (964, 2, '13:00', '13:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (965, 2, '13:15', '13:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (966, 2, '13:30', '13:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (967, 2, '13:45', '14:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (968, 2, '14:00', '14:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (969, 2, '14:15', '14:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (970, 2, '14:30', '14:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (971, 2, '14:45', '15:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (972, 2, '15:00', '15:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (973, 2, '15:15', '15:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (974, 2, '15:30', '15:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (975, 2, '15:45', '16:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (976, 2, '16:00', '16:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (977, 2, '16:15', '16:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (978, 2, '16:30', '16:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (979, 2, '16:45', '17:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (980, 2, '17:00', '17:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (981, 2, '17:15', '17:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (982, 2, '17:30', '17:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (983, 2, '17:45', '18:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (984, 2, '18:00', '18:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (985, 2, '18:15', '18:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (986, 2, '18:30', '18:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (987, 2, '18:45', '19:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (988, 2, '19:00', '19:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (989, 2, '19:15', '19:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (990, 2, '19:30', '19:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (991, 2, '19:45', '20:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (992, 3, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (993, 3, '08:30', '08:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (994, 3, '08:45', '09:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (995, 3, '09:00', '09:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (996, 3, '09:15', '09:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (997, 3, '09:30', '09:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (998, 3, '09:45', '10:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (999, 3, '10:00', '10:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1000, 3, '10:15', '10:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1001, 3, '10:30', '10:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1002, 3, '10:45', '11:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1003, 3, '11:00', '11:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1004, 3, '11:15', '11:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1005, 3, '11:30', '11:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1006, 3, '11:45', '12:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1007, 3, '12:00', '12:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1008, 3, '12:15', '12:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1009, 3, '12:30', '12:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1010, 3, '12:45', '13:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1011, 3, '13:00', '13:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1012, 3, '13:15', '13:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1013, 3, '13:30', '13:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1014, 3, '13:45', '14:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1015, 3, '14:00', '14:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1016, 3, '14:15', '14:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1017, 3, '14:30', '14:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1018, 3, '14:45', '15:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1019, 3, '15:00', '15:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1020, 3, '15:15', '15:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1021, 3, '15:30', '15:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1022, 3, '15:45', '16:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1023, 3, '16:00', '16:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1024, 3, '16:15', '16:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1025, 3, '16:30', '16:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1026, 3, '16:45', '17:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1027, 3, '17:00', '17:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1028, 3, '17:15', '17:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1029, 3, '17:30', '17:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1030, 3, '17:45', '18:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1031, 3, '18:00', '18:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1032, 3, '18:15', '18:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1033, 3, '18:30', '18:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1034, 3, '18:45', '19:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1035, 3, '19:00', '19:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1036, 3, '19:15', '19:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1037, 3, '19:30', '19:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1038, 3, '19:45', '20:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1039, 4, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1040, 4, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1041, 4, '08:30', '08:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1042, 4, '08:45', '09:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1043, 4, '09:00', '09:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1044, 4, '09:15', '09:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1045, 4, '09:30', '09:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1046, 4, '09:45', '10:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1047, 4, '10:00', '10:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1048, 4, '10:15', '10:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1049, 4, '10:30', '10:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1050, 4, '10:45', '11:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1051, 4, '11:00', '11:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1052, 4, '11:15', '11:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1053, 4, '11:30', '11:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1054, 4, '11:45', '12:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1055, 4, '12:00', '12:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1056, 4, '12:15', '12:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1057, 4, '12:30', '12:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1058, 4, '12:45', '13:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1059, 4, '13:00', '13:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1060, 4, '13:15', '13:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1061, 4, '13:30', '13:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1062, 4, '13:45', '14:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1063, 4, '14:00', '14:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1064, 4, '14:15', '14:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1065, 4, '14:30', '14:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1066, 4, '14:45', '15:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1067, 4, '15:00', '15:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1068, 4, '15:15', '15:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1069, 4, '15:30', '15:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1070, 4, '15:45', '16:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1071, 4, '16:00', '16:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1072, 4, '16:15', '16:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1073, 4, '16:30', '16:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1074, 4, '16:45', '17:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1075, 4, '17:00', '17:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1076, 4, '17:15', '17:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1077, 4, '17:30', '17:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1078, 4, '17:45', '18:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1079, 4, '18:00', '18:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1080, 4, '18:15', '18:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1081, 4, '18:30', '18:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1082, 4, '18:45', '19:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1083, 4, '19:00', '19:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1084, 4, '19:15', '19:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1085, 4, '19:30', '19:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1086, 4, '19:45', '20:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1087, 5, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1088, 5, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1089, 5, '08:30', '08:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1090, 5, '08:45', '09:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1091, 5, '09:00', '09:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1092, 5, '09:15', '09:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1093, 5, '09:30', '09:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1094, 5, '09:45', '10:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1095, 5, '10:00', '10:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1096, 5, '10:15', '10:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1097, 5, '10:30', '10:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1098, 5, '10:45', '11:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1099, 5, '11:00', '11:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1100, 5, '11:15', '11:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1101, 5, '11:30', '11:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1102, 5, '11:45', '12:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1103, 5, '12:00', '12:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1104, 5, '12:15', '12:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1105, 5, '12:30', '12:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1106, 5, '12:45', '13:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1107, 5, '13:00', '13:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1108, 5, '13:15', '13:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1109, 5, '13:30', '13:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1110, 5, '13:45', '14:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1111, 5, '14:00', '14:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1112, 5, '14:15', '14:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1113, 5, '14:30', '14:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1114, 5, '14:45', '15:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1115, 5, '15:00', '15:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1116, 5, '15:15', '15:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1117, 5, '15:30', '15:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1118, 5, '15:45', '16:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1119, 5, '16:00', '16:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1120, 5, '16:15', '16:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1121, 5, '16:30', '16:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1122, 5, '16:45', '17:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1123, 5, '17:00', '17:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1124, 5, '17:15', '17:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1125, 5, '17:30', '17:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1126, 5, '17:45', '18:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1127, 5, '18:00', '18:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1128, 5, '18:15', '18:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1129, 5, '18:30', '18:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1130, 5, '18:45', '19:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1131, 5, '19:00', '19:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1132, 5, '19:15', '19:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1133, 5, '19:30', '19:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1134, 5, '19:45', '20:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1135, 6, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1136, 6, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1137, 6, '08:30', '08:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1138, 6, '08:45', '09:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1139, 6, '09:00', '09:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1140, 6, '09:15', '09:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1141, 6, '09:30', '09:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1142, 6, '09:45', '10:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1143, 6, '10:00', '10:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1144, 6, '10:15', '10:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1145, 6, '10:30', '10:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1146, 6, '10:45', '11:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1147, 6, '11:00', '11:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1148, 6, '11:15', '11:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1149, 6, '11:30', '11:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1150, 6, '11:45', '12:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1151, 6, '12:00', '12:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1152, 6, '12:15', '12:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1153, 6, '12:30', '12:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1154, 6, '12:45', '13:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1155, 6, '13:00', '13:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1156, 6, '13:15', '13:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1157, 6, '13:30', '13:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1158, 6, '13:45', '14:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1159, 6, '14:00', '14:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1160, 6, '14:15', '14:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1161, 6, '14:30', '14:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1162, 6, '14:45', '15:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1163, 6, '15:00', '15:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1164, 6, '15:15', '15:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1165, 6, '15:30', '15:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1166, 6, '15:45', '16:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1167, 6, '16:00', '16:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1168, 6, '16:15', '16:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1169, 6, '16:30', '16:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1170, 6, '16:45', '17:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1171, 6, '17:00', '17:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1172, 6, '17:15', '17:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1173, 6, '17:30', '17:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1174, 6, '17:45', '18:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1175, 6, '18:00', '18:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1176, 6, '18:15', '18:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1177, 6, '18:30', '18:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1178, 6, '18:45', '19:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1179, 6, '19:00', '19:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1180, 6, '19:15', '19:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1181, 6, '19:30', '19:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1182, 6, '19:45', '20:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1183, 7, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1184, 7, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1185, 7, '08:30', '08:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1186, 7, '08:45', '09:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1187, 7, '09:00', '09:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1188, 7, '09:15', '09:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1189, 7, '09:30', '09:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1190, 7, '09:45', '10:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1191, 7, '10:00', '10:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1192, 7, '10:15', '10:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1193, 7, '10:30', '10:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1194, 7, '10:45', '11:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1195, 7, '11:00', '11:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1196, 7, '11:15', '11:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1197, 7, '11:30', '11:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1198, 7, '11:45', '12:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1199, 7, '12:00', '12:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1200, 7, '12:15', '12:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1201, 7, '12:30', '12:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1202, 7, '12:45', '13:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1203, 7, '13:00', '13:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1204, 7, '13:15', '13:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1205, 7, '13:30', '13:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1206, 7, '13:45', '14:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1207, 7, '14:00', '14:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1208, 7, '14:15', '14:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1209, 7, '14:30', '14:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1210, 7, '14:45', '15:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1211, 7, '15:00', '15:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1212, 7, '15:15', '15:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1213, 7, '15:30', '15:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1214, 7, '15:45', '16:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1215, 7, '16:00', '16:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1216, 7, '16:15', '16:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1217, 7, '16:30', '16:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1218, 7, '16:45', '17:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1219, 7, '17:00', '17:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1220, 7, '17:15', '17:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1221, 7, '17:30', '17:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1222, 7, '17:45', '18:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1223, 7, '18:00', '18:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1224, 7, '18:15', '18:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1225, 7, '18:30', '18:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1226, 7, '18:45', '19:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1227, 7, '19:00', '19:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1228, 7, '19:15', '19:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1229, 7, '19:30', '19:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1230, 7, '19:45', '20:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1231, 8, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1232, 8, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1233, 8, '08:30', '08:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1234, 8, '08:45', '09:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1235, 8, '09:00', '09:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1236, 8, '09:15', '09:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1237, 8, '09:30', '09:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1238, 8, '09:45', '10:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1239, 8, '10:00', '10:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1240, 8, '10:15', '10:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1241, 8, '10:30', '10:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1242, 8, '10:45', '11:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1243, 8, '11:00', '11:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1244, 8, '11:15', '11:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1245, 8, '11:30', '11:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1246, 8, '11:45', '12:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1247, 8, '12:00', '12:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1248, 8, '12:15', '12:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1249, 8, '12:30', '12:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1250, 8, '12:45', '13:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1251, 8, '13:00', '13:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1252, 8, '13:15', '13:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1253, 8, '13:30', '13:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1254, 8, '13:45', '14:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1255, 8, '14:00', '14:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1256, 8, '14:15', '14:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1257, 8, '14:30', '14:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1258, 8, '14:45', '15:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1259, 8, '15:00', '15:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1260, 8, '15:15', '15:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1261, 8, '15:30', '15:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1262, 8, '15:45', '16:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1263, 8, '16:00', '16:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1264, 8, '16:15', '16:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1265, 8, '16:30', '16:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1266, 8, '16:45', '17:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1267, 8, '17:00', '17:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1268, 8, '17:15', '17:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1269, 8, '17:30', '17:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1270, 8, '17:45', '18:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1271, 8, '18:00', '18:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1272, 8, '18:15', '18:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1273, 8, '18:30', '18:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1274, 8, '18:45', '19:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1275, 8, '19:00', '19:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1276, 8, '19:15', '19:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1277, 8, '19:30', '19:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1278, 8, '19:45', '20:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1279, 9, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1280, 9, '08:15', '08:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1281, 9, '08:30', '08:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1282, 9, '08:45', '09:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1283, 9, '09:00', '09:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1284, 9, '09:15', '09:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1285, 9, '09:30', '09:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1286, 9, '09:45', '10:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1287, 9, '10:00', '10:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1288, 9, '10:15', '10:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1289, 9, '10:30', '10:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1290, 9, '10:45', '11:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1291, 9, '11:00', '11:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1292, 9, '11:15', '11:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1293, 9, '11:30', '11:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1294, 9, '11:45', '12:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1295, 9, '12:00', '12:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1296, 9, '12:15', '12:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1297, 9, '12:30', '12:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1298, 9, '12:45', '13:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1299, 9, '13:00', '13:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1300, 9, '13:15', '13:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1301, 9, '13:30', '13:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1302, 9, '13:45', '14:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1303, 9, '14:00', '14:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1304, 9, '14:15', '14:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1305, 9, '14:30', '14:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1306, 9, '14:45', '15:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1307, 9, '15:00', '15:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1308, 9, '15:15', '15:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1309, 9, '15:30', '15:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1310, 9, '15:45', '16:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1311, 9, '16:00', '16:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1312, 9, '16:15', '16:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1313, 9, '16:30', '16:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1314, 9, '16:45', '17:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1315, 9, '17:00', '17:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1316, 9, '17:15', '17:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1317, 9, '17:30', '17:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1318, 9, '17:45', '18:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1319, 9, '18:00', '18:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1320, 9, '18:15', '18:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1321, 9, '18:30', '18:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1322, 9, '18:45', '19:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1323, 9, '19:00', '19:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1324, 9, '19:15', '19:30', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1325, 9, '19:30', '19:45', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1326, 9, '19:45', '20:00', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE');

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
    (20, 27),
    -- 3rd slot for Premium bookings (required_slot=3)
    (2,  31),
    (6,  32),
    (12, 33),
    (14, 34),
    (16, 35),
    (19, 36),
    -- TEST CHECK-IN FLOW: gắn booking 28-31 vào slot 40-43 (station 2)
    (28, 40),
    (29, 41),
    (30, 42),
    (31, 43);

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
    (15, 12, NULL, 'EARN',   120,  460,  DATE_SUB(NOW(), INTERVAL 3 DAY)),
    -- Du lieu nam truoc cho customer 1 de test filter theo nam/thang (FE-42-US-01)
    (16, 1,  NULL, 'EARN',   200,  200,  '2024-03-15 10:00:00'),
    (17, 1,  NULL, 'EARN',   150,  350,  '2024-06-20 14:30:00'),
    (18, 1,  NULL, 'REDEEM', -80,  270,  '2024-06-25 09:15:00'),
    (19, 1,  NULL, 'EARN',   100,  370,  '2025-02-10 11:00:00'),
    -- Timeline nam 2025 cho customer 1 (phong@gmail.com) de demo filter theo nam/thang + lich su len/ha hang
    -- Moi giao dich gan voi 1 booking hoan chinh (501-517) de click vao xem Booking Detail
    (20, 1,  501, 'EARN',   300,  300,  '2025-01-12 09:00:00'),
    (21, 1,  502, 'EARN',   300,  600,  '2025-02-14 10:30:00'),
    (22, 1,  503, 'REDEEM', -100, 500,  '2025-03-20 15:00:00'),
    (23, 1,  504, 'EARN',   400,  900,  '2025-04-18 11:00:00'),
    (24, 1,  505, 'EARN',   350,  1250, '2025-05-09 14:00:00'),
    (25, 1,  506, 'REDEEM', -200, 1050, '2025-06-22 16:30:00'),
    (26, 1,  507, 'EARN',   500,  1550, '2025-07-15 09:45:00'),
    (27, 1,  508, 'EARN',   600,  2150, '2025-08-19 13:20:00'),
    (28, 1,  509, 'REDEEM', -650, 1500, '2025-10-04 10:00:00'),
    (29, 1,  510, 'EARN',   300,  1800, '2025-11-10 08:30:00'),
    (30, 1,  511, 'EARN',   300,  2100, '2025-11-30 17:00:00'),
    (31, 1,  512, 'REDEEM', -150, 1950, '2025-12-20 12:00:00'),
    -- Giao dich nam 2026 trong qua khu (dau nam da reset diem tieu dung ve 0), noi lien so du toi 170 truoc giao dich id=1 (~12 ngay truoc -> 320)
    (32, 1,  513, 'EARN',   120,  120,  '2026-01-18 09:30:00'),
    (33, 1,  514, 'EARN',   100,  220,  '2026-02-22 14:15:00'),
    (34, 1,  515, 'REDEEM', -50,  170,  '2026-03-15 10:45:00'),
    (35, 1,  516, 'EARN',   90,   260,  '2026-04-20 16:00:00'),
    (36, 1,  517, 'REDEEM', -90,  170,  '2026-05-16 11:20:00');

-- =====================================================================
-- CUSTOMER TIER HISTORY (2) — du lieu mau de test GET /api/loyalty/tier-history
-- (chua co trigger tu dong cong diem trong code; du lieu duoc insert thu cong o day)
-- Chi luu cac lan NANG/HA hang thuc su (phai co ca hang cu va hang moi)
-- =====================================================================
INSERT IGNORE INTO customer_tier_history
(id, customer_id, old_tier_id, new_tier_id, points_at_transition, change_type, created_at)
VALUES
    (1, 1, 1, 2, 520,  'UPGRADE', DATE_SUB(NOW(), INTERVAL 60 DAY)),
    (2, 5, 1, 4, 2050, 'UPGRADE', DATE_SUB(NOW(), INTERVAL 30 DAY));

-- Lich su len/ha hang nam 2025 cho customer 1 (phong@gmail.com) — khop ngay + gan booking cua moc doi hang
INSERT IGNORE INTO customer_tier_history
(id, customer_id, old_tier_id, new_tier_id, points_at_transition, change_type, created_at, booking_id)
VALUES
    (3, 1, 1, 2, 600,  'UPGRADE',   '2025-02-14 10:30:00', 502),
    (4, 1, 2, 3, 1200, 'UPGRADE',   '2025-05-09 14:00:00', 505),
    (5, 1, 3, 4, 2150, 'UPGRADE',   '2025-08-19 13:20:00', 508),
    (6, 1, 4, 3, 1500, 'DOWNGRADE', '2025-10-04 10:00:00', 509),
    (7, 1, 3, 4, 2050, 'UPGRADE',   '2025-11-30 17:00:00', 511);

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
    (10, 15, 4,  100000, 0,     100000, 'CANCELED', 0,     0,    100000, 0,      DATE_SUB(NOW(), INTERVAL 8 DAY),  NULL),
    (11, 16, 5,  450000, 0,     450000, 'CANCELED', 0,     0,    300000, 150000, DATE_SUB(NOW(), INTERVAL 5 DAY),  NULL),
    (12, 17, 6,  100000, 0,     100000, 'CANCELED', 0,     0,    100000, 0,      DATE_SUB(NOW(), INTERVAL 3 DAY),  NULL),
    -- Hoa don da thanh toan cua customer 1 o nam truoc de test Total spending theo nam (FE-42-US-01)
    (13, NULL, 1, 200000, 0, 200000, 'PAID', 0, 0, 150000, 50000, '2024-03-15 10:00:00', '2024-03-15 10:05:00'),
    (14, NULL, 1, 300000, 0, 300000, 'PAID', 0, 0, 250000, 50000, '2024-06-20 14:30:00', '2024-06-20 14:35:00'),
    (15, NULL, 1, 250000, 0, 250000, 'PAID', 0, 0, 200000, 50000, '2025-02-10 11:00:00', '2025-02-10 11:05:00'),
    -- Hoa don cho cac booking lich su 501-517 cua customer 1 (gan voi loyalty transaction 20-36)
    (16, 501, 1, 300000, 0,      300000, 'PAID', 0, 0,      300000, 0, '2025-01-12 09:00:00', '2025-01-12 09:00:00'),
    (17, 502, 1, 300000, 0,      300000, 'PAID', 0, 0,      300000, 0, '2025-02-14 10:30:00', '2025-02-14 10:30:00'),
    (18, 503, 1, 300000, 100000, 200000, 'PAID', 0, 100000, 300000, 0, '2025-03-20 15:00:00', '2025-03-20 15:00:00'),
    (19, 504, 1, 400000, 0,      400000, 'PAID', 0, 0,      400000, 0, '2025-04-18 11:00:00', '2025-04-18 11:00:00'),
    (20, 505, 1, 350000, 0,      350000, 'PAID', 0, 0,      350000, 0, '2025-05-09 14:00:00', '2025-05-09 14:00:00'),
    (21, 506, 1, 400000, 200000, 200000, 'PAID', 0, 200000, 400000, 0, '2025-06-22 16:30:00', '2025-06-22 16:30:00'),
    (22, 507, 1, 500000, 0,      500000, 'PAID', 0, 0,      500000, 0, '2025-07-15 09:45:00', '2025-07-15 09:45:00'),
    (23, 508, 1, 600000, 0,      600000, 'PAID', 0, 0,      600000, 0, '2025-08-19 13:20:00', '2025-08-19 13:20:00'),
    (24, 509, 1, 900000, 650000, 250000, 'PAID', 0, 650000, 900000, 0, '2025-10-04 10:00:00', '2025-10-04 10:00:00'),
    (25, 510, 1, 300000, 0,      300000, 'PAID', 0, 0,      300000, 0, '2025-11-10 08:30:00', '2025-11-10 08:30:00'),
    (26, 511, 1, 300000, 0,      300000, 'PAID', 0, 0,      300000, 0, '2025-11-30 17:00:00', '2025-11-30 17:00:00'),
    (27, 512, 1, 350000, 150000, 200000, 'PAID', 0, 150000, 350000, 0, '2025-12-20 12:00:00', '2025-12-20 12:00:00'),
    (28, 513, 1, 120000, 0,      120000, 'PAID', 0, 0,      120000, 0, '2026-01-18 09:30:00', '2026-01-18 09:30:00'),
    (29, 514, 1, 100000, 0,      100000, 'PAID', 0, 0,      100000, 0, '2026-02-22 14:15:00', '2026-02-22 14:15:00'),
    (30, 515, 1, 150000, 50000,  100000, 'PAID', 0, 50000,  150000, 0, '2026-03-15 10:45:00', '2026-03-15 10:45:00'),
    (31, 516, 1, 90000,  0,      90000,  'PAID', 0, 0,      90000,  0, '2026-04-20 16:00:00', '2026-04-20 16:00:00'),
    (32, 517, 1, 200000, 90000,  110000, 'PAID', 0, 90000,  200000, 0, '2026-05-16 11:20:00', '2026-05-16 11:20:00');

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
    (15, NULL, 11,   'MOMO',  5400000, 'TXN-0000000015', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 15 DAY),   5400000,'PAYMENT'),
    -- Thanh toan cho hoa don 16-32 (booking lich su 501-517 cua customer 1)
    (16, 16, NULL, 'CASH', 300000, NULL, 'SUCCESS', '2025-01-12 09:00:00', 300000, 'PAYMENT'),
    (17, 17, NULL, 'CASH', 300000, NULL, 'SUCCESS', '2025-02-14 10:30:00', 300000, 'PAYMENT'),
    (18, 18, NULL, 'CASH', 200000, NULL, 'SUCCESS', '2025-03-20 15:00:00', 200000, 'PAYMENT'),
    (19, 19, NULL, 'CASH', 400000, NULL, 'SUCCESS', '2025-04-18 11:00:00', 400000, 'PAYMENT'),
    (20, 20, NULL, 'CASH', 350000, NULL, 'SUCCESS', '2025-05-09 14:00:00', 350000, 'PAYMENT'),
    (21, 21, NULL, 'CASH', 200000, NULL, 'SUCCESS', '2025-06-22 16:30:00', 200000, 'PAYMENT'),
    (22, 22, NULL, 'CASH', 500000, NULL, 'SUCCESS', '2025-07-15 09:45:00', 500000, 'PAYMENT'),
    (23, 23, NULL, 'CASH', 600000, NULL, 'SUCCESS', '2025-08-19 13:20:00', 600000, 'PAYMENT'),
    (24, 24, NULL, 'CASH', 250000, NULL, 'SUCCESS', '2025-10-04 10:00:00', 250000, 'PAYMENT'),
    (25, 25, NULL, 'CASH', 300000, NULL, 'SUCCESS', '2025-11-10 08:30:00', 300000, 'PAYMENT'),
    (26, 26, NULL, 'CASH', 300000, NULL, 'SUCCESS', '2025-11-30 17:00:00', 300000, 'PAYMENT'),
    (27, 27, NULL, 'CASH', 200000, NULL, 'SUCCESS', '2025-12-20 12:00:00', 200000, 'PAYMENT'),
    (28, 28, NULL, 'CASH', 120000, NULL, 'SUCCESS', '2026-01-18 09:30:00', 120000, 'PAYMENT'),
    (29, 29, NULL, 'CASH', 100000, NULL, 'SUCCESS', '2026-02-22 14:15:00', 100000, 'PAYMENT'),
    (30, 30, NULL, 'CASH', 100000, NULL, 'SUCCESS', '2026-03-15 10:45:00', 100000, 'PAYMENT'),
    (31, 31, NULL, 'CASH', 90000,  NULL, 'SUCCESS', '2026-04-20 16:00:00', 90000,  'PAYMENT'),
    (32, 32, NULL, 'CASH', 110000, NULL, 'SUCCESS', '2026-05-16 11:20:00', 110000, 'PAYMENT');

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
    (12, 11, NULL, 4, 'On, se tiep tuc su dung.',                                DATE_SUB(NOW(), INTERVAL 8 DAY),    NULL, false),
    -- Danh gia cho cac booking lich su 501-517 cua customer 1 (phong@gmail.com)
    (13, 1, 501, 5, 'Rua xe sach, dung gio.',              '2025-01-12 09:00:00', NULL, false),
    (14, 1, 502, 4, 'Dich vu on, nhan vien nhiet tinh.',   '2025-02-14 10:30:00', NULL, false),
    (15, 1, 503, 5, 'Dung diem giam gia rat tien loi.',    '2025-03-20 15:00:00', NULL, false),
    (16, 1, 504, 4, 'Xe sach, hai long.',                  '2025-04-18 11:00:00', NULL, false),
    (17, 1, 505, 5, 'Chat luong tot nhu moi khi.',         '2025-05-09 14:00:00', NULL, false),
    (18, 1, 506, 4, 'On dinh, se quay lai.',               '2025-06-22 16:30:00', NULL, false),
    (19, 1, 507, 5, 'Rat hai long voi goi cao cap.',       '2025-07-15 09:45:00', NULL, false),
    (20, 1, 508, 5, 'Dich vu tuyet voi.',                  '2025-08-19 13:20:00', NULL, false),
    (21, 1, 509, 4, 'Doi diem thuong hoi nhieu nhung ok.', '2025-10-04 10:00:00', NULL, false),
    (22, 1, 510, 5, 'Nhanh gon, sach se.',                 '2025-11-10 08:30:00', NULL, false),
    (23, 1, 511, 5, 'Hai long, len hang Platinum.',        '2025-11-30 17:00:00', NULL, false),
    (24, 1, 512, 4, 'Cuoi nam dich vu van tot.',           '2025-12-20 12:00:00', NULL, false),
    (25, 1, 513, 4, 'Dau nam moi, dich vu on.',            '2026-01-18 09:30:00', NULL, false),
    (26, 1, 514, 5, 'Rat tot, cam on shop.',               '2026-02-22 14:15:00', NULL, false),
    (27, 1, 515, 4, 'Dung diem thanh toan nhanh gon.',     '2026-03-15 10:45:00', NULL, false),
    (28, 1, 516, 5, 'Sach se, gia hop ly.',                '2026-04-20 16:00:00', NULL, false),
    (29, 1, 517, 4, 'On, tiep tuc ung ho.',                '2026-05-16 11:20:00', NULL, false);

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
-- QUEUE TICKET (13) — today's queue
-- WAITING tickets (6,7,8,9,13) are all linked to a real CHECK_IN booking
-- (21-25) so the Queue Dashboard / cancel-guest-left flow has full vehicle,
-- customer tier, and package data to render — covers FE-27-US-01 AC02-AC04:
--   #6  booking21 ONLINE+deposit,        customer tier GOLD     -> AC02
--   #7  booking22 WALK_IN (registered),  customer tier SILVER   -> AC04
--   #8  booking23 ONLINE+no-deposit,     customer tier PLATINUM -> AC03
--   #9  booking24 WALK_IN (anonymous),   no customer ("Guest")  -> AC04
--   #13 booking25 ONLINE+deposit,        customer tier MEMBER   -> AC02
-- =====================================================================
-- Reset queue tickets on every startup (status can be changed by cancelGuestLeft during testing)
DELETE FROM queue_ticket;
INSERT IGNORE INTO queue_ticket
(id, station_id, booking_id, ticket_number, status, issued_at, is_booking, priority_score)
VALUES
    (1,  2, 6,    'A001', 'WASHING', DATE_SUB(NOW(), INTERVAL 70 MINUTE), true,  3),
    (2,  1, 7,    'A002', 'WASHING', DATE_SUB(NOW(), INTERVAL 35 MINUTE), true,  3),
    (3,  3, 8,    'A003', 'WASHING', DATE_SUB(NOW(), INTERVAL 50 MINUTE), true,  3),
    (4,  1, 9,    'A004', 'WASHING', DATE_SUB(NOW(), INTERVAL 65 MINUTE), true,  3),
    (5,  4, 10,   'A005', 'WASHING', DATE_SUB(NOW(), INTERVAL 45 MINUTE), true,  3),
    (10, 4, NULL, 'A010', 'COMPLETED',  DATE_SUB(NOW(), INTERVAL 3 HOUR),    false, 1),
    (11, 1, NULL, 'A011', 'CANCELED',   DATE_SUB(NOW(), INTERVAL 2 HOUR),    false, 1),
    (12, 2, NULL, 'A012', 'COMPLETED',  DATE_SUB(NOW(), INTERVAL 4 HOUR),    false, 1),
    -- COMPLETED without booking_id (null)
    (16, 1, NULL, 'A016', 'COMPLETED', DATE_SUB(NOW(), INTERVAL 3 HOUR),    false, 1),
    (17, 1, NULL, 'A017', 'COMPLETED', DATE_SUB(NOW(), INTERVAL 90 MINUTE), false, 1);

-- =====================================================================
-- SYSTEM SETTING (10)
-- =====================================================================
INSERT IGNORE INTO system_setting
(setting_key, setting_value, description, data_type, is_active)
VALUES
    ('DEPOSIT_PERCENT',             '30',      'Deposit percent',                                'NUMBER',  true),
    ('MAX_BOOKING_DAY',             '30',      'Maximum booking day',                            'NUMBER',  true),
    ('DEFAULT_DEPOSIT_AMOUNT',      '20000',   'Default deposit amount',                         'NUMBER',  true),
    ('CANCEL_THRESHOLD_MINUTES',    '120',     'Minutes before appointment a booking can be CANCELED', 'NUMBER', true),
    ('LOYALTY_POINT_PER_VND',       '1000',    'VND spent per loyalty point earned',             'NUMBER',  true),
    ('MAX_VEHICLE_PER_FAMILY',      '5',       'Maximum vehicles allowed per family subscription','NUMBER', true),
    ('QUEUE_PRIORITY_BOOKING_WEIGHT','3',      'Priority weight given to booking-based queue tickets','NUMBER', true),
    ('SUPPORT_HOTLINE',             '1900-1234','Customer support hotline number',               'STRING',  true),
    ('MAINTENANCE_MODE',            'false',   'Whether the system is in maintenance mode',      'BOOLEAN', true),
    ('REVIEW_EDIT_WINDOW_HOURS',    '24',      'Hours a customer may edit their review after posting', 'NUMBER', true),
    ('LOYALTY_RESET_MONTH_DAY',     '01-01',   'Annual loyalty point reset date (MM-DD)',        'STRING',  true),
    ('LOYALTY_EARN_RATE_VND_PER_POINT', '1000', 'Customer earns 1 loyalty point for every 1,000 VND spent','NUMBER', TRUE),
    ('REVIEW_EDIT_WINDOW_HOURS',    '24',      'Hours a customer may edit their review after posting', 'NUMBER', true),
    ( 'LOYALTY_REDEEM_RATE_VND_PER_POINT', '100', '1 loyalty point can be redeemed for 100 VND', 'NUMBER', TRUE);

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO booking_slot (station_id, start_time, end_time, max_capacity, date, booked_count, status) VALUES
                                                                                                          (2, '23:30:00', '23:45:00', 3, '2026-06-29', 0, 'AVAILABLE'),
                                                                                                          (2, '23:45:00', '00:00:00', 3, '2026-06-29', 0, 'AVAILABLE'),
                                                                                                          (2, '00:00:00', '00:15:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '00:15:00', '00:30:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '00:30:00', '00:45:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '00:45:00', '01:00:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '01:00:00', '01:15:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '01:15:00', '01:30:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '01:30:00', '01:45:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '01:45:00', '02:00:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '02:00:00', '02:15:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '02:15:00', '02:30:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '02:30:00', '02:45:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '02:45:00', '03:00:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '03:00:00', '03:15:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '03:15:00', '03:30:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '03:30:00', '03:45:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '03:45:00', '04:00:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '04:00:00', '04:15:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '04:15:00', '04:30:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '04:30:00', '04:45:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '04:45:00', '05:00:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '05:00:00', '05:15:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '05:15:00', '05:30:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '05:30:00', '05:45:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '05:45:00', '06:00:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '06:00:00', '06:15:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '06:15:00', '06:30:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '06:30:00', '06:45:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '06:45:00', '07:00:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '07:00:00', '07:15:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '07:15:00', '07:30:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '07:30:00', '07:45:00', 3, '2026-06-30', 0, 'AVAILABLE'),
                                                                                                          (2, '07:45:00', '08:00:00', 3, '2026-06-30', 0, 'AVAILABLE');

-- =====================================================================
-- BOOKING SLOT — CA DEM CHI NHANH 2 (station_id = 2): 21:00 hom nay ->
-- 07:00 sang mai, moi slot 15 phut. Phan 21:00 -> 24:00 dung CURDATE(),
-- phan 00:00 -> 07:00 dung CURDATE() + 1 ngay. ids 5400-5439.
-- =====================================================================
INSERT IGNORE INTO booking_slot
(id, station_id, start_time, end_time, max_capacity, date, booked_count, status)
VALUES
    (5400, 2, '21:00', '21:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (5401, 2, '21:15', '21:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (5402, 2, '21:30', '21:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (5403, 2, '21:45', '22:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (5404, 2, '22:00', '22:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (5405, 2, '22:15', '22:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (5406, 2, '22:30', '22:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (5407, 2, '22:45', '23:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (5408, 2, '23:00', '23:15', 5, CURDATE(), 0, 'AVAILABLE'),
    (5409, 2, '23:15', '23:30', 5, CURDATE(), 0, 'AVAILABLE'),
    (5410, 2, '23:30', '23:45', 5, CURDATE(), 0, 'AVAILABLE'),
    (5411, 2, '23:45', '00:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (5412, 2, '00:00', '00:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5413, 2, '00:15', '00:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5414, 2, '00:30', '00:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5415, 2, '00:45', '01:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5416, 2, '01:00', '01:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5417, 2, '01:15', '01:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5418, 2, '01:30', '01:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5419, 2, '01:45', '02:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5420, 2, '02:00', '02:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5421, 2, '02:15', '02:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5422, 2, '02:30', '02:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5423, 2, '02:45', '03:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5424, 2, '03:00', '03:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5425, 2, '03:15', '03:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5426, 2, '03:30', '03:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5427, 2, '03:45', '04:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5428, 2, '04:00', '04:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5429, 2, '04:15', '04:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5430, 2, '04:30', '04:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5431, 2, '04:45', '05:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5432, 2, '05:00', '05:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5433, 2, '05:15', '05:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5434, 2, '05:30', '05:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5435, 2, '05:45', '06:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5436, 2, '06:00', '06:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5437, 2, '06:15', '06:30', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5438, 2, '06:30', '06:45', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5439, 2, '06:45', '07:00', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE');

-- =====================================================================
-- BOOKING TEST UI (id=500) — booking COMPLETED đầy đủ check_in_at,
-- check_out_at, check_in_employee_id để test màn hình Booking Detail
-- (hiển thị technicianName = tên staff check-in). customer=1 (phong@gmail.com),
-- vehicle=13 (51A-13131, không gắn subscription) -> booking_type=ADVANCE.
-- staff=1 (An Nguyen, station 1).
-- =====================================================================
INSERT IGNORE INTO booking
(id, customer_id, vehicle_id, service_package_id,
 appointment_date, status, booking_type, check_in_employee_id,
 created_at, check_in_at, check_out_at, canceled_at,
 is_deposit_paid,
 total_service_amount, total_addon_amount, total_amount,
 voucher_discount_amount, point_discount_amount)
VALUES
    (500, 1, 13, 1, CURDATE(), 'COMPLETED', 'ADVANCE', 1,
     DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 90 MINUTE), DATE_SUB(NOW(), INTERVAL 45 MINUTE), NULL,
     true, 100000, 0, 100000, 0, 0);

INSERT IGNORE INTO booking_slot
(id, station_id, start_time, end_time, max_capacity, date, booked_count, status)
VALUES
    (9500, 1, '09:00', '09:15', 5, CURDATE(), 1, 'COMPLETED');

INSERT IGNORE INTO booking_slot_allocation
(booking_id, booking_slot_id)
VALUES
    (500, 9500);

-- =====================================================================
-- BOOKING LICH SU (501-517) cho customer 1 (phong@gmail.com) — moi booking
-- gan 1-1 voi loyalty_point_transaction 20-36, COMPLETED, du check_in/out +
-- staff + slot + invoice + payment + review. Vehicle 1, staff 1, station 1.
-- REDEEM: point_discount_amount = |points|*1000, total = service - point_discount.
-- =====================================================================
INSERT IGNORE INTO booking
(id, customer_id, vehicle_id, service_package_id,
 appointment_date, status, booking_type, check_in_employee_id,
 created_at, check_in_at, check_out_at, canceled_at,
 is_deposit_paid,
 total_service_amount, total_addon_amount, total_amount,
 voucher_discount_amount, point_discount_amount)
VALUES
    (501, 1, 1, 3, '2025-01-12', 'CHECK_OUT', 'ADVANCE', 1, '2025-01-12 08:30:00', '2025-01-12 08:45:00', '2025-01-12 09:00:00', NULL, true, 300000, 0, 300000, 0, 0),
    (502, 1, 1, 3, '2025-02-14', 'CHECK_OUT', 'ADVANCE', 1, '2025-02-14 10:00:00', '2025-02-14 10:15:00', '2025-02-14 10:30:00', NULL, true, 300000, 0, 300000, 0, 0),
    (503, 1, 1, 3, '2025-03-20', 'CHECK_OUT', 'ADVANCE', 1, '2025-03-20 14:30:00', '2025-03-20 14:45:00', '2025-03-20 15:00:00', NULL, true, 300000, 0, 200000, 0, 100000),
    (504, 1, 1, 3, '2025-04-18', 'CHECK_OUT', 'ADVANCE', 1, '2025-04-18 10:30:00', '2025-04-18 10:45:00', '2025-04-18 11:00:00', NULL, true, 400000, 0, 400000, 0, 0),
    (505, 1, 1, 6, '2025-05-09', 'CHECK_OUT', 'ADVANCE', 1, '2025-05-09 13:30:00', '2025-05-09 13:45:00', '2025-05-09 14:00:00', NULL, true, 350000, 0, 350000, 0, 0),
    (506, 1, 1, 3, '2025-06-22', 'CHECK_OUT', 'ADVANCE', 1, '2025-06-22 16:00:00', '2025-06-22 16:15:00', '2025-06-22 16:30:00', NULL, true, 400000, 0, 200000, 0, 200000),
    (507, 1, 1, 3, '2025-07-15', 'CHECK_OUT', 'ADVANCE', 1, '2025-07-15 09:15:00', '2025-07-15 09:30:00', '2025-07-15 09:45:00', NULL, true, 500000, 0, 500000, 0, 0),
    (508, 1, 1, 3, '2025-08-19', 'CHECK_OUT', 'ADVANCE', 1, '2025-08-19 12:50:00', '2025-08-19 13:05:00', '2025-08-19 13:20:00', NULL, true, 600000, 0, 600000, 0, 0),
    (509, 1, 1, 3, '2025-10-04', 'CHECK_OUT', 'ADVANCE', 1, '2025-10-04 09:30:00', '2025-10-04 09:45:00', '2025-10-04 10:00:00', NULL, true, 900000, 0, 250000, 0, 650000),
    (510, 1, 1, 3, '2025-11-10', 'CHECK_OUT', 'ADVANCE', 1, '2025-11-10 08:00:00', '2025-11-10 08:15:00', '2025-11-10 08:30:00', NULL, true, 300000, 0, 300000, 0, 0),
    (511, 1, 1, 3, '2025-11-30', 'CHECK_OUT', 'ADVANCE', 1, '2025-11-30 16:30:00', '2025-11-30 16:45:00', '2025-11-30 17:00:00', NULL, true, 300000, 0, 300000, 0, 0),
    (512, 1, 1, 6, '2025-12-20', 'CHECK_OUT', 'ADVANCE', 1, '2025-12-20 11:30:00', '2025-12-20 11:45:00', '2025-12-20 12:00:00', NULL, true, 350000, 0, 200000, 0, 150000),
    (513, 1, 1, 2, '2026-01-18', 'CHECK_OUT', 'ADVANCE', 1, '2026-01-18 09:00:00', '2026-01-18 09:15:00', '2026-01-18 09:30:00', NULL, true, 120000, 0, 120000, 0, 0),
    (514, 1, 1, 4, '2026-02-22', 'CHECK_OUT', 'ADVANCE', 1, '2026-02-22 13:45:00', '2026-02-22 14:00:00', '2026-02-22 14:15:00', NULL, true, 100000, 0, 100000, 0, 0),
    (515, 1, 1, 2, '2026-03-15', 'CHECK_OUT', 'ADVANCE', 1, '2026-03-15 10:15:00', '2026-03-15 10:30:00', '2026-03-15 10:45:00', NULL, true, 150000, 0, 100000, 0, 50000),
    (516, 1, 1, 1, '2026-04-20', 'CHECK_OUT', 'ADVANCE', 1, '2026-04-20 15:30:00', '2026-04-20 15:45:00', '2026-04-20 16:00:00', NULL, true, 90000,  0, 90000,  0, 0),
    (517, 1, 1, 2, '2026-05-16', 'CHECK_OUT', 'ADVANCE', 1, '2026-05-16 10:50:00', '2026-05-16 11:05:00', '2026-05-16 11:20:00', NULL, true, 200000, 0, 110000, 0, 90000);

INSERT IGNORE INTO booking_slot
(id, station_id, start_time, end_time, max_capacity, date, booked_count, status)
VALUES
    (9501, 1, '09:00', '09:15', 5, '2025-01-12', 1, 'COMPLETED'),
    (9502, 1, '10:30', '10:45', 5, '2025-02-14', 1, 'COMPLETED'),
    (9503, 1, '15:00', '15:15', 5, '2025-03-20', 1, 'COMPLETED'),
    (9504, 1, '11:00', '11:15', 5, '2025-04-18', 1, 'COMPLETED'),
    (9505, 1, '14:00', '14:15', 5, '2025-05-09', 1, 'COMPLETED'),
    (9506, 1, '16:30', '16:45', 5, '2025-06-22', 1, 'COMPLETED'),
    (9507, 1, '09:45', '10:00', 5, '2025-07-15', 1, 'COMPLETED'),
    (9508, 1, '13:20', '13:35', 5, '2025-08-19', 1, 'COMPLETED'),
    (9509, 1, '10:00', '10:15', 5, '2025-10-04', 1, 'COMPLETED'),
    (9510, 1, '08:30', '08:45', 5, '2025-11-10', 1, 'COMPLETED'),
    (9511, 1, '17:00', '17:15', 5, '2025-11-30', 1, 'COMPLETED'),
    (9512, 1, '12:00', '12:15', 5, '2025-12-20', 1, 'COMPLETED'),
    (9513, 1, '09:30', '09:45', 5, '2026-01-18', 1, 'COMPLETED'),
    (9514, 1, '14:15', '14:30', 5, '2026-02-22', 1, 'COMPLETED'),
    (9515, 1, '10:45', '11:00', 5, '2026-03-15', 1, 'COMPLETED'),
    (9516, 1, '16:00', '16:15', 5, '2026-04-20', 1, 'COMPLETED'),
    (9517, 1, '11:20', '11:35', 5, '2026-05-16', 1, 'COMPLETED');

INSERT IGNORE INTO booking_slot_allocation
(booking_id, booking_slot_id)
VALUES
    (501, 9501), (502, 9502), (503, 9503), (504, 9504), (505, 9505),
    (506, 9506), (507, 9507), (508, 9508), (509, 9509), (510, 9510),
    (511, 9511), (512, 9512), (513, 9513), (514, 9514), (515, 9515),
    (516, 9516), (517, 9517);
