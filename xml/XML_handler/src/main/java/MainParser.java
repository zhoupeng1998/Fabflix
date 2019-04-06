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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainParser extends DefaultHandler {

    private String tempDirName;
    private String tempMovieId; // used for genres_in_movies
    private String tempVal;
    private Movie tempMovie;
    private List<Movie> newMovies;
    // used to add new genre in genre table in moviedb
    private int currentGenreId;
    private Map<String, Integer> allGenres;
    private List<String> allGenresInMovies;
    private Writer fw;
    private Map<String, String> allMovies;
    private Map<String, String> existingMovies;

    public MainParser (Map<String, String> movies, Writer fw) {
    	currentGenreId = 24;
        newMovies = new ArrayList<Movie>();
        allGenres = new HashMap<String, Integer>();
        allGenresInMovies = new ArrayList<String>();
        allMovies = movies;
        existingMovies = new HashMap<String, String>();
        this.fw = fw;
    }
    
    public void parseDocument () {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
        	System.out.println("Starting parsing main xml");
            SAXParser parser = factory.newSAXParser();
            parser.parse("/home/ubuntu/stanford-movies/mains243.xml", this);
            System.out.println("Finishing parsing main xml");
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public void printResult () {
        for (Movie m : newMovies) {
            System.out.println(m.toString());
        }
        for (String i : allGenres.keySet()) {
        	System.out.println(i + "," + Integer.toString(allGenres.get(i)));
        }
        for (String i : allGenresInMovies) {
        	System.out.println(i);
        }
    }

    public void toCSV () throws FileNotFoundException {
    	System.out.println("Starting writing movies csv");
    	File outFile = new File("/home/ubuntu/NewData/NewMovies.csv");
    	PrintWriter pWriter = new PrintWriter(outFile);
        for (Movie m : newMovies) {
            pWriter.write(m.toString() + "\n");
        }
        pWriter.close();
        System.out.println("Finishing writing movies csv");
    	System.out.println("Starting writing new genres csv");
    	outFile = new File("/home/ubuntu/NewData/NewGenres.csv");
    	pWriter = new PrintWriter(outFile);
        for (String i : allGenres.keySet()) {
        	pWriter.write(Integer.toString(allGenres.get(i)) + "," + i + "\n");
        }
    	pWriter.close();
    	System.out.println("Finishing writing genres csv");
    	System.out.println("Starting writing genres in movies csv");
    	outFile = new File("/home/ubuntu/NewData/NewGenresInMovies.csv");
    	pWriter = new PrintWriter(outFile);
        for (String i : allGenresInMovies) {
        	pWriter.write(i + "\n");
        }
        pWriter.close();
        System.out.println("Finishing writing all files");
	}
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tempVal = "";
        if (qName.equalsIgnoreCase("film")) {
            tempMovie = new Movie();
        } else if (qName.equalsIgnoreCase("dirname")) {
            tempDirName = "";
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("dirname")) {
            tempDirName = tempVal;
        } else if (qName.equalsIgnoreCase("fid")) {
            tempMovieId = tempVal;
            tempMovie.setId(tempVal);
        } else if (qName.equalsIgnoreCase("t")) {
        	tempVal = tempVal.replace("\\", "");
        	tempVal = tempVal.replace(",", " ");
            tempMovie.setTitle(tempVal);
        } else if (qName.equalsIgnoreCase("year")) {
            tempMovie.setYear(tempVal);
        } else if (qName.equalsIgnoreCase("film")) {
        	if (allMovies.containsKey(tempMovie.getTitle())) {
        		existingMovies.put(tempMovie.getId(), allMovies.get(tempMovie.getTitle()));
        		try {
					fw.write("MoviesParser: " + tempMovie.getId() + "-" + tempMovie.getTitle() + " is already in database\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
        	} else if (tempMovie.getId() == null || tempMovie.getId().equals("") || tempMovie.getId().equals(" ")) {
        		try {
					fw.write("MoviesParser: " + tempMovie.getTitle() + ": invalid ID\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
	            tempMovie.setDirector(tempDirName);
	            newMovies.add(tempMovie);
	            allMovies.put(tempMovie.getTitle(), tempMovie.getId());
			}
        } else if (qName.equalsIgnoreCase("cat") && !tempVal.equalsIgnoreCase("")) {
			if (!allGenres.containsKey(tempVal)) {
				allGenres.put(tempVal, this.currentGenreId);
				currentGenreId++;
			}
			if (tempMovie.getId() != null && !existingMovies.containsKey(tempMovie.getTitle()) && !tempMovie.getId().equals("") && !tempMovie.getId().equals(" ")) {
				allGenresInMovies.add(Integer.toString(allGenres.get(tempVal)) + "," + tempMovieId);
			}
		}
    }
    
    public Map<String, String> getExistingMovies() {
		return existingMovies;
	}
}
