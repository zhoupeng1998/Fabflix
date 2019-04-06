DELIMITER $$

create procedure insertMovie( IN name VARCHAR(100), IN year INT, IN director VARCHAR(100), IN starN VARCHAR(100), IN starBY INT, IN genre VARCHAR(32) )
begin
   -- check if the movie exists
   declare movID VARCHAR(10);
   declare staID VARCHAR(10);
   declare result VARCHAR(5);
   declare genID INT;
   declare amount INT;
   select id into movID from movies where movies.title=name and movies.year=year and movies.director=director;
   if( movID is NULL ) then
      -- create movie
      select concat("tt0",(select cast((select sum((select cast((select substring((select max(id) from movies WHERE id LIKE 'tt0%'),3)) as unsigned ))+1)) as char))) into movID;
      INSERT INTO movies VALUES(movID,name,year,director);
      select 'T' into result;
   else
      select 'F' into result;
   end if;
   -- check if the star exists
   select id into staID from stars where stars.name = starN;
   if( staID is NULL ) then
      -- create star
      select concat("nm",(select cast((select sum((select cast((select substring((select max(id) from stars WHERE id LIKE 'nm%'),3)) as unsigned ))+1)) as char))) into staID;
      if( starBY is NULL ) then
         INSERT INTO stars (id, name) VALUES(staID,starN);
      else
         INSERT INTO stars (id, name, birthYear) VALUES(staID,starN,starBY);
      end if;
      select concat(result,'T') into result;
   else
      select concat(result,'F') into result;
   end if;
   -- insert stars_in_movies record
   select count(*) into amount from stars_in_movies where starId = staID and movieId = movID;
   if( amount = 0 ) then
      INSERT INTO stars_in_movies VALUES(staID,movID);
      select concat(result,'T') into result;
   else
      select concat(result,'F') into result;
   end if;
   -- check if the genre exists
   select id into genID from genres where genres.name = genre;
   if( genID is NULL ) then
      -- create genre
      select sum((select max(id) from genres)+1) into genID;
      INSERT INTO genres VALUES(genID,genre);
      select concat(result,'T') into result;
   else
      select concat(result,'F') into result;
   end if;
   -- INSERT INTO genres_in_movie
   select count(*) into amount from genres_in_movies where genreId = genID and movieId = movID;
   if( amount = 0 ) then
      INSERT INTO genres_in_movies VALUES(genID,movID); 
      select concat(result,'T') into result;
   else
      select concat(result,'F') into result;
   end if;
   SELECT result as rtn;
end
$$

DELIMITER ;
