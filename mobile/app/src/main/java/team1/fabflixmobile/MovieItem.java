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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MovieItem {

    private String id;
    private String title;
    private String year;
    private String director;
    private String rating;
    private List<String> genres;
    private Map<String, String> stars;

    public MovieItem (String id, String title, String year, String director, String rating) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;
        this.rating = rating;
        this.genres = new ArrayList<String>();
        this.stars = new HashMap<String, String>();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public String getRating() {
        return rating;
    }

    public List<String> getGenres() {
        return genres;
    }

    public Map<String, String> getStars() {
        return stars;
    }

    public void addGenre (String newGenre) {
        this.genres.add(newGenre);
    }

    public void addStar (String starId, String starName) {
        this.stars.put(starId, starName);
    }

    public String getGenresString () {
        String result = "";
        if (this.genres.size() > 0) {
            for (int i = 0; i < genres.size() - 1; i++) {
                result += genres.get(i) + ", ";
            }
            result += genres.get(genres.size() - 1);
        } else {
            result = "None";
        }
        return result;
    }

    public String getStarsString () {
        String result = "";
        if (stars.keySet().size() > 0) {
            for (String key : stars.keySet()) {
                result += ", " + stars.get(key);
            }
            result = result.substring(2);
        } else {
            result = "None";
        }
        return result;
    }
}
