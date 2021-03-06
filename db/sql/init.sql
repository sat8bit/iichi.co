
CREATE DATABASE IF NOT EXISTS iichico;

GRANT USAGE ON *.* TO 'iichicoweb'@'localhost' IDENTIFIED BY PASSWORD '*CA1D61425757489BF6EB85C9618F05681700DFEB';
GRANT SELECT, INSERT, UPDATE, DELETE ON `iichico`.* TO 'iichicoweb'@'localhost';

use iichico

CREATE TABLE `google_oauth2` (
  `google_user_id` varchar(128) NOT NULL,
  `user_id` varchar(36) NOT NULL,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`google_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_info` (
  `user_id` varchar(36) NOT NULL,
  `nickname` varchar(128) DEFAULT NULL,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `session` (
  `session_id` varchar(36) NOT NULL,
  `user_id` varchar(36) NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `board` (
  `board_id` varchar(36) NOT NULL,
  `board_subject` varchar(128) NOT NULL,
  `comment_count` INT NOT NULL,
  `created_by_user_id` VARCHAR(36) NOT NULL,
  `last_posted_at` DATETIME,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`board_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `comment` (
  `board_id` varchar(36) NOT NULL,
  `comment_seq` INT NOT NULL,
  `posted_by_user_id` VARCHAR(36) NOT NULL,
  `posted_at` DATETIME,
  `body` TEXT NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`board_id`, `comment_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
