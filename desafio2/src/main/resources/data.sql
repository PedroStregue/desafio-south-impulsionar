CREATE TABLE product(
    id serial PRIMARY KEY,
    name text,
    category text,
    price real,
    stock_amount integer,
    bar_code character,
    code character,
    description text,
    material text,
    color text,
    expiration_date text,
    fabrication_date text,
    series text
);