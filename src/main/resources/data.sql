SET FOREIGN_KEY_CHECKS = 0;

-- ROLE
INSERT IGNORE INTO role(id, name)
VALUES
    (1,'ADMIN'),
    (2,'MANAGER'),
    (3,'EMPLOYEE'),
    (4,'CUSTOMER');

-- USER
INSERT IGNORE INTO `user`
(id,email,phone,password_hash,role_id,is_active,created_at)
VALUES
    (1,'admin@gmail.com','0900000001','123456',1,true,CURRENT_TIMESTAMP),
    (2,'manager@gmail.com','0900000002','123456',2,true,CURRENT_TIMESTAMP),
    (3,'staff1@gmail.com','0900000003','123456',3,true,CURRENT_TIMESTAMP),
    (4,'staff2@gmail.com','0900000004','123456',3,true,CURRENT_TIMESTAMP),
    (5,'customer1@gmail.com','0900000010','123456',4,true,CURRENT_TIMESTAMP),
    (6,'customer2@gmail.com','0900000011','123456',4,true,CURRENT_TIMESTAMP),
    (7,'customer3@gmail.com','0900000012','123456',4,true,CURRENT_TIMESTAMP);

-- PROVINCE
INSERT IGNORE INTO province(id,province_name)
VALUES
    (1,'Tien Giang'),
    (2,'Ho Chi Minh'),
    (3,'Ha Noi');

-- COMMUNE
INSERT IGNORE INTO commune(id,commune_name,province_id)
VALUES
    (1,'My Tho',1),
    (2,'Thu Duc',2),
    (3,'Cau Giay',3);

-- STATION
INSERT IGNORE INTO station
(id,station_name,address,commune_id,max_wash_capacity)
VALUES
    (1,'AutoWash My Tho','Tien Giang',1,20),
    (2,'AutoWash HCM','Thu Duc',2,30);

-- STAFF
INSERT IGNORE INTO staff
(id,user_id,station_id,first_name,last_name)
VALUES
    (1,3,1,'An','Nguyen'),
    (2,4,2,'Binh','Tran');

-- CUSTOMER TIER
INSERT IGNORE INTO customer_tier
(id,tier_name,min_points,booking_window_days,point_multiple)
VALUES
    (1,'BRONZE',0,3,1),
    (2,'SILVER',500,7,1.2),
    (3,'GOLD',1000,14,1.5);

-- CUSTOMER
INSERT IGNORE INTO customer
(id,user_id,first_name,last_name,customer_tier_id)
VALUES
    (1,5,'Phong','Huynh',1),
    (2,6,'Nam','Tran',2),
    (3,7,'Linh','Le',3);

-- VEHICLE
INSERT IGNORE INTO vehicle
(id,customer_id,license_plate,brand_name,color)
VALUES
    (1,1,'51A-11111','Toyota','White'),
    (2,2,'51B-22222','Honda','Black'),
    (3,3,'30A-33333','Mazda','Red');

-- SERVICE CATEGORY
INSERT IGNORE INTO service_category
(id,category_name)
VALUES
    (1,'Basic Wash'),
    (2,'Premium Wash');

-- ADDON SERVICE
INSERT IGNORE INTO addon_service
(id,name,price,duration_minutes,service_category_id)
VALUES
    (1,'Vacuum',50000,20,1),
    (2,'Polish',150000,40,2);

-- SERVICE PACKAGE
INSERT IGNORE INTO service_package
(id,service_category_id,name,base_price,required_slot)
VALUES
    (1,1,'Normal Wash',100000,1),
    (2,2,'Premium Wash',300000,2);

-- PACKAGE ADDON MAPPING
INSERT IGNORE INTO package_addon_mapping
VALUES
    (1,1),
    (2,2);

-- BOOKING
INSERT IGNORE INTO booking
(id,customer_id,vehicle_id,service_package_id,appointment_date,status,booking_type,total_amount)
VALUES
    (1,1,1,1,'2026-06-20','CONFIRMED','ONLINE',150000),
    (2,2,2,2,'2026-06-21','COMPLETED','ONLINE',450000);

-- BOOKING ADDON
INSERT IGNORE INTO booking_addon
(id,booking_id,addon_service_id,price)
VALUES
    (1,1,1,50000),
    (2,2,2,150000);

-- SLOT
INSERT IGNORE INTO booking_slot
(id,station_id,start_time,end_time,max_capacity,date,status)
VALUES
    (1,1,'08:00','09:00',5,'2026-06-20','AVAILABLE');

INSERT IGNORE INTO booking_slot_allocation
VALUES
    (1,1);

-- INVOICE
INSERT IGNORE INTO booking_invoice
(id,booking_id,customer_id,raw_amount,final_amount,service_amount,addon_amount,status)
VALUES
    (1,1,1,150000,150000,100000,50000,'PAID'),
    (2,2,2,450000,450000,300000,150000,'PAID');

-- PAYMENT
INSERT IGNORE INTO payment
(id,booking_invoice_id,payment_method,amount,payment_status,payment_type)
VALUES
    (1,1,'CASH',150000,'SUCCESS','PAYMENT'),
    (2,2,'MOMO',450000,'SUCCESS','PAYMENT');

-- REVIEW
INSERT IGNORE INTO review
(customer_id,booking_id,rating_stars,comment)
VALUES
    (1,1,5,'Very good'),
    (2,2,4,'Good service');

-- NOTIFICATION
INSERT IGNORE INTO notification
(id,title,content)
VALUES
    (1,'Booking Confirmed','Your booking has been confirmed'),
    (2,'Promotion','New promotion available');

INSERT IGNORE INTO customer_notification
(notification_id,customer_id,status)
VALUES
    (1,1,'READ'),
    (2,2,'UNREAD');

-- SYSTEM SETTING
INSERT IGNORE INTO system_setting
(setting_key,setting_value,description,data_type)
VALUES
    ('DEPOSIT_PERCENT','30','Deposit percent','NUMBER'),
    ('MAX_BOOKING_DAY','30','Maximum booking day','NUMBER');

SET FOREIGN_KEY_CHECKS = 1;