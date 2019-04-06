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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class RecaptchaUtils {
	
	public static final String SECRET_KEY ="6LdnXZAUAAAAAMBjLP0lzzUy3lvt1Wt9llK-uEFo";
	public static final String SITE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
	
	public static boolean verify (String recaptchaResponse) throws IOException {
        if (recaptchaResponse == null || recaptchaResponse.length() == 0) {
        	System.out.println("recaptcha verification failed: gRecaptchaResponse is null or empty");
        	return false;
        }
        
        URL verifyUrl = new URL(SITE_VERIFY_URL);
        
        HttpsURLConnection conn = (HttpsURLConnection) verifyUrl.openConnection();
        
        conn.setRequestMethod("POST");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.5");
        
        String postParams = "secret=" + SECRET_KEY + "&response=" + recaptchaResponse;
        
        conn.setDoOutput(true);
        
        OutputStream outStream = conn.getOutputStream();
        outStream.write(postParams.getBytes());
        outStream.flush();
        outStream.close();

        int responseCode = conn.getResponseCode();
        System.out.println("responseCode=" + responseCode);
        
        InputStream inputStream = conn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        
        JSONObject jsonObject = JSON.parseObject(inputStream, JSONObject.class);
        inputStreamReader.close();
        System.out.println("Response: " + jsonObject.toString());
        
        if (jsonObject.getBoolean("success")) {
        	return true;
        } else {
			System.out.println("recaptcha verification failed: response is " + jsonObject.toString());
			return false;
		}
        //throw new Exception("recaptcha verification failed: response is " + jsonObject.toString());
	}
}

/*
class RecaptchaVerificationException extends Exception {
	private static final long serialVersionUID = 1L; 
	public RecaptchaVerificationException () {
	}
	public RecaptchaVerificationException (String msg) {
		super(msg);
	}
}
*/
