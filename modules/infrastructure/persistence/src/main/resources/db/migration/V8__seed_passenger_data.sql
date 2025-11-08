INSERT INTO passenger (id, user_type, is_login)
VALUES
(1, 'KAKAO', 1),
(2, 'GOOGLE', 1),
(3, 'GUEST', 0),
(4, 'KAKAO', 1)
ON DUPLICATE KEY UPDATE user_type = VALUES(user_type);