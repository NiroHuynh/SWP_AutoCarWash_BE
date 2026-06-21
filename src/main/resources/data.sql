
SET FOREIGN_KEY_CHECKS = 0;

-- ROLE
INSERT IGNORE INTO role(id, name)
VALUES
    (1,'ADMIN'),
    (2,'MANAGER'),
    (3,'EMPLOYEE'),
    (4,'CUSTOMER');

-- USER
INSERT IGNORE INTO user
(id,email,phone,password_hash,role_id,is_active,created_at)
VALUES
    (1,'admin@gmail.com','0900000001','$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK',1,true,CURRENT_TIMESTAMP),
    (2,'manager@gmail.com','0900000002','$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK',2,false,CURRENT_TIMESTAMP),
    (3,'staff1@gmail.com','0900000003','$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK',3,true,CURRENT_TIMESTAMP),
    (4,'staff2@gmail.com','0900000004','$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK',3,true,CURRENT_TIMESTAMP),
    (5,'customer1@gmail.com','0900000010','$2a$12$FH2BUJwzWXU15vdbZziwCONvOn.FzCig22QGnt52YSA4qCeWxXuKO',4,true,CURRENT_TIMESTAMP), -- mật khẩu test: Test@123
    (6,'customer2@gmail.com','0900000011','$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK',4,true,CURRENT_TIMESTAMP),
    (7,'customer3@gmail.com','0900000012','$2a$12$WhHm2jB6QFfK5d6vCknUuO92SYuVKK8k7Qjsd6kfiA3hhC2MGUyhK',4,true,CURRENT_TIMESTAMP);

-- PROVINCE
INSERT IGNORE INTO province(id, province_name)
VALUES
    (1, 'Tien Giang'),
    (2, 'Ho Chi Minh'),
    (3, 'Ha Noi');

-- COMMUNE
INSERT IGNORE INTO commune(id, commune_name, province_id)
VALUES
    (1, 'My Tho',   1),
    (2, 'Thu Duc',  2),
    (3, 'Cau Giay', 3);

-- STATION
INSERT IGNORE INTO station
(id, station_name, address, commune_id, max_wash_capacity)
VALUES
    (1, 'AutoWash My Tho', '123 Ap Bac, My Tho',           1, 20),
    (2, 'AutoWash HCM',    '456 Vo Van Ngan, Thu Duc, HCM', 2, 30);

-- STAFF
INSERT IGNORE INTO staff
(id, user_id, station_id, first_name, last_name)
VALUES
    (1, 3, 1, 'An',   'Nguyen'),
    (2, 4, 2, 'Binh', 'Tran');

-- CUSTOMER TIER
INSERT IGNORE INTO customer_tier
(id, tier_name, min_points, booking_window_days, point_multiple)
VALUES
    (1, 'BRONZE',    0, 3,  1.0),
    (2, 'SILVER',  500, 7,  1.2),
    (3, 'GOLD',   1000, 14, 1.5);

-- CUSTOMER
INSERT IGNORE INTO customer
(id, user_id, first_name, last_name, customer_tier_id)
VALUES
    (1, 5, 'Phong', 'Huynh', 1),
    (2, 6, 'Nam',   'Tran',  2),
    (3, 7, 'Linh',  'Le',    3);

-- VEHICLE
-- restricted_until dùng timestamp quá khứ thay vì NULL
INSERT IGNORE INTO vehicle
(id, customer_id, license_plate, brand_name, color, violation_count, restricted_until, is_deleted)
VALUES
    (1, 1, '51A-11111', 'Toyota', 'White',  0, '2020-01-01 00:00:00', false),
    (2, 1, '51A-22222', 'Honda',  'Blue',   0, '2020-01-01 00:00:00', false),
    (3, 2, '51B-33333', 'Honda',  'Black',  0, '2020-01-01 00:00:00', false),
    (4, 3, '30A-44444', 'Mazda',  'Red',    0, '2020-01-01 00:00:00', false);

-- SERVICE CATEGORY
INSERT IGNORE INTO service_category
(id, category_name)
VALUES
    (1, 'Basic Wash'),
    (2, 'Premium Wash');

