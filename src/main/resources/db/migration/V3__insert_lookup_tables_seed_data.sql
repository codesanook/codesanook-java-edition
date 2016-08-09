/*roles*/
INSERT INTO roles (`name`)
SELECT * FROM (SELECT 'admin') AS temp
WHERE NOT EXISTS (SELECT * FROM roles WHERE `name` = 'admin');

INSERT INTO roles (`name`)
SELECT * FROM (SELECT 'author') AS temp
WHERE NOT EXISTS (SELECT * FROM roles WHERE `name` = 'author');

/*post_types*/
INSERT INTO post_types (`name`)
SELECT * FROM ( SELECT 'question') AS temp
WHERE NOT EXISTS ( SELECT `name` FROM post_types where `name` = 'question');

INSERT INTO post_types (`name`)
SELECT * FROM ( SELECT 'article') AS temp
WHERE NOT EXISTS ( SELECT `name` FROM post_types where `name` = 'article');



/*post status*/
INSERT INTO post_status (`name`)
SELECT * FROM (SELECT 'published') AS temp
WHERE NOT EXISTS( SELECT * FROM post_status WHERE `name` = 'published');

INSERT INTO post_status (`name`)
SELECT * FROM (SELECT 'unpublished') AS temp
WHERE NOT EXISTS( SELECT * FROM post_status WHERE `name` = 'unpublished');

INSERT INTO post_status (`name`)
SELECT * FROM (SELECT 'deleted') AS temp
WHERE NOT EXISTS( SELECT * FROM post_status WHERE `name` = 'deleted');

INSERT INTO post_status (`name`)
SELECT * FROM (SELECT 'scheduled') AS temp
WHERE NOT EXISTS( SELECT * FROM post_status WHERE `name` = 'scheduled');
