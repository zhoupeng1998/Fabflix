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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.Statement;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.jasypt.salt.StringFixedSaltGenerator;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.omg.CORBA.SystemException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;

@WebServlet(name = "DashServlet", urlPatterns = "/api/dashboard")
public class DashServlet extends HttpServlet{
    private static final long serialVersionUID = 1L;
    //@Resource(name="jdbc/moviedb")
    //private DataSource dataS;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject responseObject = new JSONObject();
        // add a star
        try {
            if( request.getParameter("name") != null ){
                String name = request.getParameter("name");
                String birthYear = request.getParameter("birthYear");
                addStar(name, birthYear, responseObject);
            } 
            else if (request.getParameter("movName") != null) {
                String movName = request.getParameter("movName");
                String movYear = request.getParameter("movYear");
                String director = request.getParameter("director");
                String starName = request.getParameter("starName");
                String starBirthY = request.getParameter("starBirthY");
                String genre = request.getParameter("genre");
                addMovie(movName, movYear, director, starName, starBirthY, genre, responseObject);
            }
            // system error
            else {
                System.out.println("Your system is wrong");
                responseObject.put("status","fail");
                responseObject.put("message", "Sorry, the system is under construction");
            }
            response.getWriter().write(responseObject.toJSONString());
            System.out.println("Mission Complete");
        } catch (Exception e) {
            e.printStackTrace();
            responseObject.put("status","fail");
            responseObject.put("message", "database error");
            System.out.println("database error");
        }
    }
   
   protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException {
        JSONObject resOb = new JSONObject();
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/movieRead");
            Connection db = ds.getConnection();
            //Connection db = dataS.getConnection();
            String query = "SHOW tables;";
            String tname;
            JSONArray userResult = SelectQueryHandler.getJSONResultArray(db, null, query, null);
            JSONArray rtn = new JSONArray();
            for (int i = 0; i < userResult.size(); ++i) {
                JSONObject temp = new JSONObject();
                tname = (String) userResult.getJSONObject(i).get("TABLE_NAME");
                query ="SHOW COLUMNS FROM " + tname + ";";
                temp.put("contents", SelectQueryHandler.getJSONResultArray(db, null, query, null));
                temp.put("table_name", tname);
                rtn.add(temp);
            }
            resOb.put("status", "success");
            resOb.put("message", "success");
            resOb.put("info", rtn);
            // debug
            System.out.println(rtn);
            System.out.println("load info success");
            response.getWriter().write(resOb.toJSONString());
            db.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            resOb.put("status","fail");
            resOb.put("message", "database error");
            System.out.println("database error");
            response.getWriter().write(resOb.toJSONString());
            //db.close();
        } catch (Exception e) {
            e.printStackTrace();
            resOb.put("status","fail");
            resOb.put("message", "connection pooling error");
            System.out.println("database error");
            response.getWriter().write(resOb.toJSONString());
        }
    }

   private void addMovie(String movName, String movYear, String director, String starName, String starBirthY, String genre, JSONObject resOb) throws SQLException, Exception {
        boolean year = false;
        if( !starBirthY.equals("") ) {
            year = true;
            System.out.println("year entered!");
        }
        int movNi = 0, directorI = 0, starNameI = 0, genreI= 0;
        String query;
        while( movName.charAt(movNi) == ' ') movNi++;
        while( director.charAt(directorI) == ' ') directorI++;
        while( starName.charAt(starNameI) == ' ') starNameI++;
        while( genre.charAt(genreI) == ' ') genreI++;
        Context initCtx = new InitialContext();
        Context envCtx = (Context) initCtx.lookup("java:comp/env");
        DataSource ds = (DataSource) envCtx.lookup("jdbc/movieWrite");
        Connection db = ds.getConnection();
        //Connection db = dataS.getConnection();
        if( year )
            query = "call insertMovie('"+movName.substring(movNi)+"',"
                                        +Integer.parseInt(movYear)+",'"
                                        +director.substring(directorI)+"','"
                                        +starName.substring(starNameI)+"',"
                                        +Integer.parseInt(starBirthY)+",'"
                                        +genre.substring(genreI)+"');";
        else
            query = "call insertMovie('"+movName.substring(movNi)+"',"
                                        +Integer.parseInt(movYear)+",'"
                                        +director.substring(directorI)+"','"
                                        +starName.substring(starNameI)+"',NULL,'"
                                        +genre.substring(genreI)+"');";
        JSONArray result = SelectQueryHandler.getJSONResultArray(db, null, query, null);
        resOb.put("status", "success");
        resOb.put("message", "success");
        resOb.put("info", result.getJSONObject(0).get("RESULT"));
        System.out.println(result.getJSONObject(0).get("RESULT"));
        db.close();
    }
   
    private void addStar( String name, String birthY, JSONObject resOb ) throws Exception{
        boolean year = false;
        String query, starID;
        Context initCtx = new InitialContext();
        Context envCtx = (Context) initCtx.lookup("java:comp/env");
        DataSource ds = (DataSource) envCtx.lookup("jdbc/movieWrite");
        Connection db = ds.getConnection();
        //Connection db = dataS.getConnection();
        PreparedStatement queryS;
        if( !birthY.equals("") ) {
            year = true;
            System.out.println("year entered!");
        }
        int nameI = 0;
        while( name.charAt(nameI) == ' ') nameI++;
        starID = nextStarId(db);
        if (year)
            query = "SELECT * from stars where stars.name = '" + name.substring(nameI) + "' AND stars.birthYear=" + birthY + ";";
        else
            query = "SELECT * from stars where stars.name = '" + name.substring(nameI) + "';";
        JSONArray result = SelectQueryHandler.getJSONResultArray(db, null, query, null);
        if( result.size() == 0 ) {
            if (year) {
                queryS = db.prepareStatement("INSERT INTO stars (id, name, birthYear) VALUES (?, ?, ?);");
                queryS.setString(1, starID);
                queryS.setString(2, name.substring(nameI));
                queryS.setInt(3, Integer.parseInt(birthY));
                // debug
                System.out.println(queryS.toString());
                queryS.executeUpdate();
            }
            else {
                queryS = db.prepareStatement("INSERT INTO stars (id, name) VALUES (?, ?);");
                queryS.setString(1, starID);
                queryS.setString(2, name.substring(nameI));
                // debug
                System.out.println(queryS.toString());
                queryS.executeUpdate();
            }
            resOb.put("status", "success");
            resOb.put("message", "success");
        }
        else {
            resOb.put("status","fail");
            resOb.put("message", "Star has existed in our database");
            System.out.println("Star existed");
        }
        db.close();
    }

    private String nextStarId(Connection db) {
        String query = "SELECT * from stars WHERE id LIKE 'nm%' ORDER BY id desc limit 1";
         try {
            JSONArray userResult = SelectQueryHandler.getJSONResultArray(db, null, query, null);
            System.out.println(userResult);
            JSONObject res = userResult.getJSONObject(0);
            System.out.println(res);
            String rtn = ((String) res.get("ID")).substring(2);
            int temp = Integer.parseInt(rtn);
            temp++;
            rtn = "nm" + Integer.toString(temp);
            System.out.println(rtn);
            return rtn;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("database error");
            return null;
        }
    }
}
