CREATE SCHEMA budget;

CREATE TABLE transaction
(
    id          INT           NOT NULL AUTO_INCREMENT,
    type        VARCHAR(45)   NOT NULL,
    description VARCHAR(80)   NOT NULL,
    amount      DECIMAL(6, 2) NOT NULL,
    date        DATETIME      NOT NULL,
    PRIMARY KEY (id)
);