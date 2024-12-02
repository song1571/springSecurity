CREATE SCHEMA IF NOT EXISTS `spring`;

USE `spring`;

-- users 테이블이 존재하면 삭제
DROP TABLE IF EXISTS `users`;

-- users 테이블 생성
CREATE TABLE `users` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(45) NULL,
    `password` VARCHAR(45) NULL,
    `enabled` INT NOT NULL,
    PRIMARY KEY (`id`)
);

-- authorities 테이블이 존재하면 삭제
DROP TABLE IF EXISTS `authorities`;

-- authorities 테이블 생성
CREATE TABLE `authorities` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(45) NULL,
    `authority` VARCHAR(45) NULL,
    PRIMARY KEY (`id`)
);