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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MovieEntryAdapter extends ArrayAdapter<MovieItem> {

    private Context context;
    private List<MovieItem> movieList;

    public MovieEntryAdapter (Context context, ArrayList<MovieItem> movies) {
        super(context, 0, movies);
        this.context = context;
        this.movieList = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listrow_movie, parent, false);
        }
        // get the movie for current position of list
        MovieItem currentMovieItem = movieList.get(position);

        TextView titleTextView = (TextView)view.findViewById(R.id.titleTextView);
        TextView idTextView = (TextView)view.findViewById(R.id.idTextView);
        TextView yearTextView = (TextView)view.findViewById(R.id.yearTextView);
        TextView directorTextView = (TextView)view.findViewById(R.id.directorTextView);
        TextView ratingTextView = (TextView)view.findViewById(R.id.ratingTextView);
        TextView genresTextView = (TextView)view.findViewById(R.id.genresTextView);
        TextView starsTextView = (TextView)view.findViewById(R.id.starsTextView);

        titleTextView.setText(currentMovieItem.getTitle());
        idTextView.setText("ID: " + currentMovieItem.getId());
        yearTextView.setText("Year: " + currentMovieItem.getYear());
        directorTextView.setText("Director: " + currentMovieItem.getDirector());
        ratingTextView.setText("Rating: " + currentMovieItem.getRating());
        genresTextView.setText("Genres: " + currentMovieItem.getGenresString());
        starsTextView.setText("Stars: " + currentMovieItem.getStarsString());


        return view;
    }
}
