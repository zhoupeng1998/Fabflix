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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import com.google.gson.JsonObject;

@WebServlet(name = "MovieSuggestionServlet", urlPatterns = "/api/movie_suggest")
public class MovieSuggestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    //@Resource(name = "jdbc/moviedb")
    //private DataSource dataSource;
    
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	String prefix = request.getParameter("name");
    	System.out.println("Get autocomplete query: " + prefix);
    	//int i;
    	
		if (prefix == null || prefix.trim().isEmpty()) {
			response.getWriter().write(new JSONArray().toString());
			return;
		}
		
    	response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String query = "SELECT title,id FROM movies WHERE MATCH(title) AGAINST ('";
        String[] splittedRequest = prefix.split(" ");
        for (int i = 0; i < splittedRequest.length; i++) {
        	query += "+" + splittedRequest[i] + "* ";
        }
        query += "' IN BOOLEAN MODE) ";
        /* disabled fuzzy search
        if (splittedRequest.length == 1) {
        	query += " or edth(title, '"+ splittedRequest[0] +"*', 3) ";
        }
        */
        query +="LIMIT 10;";
        System.out.println(query);

        try {
        	Context initCtx = new InitialContext();
        	Context envCtx = (Context) initCtx.lookup("java:comp/env");
        	DataSource ds = (DataSource) envCtx.lookup("jdbc/movieRead");
        	Connection dbcon = ds.getConnection();
        	//Connection dbcon = dataSource.getConnection();
			PreparedStatement statement = dbcon.prepareStatement(query);
			ResultSet rSet = statement.executeQuery();
			
			JSONArray resultArray = new JSONArray();
			while (rSet.next()) {
				resultArray.add(generateMyJSONObject(rSet.getString("ID"), rSet.getString("TITLE")));
			}
			//String[] params = {prefix};
			//JSONArray resultArray = SelectQueryHandler.getJSONResultArray(dbcon, null, query, null);
			
        	out.write(resultArray.toString());
        	dbcon.close();
        	return;
            //response.setStatus(200);
            //dbcon.close();
		} catch (SQLException e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("errorMessage", e.getMessage());
			out.write(jsonObject.toJSONString());
			response.setStatus(500);
         //dbcon.close();
		} catch (Exception e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("errorMessage", e.getMessage());
			out.write(jsonObject.toJSONString());
			response.setStatus(500);
		}
    }
    
	private static JSONObject generateMyJSONObject(String movieId, String movieTitle) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("value", movieTitle);
		
		JSONObject additionalObject = new JSONObject();
		additionalObject.put("movieId", movieId);
		
		jsonObject.put("data", additionalObject);
		return jsonObject;
	}
}
