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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.asm.Type;
import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.omg.CORBA.SystemException;

import java.awt.FontFormatException;
import java.awt.print.Printable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

@WebServlet(name = "ConfirmationServlet", urlPatterns = "/api/confirm" )
public class ConfirmationServlet extends HttpServlet{
	private static final long serialVersionUID = 3L;
	
    //@Resource(name = "jdbc/moviedb")
    //private DataSource dataSource;
	
	protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException{
		// visit this cart for one last time
		Connection dbcon;
		// will pass this to front-end
	    JSONArray jArray = new JSONArray();
		try {
        	Context initCtx = new InitialContext();
        	Context envCtx = (Context) initCtx.lookup("java:comp/env");
        	DataSource ds = (DataSource) envCtx.lookup("jdbc/movieRead");
        	dbcon = ds.getConnection();
			//dbcon = dataSource.getConnection();
			JSONArray jSONResultArray = new JSONArray();
	     	HttpSession session = request.getSession();
		    Map<String, Integer> cart = (Map <String, Integer>) session.getAttribute("shoppingCart");
		    // the sales record need to be retrieved 
		    int sum = 0;
			for(String key: cart.keySet() )
				sum += cart.get(key);
			// movies amount
		    int len = cart.size();
		    String query = "select sales.id, sales.movieId, movies.title from sales, movies " + 
		    	"where sales.movieId = movies.Id " +
				"order by sales.id desc " + 
				"limit ?;";
		    String[] params = {Integer.toString(sum)}; // change with ? replacement in project5. TODO: test
		    // get from database
		    jSONResultArray = SelectQueryHandler.getJSONResultArray(dbcon, null, query, params);
		    // temp JSONArray to store the id of that specific movie
		    JSONArray idArray;
		    JSONObject temp;
		    // index for future record use
		    int jraIndex = 0;
		    int num;
		    for( int i = 0 ; i < len; ++i ) {
		    	idArray = new JSONArray();
		    	temp = new JSONObject();
		    	// movie ID
		    	String movId = (String) jSONResultArray.getJSONObject(jraIndex).get("MOVIEID");
		    	// amount = salesID amount = loop amount
		    	num = cart.get(movId);
		    	String movTitle = (String) jSONResultArray.getJSONObject(jraIndex).get("TITLE");
		    	// add its movieID, movieTitle, movieAmount into a new JSONObject
		    	temp.put( "movid", movId );
		    	temp.put( "title", movTitle );
		    	temp.put( "amount", Integer.toString(num) );
		    	for( int j = 0; j < num; ++j ) {
		    		idArray.add(jSONResultArray.getJSONObject(j+jraIndex).get("ID"));
		    	}
		    	// keep track of a new-movie record
		    	jraIndex += num;
		    	System.out.println(jraIndex);
		    	// add the movie's ID list into the JSONArray
				temp.put( "SIA", idArray );
				jArray.add(temp);
		    }
		    System.out.println("cart last check");
		    // for debug purpose
		    System.out.println(jSONResultArray);
		    // for debug purpose
		    System.out.println(jArray);
		    cart.clear();
		    session.removeAttribute("shoppingCart");
          dbcon.close();
		} catch (SQLException e) {
			System.out.println("bad database access");
			e.printStackTrace();
         //dbcon.close();
		} catch (Exception e) {
			System.out.println("Connection pooling context error");
			e.printStackTrace();
		}
		response.getWriter().write(jArray.toJSONString());
	}
}
