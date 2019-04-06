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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CastsParser extends DefaultHandler {
	
	private String tempMovieId;
	private String tempVal;
    private List<String> allCasts;
    private Map<String, String> existingStars;
    private Writer fw;
    private int invalid_count;
    private Map<String, String> allMovies;
    private Map<String, String> existingMovies;
	
    public CastsParser (Map<String, String> existingStars, Map<String, String> allMovies, Map<String, String> existingMovies, Writer fw) throws Exception {
    	allCasts = new ArrayList<String>();
    	this.existingStars = existingStars;
    	invalid_count = 0;
    	this.allMovies = allMovies;
    	this.existingMovies = existingMovies;
    	this.fw = fw;
    }
    
    public void parseDocument () {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
        	System.out.println("Starting parsing cast xml");
            SAXParser parser = factory.newSAXParser();
            parser.parse("/home/ubuntu/stanford-movies/casts124.xml", this);
            System.out.println("Finishing parsing cast xml");
            //System.out.println("Invalids: " + invalid_count);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
    
    public void printResult () {
    	for (String i : allCasts) {
    		System.out.println(i);
    	}
    }
    
    public void toCSV () throws FileNotFoundException {
    	System.out.println("Starting writing stars in movies csv");
    	File outFile = new File("/home/ubuntu/NewData/NewStarsInMovies.csv");
    	PrintWriter pWriter = new PrintWriter(outFile);
    	for (String i : allCasts) {
    		pWriter.write(i+"\n");
    	}
    	pWriter.close();
    	System.out.println("Finishing writing stars in movies csv");
	}
    
    private String getCastId (String starName) throws SQLException, IOException {
    	if (!existingStars.containsKey(starName)) {
    		fw.write("CastsParser: " + starName + " is not in database\n");
    		return null;
    	}
    	return existingStars.get(starName);
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tempVal = "";

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
    	if (qName.equalsIgnoreCase("f")) {
    		tempMovieId = tempVal;
    	} else if (qName.equalsIgnoreCase("a")) {
    		try {
				String castId = getCastId(tempVal);
				if (castId != null) {
					//System.out.println("Temp movie id: " + tempMovieId);
					if (existingMovies.containsKey(tempMovieId)) {
						//System.out.println("REMATCHED!");
						fw.write("CastsParser: rematch movie ID: " + tempMovieId + "->" + existingMovies.get(tempMovieId) + "\n");
						tempMovieId = existingMovies.get(tempMovieId);
						//System.out.println("Temp movie id: " + tempMovieId);
					}
					if (allMovies.containsValue(tempMovieId)) {
						allCasts.add(castId + "," + tempMovieId);
					} else {
						fw.write("CastsParser: " + tempMovieId + " does not exist in Database\n");
					}
					//System.out.println(tempMovieId + "\t" + castId + "\t" + tempVal);
				} else {
					invalid_count++;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
}
