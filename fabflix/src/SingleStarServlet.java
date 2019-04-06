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

@WebServlet(name = "SingleStarServlet", urlPatterns = "/api/single_star")
public class SingleStarServlet extends HttpServlet {
	private static final long serialVersionUID = 3L;
	
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
        	String starQuery = "SELECT * from stars WHERE stars.id=? ;";
        	String movieQuery = "SELECT movies.* FROM stars, stars_in_movies sm, movies " + 
        			"WHERE stars.id=sm.starId AND sm.movieId=movies.id " + 
        			"AND stars.id=?;";
        	String[] params = {id};
        	
        	JSONArray result = new JSONArray();
        	JSONObject starResult = new JSONObject();
        	starResult.put("STAR", SelectQueryHandler.getJSONResultArray(dbcon, null, starQuery, params));
        	result.add(starResult);
        	starResult = new JSONObject();
        	starResult.put("MOVIELIST", SelectQueryHandler.getJSONResultArray(dbcon, null, movieQuery, params));
        	result.add(starResult);
        	out.write(result.toJSONString());
        	
            response.setStatus(200);
            dbcon.close();
            
		} catch (Exception e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("errorMessage", e.getMessage());
			out.write(jsonObject.toJSONString());
         //dbcon.close();
			response.setStatus(500);
		}
    }
}
