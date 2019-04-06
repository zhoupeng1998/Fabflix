import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.jasypt.util.password.StrongPasswordEncryptor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "StaffLoginServlet", urlPatterns = "/api/_dashboard")
public class StaffLoginServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
   protected boolean exist;
   //@Resource(name = "jdbc/moviedb")
   //private DataSource dataSource;

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
	  // boolean value to check if the staff exists
	  exist = true;
      // get the username, password from request 
      String username = request.getParameter("username");
      String password = request.getParameter("password");
      // get reCaptcha response
      String recaptchaResponse = request.getParameter("g-recaptcha-response");
      System.out.println("recaptchaResponse=" + recaptchaResponse);

      // check if the input value exists in our database
      JSONObject responseObject = new JSONObject();
	   try {
         // if reCaptcha test fail, output the reason
		   if (!RecaptchaUtils.verify(recaptchaResponse)) {
		 	   responseObject.put("status","fail");
			   responseObject.put("message", "Please proof that you are not a bot!");
		   } // if database test fails, output the reason 
         else if (!checkExistence(username, password, request, responseObject)) {
			   responseObject.put("status","fail");
			   if( !exist )
			      responseObject.put("message", "Sorry, " + username + " does not exist");
			  else
			     responseObject.put("message", "Sorry, your password is not correct");
		   }
	   } catch (Exception e) {
		   responseObject.put("message", "Database error");
		   System.out.println("DATABASE ERROR");
		   e.printStackTrace();
	   }
       response.getWriter().write(responseObject.toJSONString());
   }
   
   private boolean checkExistence( String user, String pass, HttpServletRequest request, JSONObject responseObject ) throws Exception {
	   // flag value used for a valid staff
      boolean success = false;
      // get staff data
  	   Context initCtx = new InitialContext();
  	   Context envCtx = (Context) initCtx.lookup("java:comp/env");
  	   DataSource ds = (DataSource) envCtx.lookup("jdbc/movieRead");
  	   Connection db = ds.getConnection();
	   //Connection db = dataSource.getConnection();
	   String query = "SELECT * from employees where email=?;";
	   String[] param = {user};
	   JSONArray userResult = SelectQueryHandler.getJSONResultArray(db, null, query, param);
	   // if exists
      if (userResult.size() > 0) {
		   String encryptedPassword = userResult.getJSONObject(0).get("PASSWORD").toString();
		   success = new StrongPasswordEncryptor().checkPassword(pass, encryptedPassword);
	   } // if staff does not exist
      else 
		 this.exist = false;
	  db.close();
		
	  System.out.println("Employee verify " + user + " - " + pass + " " + success);
	   // if a valid staff detected
      if (success) {
    	   request.getSession().setAttribute("admin", "success");
		   responseObject.put("status", "success");
		   responseObject.put("message", "success");
	   }
	   return success;
   }
}
