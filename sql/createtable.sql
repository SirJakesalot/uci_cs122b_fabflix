# CS 122b Project 5
#
# Run the script to create the database tables, then populate the tables
# mysql -u root -p < create_tables.sql
# mysql -u root -p -D moviedb < data.sql

DROP DATABASE IF EXISTS moviedb;
CREATE DATABASE moviedb;
USE moviedb;

CREATE TABLE IF NOT EXISTS stopwords(value VARCHAR(30)) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS movies (
    id INTEGER AUTO_INCREMENT NOT NULL,
    title VARCHAR(100) NOT NULL,
    year INTEGER NOT NULL,
    director VARCHAR(100) NOT NULL,
    banner_url VARCHAR(200) DEFAULT '',
    trailer_url VARCHAR(200) DEFAULT '',
    PRIMARY KEY(id)
) ENGINE=Innodb;

CREATE TABLE stars (
    id INTEGER AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    dob DATE default NULL,
    photo_url VARCHAR(200) default '',
    PRIMARY KEY(id)
) ENGINE=Innodb;

CREATE TABLE stars_in_movies (
    star_id INTEGER NOT NULL,
    movie_id INTEGER NOT NULL,
    FOREIGN KEY(star_id) REFERENCES stars(id) ON DELETE CASCADE,
    FOREIGN KEY(movie_id) REFERENCES movies(id) ON DELETE CASCADE
) ENGINE=Innodb;

CREATE TABLE genres (
    id INTEGER AUTO_INCREMENT NOT NULL,
    name VARCHAR(32) NOT NULL,
    PRIMARY KEY(id)
) ENGINE=Innodb;

CREATE TABLE genres_in_movies (
    genre_id INTEGER NOT NULL,
    movie_id INTEGER NOT NULL,
    FOREIGN KEY(genre_id) REFERENCES genres(id) ON DELETE CASCADE,
    FOREIGN KEY(movie_id) REFERENCES movies(id) ON DELETE CASCADE
) ENGINE=Innodb;

CREATE TABLE creditcards (
    id VARCHAR(20) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    expiration DATE NOT NULL,
    PRIMARY KEY(id)
) ENGINE=Innodb;

CREATE TABLE customers (
    id INTEGER AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    cc_id VARCHAR(20) NOT NULL,
    address VARCHAR(200) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(20) NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(cc_id) REFERENCES creditcards(id) ON DELETE CASCADE
) ENGINE=Innodb;

CREATE TABLE sales (
    id INTEGER NOT NULL AUTO_INCREMENT,
    customer_id INTEGER NOT NULL,
    movie_id INTEGER NOT NULL,
    sale_date DATE NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    FOREIGN KEY(movie_id) REFERENCES movies(id) ON DELETE CASCADE
) ENGINE=Innodb;
CREATE TABLE employees (
    id INTEGER AUTO_INCREMENT NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(20) NOT NULL,
    fullname VARCHAR(100) NOT NULL,
    PRIMARY KEY(id)
) ENGINE=Innodb;

SET GLOBAL innodb_ft_server_stopword_table = 'moviedb/stopwords';
CREATE FULLTEXT INDEX idx ON movies(title);


DROP PROCEDURE IF EXISTS add_movie;

DELIMITER //
CREATE PROCEDURE add_movie (
    IN movie_title VARCHAR(100),
    IN movie_year INTEGER,
    IN movie_director VARCHAR(100),
    IN star_first_name VARCHAR(50),
    IN star_last_name VARCHAR(50),
    IN genre_name VARCHAR(32),
    OUT movie_count INTEGER,
    OUT star_count INTEGER,
    OUT genre_count INTEGER,
    OUT movie_ID INTEGER,
    OUT star_ID INTEGER,
    OUT genre_ID INTEGER,
    OUT star_movie_links INTEGER,
    OUT genre_movie_links INTEGER
)
BEGIN
    SET movie_count = (SELECT COUNT(*) FROM movies WHERE title=movie_title);
    SET star_count = (SELECT COUNT(*) FROM stars WHERE first_name=star_first_name AND last_name=star_last_name);
    SET genre_count = (SELECT COUNT(*) FROM genres WHERE name=genre_name);

    IF movie_count = 0 THEN
        INSERT INTO movies (title, year, director) VALUES (movie_title, movie_year, movie_director);
    END IF;

    IF star_count = 0 THEN
        INSERT INTO stars (first_name, last_name) VALUES (star_first_name, star_last_name);
    END IF;

    IF genre_count = 0 THEN
        INSERT INTO genres (name) VALUES (genre_name);
    END IF;

    SET movie_ID = (SELECT MAX(id) FROM movies WHERE title=movie_title);
    SET star_ID = (SELECT MAX(id) FROM stars WHERE first_name=star_first_name AND last_name=star_last_name);
    SET genre_ID = (SELECT MAX(id) FROM genres WHERE name=genre_name);

    SET star_movie_links = (SELECT COUNT(*) FROM stars_in_movies WHERE star_id=star_ID AND movie_id=movie_ID);
    SET genre_movie_links = (SELECT COUNT(*) FROM genres_in_movies WHERE genre_id=genre_ID AND movie_id=movie_ID);

    INSERT INTO stars_in_movies VALUES(star_ID, movie_ID);

    IF genre_movie_links = 0 THEN
        INSERT INTO genres_in_movies VALUES(genre_ID, movie_ID);
    END IF;
END //

