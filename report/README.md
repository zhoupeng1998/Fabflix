# Fabflix - Team 1 Report
UCI CS 122b Winter 2019 - Team 1
Authors:
- Peng Zhou
- Kaige Wang
## Introduction
### Visiting fabflix: 
use the following public IP addresses and ports:

| Instance | IP | Opening Port |
| ------ | ------ | ------ |
| Google Cloud | 35.185.3.86 | 80 (Tomcat not opened!) |
| AWS Instance 1 | 34.217.146.203 | 80, 8080 both opened |
| AWS Instance 2 | 52.24.164.193 | 8080 |
| AWS Instance 3 | 54.188.51.12 | 8080 |

Note: logging, HTTPS are disabled according to related permission.
NOTE: Due to possible reboot of instances, the IP addresses are subject to chagne of public IP sheet!!!
### Directories
- data - create tables and employee SQL file, movie data
- xml - XML parser program, as a maven project
- mobile - Android fabflix frontend project, written in Android studio
- fabflix - backend program and web frontend program
- out - android apk file and the .war file
- test - all jMeter test log files, along with TS/TJ analysis script
### NOTE:
- Log files of task 3 are located at test/*.txt
- The HTML file is located at ?
- The script is located in test/analy.py, it's also copied into fabflix/ AND out/
- The WAR file is located at out/fabflix.war
- This README.md file is located at the project root directory, also copied into /fabflix directory, served as the report of Project 5.
- The report.pdf file, following the provided report guideline, is located at located at the project root directory, also copied into /fabflix directory.
- The measurement HTML file, named jmeter_report.html, is located at located at the project root directory, also copied into /fabflix directory.
- Networking is required for viewing all graphs and snapshots in README.md and jmeter_report.html.
- This root directory for all files mentioned in this report is the project directory in GitHub.
## Prepared Statements
- All SQL queries with SELECT statement can be handled with "getJSONResultArray" function of "SelectQueryHandler" class, returns a JSONArray object, which uses Prepared Statements to contact with Databases.
- In fabflix/src/SelectQueryHandler.java:
```java
    static JSONArray getJSONResultArray (Connection dbConn, JSONArray destination, String query, String[] params);
        // dbConn: Database connection object
        // destination: An existing JSONArray object where query result data appends on. Pass "null" to return a new JSONArray object
        // query: The string query with SELECT statement to get data from database
        // params: A string array that each string replaces a ? in query string
    {
        PreparedStatement statement = dbConn.prepareStatement(query);  // Line 37
        // replace add ? in query string with strings in params        // Line 38-42
        ResultSet rSet = statement.executeQuery();                     // Line 43
        // add all result data into result JSONArray
    }
```
### Places that uses getJSONResult function
- fabflix/src/LoginServlet.java - Line 100
![N|Solid](https://www.ics.uci.edu/~pzhou2/fabflix-resources/LoginServletPS.png)
- fabflix/src/MovieSearchServlet.java - Lines 66, 70, 71
![N|Solid](https://www.ics.uci.edu/~pzhou2/fabflix-resources/MovieSearchServletPS.png)
- fabflix/src/SingleMovieServlet.java - Lines 77, 80, 83
![N|Solid](https://www.ics.uci.edu/~pzhou2/fabflix-resources/SingleMovieServletPS.png)
- fabflix/src/SingleStarServlet.java - Lines 71, 74
![N|Solid](https://www.ics.uci.edu/~pzhou2/fabflix-resources/SingleStarServletPS.png)
- fabflix/src/StaffLoginServlet.java - Line 75
- fabflix/src/DashServlet.java - Lines 81, 87, 146, 174, 207
- fabflix/src/ShoppingCartServlet.java - Line 79
- fabflix/src/CheckoutServlet.java - Line 91
![N|Solid](https://www.ics.uci.edu/~pzhou2/fabflix-resources/CheckoutServletPS.png)
- fabflix/src/ConfirmationServlet.java - Line 83
![N|Solid](https://www.ics.uci.edu/~pzhou2/fabflix-resources/ConfirmationServletPS.png)
### Other Places that uses PreparedStatement for SELECT or UPDATE statement
- fabflix/src/MovieSuggerstionServlet.java - Lines 62-63
- fabflix/src/CheckoutServlet.java - Lines 85, 95-99
## Connection Pooling
- Connection Pooling is enabled in fabflix/WebContent/META-INF/context.xml, where two data sources are defined. Data Source for all Mysql Reads goes to "jdbc/movieRead", which could connect to local database of the instance that the Tomcat is in, because for read-only operations JDBC can connect to either master and slave. On the other hand, all Mysql Writes must connect to data source named "jdbc/movieWrite", which connects to the IP of the master instance.
![N|Solid](https://www.ics.uci.edu/~pzhou2/fabflix-resources/DataSource.png)
- In context.xml, "jdbc/movieRead" and "jdbc/movieWrite" are defined in lines 21-24, 26-29, respectively.
- By enabling "maxTotal", "maxIdle", "maxWaitMillis" properties, along with using context look-up for data sources, Database connection pooling is enabled
### movieRead pool usage
- fabflix/src/CheckoutServlet.java - Lines 65-70 (Note: I used both Read and Write connection for this Servlet)
![N|Solid](https://www.ics.uci.edu/~pzhou2/fabflix-resources/CheckoutServletPool.png)
- fabflix/src/ConfirmationServlet.java - Lines 63-66
- fabflix/src/DashServlet.java - Lines 74-77
- fabflix/src/MovieSearchServlet.java - Lines 54-57
![N|Solid](https://www.ics.uci.edu/~pzhou2/fabflix-resources/MovieSearchServletPool.png)
- fabflix/src/MovieSuggestionServlet.java - Lines 57-60
- fabflix/src/ShoppingCartServlet.java - Lines 68-71
- fabflix/src/SingleMovieServlet.java - Lines 57-60
- fabflix/src/SingleStarServlet.java - Lines 57-60
- fabflix/src/DashServlet.java - Lines 68-71
### movieWrite pool usage
- fabflix/src/CheckoutServlet.java - Lines 65-70 (See above)
- fabflix/src/DashServlet.java - Lines 128-131, 157-160
![N|Solid](https://www.ics.uci.edu/~pzhou2/fabflix-resources/DashServletPool.png)
- More snapshot are not posted since most code for getting connection is similar
## Analyzing performance testing
- All log files are located in /test directory, as *.txt files.
- SG1.txt: Single-instance without prepared statements
- SG2.txt: Single-instance without pooling
- SG3.txt: Single-instance single threaded
- SG4.txt: Single-instance 10 threaded
- SG5R.txt: Single-instance 10 threaded HTTPS
- SC1A.txt, SC1B.txt: Scaled-version  without prepared statements, request received by instance2, instance3, respectively
- SC2A.txt, SC2B.txt: Scaled-version without pooling, request received by instance2, instance3, respectively
- SC3A.txt, SC3B.txt: Scaled-version single threaded, request received by instance2, instance3, respectively
- SC3A.txt, SC3B.txt: Scaled-version multiple threaded, request received by instance2, instance3, respectively
- Other log files are deprecated.
### Usage of analy.py script
- This python program takes one or more log file paths as parameters, read all the file inputs, prints average score of TS and TJ to console, in milliseconds.
- For single-instance tests, the program takes one file, sum all the ts and tj and print its average ts and tj. The number of record in SG* files should be 2642 entries.
- For scaled version tests, the program takes two files (one file for each instance), sum all the ts and tj of two files and print its average ts and tj. The number of record in two SC* files conbined should be 2642 entries, and each of them should have about half of 2642 entries.
- Sample format of log files:
```
TS1(nanos),TJ1(nanos)
TS2(nanos),TJ2(nanos)
```
- To run analy.py:
```sh
$ cd test
$ python3 analy.py Log1.txt Log2.txt ...
