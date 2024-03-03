-- drop table accounts;

CREATE TABLE accounts (
    id UNIQUEIDENTIFIER,
    name VARCHAR(256),
    properties xml,
    photo image
    )
