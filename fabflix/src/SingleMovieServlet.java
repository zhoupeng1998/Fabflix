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

@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single_movie")
public class SingleMovieServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    //@Resource(name = "jdbc/moviedb")
    //private DataSource dataSource;
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/movieRead");
            Connection dbcon = ds.getConnection();
            //Connection dbcon = dataSource.getConnection();
            String id = request.getParameter("id");
            String movieQuery = "SELECT id, title, year, director, rating, numVotes " + 
                    "FROM movies LEFT JOIN ratings ON ratings.movieId=movies.id " + 
                    "WHERE movies.id=?;";
            System.out.println("Movie query: " + movieQuery);
            String starQuery = "SELECT stars.id AS starId, name, birthYear " + 
                    "FROM movies, stars_in_movies sm, stars " + 
                    "WHERE movies.id=? AND sm.movieID=movies.id AND stars.id=sm.starId;";
            String genreQuery = "SELECT genres.id AS genreId, genres.name " + 
                    "FROM movies, genres_in_movies gm, genres " + 
                    "WHERE movies.id=? AND gm.movieID=movies.id AND genres.id=gm.genreId;";
            String[] params = {id};
            
            JSONArray result = new JSONArray();
            JSONObject movieResult = new JSONObject();
            movieResult.put("MOVIE", SelectQueryHandler.getJSONResultArray(dbcon, null, movieQuery, params));
            result.add(movieResult);
            movieResult = new JSONObject();
            movieResult.put("STARLIST", SelectQueryHandler.getJSONResultArray(dbcon, null, starQuery, params));
            result.add(movieResult);
            movieResult = new JSONObject();
            movieResult.put("GENRELIST", SelectQueryHandler.getJSONResultArray(dbcon, null, genreQuery, params));
            result.add(movieResult);
            out.write(result.toJSONString());
            
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
}
