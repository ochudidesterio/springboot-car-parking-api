CREATE TABLE IF NOT EXISTS person(
  user_id INT PRIMARY KEY,
  name    VARCHAR,
  surname varchar,
  phone   varchar
);


CREATE TABLE IF NOT EXISTS car(
  user_id           INT,
  car_num           VARCHAR NOT NULL PRIMARY KEY,
  active            BOOLEAN,
  car_added_time    TIMESTAMP NOT NULL,
  approved_status   BOOLEAN NOT NULL,
  car_approved_time TIMESTAMP,
  FOREIGN           KEY(user_id)REFERENCES person(user_id)
);