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

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SingleMovieActivity extends AppCompatActivity {

    protected TextView titleTextView;
    protected TextView idTextView;
    protected TextView yearTextView;
    protected TextView directorTextView;
    protected TextView ratingTextView;
    protected TextView genresTextView;
    protected TextView starsTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlemovie);
        titleTextView = findViewById(R.id.titleTextView);
        idTextView = findViewById(R.id.idTextView);
        yearTextView = findViewById(R.id.yearTextView);
        directorTextView = findViewById(R.id.directorTextView);
        ratingTextView = findViewById(R.id.ratingTextView);
        genresTextView = findViewById(R.id.genresTextView);
        starsTextView = findViewById(R.id.starsTextView);

        Bundle bundle = getIntent().getExtras();
        String query = "https://ec2-34-217-146-203.us-west-2.compute.amazonaws.com:8443/fabflix/api/single_movie?id=";
        if (bundle != null && bundle.getString("query") != null) {
            query += bundle.getString("query");
        }

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        final StringRequest singleMovieRequest = new StringRequest(Request.Method.GET, query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray resultArray = new JSONArray(response);
                            // handle general info
                            JSONObject basicMovieInfoObject = resultArray.getJSONObject(0).getJSONArray("MOVIE").getJSONObject(0);
                            titleTextView.setText(basicMovieInfoObject.getString("TITLE"));
                            idTextView.setText("ID:" + basicMovieInfoObject.getString("ID"));
                            yearTextView.setText("Year: " + basicMovieInfoObject.getString("YEAR"));
                            directorTextView.setText("Director: " + basicMovieInfoObject.getString("DIRECTOR"));
                            if (basicMovieInfoObject.has("RATING")) {
                                ratingTextView.setText("Rating: " + basicMovieInfoObject.getString("RATING"));
                            } else {
                                ratingTextView.setText("Rating: None");
                            }
                            // handle genres
                            JSONArray genresArray = resultArray.getJSONObject(2).getJSONArray("GENRELIST");
                            //genresTextView.setText(resultArray.getJSONObject(1).toString());

                            if (genresArray.length() == 0) {
                                genresTextView.setText("Genres: None");
                            } else {
                                String genreResult = "";
                                for (int i = 0; i < genresArray.length(); i++) {
                                    genreResult += ", " + genresArray.getJSONObject(i).getString("NAME");
                                }
                                genreResult = genreResult.substring(2);
                                genresTextView.setText("Genres: " + genreResult);
                            }

                            // handle stars
                            JSONArray starsArray = resultArray.getJSONObject(1).getJSONArray("STARLIST");
                            if (starsArray.length() == 0) {
                                starsTextView.setText("Stars: None");
                            } else {
                                String starsResult = "";
                                for (int i = 0; i < starsArray.length(); i++) {
                                    starsResult += ", " + starsArray.getJSONObject(i).getString("NAME");
                                }
                                starsResult = starsResult.substring(2);
                                starsTextView.setText("Stars: " + starsResult);
                            }

                        } catch (JSONException je) {
                            je.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        queue.add(singleMovieRequest);
    }
}
