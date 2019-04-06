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

package team1.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    protected EditText emailField;
    protected EditText pswdField;
    protected TextView errorField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailField = findViewById(R.id.emailField);
        pswdField = findViewById(R.id.pswdField);
        errorField = findViewById(R.id.errorMsgField);
    }

    public void loginButtonClicked (final View view) {
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        final StringRequest loginRequest = new StringRequest(Request.Method.POST, "https://ec2-34-217-146-203.us-west-2.compute.amazonaws.com:8443/fabflix/api/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("login.success", response);
                        errorField.setText(response);
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getString("status").equalsIgnoreCase("success")) {
                                errorField.setText("Success!");
                                Intent intent = new Intent(view.getContext(), SearchActivity.class);
                                startActivity(intent);
                            } else {
                                errorField.setText(json.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            errorField.setText("JSON error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("login.error", error.toString());
                        errorField.setText(error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                final Map<String, String> params = new HashMap<String, String>();
                params.put("username", emailField.getText().toString());
                params.put("password", pswdField.getText().toString());
                return params;
            }
        };

        queue.add(loginRequest);
    }
}
