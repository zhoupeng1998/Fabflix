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
 *             The Buddha Blesses Us There's Never Bugs!
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class SelectQueryHandler {
	public static JSONArray getJSONResultArray (Connection dbConn, JSONArray destination, String query, String[] params) throws SQLException {
		PreparedStatement statement = dbConn.prepareStatement(query);
		if (params != null) {
			for (int i = 1; i <= params.length; i++) {
				statement.setString(i, params[i-1]);
			}
		}
		ResultSet rSet = statement.executeQuery();
		ResultSetMetaData metaData = rSet.getMetaData();
		
		JSONArray resArray;
		if (destination == null) {
			resArray = new JSONArray();
		} else {
			resArray = destination;
		}
		
		int colCount = metaData.getColumnCount();
		List<String> colNames = new ArrayList<String>();
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			colNames.add(metaData.getColumnName(i).toUpperCase());
		}
		while (rSet.next()) {
			JSONObject jsObj = new JSONObject();
			for (int i = 1; i <= colCount; i++) {
				String key = colNames.get(i - 1);
				String value = rSet.getString(i);
				jsObj.put(key, value);
			}
			resArray.add(jsObj);
		}
		
		rSet.close();
		statement.close();
		return resArray;
	}
}
