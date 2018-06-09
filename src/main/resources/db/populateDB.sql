DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (description, calories, date_time, user_id) VALUES
  ('Админ ланч','510',TIMESTAMP '2011-05-16 10:36:38',100000),
  ('Админ ужин','1500','2011-05-16 15:36:38',100000),
  ('Юзер обед','900','2011-05-16 13:36:38',100001),
  ('Юзер ужин','1000','2011-05-16 21:36:38',100001);