-- ADDON SERVICE
INSERT IGNORE INTO addon_service
(id, name, price, duration_minutes, service_category_id)
VALUES
    (1, 'Vacuum',  50000, 20, 1),
    (2, 'Polish', 150000, 40, 2);

-- SERVICE PACKAGE
INSERT IGNORE INTO service_package
(id, service_category_id, name, base_price, required_slot, is_deleted)
VALUES
    (1, 1, 'Normal Wash',  100000, 1, false),
    (2, 2, 'Premium Wash', 300000, 2, false);

-- PACKAGE ADDON MAPPING
INSERT IGNORE INTO package_addon_mapping
VALUES
    (1, 1),
    (2, 2);

-- BOOKING
-- Tất cả timestamp dùng giá trị cụ thể, không để NULL hay zero-date
-- Customer 1 (Phong): CONFIRMED, CHECKED_IN, WASHING, PAID, CANCELLED, NO_SHOW
-- Customer 2 (Nam):   CONFIRMED, PAID, NO_SHOW
-- Customer 3 (Linh):  CHECKED_IN, PAID
INSERT IGNORE INTO booking
(id, customer_id, vehicle_id, service_package_id,
 appointment_date, status, booking_type, check_in_employee_id,
 created_at, check_in_at, check_out_at, canceled_at,
 is_deposit_paid, deposit_amount,
 total_service_amount, total_addon_amount, total_amount,
 voucher_discount_amount, point_discount_amount)
VALUES
    -- [1] CONFIRMED – sắp tới (lọc được ở Upcoming)
    (1, 1, 1, 1,
     '2026-06-20', 'CONFIRMED', 'ONLINE', 1,
     '2026-06-17 10:00:00', '2020-01-01 00:00:00', '2020-01-01 00:00:00', '2020-01-01 00:00:00',
     true, 30000,
     100000, 50000, 150000,
     0, 0),

    -- [2] CHECKED_IN – đang chờ rửa (lọc được ở Upcoming)
    (2, 1, 2, 2,
     '2026-06-21', 'CHECKED_IN', 'ONLINE', 1,
     '2026-06-17 11:00:00', '2026-06-18 09:00:00', '2020-01-01 00:00:00', '2020-01-01 00:00:00',
     true, 90000,
     300000, 150000, 450000,
     0, 0),

    -- [3] WASHING – đang rửa xe (lọc được ở Upcoming)
    (3, 1, 1, 1,
     '2026-06-22', 'WASHING', 'ONLINE', 2,
     '2026-06-17 12:00:00', '2026-06-18 10:00:00', '2020-01-01 00:00:00', '2020-01-01 00:00:00',
     true, 30000,
     100000, 0, 100000,
     0, 0),

    -- [4] PAID – đã thanh toán (Past Services: WRITE_REVIEW)
    (4, 1, 1, 1,
     '2026-06-10', 'PAID', 'ONLINE', 1,
     '2026-06-08 09:00:00', '2026-06-10 08:00:00', '2026-06-10 09:00:00', '2020-01-01 00:00:00',
     true, 30000,
     100000, 50000, 150000,
     0, 0),

    -- [5] CANCELLED – đã huỷ (Past Services: không có WRITE_REVIEW)
    (5, 1, 2, 1,
     '2026-06-05', 'CANCELLED', 'ONLINE', 1,
     '2026-06-03 10:00:00', '2020-01-01 00:00:00', '2020-01-01 00:00:00', '2026-06-04 14:00:00',
     false, 0,
     100000, 0, 100000,
     0, 0),

    -- [6] Customer 2 – CONFIRMED (lọc được ở Upcoming)
    (6, 2, 3, 1,
     '2026-06-20', 'CONFIRMED', 'WALK_IN', 2,
     '2026-06-17 08:30:00', '2020-01-01 00:00:00', '2020-01-01 00:00:00', '2020-01-01 00:00:00',
     true, 30000,
     100000, 50000, 150000,
     0, 0),

    -- [7] Customer 2 – PAID (Past Services: WRITE_REVIEW)
    (7, 2, 3, 2,
     '2026-06-12', 'PAID', 'ONLINE', 1,
     '2026-06-10 09:00:00', '2026-06-12 09:00:00', '2026-06-12 11:00:00', '2020-01-01 00:00:00',
     true, 90000,
     300000, 150000, 450000,
     0, 0),

    -- [8] Customer 3 – CHECKED_IN (lọc được ở Upcoming)
    (8, 3, 4, 2,
     '2026-06-21', 'CHECKED_IN', 'ONLINE', 2,
     '2026-06-17 13:00:00', '2026-06-18 09:00:00', '2020-01-01 00:00:00', '2020-01-01 00:00:00',
     true, 90000,
     300000, 150000, 450000,
     0, 0),

    -- [9] Customer 3 – PAID (Past Services: WRITE_REVIEW)
    (9, 3, 4, 1,
     '2026-06-03', 'PAID', 'WALK_IN', 1,
     '2026-06-01 10:00:00', '2026-06-03 08:00:00', '2026-06-03 09:00:00', '2020-01-01 00:00:00',
     true, 30000,
     100000, 0, 100000,
     0, 0),

    -- [10] Customer 1 – NO_SHOW (Past Services: không có WRITE_REVIEW)
    (10, 1, 2, 1,
     '2026-06-15', 'NO_SHOW', 'ONLINE', 1,
     '2026-06-13 09:00:00', '2020-01-01 00:00:00', '2020-01-01 00:00:00', '2020-01-01 00:00:00',
     true, 30000,
     100000, 0, 100000,
     0, 0),

    -- [11] Customer 2 – NO_SHOW (Past Services: không có WRITE_REVIEW)
    (11, 2, 3, 2,
     '2026-06-14', 'NO_SHOW', 'ONLINE', 2,
     '2026-06-12 08:00:00', '2020-01-01 00:00:00', '2020-01-01 00:00:00', '2020-01-01 00:00:00',
     true, 90000,
     300000, 150000, 450000,
     0, 0);

