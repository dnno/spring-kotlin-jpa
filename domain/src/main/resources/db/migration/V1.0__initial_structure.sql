CREATE SEQUENCE hibernate_sequence;

CREATE TABLE city (
  id          VARCHAR(15) PRIMARY KEY,
  name        VARCHAR(128)  NOT NULL,
  description VARCHAR(1024) NULL,
  latitude    NUMERIC       NOT NULL,
  longitude   NUMERIC       NOT NULL,
  updated_at  TIMESTAMP     NOT NULL,
  created_at  TIMESTAMP     NOT NULL
);

