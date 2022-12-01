CREATE TABLE car
(
    id    SERIAL PRIMARY KEY,
    brand TEXT          NOT NULL,
    model TEXT          NOT NULL,
    price NUMERIC(9, 2) NOT NULL
);

CREATE TABLE man
(
    id             SERIAL PRIMARY KEY,
    name           TEXT                      NOT NULL,
    age            INTEGER CHECK ( age > 0 ) NOT NULL,
    driver_license BOOLEAN                   NOT NULL,
    car_id         INTEGER REFERENCES car (id)
);