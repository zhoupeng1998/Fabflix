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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.ObjectInputStream.GetField;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@WebServlet(name="ShoppingCartServlet", urlPatterns = "/api/shop_cart")
public class ShoppingCartServlet extends HttpServlet{
	private static final long serialVersionUID = 2L;
	
	//@Resource(name = "jdbc/moviedb")
	//private DataSource dataSource;
	
	protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("application/json");
		HttpSession session = request.getSession();
		// get User's cart info
		Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("shoppingCart");
		JSONArray jArray = new JSONArray();
		// if nothing is inside
		if( cart == null || cart.size() == 0 ) {
			System.out.println("an empty shopping cart"); 	// display empty cart info
		}
		else {
			try {
	        	Context initCtx = new InitialContext();
	        	Context envCtx = (Context) initCtx.lookup("java:comp/env");
	        	DataSource ds = (DataSource) envCtx.lookup("jdbc/movieRead");
	        	Connection db = ds.getConnection();
				//Connection db = dataSource.getConnection();
				String query;
				// TODO: query id for each item in shopping cart? Too fucking complicated! Consider creating a CartItem class instead
				// add movie info into a jsonfile for later use
				for(String key: cart.keySet() ) {
					query = "select * from movies where movies.id=?;";
					String[] params = {key};
					jArray = SelectQueryHandler.getJSONResultArray(db, jArray, query, params);
				}
				// add AMOUNT parameter
				for( int i = 0; i < cart.size(); ++i ) {
					jArray.getJSONObject(i).put("AMOUNT",cart.get(jArray.getJSONObject(i).get("ID")));
				}
				// debug use
				System.out.println(jArray);
            db.close();
			} catch (SQLException e) {
				System.out.println("Bad database");
				e.printStackTrace();
            //db.close();
			} catch (Exception e) {
				System.out.println("Connection pool error");
			}
		}
		// write to the front
		response.getWriter().write(jArray.toJSONString());
	}
	
}
