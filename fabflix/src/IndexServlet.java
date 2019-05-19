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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.awt.FontFormatException;
import java.awt.print.Printable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

/**
 * Mapped to the URL pattern /api/index
 */
@WebServlet(name = "IndexServlet", urlPatterns = "/api/index")
public class IndexServlet extends HttpServlet{
   private static final long serialVersionUID = 1L;

   /**
    * HANDLE POST requests to store session information
    */
   protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException{
	  // further use
      HttpSession session = ((HttpServletRequest) request).getSession();
      String sessionId = session.getId();
      Long lastAccessTime = session.getLastAccessedTime();

      JSONObject responseOb = new JSONObject();
      // Store User data in session
      responseOb.put("sessionID", sessionId);
      responseOb.put("user", ((User)session.getAttribute("user")).getUsername());
      responseOb.put("lastAccessTime", new Date(lastAccessTime).toString());

      // write sessionInfo(Cookie ID) and date into JSONObject
      response.getWriter().write(responseOb.toJSONString());
   }

   protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException{
	  // action type : add, update, remove
	  String type = request.getParameter("type");
	  // movieID
      String item = request.getParameter("item");
      HttpSession session = request.getSession();
      // stores movie info
      Map<String,Integer> cart = (Map <String,Integer>) session.getAttribute("shoppingCart");
      // add movie into User's cart
	  if(type.equals("1")) {
		  // if the cart is empty
	      if( cart == null ){
	     	 cart = new HashMap<String, Integer>();
	     	 cart.put(item, 1);
	     	 session.setAttribute("shoppingCart", cart);
	       }
	       else{
	          // prevent corrupted states through sharing under multi-threads
	          // will only be executed by one thread at a time
	          synchronized (cart){
	             // if user has logged in
	             if( cart.containsKey(item) )
	            	 cart.put(item, cart.get(item)+1);
	             else
	            	 cart.put(item, 1);
	          }
	       }
	  }
      // update movie amount into User's cart
	  else if(type.equals("2")) {
	      synchronized (cart) {
	    	  cart.remove(item);
	      }
	  }
      // remove movie into User's cart
	  else if(type.equals("3")) {
	      String temp = request.getParameter("amount");
	      int num = Integer.parseInt(temp);
	      System.out.println(item);
	      System.out.println(num);
	      synchronized (cart) {
	    	  cart.put(item,num);
	      }
	  }
	  // debug purpose
      User me = (User) session.getAttribute("user");
      System.out.println("User is " + me.getUsername());
   }
}
