SET FOREIGN_KEY_CHECKS = 0;

-- =====================================================================
-- ROLE (10)
-- =====================================================================
INSERT IGNORE INTO role (id, name)
VALUES
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_STAFF'),
    (3, 'ROLE_CUSTOMER');


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
    -- users 10,11: customer-role accounts for the extra FE-US-09 test customers 100,101
    -- (previously these ids were missing, leaving customer.user_id orphaned).
    (10, 'vana@gmail.com',  '0900002013', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 45 DAY)),
    (11, 'vanb@gmail.com',  '0900002014', '$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK', 3, true, DATE_SUB(NOW(), INTERVAL 44 DAY)),
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
-- PROVINCE (3) — fixed constraint: exactly HCM / Ha Noi / Da Nang
-- =====================================================================
INSERT IGNORE INTO province (id, province_name)
VALUES
    (2, 'Ho Chi Minh'),
    (3, 'Ha Noi'),
    (4, 'Da Nang');

-- =====================================================================
-- COMMUNE (6) — 2-tier admin naming (phuong only, no quan/huyen), 2 per
-- province, matching the 6-station layout in docs/seed.md section 3b.
-- =====================================================================
INSERT IGNORE INTO commune (id, commune_name, province_id)
VALUES
    (1, 'Phuong Ben Thanh', 2),  -- HCM: AutoWash Saigon Central
    (2, 'Phuong Thu Duc',   2),  -- HCM: AutoWash Thu Duc
    (3, 'Phuong Hoan Kiem', 3),  -- Ha Noi: AutoWash Hoan Kiem
    (4, 'Phuong Ba Dinh',   3),  -- Ha Noi: AutoWash Ba Dinh (busy station)
    (5, 'Phuong Hai Chau',  4),  -- Da Nang: AutoWash Hai Chau
    (6, 'Phuong Son Tra',   4);  -- Da Nang: AutoWash Son Tra (soft-deleted lane test)

-- =====================================================================
-- STATION (6) — rebuilt per docs/seed.md section 3b (previously: bad
-- commune FKs, a TRUE literal in commune_id, duplicate lane names, and
-- stations with zero lanes). 2 stations per city; max_wash_capacity kept
-- in sync with wash_lane below (5 / 1 / 3 / 2 / 2 / 2 active lanes).
-- =====================================================================
INSERT IGNORE INTO station
(id, station_name, address, commune_id, is_operating, max_wash_capacity, is_deleted)
VALUES
    (1, 'AutoWash Saigon Central', '10 Le Loi, Phuong Ben Thanh',         1, true, 5, false),
    (2, 'AutoWash Thu Duc',        '456 Vo Van Ngan, Phuong Thu Duc',     2, true, 1, false),
    (3, 'AutoWash Hoan Kiem',      '12 Cau Giay, Phuong Hoan Kiem',       3, true, 3, false),
    (4, 'AutoWash Ba Dinh',        '7 Doi Can, Phuong Ba Dinh',           4, true, 2, false),
    (5, 'AutoWash Hai Chau',       '78 Nguyen Van Linh, Phuong Hai Chau', 5, true, 2, false),
    (6, 'AutoWash Son Tra',        '34 Vo Nguyen Giap, Phuong Son Tra',   6, true, 2, false);

-- =====================================================================
-- WASH LANE (16 rows: 14 active + 1 soft-deleted + 1 that is also
-- soft-deleted-counted... see comment) — lane counts 5/1/3/2/2/2 per
-- station, matching station.max_wash_capacity and booking_slot.max_capacity.
-- Station 4 (Ba Dinh) is the "busy" station: both lanes WASHING, each
-- current_booking_id pointing at a real WASHING booking at station 4
-- (bookings 10 and 32) so BR4 negative case (EARLY_ARRIVAL_SLOT_FULL /
-- late -> NO_SHOW) has zero AVAILABLE lanes to check into.
-- Station 6 (Son Tra) has 1 lane soft-deleted (is_deleted=1) to verify it
-- does not count toward "active lane" totals (open question resolved:
-- soft-deleted lanes are excluded from max_wash_capacity/max_capacity sync).
-- =====================================================================
INSERT IGNORE INTO wash_lane
(id, station_id, lane_name, status, booking_walkin_ratio, current_booking_id, is_deleted)
VALUES
    -- Station 1: Saigon Central — 5 AVAILABLE (max lane case)
    (1,  1, 'Lane 1', 'AVAILABLE', 3, NULL, false),
    (2,  1, 'Lane 2', 'AVAILABLE', 3, NULL, false),
    (3,  1, 'Lane 3', 'AVAILABLE', 3, NULL, false),
    (4,  1, 'Lane 4', 'AVAILABLE', 4, NULL, false),
    (5,  1, 'Lane 5', 'AVAILABLE', 3, NULL, false),
    -- Station 2: Thu Duc — 1 AVAILABLE (min lane case; also the FULL-slot station)
    (6,  2, 'Lane 1', 'AVAILABLE', 3, NULL, false),
    -- Station 3: Hoan Kiem — 3 AVAILABLE (standard happy-path station)
    (7,  3, 'Lane 1', 'AVAILABLE', 3, NULL, false),
    (8,  3, 'Lane 2', 'AVAILABLE', 2, NULL, false),
    (9,  3, 'Lane 3', 'AVAILABLE', 3, NULL, false),
    -- Station 4: Ba Dinh — 2 lanes, BOTH WASHING (busy negative-BR4 station)
    (10, 4, 'Lane 1', 'WASHING',   3, 10,   false),
    (11, 4, 'Lane 2', 'WASHING',   3, 9,    false),
    -- Station 5: Hai Chau — 2 AVAILABLE
    (12, 5, 'Lane 1', 'AVAILABLE', 3, NULL, false),
    (13, 5, 'Lane 2', 'AVAILABLE', 3, NULL, false),
    -- Station 6: Son Tra — 2 AVAILABLE + 1 soft-deleted (does not count)
    (14, 6, 'Lane 1', 'AVAILABLE', 3, NULL, false),
    (15, 6, 'Lane 2', 'AVAILABLE', 3, NULL, false),
    (16, 6, 'Lane 3 (removed)', 'AVAILABLE', 3, NULL, true);

-- =====================================================================
-- STAFF (12)
-- =====================================================================
-- station_id remapped to the new 6-station layout (1=Saigon Central,
-- 2=Thu Duc, 3=Hoan Kiem, 4=Ba Dinh, 5=Hai Chau, 6=Son Tra); old ids 7/8
-- (which pointed at removed HCM/HN branches) are folded into 1/3.
INSERT IGNORE INTO staff
(id, user_id, station_id, first_name, last_name)
VALUES
    (1,  4,  1,  'An',    'Nguyen'),
    (2,  5,  1,  'Binh',  'Tran'),
    (3,  6,  2,  'Chi',   'Le'),
    (4,  7,  2,  'Dung',  'Pham'),
    (5,  8,  3,  'Em',    'Vo'),
    (6,  9,  4,  'Phuc',  'Dang'),
    (7,  28, 5,  'Giang', 'Bui'),
    (8,  29, 6,  'Hieu',  'Do'),
    (9,  12, 1,  'Ich',   'Ho'),
    (10, 13, 3,  'Khang', 'Ngo'),
    (11, 14, 6,  'Loan',  'Ly'),
    (12, 15, 1,  'Minh',  'Vu');

-- =====================================================================
-- CUSTOMER TIER (4)
-- =====================================================================
INSERT IGNORE INTO customer_tier
(id, tier_name, min_points, booking_window_days, point_multiple, retention_target_amount, queue_priority_weight)
VALUES
    (1,  'MEMBER',    0,     7,  1.0, 0,       0),
    (2,  'SILVER',    500,   10, 1.2, 1500000, 1),
    (3,  'GOLD',      1000,  12, 1.5, 3000000, 2),
    (4,  'PLATINUM',  2000,  14, 1.8, 5000000, 3);

-- =====================================================================
-- TIER BENEFIT (6)
-- =====================================================================
INSERT IGNORE INTO tier_benefit
(id, customer_tier_id, benefit_description)
VALUES
    (1,  1,  'Basic points x1 per wash'),
    (2,  2,  'Priority booking up to 10 days in advance'),
    (3,  3,  '5% discount on add-on service fees'),
    (4,  3,  'Earn points x1.5 per wash'),
    (5,  4,  'One free polishing service per quarter'),
    (12, 2,  'Early promotional email notifications');

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

    (100, 10, 'Nguyen Van', 'Anh', '2005-10-11', 3,  0, NULL),

    (101, 11, 'Nguyen Van', 'Minh', '2005-10-12', 3,  0, NULL);

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
    (9,  9,  '92B-99999', 'Mitsubishi','Black', 0, NULL, true),
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
    (5,  'Gia dinh Pham',  5,  DATE_SUB(NOW(), INTERVAL 80 DAY),  false),
    (8,  'Gia dinh Bui',   8,  DATE_SUB(NOW(), INTERVAL 65 DAY),  false);

-- =====================================================================
-- FAMILY MEMBER (12)
-- =====================================================================
INSERT IGNORE INTO family_member
(id, family_group_id, customer_id, vehicle_id, vehicle_change_count, vehicle_change_window_start)
VALUES
    (1,  1,  1,  1,  0, NULL),
    (2,  2,  2,  2,  1, DATE_SUB(NOW(), INTERVAL 10 DAY)),
    (3,  3,  3,  3,  0, NULL),
    (5,  5,  5,  5,  2, DATE_SUB(NOW(), INTERVAL 15 DAY)),
    (8,  8,  8,  8,  0, NULL),
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
-- Dong bo customer.customer_tier_id theo accumulated_points vua seed o tren,
-- KHONG gan cung tier_id trong INSERT customer nua - tranh lech du lieu nhu
-- truong hop Phong (id=1) tung bi seed cung MEMBER trong khi accumulated_points=820
-- (dang le phai la SILVER). Dung cung nguong min_points nhu logic that cua app
-- (CustomerTierRepository.findFirstByMinPointsLessThanEqualOrderByMinPointsDesc).
-- =====================================================================
UPDATE customer c
    JOIN loyalty_point_balance lb ON lb.customer_id = c.id
SET c.customer_tier_id = (
    SELECT ct.id
    FROM customer_tier ct
    WHERE ct.min_points <= lb.accumulated_points
    ORDER BY ct.min_points DESC
    LIMIT 1
);

-- =====================================================================
-- SERVICE CATEGORY (3)
-- =====================================================================
INSERT IGNORE INTO service_category
(id, category_name, description)
VALUES
    (1, 'Add-on',            'Additional services to supplement wash packages'),
    (2, 'Service Package',   'Per-visit wash service packages (Basic/Medium/Premium)'),
    (3, 'Subscription Plan', 'Membership subscription plans (Unlimited and Family)');

-- =====================================================================
-- ADDON SERVICE (8) — service_category_id = 1 (Add-on)
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
    (50, 'Underbody Anti-Rust Spray', 30000, 15, 1, false);

-- =====================================================================
-- SERVICE PACKAGE (3) — service_category_id = 2 (Service Package)
-- =====================================================================
INSERT IGNORE INTO service_package
(id, service_category_id, name, base_price, description, required_slot, is_deleted)
VALUES
    (1, 2, 'Basic',   149000, 'Basic wash: exterior foam wash, wheel cleaning, and hand dry',                                  1, false),
    (2, 2, 'Medium',  299000, 'Complete inside-out refresh: includes Basic + interior vacuum and window cleaning',              2, false),
    (3, 2, 'Premium', 499000, 'Optimal care and protection: includes Medium + ceramic boost spray and dashboard UV protection', 3, false);

