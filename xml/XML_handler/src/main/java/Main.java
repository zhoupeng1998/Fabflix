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

import java.io.FileWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class Main {
	public static void main (String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
    	Connection dbConnection = DriverManager.getConnection("jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false",
                "mytestuser", "mypassword");
    	// get all existing stars in database
    	PreparedStatement statement = dbConnection.prepareStatement("SELECT * FROM stars;");
    	Map<String, String> allStar = new HashMap<String, String>();
    	//Map<String, String> allMovies = new HashMap<String, String>();
    	ResultSet rSet = statement.executeQuery();
    	while (rSet.next()) {
    		allStar.put(rSet.getString("NAME"), rSet.getString("ID"));
    	}
    	statement = dbConnection.prepareStatement("SELECT * FROM movies;");
    	Map<String, String> allMovies = new HashMap<String, String>();
    	rSet = statement.executeQuery();
    	while (rSet.next()) {
			allMovies.put(rSet.getString("TITLE"), rSet.getString("ID"));
		}
    	// Prepare for report document
    	Writer fw = new FileWriter("/home/ubuntu/NewData/Report.txt");
    	// Parse Stars xml
    	StarsParser sp = new StarsParser(allStar,fw);
    	sp.parseDocument();
    	sp.toCSV();
    	// Parse movies, genres, genres in movies xml
    	MainParser mParser = new MainParser(allMovies, fw);
    	mParser.parseDocument();
    	mParser.toCSV();
    	// Parse casts xml. NOTE: must run AFTER Parsing stars!
    	CastsParser cp = new CastsParser(allStar,allMovies,mParser.getExistingMovies(),fw);
    	cp.parseDocument();
    	cp.toCSV();
    	fw.close();
	}
}
