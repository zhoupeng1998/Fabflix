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
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// NOTE: for the new stars to be added to moviedb.stars, I set their ids to be initial-sm, stands for "Stanford Movies"
public class StarsParser extends DefaultHandler {

    private String tempVal;
    private String tempStarName;
    private String tempBirth;
    private List<String> csvAllStars;
    private Map<String, String> existingStars;
    private Writer fw;
    int count = 1;

    public StarsParser (Map<String, String> existingStars, Writer fw) throws Exception {
    	csvAllStars = new ArrayList<String>();
    	this.existingStars = existingStars;
    	this.fw = fw;
    }
    
    // check whether the star exists in moviedb
    private boolean checkStarExistence (String starName) throws SQLException, IOException {
    	if (existingStars.containsKey(starName)) {
    		fw.write("StarsParser: " + starName + "-" + existingStars.get(starName) + " is already in database\n");
    		return true;
    	}
    	return false;
    }
    
    private void handleTempStr () {
    	tempStarName = tempStarName.replace("\\", "");
    	tempStarName = tempStarName.replace("'", "");
    	tempStarName = tempStarName.replace("`", "");
    	tempStarName = tempStarName.replace("~", " ");
    	tempBirth = tempBirth.replace("+", "");
    	try {
    		Integer.parseInt(tempBirth);
    	} catch (Exception e) {
    		tempBirth = "";
		}
    }
    
    public void parseDocument () {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
        	System.out.println("Starting parsing actors xml");
            SAXParser parser = factory.newSAXParser();
            parser.parse("/home/ubuntu/stanford-movies/actors63.xml", this);
            System.out.println("Finishing parsing actors xml");
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public void printResult () {
    	for (String i : csvAllStars) {
    		System.out.println(i);
    	}
    }
    
    public void toCSV () throws FileNotFoundException {
    	System.out.println("Starting writing actors csv");
    	File outFile = new File("/home/ubuntu/NewData/NewStars.csv");
    	PrintWriter pWriter = new PrintWriter(outFile);
    	for (String i : csvAllStars) {
    		pWriter.write(i+"\n");
    	}
    	pWriter.close();
    	System.out.println("Finishing writing actors csv");
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
        try {
        	if (qName.equalsIgnoreCase("actor")) {
        		handleTempStr();
        		boolean exists = checkStarExistence(tempStarName);
        		if (!exists) {
        			//allStars.put(tempStarName, tempBirth);
        			String newStarId = "sm" + Integer.toString(count);
        			csvAllStars.add(newStarId + "," + tempStarName + "," + tempBirth);
        			existingStars.put(tempStarName, newStarId);
        			count++;
        		}
        	}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        if (qName.equalsIgnoreCase("stagename")) {
            tempStarName = tempVal;
        } else if (qName.equalsIgnoreCase("dob")) {
            tempBirth = tempVal;
        }
    }
    
    public Map<String, String> getExistingStars () {
		return existingStars;
	}
}