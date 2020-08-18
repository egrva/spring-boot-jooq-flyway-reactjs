-- create table for friends
CREATE TABLE friend (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL CONSTRAINT friend_name_constraint CHECK (char_length(name) >= 2)
)