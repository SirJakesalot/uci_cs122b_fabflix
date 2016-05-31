import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.InputSource;
import java.sql.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class SaxParser {
        
      // JDBC driver name and database url
   private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   private static String DB_URL = "jdbc:mysql:///";  
     
   private static Connection getConnection () {
    
     String db = "moviedb_project3_grading";
     String user = "classta";
     String pass = "classta";

     //String db = "moviedb";
     //String user = "root";
     //String pass = "root";
     try {
         return DriverManager.getConnection(DB_URL + db, user, pass); 
     } catch (SQLException se) {
         System.out.println("Exception creating connection: getConnection()");
         return getConnection();
     }
   }
      
	public static void main(String[] args){
      
		try {	
         Scanner scanner = new Scanner(System.in);
       
         // Read in database name or quit
//          System.out.print("Location of actors63.xml (enter 'actors63.xml' if in the current directory) : \n");
//          String star = scanner.nextLine().trim();
//          System.out.print("Location of mains243.xml (enter 'mains243.xml' if in the current directory) : \n");
//          String main = scanner.nextLine().trim();
//          System.out.print("Location of casts124.xml (enter 'casts124.xml' if in the current directory) : \n");
//          String cast = scanner.nextLine().trim();
         String star = "actors63.xml";
         String cast = "casts124.xml";
         String main = "mains243.xml";
         
			File mainInputFile = new File(main);
         File starInputFile = new File(star);
         File castInputFile = new File(cast);
         Class.forName(JDBC_DRIVER).newInstance();
         Connection conn = getConnection();
         conn.setAutoCommit(false);  // Disable auto-commit
			
         SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);
			SAXParser saxParser = factory.newSAXParser();

         // call sax handler funstions for 3 xml files
         
         StarSaxHandler starhandler = new StarSaxHandler(conn);
			MainSaxHandler mainhandler = new MainSaxHandler(conn);
         CastSaxHandler casthandler = new CastSaxHandler(conn);
         
         System.out.println("Working on actor");
         saxParser.parse(starInputFile, starhandler);
         System.out.println("Working on main");
			saxParser.parse(mainInputFile, mainhandler);
         System.out.println("Working on cast");
         saxParser.parse(castInputFile, casthandler);
          
         
         conn.commit(); // commit
         conn.close();
         
		} catch (Exception e) {
			e.printStackTrace();
		}
	}   
}