-- BOOKING ADDON
INSERT IGNORE INTO booking_addon
(id, booking_id, addon_service_id, price)
VALUES
    (1,  1,  1,  50000),
    (2,  2,  2, 150000),
    (3,  4,  1,  50000),
    (4,  6,  1,  50000),
    (5,  7,  2, 150000),
    (6,  8,  2, 150000),
    (7,  11, 2, 150000);

-- BOOKING SLOT
INSERT IGNORE INTO booking_slot
(id, station_id, start_time, end_time, max_capacity, date, booked_count, status)
VALUES
    -- Slot cho booking sắp tới
    (1,  1, '08:00', '09:00', 5, '2026-06-20', 2, 'AVAILABLE'),  -- booking 1, 6
    (2,  1, '09:00', '10:00', 5, '2026-06-21', 1, 'AVAILABLE'),  -- booking 2 slot 1
    (3,  1, '10:00', '11:00', 5, '2026-06-21', 1, 'AVAILABLE'),  -- booking 2 slot 2
    (4,  1, '10:00', '11:00', 5, '2026-06-22', 1, 'AVAILABLE'),  -- booking 3
    (5,  2, '08:00', '09:00', 5, '2026-06-20', 1, 'AVAILABLE'),  -- booking 6
    (6,  2, '09:00', '10:00', 5, '2026-06-21', 1, 'AVAILABLE'),  -- booking 8 slot 1
    (7,  2, '10:00', '11:00', 5, '2026-06-21', 1, 'AVAILABLE'),  -- booking 8 slot 2
    -- Slot cho booking đã xong (PAID)
    (8,  1, '08:00', '09:00', 5, '2026-06-10', 1, 'COMPLETED'),  -- booking 4 (PAID)
    (9,  2, '09:00', '10:00', 5, '2026-06-12', 1, 'COMPLETED'),  -- booking 7 slot 1 (PAID)
    (10, 2, '10:00', '11:00', 5, '2026-06-12', 1, 'COMPLETED'),  -- booking 7 slot 2 (PAID)
    (11, 1, '08:00', '09:00', 5, '2026-06-03', 1, 'COMPLETED'),  -- booking 9 (PAID)
    -- Slot cho booking huỷ / NO_SHOW
    (12, 1, '10:00', '11:00', 5, '2026-06-05', 0, 'AVAILABLE'),  -- booking 5 (CANCELLED)
    (13, 1, '09:00', '10:00', 5, '2026-06-15', 1, 'COMPLETED'),  -- booking 10 (NO_SHOW)
    (14, 2, '09:00', '10:00', 5, '2026-06-14', 1, 'COMPLETED'),  -- booking 11 slot 1 (NO_SHOW)
    (15, 2, '10:00', '11:00', 5, '2026-06-14', 1, 'COMPLETED');  -- booking 11 slot 2 (NO_SHOW)