-- =====================================================================
-- PACKAGE ADDON MAPPING (15)
-- =====================================================================
INSERT IGNORE INTO package_addon_mapping
(service_package_id, addon_service_id)
VALUES
    (1, 1), (1, 2), (1, 3),
    (2, 1), (2, 2), (2, 3), (2, 4), (2, 5),
    (3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (3, 6), (3, 7);

-- =====================================================================
-- SUBSCRIPTION PLAN (12) — service_category_id = 3 (Subscription Plan)
-- =====================================================================
-- status column added (NOT NULL, @Enumerated STRING, no DB default) — the
-- previous seed omitted it entirely which would fail INSERT under
-- ddl-auto=create. Plan 13 is INACTIVE + is_deleted to test hidden-plan
-- filtering (docs/seed.md 1.10).
INSERT IGNORE INTO subscription_plan
(id, service_package_id, service_category_id, plan_name, duration_days, price, plan_type, max_vehicle_count, description, is_deleted, status)
VALUES
    (1,  1, 3, 'Unlimited Basic 1 Month',     30,  5000,   'UNLIMIT', 1, 'Unlimited car wash for 1 month',                          false, 'ACTIVE'),
    (2,  3, 3, 'Unlimited Premium 1 Month',   30,  9000,   'UNLIMIT', 1, 'Unlimited premium car wash for 1 month',                  false, 'ACTIVE'),
    (3,  1, 3, 'Unlimited Basic 3 Months',    90,  13000,  'UNLIMIT', 1, 'Unlimited car wash for 3 months',                         false, 'ACTIVE'),
    (4,  3, 3, 'Unlimited Premium 3 Months',  90,  24000,  'UNLIMIT', 1, 'Unlimited premium car wash for 3 months',                 false, 'ACTIVE'),
    (5,  3, 3, 'Unlimited Premium 6 Months',  180, 48000,  'UNLIMIT', 1, 'Unlimited premium car wash for 6 months',                 false, 'ACTIVE'),
    (6,  1, 3, 'Family Basic 1 Month',        30,  12000,  'FAMILY',    3, 'Unlimited car wash for the whole family, 1 month',        false, 'ACTIVE'),
    (7,  3, 3, 'Family Premium 1 Month',      30,  20000,  'FAMILY',    3, 'Premium car wash for the whole family, 1 month',          false, 'ACTIVE'),
    (8,  1, 3, 'Family Basic 3 Months',       90,  32000,  'FAMILY',    4, 'Unlimited car wash for the whole family, 3 months',       false, 'ACTIVE'),
    (9,  3, 3, 'Family Premium 3 Months',     90,  54000,  'FAMILY',    4, 'Premium car wash for the whole family, 3 months',         false, 'ACTIVE'),
    (10, 3, 3, 'Family Premium 6 Months',     180, 108000, 'FAMILY',    5, 'Premium car wash for the whole family, 6 months',         false, 'ACTIVE'),
    (11, 1, 3, 'Unlimited Basic 6 Months',    180, 27000,  'UNLIMIT', 1, 'Unlimited car wash for 6 months',                         false, 'ACTIVE'),
    (12, 1, 3, 'Family Basic 6 Months',       180, 65000,  'FAMILY',    3, 'Unlimited car wash for the whole family, 6 months',       false, 'ACTIVE'),
    (13, 1, 3, 'Unlimited Basic 1 Month (Discontinued)', 30, 50000, 'UNLIMIT', 1, 'Discontinued plan, kept only for testing hidden-plan filtering', true, 'INACTIVE'),

    (14, 2, 3, 'Unlimited Medium 1 Month',    30,  70000,   'UNLIMIT', 1, 'Unlimited medium-tier car wash for 1 month',              false, 'ACTIVE'),
    (15, 2, 3, 'Unlimited Medium 3 Months',   90,  18750,  'UNLIMIT', 1, 'Unlimited medium-tier car wash for 3 months',             false, 'ACTIVE'),
    (16, 2, 3, 'Unlimited Medium 6 Months',   180, 37500,  'UNLIMIT', 1, 'Unlimited medium-tier car wash for 6 months',             false, 'ACTIVE'),
    (17, 2, 3, 'Family Medium 1 Month',       30,  16000,  'FAMILY',    3, 'Medium-tier car wash for the whole family, 1 month',      false, 'ACTIVE'),
    (18, 2, 3, 'Family Medium 3 Months',      90,  43000,  'FAMILY',    4, 'Medium-tier car wash for the whole family, 3 months',      false, 'ACTIVE'),
    (19, 2, 3, 'Family Medium 6 Months',      180, 86500,  'FAMILY',    4, 'Medium-tier car wash for the whole family, 6 months',      false, 'ACTIVE');

-- =====================================================================
-- UNLIMIT SUBSCRIPTION (10)
-- =====================================================================
INSERT IGNORE INTO unlimit_subscription
(id, customer_id, vehicle_id, subscription_plan_id, last_vehicle_change_at, start_date, end_date, status, canceled_at)
VALUES
    (1,  1,  1,  1, NULL, DATE_SUB(CURDATE(), INTERVAL 10 DAY),  DATE_ADD(CURDATE(), INTERVAL 20 DAY),  'ACTIVE',    NULL),
    (2,  2,  2,  2, NULL, DATE_SUB(CURDATE(), INTERVAL 65 DAY),  DATE_SUB(CURDATE(), INTERVAL 35 DAY),  'EXPIRED',   NULL),
    (3,  3,  3,  3, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(CURDATE(), INTERVAL 20 DAY),  DATE_ADD(CURDATE(), INTERVAL 70 DAY),  'ACTIVE',    NULL),
    (5,  5,  5,  5, NULL, DATE_SUB(CURDATE(), INTERVAL 200 DAY), DATE_SUB(CURDATE(), INTERVAL 100 DAY), 'EXPIRED',   NULL),
    (8,  8,  8,  3, DATE_SUB(NOW(), INTERVAL 50 DAY), DATE_SUB(CURDATE(), INTERVAL 120 DAY), DATE_SUB(CURDATE(), INTERVAL 30 DAY),  'CANCELED', DATE_SUB(NOW(), INTERVAL 40 DAY));

-- =====================================================================
-- FAMILY SUBSCRIPTION (10)
-- =====================================================================
INSERT IGNORE INTO family_subscription
(id, family_group_id, subscription_plan_id, start_date, end_date, status, canceled_at)
VALUES
    (1,  1,  6,  DATE_SUB(CURDATE(), INTERVAL 70 DAY),  DATE_SUB(CURDATE(), INTERVAL 40 DAY),  'EXPIRED',   NULL),
    (2,  2,  7,  DATE_SUB(CURDATE(), INTERVAL 5 DAY),   DATE_ADD(CURDATE(), INTERVAL 25 DAY),  'ACTIVE',    NULL),
    (3,  3,  8,  DATE_SUB(CURDATE(), INTERVAL 110 DAY), DATE_SUB(CURDATE(), INTERVAL 50 DAY),  'EXPIRED',   NULL),
    (5,  5,  10, DATE_SUB(CURDATE(), INTERVAL 100 DAY), DATE_ADD(CURDATE(), INTERVAL 265 DAY), 'ACTIVE',    NULL),
    (8,  8,  8,  DATE_SUB(CURDATE(), INTERVAL 120 DAY), DATE_SUB(CURDATE(), INTERVAL 30 DAY),  'CANCELED', DATE_SUB(NOW(), INTERVAL 40 DAY));

-- =====================================================================
-- SUBSCRIPTION INVOICE (12)
-- =====================================================================
INSERT IGNORE INTO subscription_invoice
(id, customer_id, unlimit_subscription_id, family_subscription_id, plan_price, status, created_at, paid_at,type)
VALUES
    (1,  1,  1,    NULL, 500000,   'PAID',    DATE_SUB(NOW(), INTERVAL 10 DAY),  DATE_SUB(NOW(), INTERVAL 10 DAY),0),
    (2,  2,  2,    NULL, 900000,   'PAID',    DATE_SUB(NOW(), INTERVAL 5 DAY),   DATE_SUB(NOW(), INTERVAL 5 DAY),0),
    -- Sua tu 1.350.000 xuong 400.000: cung subscription_invoice(10) tao tong chi tieu 2026
    -- cua customer 3 = 873.500d, khop accumulated_points=1310 theo calculatePoint(amount,1.5) GOLD
    (3,  3,  3,    NULL, 400000,   'PAID',    DATE_SUB(NOW(), INTERVAL 20 DAY),  DATE_SUB(NOW(), INTERVAL 20 DAY),0),
    (5,  5,  5,    NULL, 8500000,  'PAID',    DATE_SUB(NOW(), INTERVAL 100 DAY), DATE_SUB(NOW(), INTERVAL 100 DAY),0),
    (7,  8,  8,    NULL, 1350000,  'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY),   NULL,0),
    (8,  1,  NULL, 1,    1200000,  'PAID',    DATE_SUB(NOW(), INTERVAL 10 DAY),  DATE_SUB(NOW(), INTERVAL 10 DAY),0),
    (9,  2,  NULL, 2,    2000000,  'PAID',    DATE_SUB(NOW(), INTERVAL 5 DAY),   DATE_SUB(NOW(), INTERVAL 5 DAY),0),
    (10, 3,  NULL, 3,    353500,   'PAID',    DATE_SUB(NOW(), INTERVAL 20 DAY),  DATE_SUB(NOW(), INTERVAL 20 DAY),0),
    (12, 5,  NULL, 5,    18000000, 'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY),   NULL,0);

-- =====================================================================
-- PROMOTION (10)
-- =====================================================================
INSERT IGNORE INTO promotion
(id, title, description, start_date, end_date, status, created_at, is_deleted)
VALUES
    (1,  'Summer Promotion',                'Discount on wash packages during summer', DATE_SUB(CURDATE(), INTERVAL 30 DAY),  DATE_ADD(CURDATE(), INTERVAL 15 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 30 DAY), FALSE),
    (2,  'Weekend Discount',                'Weekend offer for customers', DATE_SUB(CURDATE(), INTERVAL 10 DAY),  DATE_ADD(CURDATE(), INTERVAL 50 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 10 DAY), FALSE),
    (3,  'New Customer Offer',             'For customers using the service for the first time', DATE_SUB(CURDATE(), INTERVAL 5 DAY),   DATE_ADD(CURDATE(), INTERVAL 25 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 5 DAY), FALSE),
    (4,  'Flash Sale Tet',                    'Tet holiday promotion', DATE_SUB(CURDATE(), INTERVAL 200 DAY), DATE_SUB(CURDATE(), INTERVAL 180 DAY), 'EXPIRED', DATE_SUB(NOW(), INTERVAL 200 DAY), FALSE),
    (5,  'Company Anniversary',                  'Celebrating the company founding anniversary', DATE_SUB(CURDATE(), INTERVAL 100 DAY), DATE_SUB(CURDATE(), INTERVAL 90 DAY),  'EXPIRED', DATE_SUB(NOW(), INTERVAL 100 DAY), FALSE),
    (6,  'New Branch Grand Opening',     'Grand opening promotion', DATE_SUB(CURDATE(), INTERVAL 2 DAY),   DATE_ADD(CURDATE(), INTERVAL 40 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 2 DAY), FALSE),
    (7,  'Black Friday',                       'Biggest sale of the year', DATE_SUB(CURDATE(), INTERVAL 220 DAY), DATE_SUB(CURDATE(), INTERVAL 218 DAY), 'EXPIRED', DATE_SUB(NOW(), INTERVAL 220 DAY), FALSE),
    (8,  'Double Points',                  'Earn 2x points for every wash', DATE_SUB(CURDATE(), INTERVAL 15 DAY),  DATE_ADD(CURDATE(), INTERVAL 10 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 15 DAY), FALSE),
    (9,  'Rainy Season Offer',                     'Rainy season promotion', DATE_SUB(CURDATE(), INTERVAL 60 DAY),  DATE_SUB(CURDATE(), INTERVAL 30 DAY),  'EXPIRED', DATE_SUB(NOW(), INTERVAL 60 DAY), FALSE),
    (11, 'Family Combo',                     'Offer for the family package', DATE_SUB(CURDATE(), INTERVAL 1 DAY),   DATE_ADD(CURDATE(), INTERVAL 60 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 1 DAY), FALSE),
    (10, 'Vibrant Summer Campaign', 'Deep discount for summer days', DATE_SUB(CURDATE(), INTERVAL 7 DAY), DATE_ADD(CURDATE(), INTERVAL 20 DAY), 'ACTIVE', DATE_SUB(NOW(), INTERVAL 7 DAY), FALSE),
-- Campaign 2: Upcoming, starts next month
    (20, 'Autumn Welcome Campaign', 'Promotion to welcome the new month', DATE_ADD(CURDATE(), INTERVAL 25 DAY), DATE_ADD(CURDATE(), INTERVAL 55 DAY), 'UPCOMING', DATE_SUB(NOW(), INTERVAL 1 DAY), FALSE);

-- =====================================================================
-- PROMOTION TARGET (4) - ids khớp với customer_tier: 1=MEMBER, 2=SILVER, 3=GOLD, 4=PLATINUM
-- =====================================================================
INSERT IGNORE INTO promotion_target
(id, target_name, target_code, description)
VALUES
    (1, 'Member Tier',   'MEMBER',   'Member tier customer'),
    (2, 'Silver Tier',   'SILVER',   'Silver tier customer'),
    (3, 'Gold Tier',     'GOLD',     'Gold tier customer'),
    (4, 'Platinum Tier', 'PLATINUM', 'Platinum tier customer');

-- =====================================================================
-- PROMOTION TARGET MAPPING (15)
-- =====================================================================

INSERT IGNORE INTO promotion_station_mapping (promotion_id, station_id) VALUES
                                                                            (10, 1), -- Chiến dịch Hè Rực Rỡ áp dụng cho Quận 1
                                                                            (10, 2), -- Chiến dịch Hè Rực Rỡ áp dụng cho Quận 7
                                                                            (20, 1); -- Chiến dịch Chào Thu CHỈ áp dụng cho Quận 1

INSERT IGNORE INTO promotion_target_mapping
(promotion_id, promotion_target_id)
VALUES
    (1, 1), (1, 2), (1, 3), (1, 4),
    (2, 1), (2, 2), (2, 3), (2, 4),
    (3, 1), (3, 2), (3, 3), (3, 4),
    (4, 1), (4, 2), (4, 3), (4, 4),
    (5, 1), (5, 2), (5, 3), (5, 4),
    (6, 1), (6, 2), (6, 3), (6, 4),
    (7, 1), (7, 2), (7, 3), (7, 4),
    (8, 1), (8, 2), (8, 3),
    (9, 1), (9, 2), (9, 3), (9, 4),
    (10, 1), (10, 2), (10, 3), (10, 4);

-- =====================================================================
-- VOUCHER (12)
-- =====================================================================
INSERT IGNORE INTO voucher
(id, promotion_id, voucher_code, max_discount_amount, min_order_value, usage_limit, used_count, expiry_date, status, start_date, reusable, discount_percentage, created_at, is_deleted)
VALUES
    (1,  1,    'SUMMER10',  50000,  100000, 200, 35,  DATE_ADD(NOW(), INTERVAL 15 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 30 DAY), true,  10, DATE_SUB(NOW(), INTERVAL 30 DAY), FALSE),
    (2,  2,    'WEEKEND15', 60000,  150000, 150, 40,  DATE_ADD(NOW(), INTERVAL 50 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 10 DAY), true,  15, DATE_SUB(NOW(), INTERVAL 10 DAY), FALSE),
    (3,  3,    'NEWCUS20',  40000,  0,      500, 120, DATE_ADD(NOW(), INTERVAL 25 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 5 DAY),  false, 20, DATE_SUB(NOW(), INTERVAL 5 DAY), FALSE),
    (4,  4,    'TETSALE',   100000, 200000, 100, 100, DATE_SUB(NOW(), INTERVAL 180 DAY), 'EXPIRED', DATE_SUB(NOW(), INTERVAL 200 DAY), false, 25, DATE_SUB(NOW(), INTERVAL 200 DAY), FALSE),
    (5,  5,    'BDAY2025',  80000,  0,      50,  50,  DATE_SUB(NOW(), INTERVAL 90 DAY), 'USED_UP', DATE_SUB(NOW(), INTERVAL 100 DAY), false, 30, DATE_SUB(NOW(), INTERVAL 100 DAY), FALSE),
    (6,  6,    'GRANDOPEN', 70000,  100000, 300, 60,  DATE_ADD(NOW(), INTERVAL 40 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 2 DAY),  true,  20, DATE_SUB(NOW(), INTERVAL 2 DAY), FALSE),
    (7,  7,    'BLACKFRI',  150000, 300000, 80,  80,  DATE_SUB(NOW(), INTERVAL 218 DAY), 'EXPIRED', DATE_SUB(NOW(), INTERVAL 220 DAY), false, 35, DATE_SUB(NOW(), INTERVAL 220 DAY), FALSE),
    (8,  8,    'DOUBLEPT',  100000,   50000,      1000, 230, DATE_ADD(NOW(), INTERVAL 10 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 15 DAY), true,  10, DATE_SUB(NOW(), INTERVAL 15 DAY), FALSE),
    (9,  9,    'RAINY10',   30000,  50000,  120, 120, DATE_SUB(NOW(), INTERVAL 30 DAY), 'EXPIRED', DATE_SUB(NOW(), INTERVAL 60 DAY), false, 10, DATE_SUB(NOW(), INTERVAL 60 DAY), FALSE),
    (10, 10,   'FAMILY5',   45000,  100000, 200, 18,  DATE_ADD(NOW(), INTERVAL 60 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 1 DAY),  true,  5,  DATE_SUB(NOW(), INTERVAL 1 DAY), FALSE),
    (11, NULL, 'WELCOME50', 50000,  10000,      1000, 5,   DATE_ADD(NOW(), INTERVAL 90 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 3 DAY),  false, 5, DATE_SUB(NOW(), INTERVAL 3 DAY), FALSE),
    (12, NULL, 'VIP100',    100000, 500000, 30,  4,   DATE_ADD(NOW(), INTERVAL 45 DAY), 'ACTIVE',  DATE_SUB(NOW(), INTERVAL 7 DAY),  true,  10, DATE_SUB(NOW(), INTERVAL 7 DAY), FALSE),
    (101, NULL, 'VOUCHER_LE_ACTIVE', 30000, 50000, 100, 0, DATE_ADD(NOW(), INTERVAL 20 DAY), 'ACTIVE', DATE_SUB(NOW(), INTERVAL 7 DAY), true, 10, DATE_SUB(NOW(), INTERVAL 7 DAY), FALSE),
    (102, NULL, 'VOUCHER_LE_UPCOMING', 40000, 60000, 150, 0, DATE_ADD(NOW(), INTERVAL 50 DAY), 'UPCOMING', DATE_ADD(NOW(), INTERVAL 20 DAY), true, 15, DATE_SUB(NOW(), INTERVAL 1 DAY), FALSE);

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
    -- APPLIED: voucher held on a still-PENDING booking (3) that has not been finalized
    (14, 3,  3,  3,    DATE_SUB(NOW(), INTERVAL 1 HOUR), 'APPLIED'),
    -- REVERTED: booking 15 was CANCELED, so the voucher hold was released back
    (15, 12, 4,  15,   DATE_SUB(NOW(), INTERVAL 5 DAY),  'REVERTED');

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
    (8,  9,  9,  2,  CURDATE(), 'CANCELED', 'ADVANCE',  5, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 45 MINUTE), NULL, DATE_SUB(NOW(), INTERVAL 5 MINUTE), true, 220000, 40000,  260000, 0,     0),
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

    (99, 101, 203, 1, CURDATE(), 'NO_SHOW', 'ADVANCE', NULL, DATE_SUB(NOW(), INTERVAL 6 DAY),  NULL, NULL, NULL, true, 100000, 0, 100000, 0, 0);

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
    (30, 9,  9,  3, CURDATE(), 'CANCELED', 'ADVANCE',  NULL, DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, NULL, DATE_SUB(NOW(), INTERVAL 5 MINUTE), false, 300000, 0, 300000, 0, 0),
    (31, 10, 10, 1, CURDATE(), 'CONFIRMED', 'WALK_IN', NULL, NOW(),                           NULL, NULL, NULL, true,  100000, 0, 100000, 0, 0);

-- =====================================================================
-- BOOKING ADDON (15)
-- =====================================================================
INSERT IGNORE INTO booking_addon
(id, booking_id, addon_service_id, price)
VALUES
    (1,  1,  1, 50000),
    (2,  1,  3, 20000),
    (3,  2,  2, 150000),
    (4,  2,  3, 180000),
    (5,  6,  5, 120000),
    (6,  8,  4, 40000),
    (7,  9,  7, 90000),
    (8,  11, 1, 50000),
    (9,  12, 2, 150000),
    (10, 13, 3, 20000),
    (11, 14, 7, 90000),
    (12, 14, 6, 130000),
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
    (1, 1, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 3 DAY), 2, 'AVAILABLE'),
    -- booking 2 (station2, +5d, 2 slots)
    (2, 2, '08:00', '08:15', 1, DATE_ADD(CURDATE(), INTERVAL 5 DAY), 1, 'AVAILABLE'),
    (3, 2, '08:15', '08:30', 1, DATE_ADD(CURDATE(), INTERVAL 5 DAY), 1, 'AVAILABLE'),
    -- booking 3 (station3, +2d, 1 slot)
    (4, 3, '08:00', '08:15', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 1, 'AVAILABLE'),
    -- booking 4 (station4, +7d, 1 slot)
    (5, 4, '08:00', '08:15', 2, DATE_ADD(CURDATE(), INTERVAL 7 DAY), 1, 'AVAILABLE'),
    -- booking 5 (station1, +1d, 1 slot)
    (6, 1, '08:00', '08:15', 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 1, 'AVAILABLE'),
    -- booking 6 (station2, today, 2 slots)
    (7, 2, '08:00', '08:15', 1, CURDATE(), 1, 'AVAILABLE'),
    (8, 2, '08:15', '08:30', 1, CURDATE(), 1, 'AVAILABLE'),
    -- booking 7 (station1, today, 1 slot)
    (9, 1, '08:00', '08:15', 5, CURDATE(), 1, 'AVAILABLE'),
    -- booking 8 (station3, today, 2 slots)
    (37, 3, '08:00', '08:15', 3, CURDATE(), 1, 'AVAILABLE'),
    (38, 3, '08:15', '08:30', 3, CURDATE(), 1, 'AVAILABLE'),
    -- booking 9 (station1, today, 1 slot)
    (39, 1, '08:15', '08:30', 5, CURDATE(), 1, 'AVAILABLE'),
    -- booking 10 (station4, today, 1 slot)
    (13, 4, '08:00', '08:15', 2, CURDATE(), 1, 'AVAILABLE'),
    -- booking 11 (station1, -10d, 1 slot, COMPLETED)
    (14, 1, '08:00', '08:15', 5, DATE_SUB(CURDATE(), INTERVAL 10 DAY), 1, 'COMPLETED'),
    -- booking 12 (station2, -7d, 2 slots, COMPLETED)
    (15, 2, '08:00', '08:15', 1, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 1, 'COMPLETED'),
    (16, 2, '08:15', '08:30', 1, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 1, 'COMPLETED'),
    -- booking 13 (station3, -15d, 1 slot, COMPLETED)
    (17, 3, '08:00', '08:15', 3, DATE_SUB(CURDATE(), INTERVAL 15 DAY), 1, 'COMPLETED'),
    -- booking 14 (station6, -20d, 2 slots, COMPLETED)
    (18, 6, '08:00', '08:15', 2, DATE_SUB(CURDATE(), INTERVAL 20 DAY), 1, 'COMPLETED'),
    (19, 6, '08:15', '08:30', 2, DATE_SUB(CURDATE(), INTERVAL 20 DAY), 1, 'COMPLETED'),
    -- booking 15 (station1, -5d, 1 slot, freed by cancellation)
    (20, 1, '08:00', '08:15', 5, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 0, 'AVAILABLE'),
    -- booking 16 (station2, -3d, 2 slots, freed by cancellation)
    (21, 2, '08:00', '08:15', 1, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 0, 'AVAILABLE'),
    (22, 2, '08:15', '08:30', 1, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 0, 'AVAILABLE'),
    -- booking 17 (station3, -2d, 1 slot, freed by cancellation)
    (23, 3, '08:00', '08:15', 3, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    -- booking 18 (station1, -9d, 1 slot, NO_SHOW => COMPLETED)
    (24, 1, '08:00', '08:15', 5, DATE_SUB(CURDATE(), INTERVAL 9 DAY), 1, 'COMPLETED'),
    -- booking 19 (station2, -6d, 2 slots, NO_SHOW => COMPLETED)
    (25, 2, '08:00', '08:15', 1, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 1, 'COMPLETED'),
    (26, 2, '08:15', '08:30', 1, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 1, 'COMPLETED'),
    -- booking 20 (station3, -4d, 1 slot, NO_SHOW => COMPLETED)
    (27, 3, '08:00', '08:15', 3, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 1, 'COMPLETED'),
    -- spare unbooked AVAILABLE slots for browsing
    (28, 5, '08:00', '08:15', 2, DATE_ADD(CURDATE(), INTERVAL 4 DAY), 0, 'AVAILABLE'),
    (29, 5, '08:15', '08:30', 2, DATE_ADD(CURDATE(), INTERVAL 4 DAY), 0, 'AVAILABLE'),
    -- 3rd slot for Premium (required_slot=3) bookings 2,6,12,14,16,19
    (31, 2, '08:30', '08:45', 1, DATE_ADD(CURDATE(), INTERVAL 5 DAY), 1, 'AVAILABLE'),
    (32, 2, '08:30', '08:45', 1, CURDATE(), 1, 'AVAILABLE'),
    (33, 2, '08:30', '08:45', 1, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 1, 'COMPLETED'),
    (34, 6, '08:30', '08:45', 2, DATE_SUB(CURDATE(), INTERVAL 20 DAY), 1, 'COMPLETED'),
    (35, 2, '08:30', '08:45', 1, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 0, 'AVAILABLE'),
    (36, 2, '08:30', '08:45', 1, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 1, 'COMPLETED'),

    (10, 1, '16:00:00', '16:15:00', 5, CURDATE(), 0, 'AVAILABLE'),
    (11, 1, '16:15:00', '16:30:00', 5, CURDATE(), 0, 'AVAILABLE'),
    -- booking 9 slot moved to station 4 (Ba Dinh) so its WASHING lane (wash_lane 11) is co-located
    (12, 4, '16:30:00', '16:45:00', 2, CURDATE(), 1, 'WASHING');

-- =====================================================================
-- BOOKING SLOT (40-43) — slots for the TEST CHECK-IN FLOW bookings 28-31 at
-- station 2 (Thu Duc, 1 lane -> max_capacity=1). Each slot booked_count=1 =
-- max_capacity=1 -> status FULL (covers BookingSlotStatus.FULL, docs/seed.md 1.3).
-- start_time derived from NOW so bookings are inside the check-in window whenever
-- the app is booted; date derived from the same timestamp so it rolls past midnight.
-- =====================================================================
INSERT IGNORE INTO booking_slot
(id, station_id, start_time, end_time, max_capacity, date, booked_count, status)
VALUES
    (40, 2, TIME(DATE_ADD(NOW(), INTERVAL 5 MINUTE)), TIME(DATE_ADD(NOW(), INTERVAL 20 MINUTE)), 1, DATE(DATE_ADD(NOW(), INTERVAL 5 MINUTE)), 1, 'FULL'),
    (41, 2, TIME(DATE_ADD(NOW(), INTERVAL 20 MINUTE)), TIME(DATE_ADD(NOW(), INTERVAL 35 MINUTE)), 1, DATE(DATE_ADD(NOW(), INTERVAL 20 MINUTE)), 1, 'FULL'),
    (42, 2, TIME(DATE_ADD(NOW(), INTERVAL 35 MINUTE)), TIME(DATE_ADD(NOW(), INTERVAL 50 MINUTE)), 1, DATE(DATE_ADD(NOW(), INTERVAL 35 MINUTE)), 1, 'FULL'),
    (43, 2, TIME(DATE_ADD(NOW(), INTERVAL 50 MINUTE)), TIME(DATE_ADD(NOW(), INTERVAL 65 MINUTE)), 1, DATE(DATE_ADD(NOW(), INTERVAL 50 MINUTE)), 1, 'FULL');

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
    (87, 2, '08:45', '09:00', 1, CURDATE(), 0, 'AVAILABLE'),
    (88, 2, '09:00', '09:15', 1, CURDATE(), 0, 'AVAILABLE'),
    (89, 2, '09:15', '09:30', 1, CURDATE(), 0, 'AVAILABLE'),
    (90, 2, '09:30', '09:45', 1, CURDATE(), 0, 'AVAILABLE'),
    (91, 2, '09:45', '10:00', 1, CURDATE(), 0, 'AVAILABLE'),
    (92, 2, '10:00', '10:15', 1, CURDATE(), 0, 'AVAILABLE'),
    (93, 2, '10:15', '10:30', 1, CURDATE(), 0, 'AVAILABLE'),
    (94, 2, '10:30', '10:45', 1, CURDATE(), 0, 'AVAILABLE'),
    (95, 2, '10:45', '11:00', 1, CURDATE(), 0, 'AVAILABLE'),
    (96, 2, '11:00', '11:15', 1, CURDATE(), 0, 'AVAILABLE'),
    (97, 2, '11:15', '11:30', 1, CURDATE(), 0, 'AVAILABLE'),
    (98, 2, '11:30', '11:45', 1, CURDATE(), 0, 'AVAILABLE'),
    (99, 2, '11:45', '12:00', 1, CURDATE(), 0, 'AVAILABLE'),
    (100, 2, '12:00', '12:15', 1, CURDATE(), 0, 'AVAILABLE'),
    (101, 2, '12:15', '12:30', 1, CURDATE(), 0, 'AVAILABLE'),
    (102, 2, '12:30', '12:45', 1, CURDATE(), 0, 'AVAILABLE'),
    (103, 2, '12:45', '13:00', 1, CURDATE(), 0, 'AVAILABLE'),
    (104, 2, '13:00', '13:15', 1, CURDATE(), 0, 'AVAILABLE'),
    (105, 2, '13:15', '13:30', 1, CURDATE(), 0, 'AVAILABLE'),
    (106, 2, '13:30', '13:45', 1, CURDATE(), 0, 'AVAILABLE'),
    (107, 2, '13:45', '14:00', 1, CURDATE(), 0, 'AVAILABLE'),
    (108, 2, '14:00', '14:15', 1, CURDATE(), 0, 'AVAILABLE'),
    (109, 2, '14:15', '14:30', 1, CURDATE(), 0, 'AVAILABLE'),
    (110, 2, '14:30', '14:45', 1, CURDATE(), 0, 'AVAILABLE'),
    (111, 2, '14:45', '15:00', 1, CURDATE(), 0, 'AVAILABLE'),
    (112, 2, '15:00', '15:15', 1, CURDATE(), 0, 'AVAILABLE'),
    (113, 2, '15:15', '15:30', 1, CURDATE(), 0, 'AVAILABLE'),
    (114, 2, '15:30', '15:45', 1, CURDATE(), 0, 'AVAILABLE'),
    (115, 2, '15:45', '16:00', 1, CURDATE(), 0, 'AVAILABLE'),
    (116, 2, '16:00', '16:15', 1, CURDATE(), 0, 'AVAILABLE'),
    (117, 2, '16:15', '16:30', 1, CURDATE(), 0, 'AVAILABLE'),
    (118, 2, '16:30', '16:45', 1, CURDATE(), 0, 'AVAILABLE'),
    (119, 2, '16:45', '17:00', 1, CURDATE(), 0, 'AVAILABLE'),
    (120, 2, '17:00', '17:15', 1, CURDATE(), 0, 'AVAILABLE'),
    (121, 2, '17:15', '17:30', 1, CURDATE(), 0, 'AVAILABLE'),
    (122, 2, '17:30', '17:45', 1, CURDATE(), 0, 'AVAILABLE'),
    (123, 2, '17:45', '18:00', 1, CURDATE(), 0, 'AVAILABLE'),
    (124, 2, '18:00', '18:15', 1, CURDATE(), 0, 'AVAILABLE'),
    (125, 2, '18:15', '18:30', 1, CURDATE(), 0, 'AVAILABLE'),
    (126, 2, '18:30', '18:45', 1, CURDATE(), 0, 'AVAILABLE'),
    (127, 2, '18:45', '19:00', 1, CURDATE(), 0, 'AVAILABLE'),
    (128, 2, '19:00', '19:15', 1, CURDATE(), 0, 'AVAILABLE'),
    (129, 2, '19:15', '19:30', 1, CURDATE(), 0, 'AVAILABLE'),
    (130, 2, '19:30', '19:45', 1, CURDATE(), 0, 'AVAILABLE'),
    (131, 2, '19:45', '20:00', 1, CURDATE(), 0, 'AVAILABLE'),
    (132, 3, '08:30', '08:45', 3, CURDATE(), 0, 'AVAILABLE'),
    (133, 3, '08:45', '09:00', 3, CURDATE(), 0, 'AVAILABLE'),
    (134, 3, '09:00', '09:15', 3, CURDATE(), 0, 'AVAILABLE'),
    (135, 3, '09:15', '09:30', 3, CURDATE(), 0, 'AVAILABLE'),
    (136, 3, '09:30', '09:45', 3, CURDATE(), 0, 'AVAILABLE'),
    (137, 3, '09:45', '10:00', 3, CURDATE(), 0, 'AVAILABLE'),
    (138, 3, '10:00', '10:15', 3, CURDATE(), 0, 'AVAILABLE'),
    (139, 3, '10:15', '10:30', 3, CURDATE(), 0, 'AVAILABLE'),
    (140, 3, '10:30', '10:45', 3, CURDATE(), 0, 'AVAILABLE'),
    (141, 3, '10:45', '11:00', 3, CURDATE(), 0, 'AVAILABLE'),
    (142, 3, '11:00', '11:15', 3, CURDATE(), 0, 'AVAILABLE'),
    (143, 3, '11:15', '11:30', 3, CURDATE(), 0, 'AVAILABLE'),
    (144, 3, '11:30', '11:45', 3, CURDATE(), 0, 'AVAILABLE'),
    (145, 3, '11:45', '12:00', 3, CURDATE(), 0, 'AVAILABLE'),
    (146, 3, '12:00', '12:15', 3, CURDATE(), 0, 'AVAILABLE'),
    (147, 3, '12:15', '12:30', 3, CURDATE(), 0, 'AVAILABLE'),
    (148, 3, '12:30', '12:45', 3, CURDATE(), 0, 'AVAILABLE'),
    (149, 3, '12:45', '13:00', 3, CURDATE(), 0, 'AVAILABLE'),
    (150, 3, '13:00', '13:15', 3, CURDATE(), 0, 'AVAILABLE'),
    (151, 3, '13:15', '13:30', 3, CURDATE(), 0, 'AVAILABLE'),
    (152, 3, '13:30', '13:45', 3, CURDATE(), 0, 'AVAILABLE'),
    (153, 3, '13:45', '14:00', 3, CURDATE(), 0, 'AVAILABLE'),
    (154, 3, '14:00', '14:15', 3, CURDATE(), 0, 'AVAILABLE'),
    (155, 3, '14:15', '14:30', 3, CURDATE(), 0, 'AVAILABLE'),
    (156, 3, '14:30', '14:45', 3, CURDATE(), 0, 'AVAILABLE'),
    (157, 3, '14:45', '15:00', 3, CURDATE(), 0, 'AVAILABLE'),
    (158, 3, '15:00', '15:15', 3, CURDATE(), 0, 'AVAILABLE'),
    (159, 3, '15:15', '15:30', 3, CURDATE(), 0, 'AVAILABLE'),
    (160, 3, '15:30', '15:45', 3, CURDATE(), 0, 'AVAILABLE'),
    (161, 3, '15:45', '16:00', 3, CURDATE(), 0, 'AVAILABLE'),
    (162, 3, '16:00', '16:15', 3, CURDATE(), 0, 'AVAILABLE'),
    (163, 3, '16:15', '16:30', 3, CURDATE(), 0, 'AVAILABLE'),
    (164, 3, '16:30', '16:45', 3, CURDATE(), 0, 'AVAILABLE'),
    (165, 3, '16:45', '17:00', 3, CURDATE(), 0, 'AVAILABLE'),
    (166, 3, '17:00', '17:15', 3, CURDATE(), 0, 'AVAILABLE'),
    (167, 3, '17:15', '17:30', 3, CURDATE(), 0, 'AVAILABLE'),
    (168, 3, '17:30', '17:45', 3, CURDATE(), 0, 'AVAILABLE'),
    (169, 3, '17:45', '18:00', 3, CURDATE(), 0, 'AVAILABLE'),
    (170, 3, '18:00', '18:15', 3, CURDATE(), 0, 'AVAILABLE'),
    (171, 3, '18:15', '18:30', 3, CURDATE(), 0, 'AVAILABLE'),
    (172, 3, '18:30', '18:45', 3, CURDATE(), 0, 'AVAILABLE'),
    (173, 3, '18:45', '19:00', 3, CURDATE(), 0, 'AVAILABLE'),
    (174, 3, '19:00', '19:15', 3, CURDATE(), 0, 'AVAILABLE'),
    (175, 3, '19:15', '19:30', 3, CURDATE(), 0, 'AVAILABLE'),
    (176, 3, '19:30', '19:45', 3, CURDATE(), 0, 'AVAILABLE'),
    (177, 3, '19:45', '20:00', 3, CURDATE(), 0, 'AVAILABLE'),
    (178, 4, '08:15', '08:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (179, 4, '08:30', '08:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (180, 4, '08:45', '09:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (181, 4, '09:00', '09:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (182, 4, '09:15', '09:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (183, 4, '09:30', '09:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (184, 4, '09:45', '10:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (185, 4, '10:00', '10:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (186, 4, '10:15', '10:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (187, 4, '10:30', '10:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (188, 4, '10:45', '11:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (189, 4, '11:00', '11:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (190, 4, '11:15', '11:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (191, 4, '11:30', '11:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (192, 4, '11:45', '12:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (193, 4, '12:00', '12:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (194, 4, '12:15', '12:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (195, 4, '12:30', '12:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (196, 4, '12:45', '13:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (197, 4, '13:00', '13:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (198, 4, '13:15', '13:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (199, 4, '13:30', '13:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (200, 4, '13:45', '14:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (201, 4, '14:00', '14:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (202, 4, '14:15', '14:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (203, 4, '14:30', '14:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (204, 4, '14:45', '15:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (205, 4, '15:00', '15:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (206, 4, '15:15', '15:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (207, 4, '15:30', '15:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (208, 4, '15:45', '16:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (209, 4, '16:00', '16:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (210, 4, '16:15', '16:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (211, 4, '16:30', '16:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (212, 4, '16:45', '17:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (213, 4, '17:00', '17:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (214, 4, '17:15', '17:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (215, 4, '17:30', '17:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (216, 4, '17:45', '18:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (217, 4, '18:00', '18:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (218, 4, '18:15', '18:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (219, 4, '18:30', '18:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (220, 4, '18:45', '19:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (221, 4, '19:00', '19:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (222, 4, '19:15', '19:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (223, 4, '19:30', '19:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (224, 4, '19:45', '20:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (225, 5, '08:00', '08:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (226, 5, '08:15', '08:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (227, 5, '08:30', '08:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (228, 5, '08:45', '09:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (229, 5, '09:00', '09:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (230, 5, '09:15', '09:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (231, 5, '09:30', '09:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (232, 5, '09:45', '10:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (233, 5, '10:00', '10:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (234, 5, '10:15', '10:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (235, 5, '10:30', '10:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (236, 5, '10:45', '11:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (237, 5, '11:00', '11:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (238, 5, '11:15', '11:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (239, 5, '11:30', '11:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (240, 5, '11:45', '12:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (241, 5, '12:00', '12:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (242, 5, '12:15', '12:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (243, 5, '12:30', '12:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (244, 5, '12:45', '13:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (245, 5, '13:00', '13:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (246, 5, '13:15', '13:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (247, 5, '13:30', '13:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (248, 5, '13:45', '14:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (249, 5, '14:00', '14:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (250, 5, '14:15', '14:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (251, 5, '14:30', '14:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (252, 5, '14:45', '15:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (253, 5, '15:00', '15:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (254, 5, '15:15', '15:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (255, 5, '15:30', '15:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (256, 5, '15:45', '16:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (257, 5, '16:00', '16:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (258, 5, '16:15', '16:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (259, 5, '16:30', '16:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (260, 5, '16:45', '17:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (261, 5, '17:00', '17:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (262, 5, '17:15', '17:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (263, 5, '17:30', '17:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (264, 5, '17:45', '18:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (265, 5, '18:00', '18:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (266, 5, '18:15', '18:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (267, 5, '18:30', '18:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (268, 5, '18:45', '19:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (269, 5, '19:00', '19:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (270, 5, '19:15', '19:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (271, 5, '19:30', '19:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (272, 5, '19:45', '20:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (273, 6, '08:00', '08:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (274, 6, '08:15', '08:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (275, 6, '08:30', '08:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (276, 6, '08:45', '09:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (277, 6, '09:00', '09:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (278, 6, '09:15', '09:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (279, 6, '09:30', '09:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (280, 6, '09:45', '10:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (281, 6, '10:00', '10:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (282, 6, '10:15', '10:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (283, 6, '10:30', '10:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (284, 6, '10:45', '11:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (285, 6, '11:00', '11:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (286, 6, '11:15', '11:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (287, 6, '11:30', '11:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (288, 6, '11:45', '12:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (289, 6, '12:00', '12:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (290, 6, '12:15', '12:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (291, 6, '12:30', '12:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (292, 6, '12:45', '13:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (293, 6, '13:00', '13:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (294, 6, '13:15', '13:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (295, 6, '13:30', '13:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (296, 6, '13:45', '14:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (297, 6, '14:00', '14:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (298, 6, '14:15', '14:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (299, 6, '14:30', '14:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (300, 6, '14:45', '15:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (301, 6, '15:00', '15:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (302, 6, '15:15', '15:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (303, 6, '15:30', '15:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (304, 6, '15:45', '16:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (305, 6, '16:00', '16:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (306, 6, '16:15', '16:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (307, 6, '16:30', '16:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (308, 6, '16:45', '17:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (309, 6, '17:00', '17:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (310, 6, '17:15', '17:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (311, 6, '17:30', '17:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (312, 6, '17:45', '18:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (313, 6, '18:00', '18:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (314, 6, '18:15', '18:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (315, 6, '18:30', '18:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (316, 6, '18:45', '19:00', 2, CURDATE(), 0, 'AVAILABLE'),
    (317, 6, '19:00', '19:15', 2, CURDATE(), 0, 'AVAILABLE'),
    (318, 6, '19:15', '19:30', 2, CURDATE(), 0, 'AVAILABLE'),
    (319, 6, '19:30', '19:45', 2, CURDATE(), 0, 'AVAILABLE'),
    (320, 6, '19:45', '20:00', 2, CURDATE(), 0, 'AVAILABLE'),
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
    (512, 2, '08:00', '08:15', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (513, 2, '08:15', '08:30', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (514, 2, '08:30', '08:45', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (515, 2, '08:45', '09:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (516, 2, '09:00', '09:15', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (517, 2, '09:15', '09:30', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (518, 2, '09:30', '09:45', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (519, 2, '09:45', '10:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (520, 2, '10:00', '10:15', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (521, 2, '10:15', '10:30', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (522, 2, '10:30', '10:45', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (523, 2, '10:45', '11:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (524, 2, '11:00', '11:15', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (525, 2, '11:15', '11:30', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (526, 2, '11:30', '11:45', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (527, 2, '11:45', '12:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (528, 2, '12:00', '12:15', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (529, 2, '12:15', '12:30', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (530, 2, '12:30', '12:45', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (531, 2, '12:45', '13:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (532, 2, '13:00', '13:15', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (533, 2, '13:15', '13:30', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (534, 2, '13:30', '13:45', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (535, 2, '13:45', '14:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (536, 2, '14:00', '14:15', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (537, 2, '14:15', '14:30', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (538, 2, '14:30', '14:45', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (539, 2, '14:45', '15:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (540, 2, '15:00', '15:15', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (541, 2, '15:15', '15:30', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (542, 2, '15:30', '15:45', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (543, 2, '15:45', '16:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (544, 2, '16:00', '16:15', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (545, 2, '16:15', '16:30', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (546, 2, '16:30', '16:45', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (547, 2, '16:45', '17:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (548, 2, '17:00', '17:15', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (549, 2, '17:15', '17:30', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (550, 2, '17:30', '17:45', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (551, 2, '17:45', '18:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (552, 2, '18:00', '18:15', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (553, 2, '18:15', '18:30', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (554, 2, '18:30', '18:45', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (555, 2, '18:45', '19:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (556, 2, '19:00', '19:15', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (557, 2, '19:15', '19:30', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (558, 2, '19:30', '19:45', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (559, 2, '19:45', '20:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (560, 3, '08:00', '08:15', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (561, 3, '08:15', '08:30', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (562, 3, '08:30', '08:45', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (563, 3, '08:45', '09:00', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (564, 3, '09:00', '09:15', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (565, 3, '09:15', '09:30', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (566, 3, '09:30', '09:45', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (567, 3, '09:45', '10:00', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (568, 3, '10:00', '10:15', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (569, 3, '10:15', '10:30', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (570, 3, '10:30', '10:45', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (571, 3, '10:45', '11:00', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (572, 3, '11:00', '11:15', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (573, 3, '11:15', '11:30', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (574, 3, '11:30', '11:45', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (575, 3, '11:45', '12:00', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (576, 3, '12:00', '12:15', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (577, 3, '12:15', '12:30', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (578, 3, '12:30', '12:45', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (579, 3, '12:45', '13:00', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (580, 3, '13:00', '13:15', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (581, 3, '13:15', '13:30', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (582, 3, '13:30', '13:45', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (583, 3, '13:45', '14:00', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (584, 3, '14:00', '14:15', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (585, 3, '14:15', '14:30', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (586, 3, '14:30', '14:45', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (587, 3, '14:45', '15:00', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (588, 3, '15:00', '15:15', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (589, 3, '15:15', '15:30', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (590, 3, '15:30', '15:45', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (591, 3, '15:45', '16:00', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (592, 3, '16:00', '16:15', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (593, 3, '16:15', '16:30', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (594, 3, '16:30', '16:45', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (595, 3, '16:45', '17:00', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (596, 3, '17:00', '17:15', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (597, 3, '17:15', '17:30', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (598, 3, '17:30', '17:45', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (599, 3, '17:45', '18:00', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (600, 3, '18:00', '18:15', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (601, 3, '18:15', '18:30', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (602, 3, '18:30', '18:45', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (603, 3, '18:45', '19:00', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (604, 3, '19:00', '19:15', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (605, 3, '19:15', '19:30', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (606, 3, '19:30', '19:45', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (607, 3, '19:45', '20:00', 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (608, 4, '08:00', '08:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (609, 4, '08:15', '08:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (610, 4, '08:30', '08:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (611, 4, '08:45', '09:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (612, 4, '09:00', '09:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (613, 4, '09:15', '09:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (614, 4, '09:30', '09:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (615, 4, '09:45', '10:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (616, 4, '10:00', '10:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (617, 4, '10:15', '10:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (618, 4, '10:30', '10:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (619, 4, '10:45', '11:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (620, 4, '11:00', '11:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (621, 4, '11:15', '11:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (622, 4, '11:30', '11:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (623, 4, '11:45', '12:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (624, 4, '12:00', '12:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (625, 4, '12:15', '12:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (626, 4, '12:30', '12:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (627, 4, '12:45', '13:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (628, 4, '13:00', '13:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (629, 4, '13:15', '13:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (630, 4, '13:30', '13:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (631, 4, '13:45', '14:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (632, 4, '14:00', '14:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (633, 4, '14:15', '14:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (634, 4, '14:30', '14:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (635, 4, '14:45', '15:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (636, 4, '15:00', '15:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (637, 4, '15:15', '15:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (638, 4, '15:30', '15:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (639, 4, '15:45', '16:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (640, 4, '16:00', '16:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (641, 4, '16:15', '16:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (642, 4, '16:30', '16:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (643, 4, '16:45', '17:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (644, 4, '17:00', '17:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (645, 4, '17:15', '17:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (646, 4, '17:30', '17:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (647, 4, '17:45', '18:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (648, 4, '18:00', '18:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (649, 4, '18:15', '18:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (650, 4, '18:30', '18:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (651, 4, '18:45', '19:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (652, 4, '19:00', '19:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (653, 4, '19:15', '19:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (654, 4, '19:30', '19:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (655, 4, '19:45', '20:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (656, 5, '08:00', '08:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (657, 5, '08:15', '08:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (658, 5, '08:30', '08:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (659, 5, '08:45', '09:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (660, 5, '09:00', '09:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (661, 5, '09:15', '09:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (662, 5, '09:30', '09:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (663, 5, '09:45', '10:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (664, 5, '10:00', '10:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (665, 5, '10:15', '10:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (666, 5, '10:30', '10:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (667, 5, '10:45', '11:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (668, 5, '11:00', '11:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (669, 5, '11:15', '11:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (670, 5, '11:30', '11:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (671, 5, '11:45', '12:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (672, 5, '12:00', '12:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (673, 5, '12:15', '12:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (674, 5, '12:30', '12:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (675, 5, '12:45', '13:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (676, 5, '13:00', '13:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (677, 5, '13:15', '13:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (678, 5, '13:30', '13:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (679, 5, '13:45', '14:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (680, 5, '14:00', '14:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (681, 5, '14:15', '14:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (682, 5, '14:30', '14:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (683, 5, '14:45', '15:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (684, 5, '15:00', '15:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (685, 5, '15:15', '15:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (686, 5, '15:30', '15:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (687, 5, '15:45', '16:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (688, 5, '16:00', '16:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (689, 5, '16:15', '16:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (690, 5, '16:30', '16:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (691, 5, '16:45', '17:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (692, 5, '17:00', '17:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (693, 5, '17:15', '17:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (694, 5, '17:30', '17:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (695, 5, '17:45', '18:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (696, 5, '18:00', '18:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (697, 5, '18:15', '18:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (698, 5, '18:30', '18:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (699, 5, '18:45', '19:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (700, 5, '19:00', '19:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (701, 5, '19:15', '19:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (702, 5, '19:30', '19:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (703, 5, '19:45', '20:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (704, 6, '08:00', '08:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (705, 6, '08:15', '08:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (706, 6, '08:30', '08:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (707, 6, '08:45', '09:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (708, 6, '09:00', '09:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (709, 6, '09:15', '09:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (710, 6, '09:30', '09:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (711, 6, '09:45', '10:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (712, 6, '10:00', '10:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (713, 6, '10:15', '10:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (714, 6, '10:30', '10:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (715, 6, '10:45', '11:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (716, 6, '11:00', '11:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (717, 6, '11:15', '11:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (718, 6, '11:30', '11:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (719, 6, '11:45', '12:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (720, 6, '12:00', '12:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (721, 6, '12:15', '12:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (722, 6, '12:30', '12:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (723, 6, '12:45', '13:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (724, 6, '13:00', '13:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (725, 6, '13:15', '13:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (726, 6, '13:30', '13:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (727, 6, '13:45', '14:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (728, 6, '14:00', '14:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (729, 6, '14:15', '14:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (730, 6, '14:30', '14:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (731, 6, '14:45', '15:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (732, 6, '15:00', '15:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (733, 6, '15:15', '15:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (734, 6, '15:30', '15:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (735, 6, '15:45', '16:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (736, 6, '16:00', '16:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (737, 6, '16:15', '16:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (738, 6, '16:30', '16:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (739, 6, '16:45', '17:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (740, 6, '17:00', '17:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (741, 6, '17:15', '17:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (742, 6, '17:30', '17:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (743, 6, '17:45', '18:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (744, 6, '18:00', '18:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (745, 6, '18:15', '18:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (746, 6, '18:30', '18:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (747, 6, '18:45', '19:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (748, 6, '19:00', '19:15', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (749, 6, '19:15', '19:30', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (750, 6, '19:30', '19:45', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (751, 6, '19:45', '20:00', 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
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
    (944, 2, '08:00', '08:15', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (945, 2, '08:15', '08:30', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (946, 2, '08:30', '08:45', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (947, 2, '08:45', '09:00', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (948, 2, '09:00', '09:15', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (949, 2, '09:15', '09:30', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (950, 2, '09:30', '09:45', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (951, 2, '09:45', '10:00', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (952, 2, '10:00', '10:15', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (953, 2, '10:15', '10:30', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (954, 2, '10:30', '10:45', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (955, 2, '10:45', '11:00', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (956, 2, '11:00', '11:15', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (957, 2, '11:15', '11:30', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (958, 2, '11:30', '11:45', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (959, 2, '11:45', '12:00', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (960, 2, '12:00', '12:15', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (961, 2, '12:15', '12:30', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (962, 2, '12:30', '12:45', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (963, 2, '12:45', '13:00', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (964, 2, '13:00', '13:15', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (965, 2, '13:15', '13:30', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (966, 2, '13:30', '13:45', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (967, 2, '13:45', '14:00', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (968, 2, '14:00', '14:15', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (969, 2, '14:15', '14:30', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (970, 2, '14:30', '14:45', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (971, 2, '14:45', '15:00', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (972, 2, '15:00', '15:15', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (973, 2, '15:15', '15:30', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (974, 2, '15:30', '15:45', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (975, 2, '15:45', '16:00', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (976, 2, '16:00', '16:15', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (977, 2, '16:15', '16:30', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (978, 2, '16:30', '16:45', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (979, 2, '16:45', '17:00', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (980, 2, '17:00', '17:15', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (981, 2, '17:15', '17:30', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (982, 2, '17:30', '17:45', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (983, 2, '17:45', '18:00', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (984, 2, '18:00', '18:15', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (985, 2, '18:15', '18:30', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (986, 2, '18:30', '18:45', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (987, 2, '18:45', '19:00', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (988, 2, '19:00', '19:15', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (989, 2, '19:15', '19:30', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (990, 2, '19:30', '19:45', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (991, 2, '19:45', '20:00', 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (992, 3, '08:15', '08:30', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (993, 3, '08:30', '08:45', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (994, 3, '08:45', '09:00', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (995, 3, '09:00', '09:15', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (996, 3, '09:15', '09:30', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (997, 3, '09:30', '09:45', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (998, 3, '09:45', '10:00', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (999, 3, '10:00', '10:15', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1000, 3, '10:15', '10:30', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1001, 3, '10:30', '10:45', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1002, 3, '10:45', '11:00', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1003, 3, '11:00', '11:15', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1004, 3, '11:15', '11:30', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1005, 3, '11:30', '11:45', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1006, 3, '11:45', '12:00', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1007, 3, '12:00', '12:15', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1008, 3, '12:15', '12:30', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1009, 3, '12:30', '12:45', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1010, 3, '12:45', '13:00', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1011, 3, '13:00', '13:15', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1012, 3, '13:15', '13:30', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1013, 3, '13:30', '13:45', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1014, 3, '13:45', '14:00', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1015, 3, '14:00', '14:15', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1016, 3, '14:15', '14:30', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1017, 3, '14:30', '14:45', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1018, 3, '14:45', '15:00', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1019, 3, '15:00', '15:15', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1020, 3, '15:15', '15:30', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1021, 3, '15:30', '15:45', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1022, 3, '15:45', '16:00', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1023, 3, '16:00', '16:15', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1024, 3, '16:15', '16:30', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1025, 3, '16:30', '16:45', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1026, 3, '16:45', '17:00', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1027, 3, '17:00', '17:15', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1028, 3, '17:15', '17:30', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1029, 3, '17:30', '17:45', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1030, 3, '17:45', '18:00', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1031, 3, '18:00', '18:15', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1032, 3, '18:15', '18:30', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1033, 3, '18:30', '18:45', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1034, 3, '18:45', '19:00', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1035, 3, '19:00', '19:15', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1036, 3, '19:15', '19:30', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1037, 3, '19:30', '19:45', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1038, 3, '19:45', '20:00', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1039, 4, '08:00', '08:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1040, 4, '08:15', '08:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1041, 4, '08:30', '08:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1042, 4, '08:45', '09:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1043, 4, '09:00', '09:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1044, 4, '09:15', '09:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1045, 4, '09:30', '09:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1046, 4, '09:45', '10:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1047, 4, '10:00', '10:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1048, 4, '10:15', '10:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1049, 4, '10:30', '10:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1050, 4, '10:45', '11:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1051, 4, '11:00', '11:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1052, 4, '11:15', '11:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1053, 4, '11:30', '11:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1054, 4, '11:45', '12:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1055, 4, '12:00', '12:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1056, 4, '12:15', '12:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1057, 4, '12:30', '12:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1058, 4, '12:45', '13:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1059, 4, '13:00', '13:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1060, 4, '13:15', '13:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1061, 4, '13:30', '13:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1062, 4, '13:45', '14:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1063, 4, '14:00', '14:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1064, 4, '14:15', '14:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1065, 4, '14:30', '14:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1066, 4, '14:45', '15:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1067, 4, '15:00', '15:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1068, 4, '15:15', '15:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1069, 4, '15:30', '15:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1070, 4, '15:45', '16:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1071, 4, '16:00', '16:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1072, 4, '16:15', '16:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1073, 4, '16:30', '16:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1074, 4, '16:45', '17:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1075, 4, '17:00', '17:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1076, 4, '17:15', '17:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1077, 4, '17:30', '17:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1078, 4, '17:45', '18:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1079, 4, '18:00', '18:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1080, 4, '18:15', '18:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1081, 4, '18:30', '18:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1082, 4, '18:45', '19:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1083, 4, '19:00', '19:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1084, 4, '19:15', '19:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1085, 4, '19:30', '19:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1086, 4, '19:45', '20:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1087, 5, '08:00', '08:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1088, 5, '08:15', '08:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1089, 5, '08:30', '08:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1090, 5, '08:45', '09:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1091, 5, '09:00', '09:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1092, 5, '09:15', '09:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1093, 5, '09:30', '09:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1094, 5, '09:45', '10:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1095, 5, '10:00', '10:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1096, 5, '10:15', '10:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1097, 5, '10:30', '10:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1098, 5, '10:45', '11:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1099, 5, '11:00', '11:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1100, 5, '11:15', '11:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1101, 5, '11:30', '11:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1102, 5, '11:45', '12:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1103, 5, '12:00', '12:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1104, 5, '12:15', '12:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1105, 5, '12:30', '12:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1106, 5, '12:45', '13:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1107, 5, '13:00', '13:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1108, 5, '13:15', '13:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1109, 5, '13:30', '13:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1110, 5, '13:45', '14:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1111, 5, '14:00', '14:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1112, 5, '14:15', '14:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1113, 5, '14:30', '14:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1114, 5, '14:45', '15:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1115, 5, '15:00', '15:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1116, 5, '15:15', '15:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1117, 5, '15:30', '15:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1118, 5, '15:45', '16:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1119, 5, '16:00', '16:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1120, 5, '16:15', '16:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1121, 5, '16:30', '16:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1122, 5, '16:45', '17:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1123, 5, '17:00', '17:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1124, 5, '17:15', '17:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1125, 5, '17:30', '17:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1126, 5, '17:45', '18:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1127, 5, '18:00', '18:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1128, 5, '18:15', '18:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1129, 5, '18:30', '18:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1130, 5, '18:45', '19:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1131, 5, '19:00', '19:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1132, 5, '19:15', '19:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1133, 5, '19:30', '19:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1134, 5, '19:45', '20:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1135, 6, '08:00', '08:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1136, 6, '08:15', '08:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1137, 6, '08:30', '08:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1138, 6, '08:45', '09:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1139, 6, '09:00', '09:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1140, 6, '09:15', '09:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1141, 6, '09:30', '09:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1142, 6, '09:45', '10:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1143, 6, '10:00', '10:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1144, 6, '10:15', '10:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1145, 6, '10:30', '10:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1146, 6, '10:45', '11:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1147, 6, '11:00', '11:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1148, 6, '11:15', '11:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1149, 6, '11:30', '11:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1150, 6, '11:45', '12:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1151, 6, '12:00', '12:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1152, 6, '12:15', '12:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1153, 6, '12:30', '12:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1154, 6, '12:45', '13:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1155, 6, '13:00', '13:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1156, 6, '13:15', '13:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1157, 6, '13:30', '13:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1158, 6, '13:45', '14:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1159, 6, '14:00', '14:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1160, 6, '14:15', '14:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1161, 6, '14:30', '14:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1162, 6, '14:45', '15:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1163, 6, '15:00', '15:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1164, 6, '15:15', '15:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1165, 6, '15:30', '15:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1166, 6, '15:45', '16:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1167, 6, '16:00', '16:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1168, 6, '16:15', '16:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1169, 6, '16:30', '16:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1170, 6, '16:45', '17:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1171, 6, '17:00', '17:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1172, 6, '17:15', '17:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1173, 6, '17:30', '17:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1174, 6, '17:45', '18:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1175, 6, '18:00', '18:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1176, 6, '18:15', '18:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1177, 6, '18:30', '18:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1178, 6, '18:45', '19:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1179, 6, '19:00', '19:15', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1180, 6, '19:15', '19:30', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1181, 6, '19:30', '19:45', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (1182, 6, '19:45', '20:00', 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE');

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
-- source_type populated for every row (NOT NULL @Enumerated STRING column
-- that the previous seed omitted entirely, silently dropping rows under
-- INSERT IGNORE). Rows tied to a booking use BOOKING; the pre-2025 history
-- rows with no booking reference use MANUAL; row 37 is a SUBSCRIPTION-sourced
-- earn tied to a subscription_invoice; row 38 is a RESET (annual reset to 0).
INSERT IGNORE INTO loyalty_point_transaction
(id, customer_id, booking_id, subscription_invoice_id, source_type, transaction_type, points, balance_after, created_at)
VALUES
    (1,  1,  11,   NULL, 'BOOKING', 'EARN',   150,  320,  DATE_SUB(NOW(), INTERVAL 12 DAY)),
    (2,  2,  12,   NULL, 'BOOKING', 'EARN',   300,  450,  DATE_SUB(NOW(), INTERVAL 9 DAY)),
    (3,  3,  13,   NULL, 'BOOKING', 'EARN',   100,  610,  DATE_SUB(NOW(), INTERVAL 16 DAY)),
    (4,  12, 14,   NULL, 'BOOKING', 'EARN',   400,  340,  DATE_SUB(NOW(), INTERVAL 22 DAY)),
    (5,  1,  NULL, NULL, 'MANUAL',  'REDEEM', -50,  270,  DATE_SUB(NOW(), INTERVAL 5 DAY)),
    (6,  4,  NULL, NULL, 'MANUAL',  'EARN',   80,   280,  DATE_SUB(NOW(), INTERVAL 30 DAY)),
    (7,  5,  NULL, NULL, 'MANUAL',  'EARN',   200,  900,  DATE_SUB(NOW(), INTERVAL 25 DAY)),
    (8,  6,  NULL, NULL, 'MANUAL',  'EARN',   60,   150,  DATE_SUB(NOW(), INTERVAL 20 DAY)),
    (9,  7,  NULL, NULL, 'MANUAL',  'EARN',   250,  1200, DATE_SUB(NOW(), INTERVAL 18 DAY)),
    (10, 8,  NULL, NULL, 'MANUAL',  'REDEEM', -100, 980,  DATE_SUB(NOW(), INTERVAL 15 DAY)),
    (11, 9,  NULL, NULL, 'MANUAL',  'EARN',   300,  1500, DATE_SUB(NOW(), INTERVAL 12 DAY)),
    (12, 10, NULL, NULL, 'MANUAL',  'EARN',   75,   75,   DATE_SUB(NOW(), INTERVAL 10 DAY)),
    (13, 11, NULL, NULL, 'MANUAL',  'EARN',   200,  200,  DATE_SUB(NOW(), INTERVAL 8 DAY)),
    (14, 10, 9,    NULL, 'BOOKING', 'REDEEM', -50,  25,   DATE_SUB(NOW(), INTERVAL 1 HOUR)),
    (15, 12, NULL, NULL, 'MANUAL',  'EARN',   120,  460,  DATE_SUB(NOW(), INTERVAL 3 DAY)),
    -- Du lieu nam truoc cho customer 1 de test filter theo nam/thang (FE-42-US-01)
    (16, 1,  NULL, NULL, 'MANUAL', 'EARN',   200,  200,  '2024-03-15 10:00:00'),
    (17, 1,  NULL, NULL, 'MANUAL', 'EARN',   150,  350,  '2024-06-20 14:30:00'),
    (18, 1,  NULL, NULL, 'MANUAL', 'REDEEM', -80,  270,  '2024-06-25 09:15:00'),
    (19, 1,  NULL, NULL, 'MANUAL', 'EARN',   100,  370,  '2025-02-10 11:00:00'),
    -- Timeline nam 2025 cho customer 1 (phong@gmail.com) de demo filter theo nam/thang + lich su len/ha hang
    -- Moi giao dich gan voi 1 booking hoan chinh (501-517) de click vao xem Booking Detail
    (20, 1,  501, NULL, 'BOOKING', 'EARN',   300,  300,  '2025-01-12 09:00:00'),
    (21, 1,  502, NULL, 'BOOKING', 'EARN',   300,  600,  '2025-02-14 10:30:00'),
    (22, 1,  503, NULL, 'BOOKING', 'REDEEM', -100, 500,  '2025-03-20 15:00:00'),
    (23, 1,  504, NULL, 'BOOKING', 'EARN',   400,  900,  '2025-04-18 11:00:00'),
    (24, 1,  505, NULL, 'BOOKING', 'EARN',   350,  1250, '2025-05-09 14:00:00'),
    (25, 1,  506, NULL, 'BOOKING', 'REDEEM', -200, 1050, '2025-06-22 16:30:00'),
    (26, 1,  507, NULL, 'BOOKING', 'EARN',   500,  1550, '2025-07-15 09:45:00'),
    (27, 1,  508, NULL, 'BOOKING', 'EARN',   600,  2150, '2025-08-19 13:20:00'),
    (28, 1,  509, NULL, 'BOOKING', 'REDEEM', -650, 1500, '2025-10-04 10:00:00'),
    (29, 1,  510, NULL, 'BOOKING', 'EARN',   300,  1800, '2025-11-10 08:30:00'),
    (30, 1,  511, NULL, 'BOOKING', 'EARN',   300,  2100, '2025-11-30 17:00:00'),
    (31, 1,  512, NULL, 'BOOKING', 'REDEEM', -150, 1950, '2025-12-20 12:00:00'),
    -- Giao dich nam 2026 trong qua khu (dau nam da reset diem tieu dung ve 0), noi lien so du toi 170 truoc giao dich id=1 (~12 ngay truoc -> 320)
    (32, 1,  513, NULL, 'BOOKING', 'EARN',   120,  120,  '2026-01-18 09:30:00'),
    (33, 1,  514, NULL, 'BOOKING', 'EARN',   100,  220,  '2026-02-22 14:15:00'),
    (34, 1,  515, NULL, 'BOOKING', 'REDEEM', -50,  170,  '2026-03-15 10:45:00'),
    (35, 1,  516, NULL, 'BOOKING', 'EARN',   90,   260,  '2026-04-20 16:00:00'),
    (36, 1,  517, NULL, 'BOOKING', 'REDEEM', -90,  170,  '2026-05-16 11:20:00'),
    -- Enum coverage additions (docs/seed.md 1.13): SUBSCRIPTION source tied to
    -- subscription_invoice 1 (customer 1's REGISTER invoice), and a RESET
    -- transaction (annual point reset, balance goes to 0).
    (37, 1,  NULL, 1,    'SUBSCRIPTION', 'EARN',  500, 670, DATE_SUB(NOW(), INTERVAL 10 DAY)),
    (38, 1,  NULL, NULL, 'ADJUSTMENT',   'RESET', -670, 0,  DATE_SUB(NOW(), INTERVAL 9 DAY));

-- =====================================================================
-- CUSTOMER TIER HISTORY (2) — du lieu mau de test GET /api/loyalty/tier-history
-- (chua co trigger tu dong cong diem trong code; du lieu duoc insert thu cong o day)
-- Chi luu cac lan NANG/HA hang thuc su (phai co ca hang cu va hang moi)
-- =====================================================================
INSERT IGNORE INTO customer_tier_history
(id, customer_id, old_tier_id, new_tier_id, value_at_transition, change_type, created_at)
VALUES
    (1, 1, 1, 2, 520,  'UPGRADE', DATE_SUB(NOW(), INTERVAL 60 DAY)),
    (2, 5, 1, 4, 2050, 'UPGRADE', DATE_SUB(NOW(), INTERVAL 30 DAY));

-- Lich su len/ha hang nam 2025 cho customer 1 (phong@gmail.com) — khop ngay + gan booking cua moc doi hang
INSERT IGNORE INTO customer_tier_history
(id, customer_id, old_tier_id, new_tier_id, value_at_transition, change_type, created_at, booking_id)
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
    (5,  6,  7,  420000, 15000, 405000, 'PROVISIONAL',   15000, 0,    300000, 120000, DATE_SUB(NOW(), INTERVAL 3 DAY),  NULL),
    (6,  7,  8,  100000, 0,     100000, 'PROVISIONAL',   0,     0,    100000, 0,      NOW(),                            NULL),
    (7,  8,  9,  260000, 0,     260000, 'PROVISIONAL',   0,     0,    220000, 40000,  DATE_SUB(NOW(), INTERVAL 2 DAY),  NULL),
    (8,  9,  10, 190000, 5000,  185000, 'PROVISIONAL',   0,     5000, 100000, 90000,  DATE_SUB(NOW(), INTERVAL 2 DAY),  NULL),
    (9,  10, 11, 150000, 0,     150000, 'PROVISIONAL',   0,     0,    150000, 0,      NOW(),                            NULL),
    (10, 15, 4,  100000, 0,     100000, 'CANCEL', 0,     0,    100000, 0,      DATE_SUB(NOW(), INTERVAL 8 DAY),  NULL),
    (11, 16, 5,  450000, 0,     450000, 'CANCEL', 0,     0,    300000, 150000, DATE_SUB(NOW(), INTERVAL 5 DAY),  NULL),
    (12, 17, 6,  100000, 0,     100000, 'CANCEL', 0,     0,    100000, 0,      DATE_SUB(NOW(), INTERVAL 3 DAY),  NULL),
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
    (32, 517, 1, 200000, 90000,  110000, 'PAID', 0, 90000,  200000, 0, '2026-05-16 11:20:00', '2026-05-16 11:20:00'),
    -- Hoa don cho cac booking CHECK_OUT moi cua customer 2-12,100,101 (FE-US-09)
    (33, 600, 2,   150000, 0, 150000, 'PAID', 0, 0, 150000, 0, '2025-06-01 08:00:00', '2025-06-01 08:30:00'),
    (34, 601, 3,   150000, 0, 150000, 'PAID', 0, 0, 150000, 0, '2025-06-05 09:00:00', '2025-06-05 09:30:00'),
    (35, 602, 4,   300000, 0, 300000, 'PAID', 0, 0, 300000, 0, '2025-06-10 10:00:00', '2025-06-10 10:30:00'),
    (36, 603, 5,   150000, 0, 150000, 'PAID', 0, 0, 150000, 0, '2025-06-15 11:00:00', '2025-06-15 11:30:00'),
    (37, 604, 6,   300000, 0, 300000, 'PAID', 0, 0, 300000, 0, '2025-06-20 08:00:00', '2025-06-20 08:30:00'),
    (38, 605, 7,   150000, 0, 150000, 'PAID', 0, 0, 150000, 0, '2025-06-25 09:00:00', '2025-06-25 09:30:00'),
    (39, 606, 8,   500000, 0, 500000, 'PAID', 0, 0, 500000, 0, '2025-07-01 10:00:00', '2025-07-01 10:30:00'),
    (40, 607, 9,   150000, 0, 150000, 'PAID', 0, 0, 150000, 0, '2025-07-05 11:00:00', '2025-07-05 11:30:00'),
    (41, 608, 10,  300000, 0, 300000, 'PAID', 0, 0, 300000, 0, '2025-07-10 08:00:00', '2025-07-10 08:30:00'),
    (42, 609, 11,  150000, 0, 150000, 'PAID', 0, 0, 150000, 0, '2025-07-15 09:00:00', '2025-07-15 09:30:00'),
    (43, 610, 12,  300000, 0, 300000, 'PAID', 0, 0, 300000, 0, '2025-07-20 10:00:00', '2025-07-20 10:30:00'),
    (44, 611, 100, 150000, 0, 150000, 'PAID', 0, 0, 150000, 0, '2026-06-01 08:00:00', '2026-06-01 08:30:00'),
    (45, 612, 101, 150000, 0, 150000, 'PAID', 0, 0, 150000, 0, '2026-06-10 09:00:00', '2026-06-10 09:30:00');

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
    (8,  NULL, 3,    'VNPAY', 400000,  'TXN-0000000008', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 20 DAY),   400000, 'PAYMENT'),
    (10, NULL, 5,    'CASH',  8500000, NULL,            'SUCCESS', DATE_SUB(NOW(), INTERVAL 100 DAY),  8500000,'PAYMENT'),
    (12, NULL, 8,    'CASH',  1200000, NULL,            'SUCCESS', DATE_SUB(NOW(), INTERVAL 10 DAY),   1200000,'PAYMENT'),
    (13, NULL, 9,    'VNPAY', 2000000, 'TXN-0000000013', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 5 DAY),    2000000,'PAYMENT'),
    (14, NULL, 10,   'CASH',  353500,  NULL,            'SUCCESS', DATE_SUB(NOW(), INTERVAL 20 DAY),   353500, 'PAYMENT'),
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
    (32, 32, NULL, 'CASH', 110000, NULL, 'SUCCESS', '2026-05-16 11:20:00', 110000, 'PAYMENT'),
    -- Thanh toan cho hoa don 33-45 (booking CHECK_OUT moi cua customer 2-12,100,101)
    (33, 33, NULL, 'CASH', 150000, NULL, 'SUCCESS', '2025-06-01 08:30:00', 150000, 'PAYMENT'),
    (34, 34, NULL, 'CASH', 150000, NULL, 'SUCCESS', '2025-06-05 09:30:00', 150000, 'PAYMENT'),
    (35, 35, NULL, 'CASH', 300000, NULL, 'SUCCESS', '2025-06-10 10:30:00', 300000, 'PAYMENT'),
    (36, 36, NULL, 'CASH', 150000, NULL, 'SUCCESS', '2025-06-15 11:30:00', 150000, 'PAYMENT'),
    (37, 37, NULL, 'CASH', 300000, NULL, 'SUCCESS', '2025-06-20 08:30:00', 300000, 'PAYMENT'),
    (38, 38, NULL, 'CASH', 150000, NULL, 'SUCCESS', '2025-06-25 09:30:00', 150000, 'PAYMENT'),
    (39, 39, NULL, 'CASH', 500000, NULL, 'SUCCESS', '2025-07-01 10:30:00', 500000, 'PAYMENT'),
    (40, 40, NULL, 'CASH', 150000, NULL, 'SUCCESS', '2025-07-05 11:30:00', 150000, 'PAYMENT'),
    (41, 41, NULL, 'CASH', 300000, NULL, 'SUCCESS', '2025-07-10 08:30:00', 300000, 'PAYMENT'),
    (42, 42, NULL, 'CASH', 150000, NULL, 'SUCCESS', '2025-07-15 09:30:00', 150000, 'PAYMENT'),
    (43, 43, NULL, 'CASH', 300000, NULL, 'SUCCESS', '2025-07-20 10:30:00', 300000, 'PAYMENT'),
    (44, 44, NULL, 'CASH', 150000, NULL, 'SUCCESS', '2026-06-01 08:30:00', 150000, 'PAYMENT'),
    (45, 45, NULL, 'CASH', 150000, NULL, 'SUCCESS', '2026-06-10 09:30:00', 150000, 'PAYMENT');

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
    (4,  4, 9,    'A004', 'WASHING', DATE_SUB(NOW(), INTERVAL 65 MINUTE), true,  3),
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
    ('PENDING_PAYMENT_TIMEOUT_MINUTES', '5',   'Minutes a PENDING booking may await deposit transfer before auto-cancel', 'NUMBER', true),
    ('LOYALTY_POINT_PER_VND',       '1000',    'VND spent per loyalty point earned',             'NUMBER',  true),
    ('MAX_VEHICLE_PER_FAMILY',      '5',       'Maximum vehicles allowed per family subscription','NUMBER', true),
    ('QUEUE_PRIORITY_BOOKING_WEIGHT','3',      'Priority weight given to booking-based queue tickets','NUMBER', true),
    ('QUEUE_BOOKING_WALKIN_INTERLEAVE_RATIO','3','So ve booking hien thi lien tiep truoc khi xen 1 ve walk-in tren queue board','NUMBER', true),
    ('SUPPORT_HOTLINE',             '1900-1234','Customer support hotline number',               'STRING',  true),
    ('MAINTENANCE_MODE',            'false',   'Whether the system is in maintenance mode',      'BOOLEAN', true),
    ('REVIEW_EDIT_WINDOW_HOURS',    '24',      'Hours a customer may edit their review after posting', 'NUMBER', true),
    ('LOYALTY_RESET_MONTH_DAY',     '01-01',   'Annual loyalty point reset date (MM-DD)',        'STRING',  true),
    ('LOYALTY_EARN_RATE_VND_PER_POINT', '1000', 'Customer earns 1 loyalty point for every 1,000 VND spent','NUMBER', TRUE),
    ( 'LOYALTY_REDEEM_RATE_VND_PER_POINT', '100', '1 loyalty point can be redeemed for 100 VND', 'NUMBER', TRUE),
    -- MAX_VIOLATION_LIMIT: matches the hard-coded VIOLATION_LIMIT=3 in code (docs/seed.md 4.4)
    ('MAX_VIOLATION_LIMIT',         '3',       'Max cancellations/no-shows before a 14-day restriction', 'NUMBER', true),
    ('REFUND_TRANSFER_CONTENT_PREFIX', 'RF',   'Prefix for refund bank-transfer content (RF{refundId})', 'STRING', true),
    ('REFUND_TRANSFER_CONTENT_TEMPLATE', 'Hoan tien coc booking {booking_id}', 'Bank-transfer content template for deposit refunds; replace {booking_id}', 'STRING', true);

-- =====================================================================
-- ENUM/BR COVERAGE COMPLETION (docs/seed.md §1, §2) — added while
-- FOREIGN_KEY_CHECKS is still 0 so referenced rows may appear out of order.
-- =====================================================================

-- Refund bookings: 26 REFUND_PENDING (cancel requested >120' before slot),
-- 27 REFUNDED (admin confirmed), 32 COMPLETED-unpaid (FINAL invoice / cash checkout).
INSERT IGNORE INTO booking
(id, customer_id, vehicle_id, service_package_id,
 appointment_date, status, booking_type, check_in_employee_id,
 created_at, check_in_at, check_out_at, canceled_at,
 is_deposit_paid,
 total_service_amount, total_addon_amount, total_amount,
 voucher_discount_amount, point_discount_amount)
VALUES
    (26, 6, 6, 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'REFUND_PENDING', 'ADVANCE', NULL, DATE_SUB(NOW(), INTERVAL 3 HOUR), NULL, NULL, NULL, true, 100000, 0, 100000, 0, 0),
    (27, 7, 7, 1, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'REFUNDED',       'ADVANCE', NULL, DATE_SUB(NOW(), INTERVAL 3 DAY),  NULL, NULL, DATE_SUB(NOW(), INTERVAL 2 DAY), true, 100000, 0, 100000, 0, 0),
    (32, 8, 8, 1, CURDATE(),                            'COMPLETED',      'ADVANCE', 5,    DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR), NULL, NULL, true, 100000, 0, 100000, 0, 0);

-- Slots + allocations for the refund/checkout bookings above.
INSERT IGNORE INTO booking_slot
(id, station_id, start_time, end_time, max_capacity, date, booked_count, status)
VALUES
    (9700, 1, '09:00', '09:15', 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 0, 'AVAILABLE'),
    (9701, 1, '09:00', '09:15', 5, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (9702, 3, '10:00', '10:15', 3, CURDATE(),                            1, 'AVAILABLE');
INSERT IGNORE INTO booking_slot_allocation (booking_id, booking_slot_id)
VALUES (26, 9700), (27, 9701), (32, 9702);

-- REFUND (docs/seed.md §1.4): PENDING (booking 26) + REFUNDED (booking 27).
-- refund_amount = DEFAULT_DEPOSIT_AMOUNT = 20000; BINs are real BankEnum values
-- (TPBank 970423, Vietcombank 970436) so VietQR can render.
INSERT IGNORE INTO refund
(id, booking_id, refund_method, refund_bank_name, refund_bank_bin, refund_account_number, refund_account_holder,
 refund_amount, status, refund_note, refunded_at, refunded_by, created_at)
VALUES
    (1, 26, 'BANK_TRANSFER', 'TPBank',      '970423', '0388123456',    'NGUYEN VAN CHI',  20000, 'PENDING',  NULL,            NULL,                             NULL, DATE_SUB(NOW(), INTERVAL 1 HOUR)),
    (2, 27, 'BANK_TRANSFER', 'Vietcombank', '970436', '0071000123456', 'NGUYEN VAN DUNG', 20000, 'REFUNDED', 'FT24123456789', DATE_SUB(NOW(), INTERVAL 1 DAY), 1,    DATE_SUB(NOW(), INTERVAL 2 DAY));

-- FINAL invoice (docs/seed.md §1.6) for COMPLETED-unpaid booking 32 (cash checkout input),
-- + CANCEL invoice for refunded booking 27 (so the REFUND payment below has an invoice to hang on).
INSERT IGNORE INTO booking_invoice
(id, booking_id, customer_id, raw_amount, discount_amount, final_amount, status, voucher_discount, point_discount, service_amount, addon_amount, created_at, paid_at)
VALUES
    (46, 32, 8, 100000, 0, 100000, 'FINAL',  0, 0, 100000, 0, DATE_SUB(NOW(), INTERVAL 2 HOUR), NULL),
    (47, 27, 7, 100000, 0, 100000, 'CANCEL', 0, 0, 100000, 0, DATE_SUB(NOW(), INTERVAL 3 DAY),  NULL);

-- Subscription invoices (docs/seed.md §1.7): RENEW type=1 PAID, + CANCELED / EXPIRED / FAILED.
INSERT IGNORE INTO subscription_invoice
(id, customer_id, unlimit_subscription_id, family_subscription_id, plan_price, status, created_at, paid_at, type)
VALUES
    (13, 1, 1, NULL, 500000,  'PAID',     DATE_SUB(NOW(), INTERVAL 2 DAY),    DATE_SUB(NOW(), INTERVAL 2 DAY), 1),
    (14, 2, 2, NULL, 900000,  'CANCELED', DATE_SUB(NOW(), INTERVAL 3 DAY),    NULL,                            1),
    (15, 3, 3, NULL, 400000,  'EXPIRED',  DATE_SUB(NOW(), INTERVAL 40 DAY),   NULL,                            1),
    (16, 8, 8, NULL, 1350000, 'FAILED',   DATE_SUB(NOW(), INTERVAL 10 MINUTE),NULL,                            1);

-- Payments covering BANK_TRANSFER + MANUAL methods and a REFUND with reference_payment_id
-- pointing back at the original DEPOSIT payment (docs/seed.md §1.5).
INSERT IGNORE INTO payment
(id, booking_invoice_id, subscription_invoice_id, payment_method, amount, transaction_code, payment_status, paid_at, received_amount, payment_type, reference_payment_id)
VALUES
    (46, 6,    NULL, 'BANK_TRANSFER', 20000,  'SEPAY-DEP-0006', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 2 HOUR),  20000,  'DEPOSIT',      NULL),
    (47, NULL, 13,   'MANUAL',        500000, 'MANUAL-SUB-0013','SUCCESS', DATE_SUB(NOW(), INTERVAL 2 DAY),   500000, 'SUBSCRIPTION', NULL),
    (48, 47,   NULL, 'BANK_TRANSFER', 20000,  'SEPAY-RF-0027',  'SUCCESS', DATE_SUB(NOW(), INTERVAL 1 DAY),   20000,  'REFUND',       46);

-- payment_webhook_log (docs/seed.md §1.8): one row per WebhookLogStatus, sepay_transaction_id UNIQUE.
INSERT IGNORE INTO payment_webhook_log
(id, sepay_transaction_id, gateway, transfer_amount, transfer_content, transaction_date, booking_id, subscription_invoice_id, status, note, received_at)
VALUES
    (1, 'SEPAY-WH-1001', 'TPBank',      20000,  'BK1',            DATE_FORMAT(DATE_SUB(NOW(), INTERVAL 2 HOUR), '%Y-%m-%d %H:%i:%s'), 1,    NULL, 'PROCESSED',             'Deposit matched booking 1', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
    (2, 'SEPAY-WH-1002', 'MBBank',      30000,  'CHUYEN TIEN ABC',DATE_FORMAT(DATE_SUB(NOW(), INTERVAL 3 HOUR), '%Y-%m-%d %H:%i:%s'), NULL, NULL, 'ORPHAN',                'No booking/invoice matched content', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
    (3, 'SEPAY-WH-1003', 'TPBank',      15000,  'BK5',            DATE_FORMAT(DATE_SUB(NOW(), INTERVAL 4 HOUR), '%Y-%m-%d %H:%i:%s'), 5,    NULL, 'AMOUNT_MISMATCH',       'Expected 20000, received 15000', DATE_SUB(NOW(), INTERVAL 4 HOUR)),
    (4, 'SEPAY-WH-1004', 'TPBank',      20000,  'BK1',            DATE_FORMAT(DATE_SUB(NOW(), INTERVAL 5 HOUR), '%Y-%m-%d %H:%i:%s'), 1,    NULL, 'INVALID_BOOKING_STATE', 'Booking 1 already CONFIRMED', DATE_SUB(NOW(), INTERVAL 5 HOUR)),
    (5, 'SEPAY-WH-1005', 'Vietcombank', 500000, 'SUB1',           DATE_FORMAT(DATE_SUB(NOW(), INTERVAL 6 HOUR), '%Y-%m-%d %H:%i:%s'), NULL, 1,    'INVALID_INVOICE_STATE', 'Subscription invoice 1 already PAID', DATE_SUB(NOW(), INTERVAL 6 HOUR));

-- Queue tickets (docs/seed.md §1.11): WAITING tied to CHECK_IN booking 8, plus
-- COMPLETED / CANCELED tickets tied to real bookings (11 COMPLETED, 15 CANCELED).
INSERT IGNORE INTO queue_ticket
(id, station_id, booking_id, ticket_number, status, issued_at, is_booking, priority_score)
VALUES
    (18, 1, 8,  'A018', 'CANCELED',  DATE_SUB(NOW(), INTERVAL 10 MINUTE), true, 3),
    (19, 1, 11, 'A019', 'COMPLETED', DATE_SUB(NOW(), INTERVAL 240 HOUR),  true, 1),
    (20, 1, 15, 'A020', 'CANCELED',  DATE_SUB(NOW(), INTERVAL 6 DAY),     true, 1);

-- =====================================================================
-- QUEUE_BOOKING_WALKIN_INTERLEAVE_RATIO TEST SEED (3:1)
-- Station 5 (staff user_id=28) and station 6 (staff user_id=29) have no other
-- queue_ticket rows above, so GET /api/queue for either staff gives a clean,
-- deterministic board to verify QueueServiceImpl.interleaveByRatio(ratio=3)
-- AND QueueTicketRepository.findActiveQueueByStation's ORDER BY
-- (issuedAt ASC -> isBooking DESC -> rank DESC). Each ticket uses a distinct
-- real customer+vehicle (distinct license plate) with an existing computed
-- customer_tier, and ranks are deliberately scrambled against issuedAt order
-- so the test fails loudly if rank is ever mistakenly prioritized over FIFO.
--
-- Scenario A (station 5, id 900-909): 8 booking : 2 walk-in, with a same-
-- issuedAt (55 min ago) rank tie-break pair (904=PLATINUM vs 905=MEMBER).
-- Expected board order (ticket_number): S501,S502,S503,S504,S505,S506,S507,S509,S508,S510
--   - S501(PLATINUM) still first, S502(MEMBER) still second -> issuedAt beats rank.
--   - S505(PLATINUM,55') sorts before S506(MEMBER,55') -> rank tie-break when issuedAt equal.
--   - 3:1 interleave: [S501,S502,S503]+S504(W), [S505,S506,S507]+S509(W), [S508,S510] leftover.
--
-- Scenario B (station 6, id 910-916): 2 booking : 5 walk-in, with a same-
-- issuedAt (28 min ago) isBooking tie-break pair (911=booking vs 912=walk-in,
-- both SILVER rank so the tie is isolated to isBooking, not rank).
-- Expected board order: S601,S602,S603,S604,S605,S606,S607
--   - S602(booking) sorts before S603(walk-in) despite equal issuedAt -> isBooking tie-break.
--   - bookings exhaust after the first cycle; remaining walk-ins flow out FIFO,
--     one per loop iteration, since there are no more bookings to batch with them.
-- =====================================================================
INSERT IGNORE INTO booking
(id, customer_id, vehicle_id, service_package_id,
 appointment_date, status, booking_type, check_in_employee_id,
 created_at, check_in_at, check_out_at, canceled_at,
 is_deposit_paid,
 total_service_amount, total_addon_amount, total_amount,
 voucher_discount_amount, point_discount_amount)
VALUES
    -- Scenario A / station 5 -- customer/vehicle/rank: 7=PLATINUM,10=MEMBER,3=GOLD,11=SILVER,5=PLATINUM,6=MEMBER,1=SILVER,8=PLATINUM,12=SILVER,9=PLATINUM
    (900, 7,  7,   1, CURDATE(), 'CHECK_IN', 'ADVANCE', NULL, DATE_SUB(NOW(), INTERVAL 70 MINUTE), NULL, NULL, NULL, false, 149000, 0, 149000, 0, 0),
    (901, 10, 10,  1, CURDATE(), 'CHECK_IN', 'ADVANCE', NULL, DATE_SUB(NOW(), INTERVAL 65 MINUTE), NULL, NULL, NULL, false, 149000, 0, 149000, 0, 0),
    (902, 3,  3,   1, CURDATE(), 'CHECK_IN', 'ADVANCE', NULL, DATE_SUB(NOW(), INTERVAL 60 MINUTE), NULL, NULL, NULL, false, 149000, 0, 149000, 0, 0),
    (903, 11, 11,  1, CURDATE(), 'CHECK_IN', 'WALK_IN', NULL, DATE_SUB(NOW(), INTERVAL 58 MINUTE), NULL, NULL, NULL, false, 149000, 0, 149000, 0, 0),
    (904, 5,  5,   1, CURDATE(), 'CHECK_IN', 'ADVANCE', NULL, DATE_SUB(NOW(), INTERVAL 55 MINUTE), NULL, NULL, NULL, false, 149000, 0, 149000, 0, 0),
    (905, 6,  6,   1, CURDATE(), 'CHECK_IN', 'ADVANCE', NULL, DATE_SUB(NOW(), INTERVAL 55 MINUTE), NULL, NULL, NULL, false, 149000, 0, 149000, 0, 0),
    (906, 1,  1,   1, CURDATE(), 'CHECK_IN', 'ADVANCE', NULL, DATE_SUB(NOW(), INTERVAL 50 MINUTE), NULL, NULL, NULL, false, 149000, 0, 149000, 0, 0),
    (907, 8,  8,   1, CURDATE(), 'CHECK_IN', 'ADVANCE', NULL, DATE_SUB(NOW(), INTERVAL 45 MINUTE), NULL, NULL, NULL, false, 149000, 0, 149000, 0, 0),
    (908, 12, 12,  1, CURDATE(), 'CHECK_IN', 'WALK_IN', NULL, DATE_SUB(NOW(), INTERVAL 40 MINUTE), NULL, NULL, NULL, false, 149000, 0, 149000, 0, 0),
    (909, 9,  9,   1, CURDATE(), 'CHECK_IN', 'ADVANCE', NULL, DATE_SUB(NOW(), INTERVAL 35 MINUTE), NULL, NULL, NULL, false, 149000, 0, 149000, 0, 0),
    -- Scenario B / station 6 -- customer/vehicle/rank: 2=SILVER,4=SILVER,1(v13)=SILVER,2(v14)=SILVER,3(v15)=GOLD,100=GOLD,101=GOLD
    (910, 2,   2,   1, CURDATE(), 'CHECK_IN', 'ADVANCE', NULL, DATE_SUB(NOW(), INTERVAL 30 MINUTE), NULL, NULL, NULL, false, 149000, 0, 149000, 0, 0),
    (911, 4,   4,   1, CURDATE(), 'CHECK_IN', 'ADVANCE', NULL, DATE_SUB(NOW(), INTERVAL 28 MINUTE), NULL, NULL, NULL, false, 149000, 0, 149000, 0, 0),
    (912, 1,   13,  1, CURDATE(), 'CHECK_IN', 'WALK_IN', NULL, DATE_SUB(NOW(), INTERVAL 28 MINUTE), NULL, NULL, NULL, false, 149000, 0, 149000, 0, 0),
    (913, 2,   14,  1, CURDATE(), 'CHECK_IN', 'WALK_IN', NULL, DATE_SUB(NOW(), INTERVAL 26 MINUTE), NULL, NULL, NULL, false, 149000, 0, 149000, 0, 0),
    (914, 3,   15,  1, CURDATE(), 'CHECK_IN', 'WALK_IN', NULL, DATE_SUB(NOW(), INTERVAL 24 MINUTE), NULL, NULL, NULL, false, 149000, 0, 149000, 0, 0),
    (915, 100, 201, 1, CURDATE(), 'CHECK_IN', 'WALK_IN', NULL, DATE_SUB(NOW(), INTERVAL 22 MINUTE), NULL, NULL, NULL, false, 149000, 0, 149000, 0, 0),
    (916, 101, 203, 1, CURDATE(), 'CHECK_IN', 'WALK_IN', NULL, DATE_SUB(NOW(), INTERVAL 20 MINUTE), NULL, NULL, NULL, false, 149000, 0, 149000, 0, 0);

INSERT IGNORE INTO queue_ticket
(id, station_id, booking_id, ticket_number, status, issued_at, is_booking, priority_score)
VALUES
    -- Scenario A: station 5, expect S501,S502,S503,S504,S505,S506,S507,S509,S508,S510
    (900, 5, 900, 'S501', 'WAITING', DATE_SUB(NOW(), INTERVAL 70 MINUTE), true,  0),
    (901, 5, 901, 'S502', 'WAITING', DATE_SUB(NOW(), INTERVAL 65 MINUTE), true,  0),
    (902, 5, 902, 'S503', 'WAITING', DATE_SUB(NOW(), INTERVAL 60 MINUTE), true,  0),
    (903, 5, 903, 'S504', 'WAITING', DATE_SUB(NOW(), INTERVAL 58 MINUTE), false, 0),
    (904, 5, 904, 'S505', 'WAITING', DATE_SUB(NOW(), INTERVAL 55 MINUTE), true,  0),
    (905, 5, 905, 'S506', 'WAITING', DATE_SUB(NOW(), INTERVAL 55 MINUTE), true,  0),
    (906, 5, 906, 'S507', 'WAITING', DATE_SUB(NOW(), INTERVAL 50 MINUTE), true,  0),
    (907, 5, 907, 'S508', 'WAITING', DATE_SUB(NOW(), INTERVAL 45 MINUTE), true,  0),
    (908, 5, 908, 'S509', 'WAITING', DATE_SUB(NOW(), INTERVAL 40 MINUTE), false, 0),
    (909, 5, 909, 'S510', 'WAITING', DATE_SUB(NOW(), INTERVAL 35 MINUTE), true,  0),
    -- Scenario B: station 6, expect S601,S602,S603,S604,S605,S606,S607
    (910, 6, 910, 'S601', 'WAITING', DATE_SUB(NOW(), INTERVAL 30 MINUTE), true,  0),
    (911, 6, 911, 'S602', 'WAITING', DATE_SUB(NOW(), INTERVAL 28 MINUTE), true,  0),
    (912, 6, 912, 'S603', 'WAITING', DATE_SUB(NOW(), INTERVAL 28 MINUTE), false, 0),
    (913, 6, 913, 'S604', 'WAITING', DATE_SUB(NOW(), INTERVAL 26 MINUTE), false, 0),
    (914, 6, 914, 'S605', 'WAITING', DATE_SUB(NOW(), INTERVAL 24 MINUTE), false, 0),
    (915, 6, 915, 'S606', 'WAITING', DATE_SUB(NOW(), INTERVAL 22 MINUTE), false, 0),
    (916, 6, 916, 'S607', 'WAITING', DATE_SUB(NOW(), INTERVAL 20 MINUTE), false, 0);

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO booking_slot (station_id, start_time, end_time, max_capacity, date, booked_count, status) VALUES
                                                                                                          (2, '23:30:00', '23:45:00', 1, CURDATE(), 0, 'AVAILABLE'),
                                                                                                          (2, '23:45:00', '00:00:00', 1, CURDATE(), 0, 'AVAILABLE'),
                                                                                                          (2, '00:00:00', '00:15:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '00:15:00', '00:30:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '00:30:00', '00:45:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '00:45:00', '01:00:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '01:00:00', '01:15:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '01:15:00', '01:30:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '01:30:00', '01:45:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '01:45:00', '02:00:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '02:00:00', '02:15:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '02:15:00', '02:30:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '02:30:00', '02:45:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '02:45:00', '03:00:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '03:00:00', '03:15:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '03:15:00', '03:30:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '03:30:00', '03:45:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '03:45:00', '04:00:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '04:00:00', '04:15:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '04:15:00', '04:30:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '04:30:00', '04:45:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '04:45:00', '05:00:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '05:00:00', '05:15:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '05:15:00', '05:30:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '05:30:00', '05:45:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '05:45:00', '06:00:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '06:00:00', '06:15:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '06:15:00', '06:30:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '06:30:00', '06:45:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '06:45:00', '07:00:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '07:00:00', '07:15:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '07:15:00', '07:30:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '07:30:00', '07:45:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
                                                                                                          (2, '07:45:00', '08:00:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE');

-- =====================================================================
-- BOOKING SLOT — CA DEM CHI NHANH 2 (station_id = 2): 21:00 hom nay ->
-- 07:00 sang mai, moi slot 15 phut. Phan 21:00 -> 24:00 dung CURDATE(),
-- phan 00:00 -> 07:00 dung CURDATE() + 1 ngay. ids 5400-5439.
-- =====================================================================
INSERT IGNORE INTO booking_slot
(id, station_id, start_time, end_time, max_capacity, date, booked_count, status)
VALUES
    (5400, 2, '21:00', '21:15', 1, CURDATE(), 0, 'AVAILABLE'),
    (5401, 2, '21:15', '21:30', 1, CURDATE(), 0, 'AVAILABLE'),
    (5402, 2, '21:30', '21:45', 1, CURDATE(), 0, 'AVAILABLE'),
    (5403, 2, '21:45', '22:00', 1, CURDATE(), 0, 'AVAILABLE'),
    (5404, 2, '22:00', '22:15', 1, CURDATE(), 0, 'AVAILABLE'),
    (5405, 2, '22:15', '22:30', 1, CURDATE(), 0, 'AVAILABLE'),
    (5406, 2, '22:30', '22:45', 1, CURDATE(), 0, 'AVAILABLE'),
    (5407, 2, '22:45', '23:00', 1, CURDATE(), 0, 'AVAILABLE'),
    (5408, 2, '23:00', '23:15', 1, CURDATE(), 0, 'AVAILABLE'),
    (5409, 2, '23:15', '23:30', 1, CURDATE(), 0, 'AVAILABLE'),
    (5410, 2, '23:30', '23:45', 1, CURDATE(), 0, 'AVAILABLE'),
    (5411, 2, '23:45', '00:00', 1, CURDATE(), 0, 'AVAILABLE'),
    (5412, 2, '00:00', '00:15', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5413, 2, '00:15', '00:30', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5414, 2, '00:30', '00:45', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5415, 2, '00:45', '01:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5416, 2, '01:00', '01:15', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5417, 2, '01:15', '01:30', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5418, 2, '01:30', '01:45', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5419, 2, '01:45', '02:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5420, 2, '02:00', '02:15', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5421, 2, '02:15', '02:30', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5422, 2, '02:30', '02:45', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5423, 2, '02:45', '03:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5424, 2, '03:00', '03:15', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5425, 2, '03:15', '03:30', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5426, 2, '03:30', '03:45', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5427, 2, '03:45', '04:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5428, 2, '04:00', '04:15', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5429, 2, '04:15', '04:30', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5430, 2, '04:30', '04:45', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5431, 2, '04:45', '05:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5432, 2, '05:00', '05:15', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5433, 2, '05:15', '05:30', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5434, 2, '05:30', '05:45', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5435, 2, '05:45', '06:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5436, 2, '06:00', '06:15', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5437, 2, '06:15', '06:30', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5438, 2, '06:30', '06:45', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE'),
    (5439, 2, '06:45', '07:00', 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0, 'AVAILABLE');

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
    (505, 1, 1, 3, '2025-05-09', 'CHECK_OUT', 'ADVANCE', 1, '2025-05-09 13:30:00', '2025-05-09 13:45:00', '2025-05-09 14:00:00', NULL, true, 350000, 0, 350000, 0, 0),
    (506, 1, 1, 3, '2025-06-22', 'CHECK_OUT', 'ADVANCE', 1, '2025-06-22 16:00:00', '2025-06-22 16:15:00', '2025-06-22 16:30:00', NULL, true, 400000, 0, 200000, 0, 200000),
    (507, 1, 1, 3, '2025-07-15', 'CHECK_OUT', 'ADVANCE', 1, '2025-07-15 09:15:00', '2025-07-15 09:30:00', '2025-07-15 09:45:00', NULL, true, 500000, 0, 500000, 0, 0),
    (508, 1, 1, 3, '2025-08-19', 'CHECK_OUT', 'ADVANCE', 1, '2025-08-19 12:50:00', '2025-08-19 13:05:00', '2025-08-19 13:20:00', NULL, true, 600000, 0, 600000, 0, 0),
    (509, 1, 1, 3, '2025-10-04', 'CHECK_OUT', 'ADVANCE', 1, '2025-10-04 09:30:00', '2025-10-04 09:45:00', '2025-10-04 10:00:00', NULL, true, 900000, 0, 250000, 0, 650000),
    (510, 1, 1, 3, '2025-11-10', 'CHECK_OUT', 'ADVANCE', 1, '2025-11-10 08:00:00', '2025-11-10 08:15:00', '2025-11-10 08:30:00', NULL, true, 300000, 0, 300000, 0, 0),
    (511, 1, 1, 3, '2025-11-30', 'CHECK_OUT', 'ADVANCE', 1, '2025-11-30 16:30:00', '2025-11-30 16:45:00', '2025-11-30 17:00:00', NULL, true, 300000, 0, 300000, 0, 0),
    (512, 1, 1, 3, '2025-12-20', 'CHECK_OUT', 'ADVANCE', 1, '2025-12-20 11:30:00', '2025-12-20 11:45:00', '2025-12-20 12:00:00', NULL, true, 350000, 0, 200000, 0, 150000),
    (513, 1, 1, 2, '2026-01-18', 'CHECK_OUT', 'ADVANCE', 1, '2026-01-18 09:00:00', '2026-01-18 09:15:00', '2026-01-18 09:30:00', NULL, true, 120000, 0, 120000, 0, 0),
    (514, 1, 1, 2, '2026-02-22', 'CHECK_OUT', 'ADVANCE', 1, '2026-02-22 13:45:00', '2026-02-22 14:00:00', '2026-02-22 14:15:00', NULL, true, 100000, 0, 100000, 0, 0),
    (515, 1, 1, 2, '2026-03-15', 'CHECK_OUT', 'ADVANCE', 1, '2026-03-15 10:15:00', '2026-03-15 10:30:00', '2026-03-15 10:45:00', NULL, true, 150000, 0, 100000, 0, 50000),
    (516, 1, 1, 1, '2026-04-20', 'CHECK_OUT', 'ADVANCE', 1, '2026-04-20 15:30:00', '2026-04-20 15:45:00', '2026-04-20 16:00:00', NULL, true, 90000,  0, 90000,  0, 0),
    (517, 1, 1, 2, '2026-05-16', 'CHECK_OUT', 'ADVANCE', 1, '2026-05-16 10:50:00', '2026-05-16 11:05:00', '2026-05-16 11:20:00', NULL, true, 200000, 0, 110000, 0, 90000),
    -- Booking CHECK_OUT cho cac customer con lai (2-12,100,101) de moi khach deu co it nhat 1 luot rua da hoan thanh (FE-US-09)
    (600, 2,   2,   1, '2025-06-01', 'CHECK_OUT', 'ADVANCE', 1, '2025-06-01 08:00:00', '2025-06-01 08:10:00', '2025-06-01 08:30:00', NULL, true, 150000, 0, 150000, 0, 0),
    (601, 3,   3,   1, '2025-06-05', 'CHECK_OUT', 'ADVANCE', 1, '2025-06-05 09:00:00', '2025-06-05 09:10:00', '2025-06-05 09:30:00', NULL, true, 150000, 0, 150000, 0, 0),
    (602, 4,   4,   2, '2025-06-10', 'CHECK_OUT', 'ADVANCE', 1, '2025-06-10 10:00:00', '2025-06-10 10:10:00', '2025-06-10 10:30:00', NULL, true, 300000, 0, 300000, 0, 0),
    (603, 5,   5,   1, '2025-06-15', 'CHECK_OUT', 'ADVANCE', 1, '2025-06-15 11:00:00', '2025-06-15 11:10:00', '2025-06-15 11:30:00', NULL, true, 150000, 0, 150000, 0, 0),
    (604, 6,   6,   2, '2025-06-20', 'CHECK_OUT', 'ADVANCE', 1, '2025-06-20 08:00:00', '2025-06-20 08:10:00', '2025-06-20 08:30:00', NULL, true, 300000, 0, 300000, 0, 0),
    (605, 7,   7,   1, '2025-06-25', 'CHECK_OUT', 'ADVANCE', 1, '2025-06-25 09:00:00', '2025-06-25 09:10:00', '2025-06-25 09:30:00', NULL, true, 150000, 0, 150000, 0, 0),
    (606, 8,   8,   3, '2025-07-01', 'CHECK_OUT', 'ADVANCE', 1, '2025-07-01 10:00:00', '2025-07-01 10:10:00', '2025-07-01 10:30:00', NULL, true, 500000, 0, 500000, 0, 0),
    (607, 9,   9,   1, '2025-07-05', 'CHECK_OUT', 'ADVANCE', 1, '2025-07-05 11:00:00', '2025-07-05 11:10:00', '2025-07-05 11:30:00', NULL, true, 150000, 0, 150000, 0, 0),
    (608, 10,  10,  2, '2025-07-10', 'CHECK_OUT', 'ADVANCE', 1, '2025-07-10 08:00:00', '2025-07-10 08:10:00', '2025-07-10 08:30:00', NULL, true, 300000, 0, 300000, 0, 0),
    (609, 11,  11,  1, '2025-07-15', 'CHECK_OUT', 'ADVANCE', 1, '2025-07-15 09:00:00', '2025-07-15 09:10:00', '2025-07-15 09:30:00', NULL, true, 150000, 0, 150000, 0, 0),
    (610, 12,  12,  2, '2025-07-20', 'CHECK_OUT', 'ADVANCE', 1, '2025-07-20 10:00:00', '2025-07-20 10:10:00', '2025-07-20 10:30:00', NULL, true, 300000, 0, 300000, 0, 0),
    (611, 100, 201, 1, '2026-06-01', 'CHECK_OUT', 'ADVANCE', 1, '2026-06-01 08:00:00', '2026-06-01 08:10:00', '2026-06-01 08:30:00', NULL, true, 150000, 0, 150000, 0, 0),
    (612, 101, 203, 1, '2026-06-10', 'CHECK_OUT', 'ADVANCE', 1, '2026-06-10 09:00:00', '2026-06-10 09:10:00', '2026-06-10 09:30:00', NULL, true, 150000, 0, 150000, 0, 0);

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
    (9517, 1, '11:20', '11:35', 5, '2026-05-16', 1, 'COMPLETED'),
    (9600, 1, '08:00', '08:15', 5, '2025-06-01', 1, 'COMPLETED'),
    (9601, 1, '09:00', '09:15', 5, '2025-06-05', 1, 'COMPLETED'),
    (9602, 1, '10:00', '10:15', 5, '2025-06-10', 1, 'COMPLETED'),
    (9603, 1, '11:00', '11:15', 5, '2025-06-15', 1, 'COMPLETED'),
    (9604, 1, '08:00', '08:15', 5, '2025-06-20', 1, 'COMPLETED'),
    (9605, 1, '09:00', '09:15', 5, '2025-06-25', 1, 'COMPLETED'),
    (9606, 1, '10:00', '10:15', 5, '2025-07-01', 1, 'COMPLETED'),
    (9607, 1, '11:00', '11:15', 5, '2025-07-05', 1, 'COMPLETED'),
    (9608, 1, '08:00', '08:15', 5, '2025-07-10', 1, 'COMPLETED'),
    (9609, 1, '09:00', '09:15', 5, '2025-07-15', 1, 'COMPLETED'),
    (9610, 1, '10:00', '10:15', 5, '2025-07-20', 1, 'COMPLETED'),
    (9611, 1, '08:00', '08:15', 5, '2026-06-01', 1, 'COMPLETED'),
    (9612, 1, '09:00', '09:15', 5, '2026-06-10', 1, 'COMPLETED');

INSERT IGNORE INTO booking_slot_allocation
(booking_id, booking_slot_id)
VALUES
    (501, 9501), (502, 9502), (503, 9503), (504, 9504), (505, 9505),
    (506, 9506), (507, 9507), (508, 9508), (509, 9509), (510, 9510),
    (511, 9511), (512, 9512), (513, 9513), (514, 9514), (515, 9515),
    (516, 9516), (517, 9517),
    (600, 9600), (601, 9601), (602, 9602), (603, 9603), (604, 9604), (605, 9605),
    (606, 9606), (607, 9607), (608, 9608), (609, 9609), (610, 9610), (611, 9611), (612, 9612);

INSERT IGNORE INTO system_setting (
    setting_key,
    setting_value,
    category,
    description,
    data_type,
    is_active
)
VALUES (
           'SUBSCRIPTION_TRANSFER_LOCK_DAYS',
           '14',
           'Subscription',
           'Minimum number of days required before transferring a subscription to another vehicle.',
           'NUMBER',
           TRUE
       );