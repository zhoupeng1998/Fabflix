/*         \\\\\\\\\\\\\\\\\\\\|||||||////////////////////  
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
 *             The Buddha Blesses Us There's Never Bugs!
 */
 
set global local_infile = 'ON';
SET NAMES 'utf8';
USE moviedb;
LOAD DATA LOCAL INFILE "/home/ubuntu/NewData/NewStars.csv" INTO TABLE stars FIELDS TERMINATED BY ",";
LOAD DATA LOCAL INFILE "/home/ubuntu/NewData/NewGenres.csv" INTO TABLE genres FIELDS TERMINATED BY ",";
LOAD DATA LOCAL INFILE "/home/ubuntu/NewData/NewMovies.csv" INTO TABLE movies FIELDS TERMINATED BY ",";
LOAD DATA LOCAL INFILE "/home/ubuntu/NewData/NewGenresInMovies.csv" INTO TABLE genres_in_movies FIELDS TERMINATED BY ",";
LOAD DATA LOCAL INFILE "/home/ubuntu/NewData/NewStarsInMovies.csv" INTO TABLE stars_in_movies FIELDS TERMINATED BY ",";
UPDATE stars SET birthYear=null WHERE birthYear=0;