-- BOOKING SLOT ALLOCATION
-- (booking_id, booking_slot_id)
-- Premium Wash (required_slot=2) cần 2 dòng
INSERT IGNORE INTO booking_slot_allocation
VALUES
    (1,  1),   -- booking 1 → slot 1
    (2,  2),   -- booking 2 → slot 2
    (2,  3),   -- booking 2 → slot 3 (Premium, 2 slots)
    (3,  4),   -- booking 3 → slot 4
    (4,  8),   -- booking 4 → slot 8 (PAID)
    (5,  12),  -- booking 5 → slot 12 (CANCELLED)
    (6,  5),   -- booking 6 → slot 5
    (7,  9),   -- booking 7 → slot 9 (PAID)
    (7,  10),  -- booking 7 → slot 10 (PAID, Premium 2 slots)
    (8,  6),   -- booking 8 → slot 6
    (8,  7),   -- booking 8 → slot 7 (Premium, 2 slots)
    (9,  11),  -- booking 9 → slot 11 (PAID)
    (10, 13),  -- booking 10 → slot 13 (NO_SHOW)
    (11, 14),  -- booking 11 → slot 14 (NO_SHOW)
    (11, 15);  -- booking 11 → slot 15 (NO_SHOW, Premium 2 slots)

-- INVOICE (cho booking PAID và CHECKED_IN)
INSERT IGNORE INTO booking_invoice
(id, booking_id, customer_id, raw_amount, final_amount, service_amount, addon_amount, status)
VALUES
    (1, 4, 1, 150000, 150000, 100000,  50000, 'PAID'),
    (2, 7, 2, 450000, 450000, 300000, 150000, 'PAID'),
    (3, 9, 3, 100000, 100000, 100000,      0, 'PAID'),
    (4, 2, 1, 450000, 450000, 300000, 150000, 'PENDING'),
    (5, 8, 3, 450000, 450000, 300000, 150000, 'PENDING');

-- PAYMENT
INSERT IGNORE INTO payment
(id, booking_invoice_id, payment_method, amount, payment_status, payment_type)
VALUES
    (1, 1, 'CASH', 150000, 'SUCCESS', 'PAYMENT'),
    (2, 2, 'MOMO', 450000, 'SUCCESS', 'PAYMENT'),
    (3, 3, 'CASH', 100000, 'SUCCESS', 'PAYMENT');

-- REVIEW (chỉ cho PAID)
INSERT IGNORE INTO review
(customer_id, booking_id, rating_stars, comment)
VALUES
    (1, 4, 5, 'Xe sạch bóng, nhân viên thân thiện, rất hài lòng!'),
    (2, 7, 4, 'Dịch vụ tốt, thời gian hơi lâu nhưng chấp nhận được.'),
    (3, 9, 5, 'Nhanh và chất lượng, sẽ quay lại lần sau.');

-- NOTIFICATION
INSERT IGNORE INTO notification
(id, title, content)
VALUES
    (1, 'Booking Confirmed',  'Your booking #1 has been confirmed'),
    (2, 'Booking Confirmed',  'Your booking #6 has been confirmed'),
    (3, 'Check-in Success',   'You have successfully checked in for booking #2'),
    (4, 'Promotion',          'New promotion: 10% off on weekends!');

INSERT IGNORE INTO customer_notification
(notification_id, customer_id, status)
VALUES
    (1, 1, 'READ'),
    (2, 2, 'UNREAD'),
    (3, 1, 'READ'),
    (4, 3, 'UNREAD');

-- SYSTEM SETTING
INSERT IGNORE INTO system_setting
(setting_key, setting_value, description, data_type)
VALUES
    ('DEPOSIT_PERCENT', '30', 'Deposit percent',    'NUMBER'),
    ('MAX_BOOKING_DAY', '30', 'Maximum booking day','NUMBER');

SET FOREIGN_KEY_CHECKS = 1;
