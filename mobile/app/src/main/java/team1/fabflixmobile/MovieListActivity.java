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
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

public class MovieListActivity extends AppCompatActivity {

    //protected TextView textView;
    protected Button prevButton;
    protected Button nextButton;
    protected int pageNum;
    protected String query;
    protected ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);
        //textView = findViewById(R.id.textView);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        listView = findViewById(R.id.listView);
        pageNum = 0;

        //ArrayList<MovieItem> movies = new ArrayList<MovieItem>();
        //movies.add(new MovieItem("id1","title1","year1","director1","rating1"));
        //movies.add(new MovieItem("id2","title2","year2","director2","rating2"));
        //handleListView(movies);
        getData();
    }

    public void nextButtonClicked (View view) {
        pageNum++;
        getData();
    }

    public void prevButtonClicked (View view) {
        pageNum--;
        getData();
    }

    public void getData () {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.getString("query") == null || bundle.getString("query").equals("")) {
            query = "https://ec2-34-217-146-203.us-west-2.compute.amazonaws.com:8443/fabflix/api/movie_search?method=S&title=&year=&director=&star=&sort=R&order=D&page=" + Integer.toString(pageNum) + "&limit=20";
        } else {
            String[] splittedQuery = bundle.getString("query").split(" ");
            query = "https://ec2-34-217-146-203.us-west-2.compute.amazonaws.com:8443/fabflix/api/movie_search?method=F&title=";
            //if (splittedQuery.length == 1) {
            query += splittedQuery[0];
            //}
            if (splittedQuery.length > 1) {
                for (int i = 1; i < splittedQuery.length; i++) {
                    query += "%20" + splittedQuery[i];
                }
            }
            query += "&sort=T&order=A&page=" + Integer.toString(pageNum) + "&limit=20";
        }
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        final ArrayList<MovieItem> movies = new ArrayList<MovieItem>();

        final StringRequest movieListRequest = new StringRequest(Request.Method.GET, query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //String result = "";
                            JSONArray resultArray = new JSONArray(response);
                            // set prev/next button visibility according to page num and json result
                            if (pageNum == 0) {
                                prevButton.setVisibility(View.GONE);
                            } else {
                                prevButton.setVisibility(View.VISIBLE);
                            }
                            if (resultArray.length() == 20) {
                                nextButton.setVisibility(View.VISIBLE);
                            } else {
                                nextButton.setVisibility(View.GONE);
                            }
                            // handle json

                            for (int i = 0; i < resultArray.length(); i++) {
                                JSONObject resultObject = resultArray.getJSONObject(i);
                                // basic information

                                String movieRating;
                                if (resultObject.has("RATING")) {
                                    movieRating = resultObject.getString("RATING");
                                } else {
                                    movieRating = "None";
                                }
                                MovieItem movieItem = new MovieItem(resultObject.getString("ID"), resultObject.getString("TITLE"), resultObject.getString("YEAR"), resultObject.getString("DIRECTOR"), movieRating);
                                // add all genres
                                JSONArray genresInMovie = resultObject.getJSONArray("GENRELIST");
                                for (int j = 0; j < genresInMovie.length(); j++) {
                                    movieItem.addGenre(genresInMovie.getJSONObject(j).getString("NAME"));
                                }
                                // add all stars
                                JSONArray starsInMovie = resultObject.getJSONArray("STARLIST");
                                for (int k = 0; k < starsInMovie.length(); k++) {
                                    movieItem.addStar(starsInMovie.getJSONObject(k).getString("ID"), starsInMovie.getJSONObject(k).getString("NAME"));
                                }
                                //result += resultObject.getString("ID") + "\t" + resultObject.getString("TITLE") + "\n";
                                movies.add(movieItem);
                            }
                            //textView.setText(result);
                            handleListView(movies);
                            //textView.setText(response);
                        } catch (JSONException je) {
                            //textView.setText("JSON ERROR");
                            je.printStackTrace();
                        }

                        //textView.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //textView.setText("DATABASE ERROR");
                        error.printStackTrace();
                    }
                });
        queue.add(movieListRequest);
    }

    public void handleListView (ArrayList<MovieItem> movies) {
        MovieEntryAdapter adapter = new MovieEntryAdapter(this, movies);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final MovieItem movie = (MovieItem) parent.getItemAtPosition(position);
                String message = "Selected movie " + movie.getId() + " " + movie.getTitle();

                Intent intent = new Intent(view.getContext(), SingleMovieActivity.class);
                intent.putExtra("query", movie.getId());
                startActivity(intent);
            }
        });

        listView.setAdapter(adapter);
    }
}
