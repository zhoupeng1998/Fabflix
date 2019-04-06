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
 *             The Buddha Blesses Us There's Never�Bugs!
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


@WebServlet(name = "MovieSearchServlet", urlPatterns = "/api/movie_search")
public class MovieSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
        	Context initCtx = new InitialContext();
        	Context envCtx = (Context) initCtx.lookup("java:comp/env");
        	DataSource ds = (DataSource) envCtx.lookup("jdbc/movieRead");
        	Connection dbcon = ds.getConnection();
        	//Connection dbcon = dataSource.getConnection();
            String query = this.prepareSelectQuery(request);
            String starQuery = "SELECT stars.id AS starId, stars.name " + 
        			"FROM movies, stars_in_movies sm, stars " + 
        			"WHERE movies.id=? AND sm.movieID=movies.id AND stars.id=sm.starId;";
        	String genreQuery = "SELECT genres.id AS genreId, genres.name " + 
        			"FROM movies, genres_in_movies gm, genres " + 
        			"WHERE movies.id=? AND gm.movieID=movies.id AND genres.id=gm.genreId;";
            JSONArray jSONResultArray = SelectQueryHandler.getJSONResultArray(dbcon, null, query, null);
            for (int i = 0; i < jSONResultArray.size(); i++) {
            	JSONObject temp = jSONResultArray.getJSONObject(i);
            	String[] params = {temp.get("ID").toString()};
            	temp.put("STARLIST", SelectQueryHandler.getJSONResultArray(dbcon, null, starQuery, params));
            	temp.put("GENRELIST", SelectQueryHandler.getJSONResultArray(dbcon, null, genreQuery, params));
            }
        	out.write(jSONResultArray.toJSONString());
        	
            response.setStatus(200);
            dbcon.close();
            
		} catch (Exception e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("errorMessage", e.getMessage());
			out.write(jsonObject.toJSONString());
			response.setStatus(500);
         //dbcon.close();
		}

    	
    }
    
    private String prepareSelectQuery (HttpServletRequest request) {
    	boolean flag = true; // this one simple deal with when I want to use "WHERE" or "AND" clause. 0:WHERE; 1:AND
    	//int i;
        String sortstr = request.getParameter("sort");
        String order = request.getParameter("order");
        String pageNum = request.getParameter("page");
        String limit = request.getParameter("limit");
        // prepare query
        String query = "select movies.id,title,year,director,rating from movies, ratings\n" + 
				"where movies.id=ratings.movieId ";
        // if sort by title, include movies without ratings by using LEFT JOIN
        if (sortstr.equals("T")) {
        	query = "SELECT movies.id,title,year,director,rating FROM movies LEFT JOIN ratings ON movies.id=ratings.movieId ";
        	flag = false;
        }
        String method = request.getParameter("method");
        if (method.equals("F")) {
        	//SELECT * FROM movies WHERE MATCH(title) AGAINST ('movie*' IN BOOLEAN MODE) LIMIT 10;
        	//SELECT * FROM movies LEFT JOIN ratings ON movies.id=ratings.movieId WHERE MATCH(title) AGAINST ('movie*' IN BOOLEAN MODE) ORDER BY movies.title LIMIT 20 OFFSET 0;
        	String title = request.getParameter("title");
        	if (sortstr.equals("T")) {
        		query = "SELECT * FROM movies LEFT JOIN ratings ON movies.id=ratings.movieId WHERE MATCH(title) AGAINST ('";
        	} else {
        		query = "SELECT * FROM movies INNER JOIN ratings ON movies.id=ratings.movieId WHERE MATCH(title) AGAINST ('";
			}
        	String[] splittedTitle = title.toLowerCase().split(" ");
        	for (int i = 0; i < splittedTitle.length; i++) {
        		query += "+" + splittedTitle[i] + "* ";
        	}
        	query += "' IN BOOLEAN MODE) ";
        	/*
        	if (splittedTitle.length == 1) {
        		query += " or edth(title, '"+ splittedTitle[0] +"*', 3) ";
        	}
        	*/
        } else if (method.equals("T")) {
    		if (sortstr.equals("T")) {
    			query += "WHERE ";
    		} else {
    			query += "AND ";
    		}
        	if (request.getParameter("title").equals("0")) {
        		query += "movies.title REGEXP '^[0-9]' "; 
        	} else {
        		query += "movies.title LIKE '" + request.getParameter("title") + "%' "; 
			}
        } else if (method.equals("G")) {
        	String genre = request.getParameter("genre");
        	if (sortstr.equals("T")) {
        		query = "SELECT movies.id,title,year,director,rating FROM movies LEFT JOIN ratings ON movies.id=ratings.movieId " + 
        				"INNER JOIN genres_in_movies gm ON movies.id=gm.movieId " + 
        				"INNER JOIN genres ON genres.id=gm.genreId AND genres.name='" + genre + "' ";
        		if (genre.equalsIgnoreCase("Others")) {
            		query = "SELECT movies.id,title,year,director,rating FROM movies LEFT JOIN ratings ON movies.id=ratings.movieId " + 
            				"LEFT JOIN genres_in_movies gm ON movies.id=gm.movieId " + 
            				"LEFT JOIN genres ON genres.id=gm.genreId AND genres.ID>23 ";
        		}
        	} else {
	        	query = "select movies.id,title,year,director,rating from movies, ratings, genres_in_movies gm, genres " +
	        			"where movies.id=ratings.movieId AND movies.id=gm.movieId AND genres.id=gm.genreId AND genres.name='" +
	        			genre + "'\n";
        	}
		} else {
			String star = request.getParameter("star");
			String title = request.getParameter("title");
			String year = request.getParameter("year");
			String director = request.getParameter("director");
			if (!star.isEmpty()) {
				query = "select distinct movies.id,title,year,director,rating from movies " + 
						"INNER JOIN ratings ON movies.id=ratings.movieId " + 
						"INNER JOIN stars_in_movies sm ON movies.id=sm.movieId " + 
						"INNER JOIN stars ON stars.id=sm.starId AND stars.name LIKE '%" + star +"%'";
				if (sortstr.equals("T")) {
					query = "select distinct movies.id,title,year,director,rating from movies " + 
							"LEFT JOIN ratings ON movies.id=ratings.movieId " + 
							"INNER JOIN stars_in_movies sm ON movies.id=sm.movieId " + 
							"INNER JOIN stars ON stars.id=sm.starId AND stars.name LIKE '%" + star +"%'";
				}
			}
			if (!title.isEmpty()) {
				if (flag == false) {
					query += "WHERE movies.title LIKE '%" + title + "%' ";
				} else {
					query += "AND movies.title LIKE '%" + title + "%' ";
				}
				flag = true;
			}
			if (!year.isEmpty()) {
				if (flag == false) {
					query += "WHERE movies.year=" + year + " ";
				} else {
					query += "AND movies.year=" + year + " ";
				}
				flag = true;
			}
			if (!director.isEmpty()) {
				if (flag == false) {
					query += "WHERE movies.director LIKE '%" + director + "%' ";
				} else {
					query += "AND movies.director LIKE '%" + director + "%' ";
				}
				flag = true;
			}
		}
        // handle sorting / ordering / pagination
        if (sortstr.equals("T")) {
        	query += "ORDER BY movies.title ";
        } else {
        	query += "ORDER BY rating ";
        }
        
        if (!order.equals("A")) {
        	query += "DESC ";
        }
        
        query += "LIMIT " + limit + " OFFSET " + Integer.toString(Integer.parseInt(pageNum) * Integer.parseInt(limit)) + ";";
        System.out.println("Query: " + query);
        return query;
	}
}
