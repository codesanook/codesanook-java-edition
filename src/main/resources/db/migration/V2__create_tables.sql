/* session based */
SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE IF NOT EXISTS `comment_uploaded_files` (
  `comment_id` int(11) NOT NULL,
  `uploaded_file_id` int(11) NOT NULL,

  KEY `IX__comment_uploaded_files__comment_id` (`comment_id`),
  KEY `IX__comment_uploaded_files__uploaded_file_id` (`uploaded_file_id`),

  CONSTRAINT `FK__comment_uploaded_files__comment_id` FOREIGN KEY (`comment_id`) REFERENCES `comments` (`id`),
  CONSTRAINT `FK__comment_uploaded_files__uploaded_file_id` FOREIGN KEY (`uploaded_file_id`) REFERENCES `uploaded_files` (`id`)
);


CREATE TABLE IF NOT EXISTS `comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` text  NOT NULL,
  `html_content` text  NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `user_ip` varchar(255)  NULL,
  `utc_create_date` datetime NULL,
  `utc_last_update` datetime NULL,
  `post_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),

  KEY `IX__comments__user_id` (`user_id`),
  KEY `IX__comments__post_id` (`post_id`),

  CONSTRAINT `FK__comments__user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK__comments__post_id` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
);


CREATE TABLE IF NOT EXISTS `multipart_post_items`  (
  `post_id` int(11) NOT NULL,
  `order_index` int(11) NOT NULL,
  `utc_create_date` datetime NOT NULL,
  `utc_last_update` datetime  NULL,
  `multipart_post_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`post_id`),

  KEY `IX__multipart_post_items__multipart_post_id` (`multipart_post_id`),
  KEY `IX__multipart_post_items__user_id` (`user_id`),

  CONSTRAINT `FK__multipart_post_items__post_id` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
  CONSTRAINT `FK__multipart_post_items__multipart_post_id` FOREIGN KEY (`multipart_post_id`) REFERENCES `multipart_posts` (`id`),
  CONSTRAINT `FK__multipart_post_items__user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);


CREATE TABLE IF NOT  EXISTS `multipart_posts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255)  NOT NULL,
  `utc_create_date` datetime NOT NULL,
  `utc_last_update` datetime NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),

  KEY `IX__multipart_posts__user_id` (`user_id`),

  CONSTRAINT `FK__multipart_posts__user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);


CREATE TABLE IF NOT  EXISTS `post_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE IF NOT  EXISTS  `post_tags` (
  `post_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,

  KEY `IX__post_tags__tag_id` (`tag_id`),
  KEY `IX__post_tags__post_id` (`post_id`),

  CONSTRAINT `FK__post_tags__tag_id` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`),
  CONSTRAINT `FK__post_tags__post_id` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
);


CREATE TABLE IF NOT EXISTS `post_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE IF NOT EXISTS `post_uploaded_files` (
  `post_id` int(11) NOT NULL,
  `uploaded_file_id` int(11) NOT NULL,

  KEY `IX__post_uploaded_files__post_id` (`post_id`),
  KEY `IX__post_uploaded_files__uploaded_file_id` (`uploaded_file_id`),

  CONSTRAINT `FK__post_uploaded_files__post_id` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
  CONSTRAINT `FK__post_uploaded_files__uploaded_file_id` FOREIGN KEY (`uploaded_file_id`) REFERENCES `uploaded_files` (`id`)
);

CREATE TABLE IF NOT EXISTS `posts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `alias` varchar(255) NOT NULL,
  `content` text  NOT NULL,
  `html_content` text,
  `is_deleted` bit(1) NOT NULL,
  `is_multipart_post` bit(1) NOT NULL,
  `page_view_count` int(11) NOT NULL,
  `short_introduction` varchar(1000) NULL,
  `title` varchar(255)  NOT NULL,
  `utc_create_date` datetime NULL,
  `utc_last_update` datetime NULL,
  `utc_release_date` datetime NULL,
  `featured_image_id` int(11) NULL,
  `post_status_id` int(11) NOT NULL,
  `post_type_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),

  KEY `IX__posts__featured_image_id` (`featured_image_id`),
  KEY `IX__posts__post_type_id` (`post_type_id`),
  KEY `IX__posts__post_status_id` (`post_status_id`),
  KEY `IX__posts__user_id` (`user_id`),

  CONSTRAINT `FK__posts__featured_image_id_` FOREIGN KEY (`featured_image_id`) REFERENCES `uploaded_files` (`id`),
  CONSTRAINT `FK__posts__post_type_id` FOREIGN KEY (`post_type_id`) REFERENCES `post_types` (`id`),
  CONSTRAINT `FK__posts__post_status_id` FOREIGN KEY (`post_status_id`) REFERENCES `post_status` (`id`),
  CONSTRAINT `FK__posts__user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);


CREATE TABLE IF NOT  EXISTS `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE IF NOT  EXISTS `tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE IF NOT EXISTS `uploaded_files` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `context_reference_id` int(11) NOT NULL,
  `height` int(11) NOT NULL,
  `name` varchar(255)  NULL,
  `relative_path` varchar(255) NULL,
  `width` int(11) NOT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE IF NOT  EXISTS  `user_roles` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,

  KEY `IX__user_roles__role_id` (`role_id`),
  KEY `IX__user_roles__user_id` (`user_id`),

  CONSTRAINT `FK__user_roles__role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FK__user_roles__user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);


/* users */
CREATE TABLE IF NOT  EXISTS `users` (

  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activation_code` varchar(255)  NULL,
  `email` varchar(255) NOT NULL,
  `facebook_access_token` varchar(255) NULL,
  `facebook_app_scope_user_id` bigint(20) NOT NULL,
  `is_activated` bit(1) NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `name` varchar(255)  NULL,
  `password` varchar(255)  NULL,
  `utc_create_date` datetime NULL,
  `utc_last_update` datetime NULL,
  `uploaded_file_id` int(11) NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `UX__users__email` (`email`),
  KEY `IX__users_uploaded_file_id` (`uploaded_file_id`),

  CONSTRAINT `FK__users__uploaded_file_id` FOREIGN KEY (`uploaded_file_id`) REFERENCES `uploaded_files` (`id`)
);
