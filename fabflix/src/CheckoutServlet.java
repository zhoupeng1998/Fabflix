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
import java.util.Map;
import java.text.SimpleDateFormat;  
import java.util.Date;  

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@WebServlet(name = "CheckoutServlet", urlPatterns = "/api/check_out")
public class CheckoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    //@Resource(name = "jdbc/moviedb")
    //private DataSource dataSource;
	private Connection dbRead;
	private Connection dbWrite;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	System.out.println("Inside checkout api");
    	Date date = new Date();  
    	response.setContentType("application/json");
        PrintWriter out = response.getWriter();
    	String id = request.getParameter("id");
    	String firstName = request.getParameter("firstname");
    	String lastName = request.getParameter("lastname");
    	String expDate = request.getParameter("expdate");
    	// User info for current session
    	try {
        	Context initCtx = new InitialContext();
        	Context envCtx = (Context) initCtx.lookup("java:comp/env");
        	DataSource dsRead = (DataSource) envCtx.lookup("jdbc/movieRead");
        	DataSource dsWrite = (DataSource) envCtx.lookup("jdbc/movieWrite");
        	dbRead = dsRead.getConnection();
        	dbWrite = dsWrite.getConnection();
    		//Connection db = dataSource.getConnection();
    		JSONObject resultObject = new JSONObject();
			if (checkExistence(id, firstName, lastName, expDate)) {
				System.out.println("Credit card record found.");
				HttpSession session = request.getSession();
				User user = (User)session.getAttribute("user");
				Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("shoppingCart");
				
				for (String key: cart.keySet()) {
					System.out.println(key + " " + cart.get(key));
				}
				
				JSONArray jArray = new JSONArray();
				String query;
				PreparedStatement queryStatement = dbWrite.prepareStatement("INSERT INTO sales (customerId, movieId, saleDate) VALUES (?, ?, ?);");
				for(String key: cart.keySet() ) {
					// new modification in project 5
					// TODO: test
					query = "select * from movies where movies.id=?;";
					String[] params = {key};
					jArray = SelectQueryHandler.getJSONResultArray(dbRead, jArray, query, params);
				}
				for( String key:cart.keySet() ) {               // amount, id, movieID, date
					for (int i = 0; i < cart.get(key); i++) {
						queryStatement.setInt(1, Integer.parseInt(user.getId()));
						queryStatement.setString(2, key);
						queryStatement.setDate(3, new java.sql.Date(date.getTime()));
						System.out.println(queryStatement.toString());
						queryStatement.executeUpdate();
					}
					System.out.println("Update DB success!");
				}
				// if transaction success, put success message
				resultObject.put("message", "success");
			} else {
				// if transaction failed, put fail message
				System.out.println("Transaction failed!");
				resultObject.put("message", "fail");
			}
			out.write(resultObject.toJSONString());
            response.setStatus(200);
            dbRead.close();
            dbWrite.close();
		} catch (Exception e) {
			System.out.print(e.getMessage());
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("errorMessage", e.getMessage());
			out.write(jsonObject.toJSONString());
			response.setStatus(500);
         //db.close();
		}
    
    	response.setStatus(200);
    }
    
    private boolean checkExistence (String id, String fname, String lname, String date) throws Exception {
 	   //Connection db = dataSource.getConnection();
 	   if (id.isEmpty() || fname.isEmpty() || lname.isEmpty() || date.isEmpty()) { // check empty input
 		   return false;
 	   }
 	   /*
 	   String query = "SELECT * FROM moviedb.creditcards\n" + 
 	   		"WHERE id=" + id + " AND UPPER(firstName)='" + fname.toUpperCase()+ "' AND UPPER(lastName)='" + lname.toUpperCase() + "' AND expiration='" + date + "';";
 	   */
 	   //TODO: test
 	   String query = "SELECT * FROM moviedb.creditcards\n" + 
 			  "WHERE id=? AND UPPER(firstName)=? AND UPPER(lastName)=? AND expiration=?;";
 	   String[] params = {id, fname.toUpperCase(), lname.toUpperCase(), date};
	   JSONArray result = SelectQueryHandler.getJSONResultArray(dbRead, null, query, params);
       //db.close();
	   return result.size() != 0;
    }
}
