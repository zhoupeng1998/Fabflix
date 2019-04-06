/*                        Author: Peng Zhou              
 *                             _ooOoo_
 *                            o8888888o
 *                            88" . "88
 *                            (| -_- |)
 *                            O\  =  /O
 *                         ____/`---'\____
 *                       .'  \\|     |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -  /// |   |
 *                     | \_|  ''\---/''  |   |
 *                     \  .-\__  `-`  ___/-. /
 *                   ___`. .'  /--.--\  `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 *          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
*/

DROP DATABASE IF EXISTS moviedb;
CREATE DATABASE moviedb;
USE moviedb;
SET NAMES 'utf8';

CREATE TABLE movies (
    id VARCHAR(10) NOT NULL,
    title VARCHAR(100) NOT NULL,
    year INTEGER NOT NULL,
    director VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    FULLTEXT (title)
);

CREATE TABLE stars (
    id VARCHAR(10) NOT NULL,
    name VARCHAR(100) NOT NULL,
    birthYear INTEGER,
    PRIMARY KEY (id)
);

CREATE TABLE stars_in_movies (
    starId VARCHAR(10) NOT NULL,
    movieId VARCHAR(10) NOT NULL,
    FOREIGN KEY (starId) REFERENCES stars(id) ON DELETE CASCADE,
    FOREIGN KEY (movieId) REFERENCES movies(id) ON DELETE CASCADE
);

CREATE TABLE genres (
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(32) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE genres_in_movies (
    genreId INTEGER NOT NULL,
    movieId VARCHAR(10) NOT NULL,
    FOREIGN KEY (genreId) REFERENCES genres(id) ON DELETE CASCADE,
    FOREIGN KEY (movieId) REFERENCES movies(id) ON DELETE CASCADE
);

CREATE TABLE creditcards (
    id VARCHAR(20) NOT NULL,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    expiration DATE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE customers (
    id INTEGER NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    ccID VARCHAR(20) NOT NULL,
    address VARCHAR(200) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (ccId) REFERENCES creditcards(id) ON DELETE CASCADE
);

CREATE TABLE sales (
    id INTEGER NOT NULL AUTO_INCREMENT,
    customerId INTEGER NOT NULL,
    movieId VARCHAR(10) NOT NULL,
    saleDate DATE NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (customerId) REFERENCES customers(id) ON DELETE CASCADE,
    FOREIGN KEY (movieId) REFERENCES movies(id) ON DELETE CASCADE
);

CREATE TABLE ratings (
    movieId VARCHAR(10) NOT NULL,
    rating FLOAT NOT NULL,
    numVotes INTEGER NOT NULL,
    FOREIGN KEY (movieId) REFERENCES movies(id) ON DELETE CASCADE
);

create table employees(
   email VARCHAR(50) not null,
   password VARCHAR(20) not null,
   fullname VARCHAR(100),
   primary key(email)
);

insert into employees values('classta@email.edu', 'classta', 'TA CS122B');
