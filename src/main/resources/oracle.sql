-- drop table accounts;

CREATE TABLE accounts (
  id RAW(32),
  name varchar(256),
  properties SYS.XMLTYPE,
  photo BLOB
);

