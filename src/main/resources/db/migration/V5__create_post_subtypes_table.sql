CREATE TABLE `post_subtypes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NULL,
  PRIMARY KEY (`id`)
);

ALTER TABLE posts
ADD COLUMN post_subtype_id INT NULL;

ALTER TABLE posts
ADD CONSTRAINT FK__posts__post_subtype_id
FOREIGN KEY (post_subtype_id)
REFERENCES post_subtypes(id);

CREATE INDEX IX__posts__post_subtype_id
ON posts (post_subtype_id);

