USE springcloud_lab;

INSERT INTO t_user (id, username)
VALUES (1, 'alice'), (2, 'bob')
ON DUPLICATE KEY UPDATE username = VALUES(username);

INSERT INTO t_account (user_id, balance)
VALUES (1, 1000.00), (2, 500.00)
ON DUPLICATE KEY UPDATE balance = VALUES(balance);

INSERT INTO t_product (id, name, price, stock)
VALUES (1, 'spring-cloud-book', 88.00, 100),
       (2, 'keyboard', 199.00, 20)
ON DUPLICATE KEY UPDATE name = VALUES(name), price = VALUES(price), stock = VALUES(stock);
