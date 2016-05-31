import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import java.sql.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class CastSaxHandler extends DefaultHandler implements ErrorHandler {
	boolean btitle = false; 
	boolean bstar = false;
   boolean readingFile = false;
   boolean readingStarFilm = false;
   String ftitle= ""; // store movie title
   String fstar= ""; // store star name
   ArrayList<String> starMoviesList = new ArrayList<String>(); // store list of movie titles and star names
   int Counter = 0;
   int total = 0;
   Connection connection=null;

   // prepared statements
   String queryStar =  "INSERT INTO stars (first_name, last_name) " +
                        "SELECT * FROM (SELECT ? AS first_name, ? AS last_name) AS tmp " + 
                        "WHERE NOT EXISTS (  SELECT id FROM stars WHERE first_name = ? AND last_name = ?) LIMIT 1; ";
//    String queryStarMovie =   "INSERT INTO stars_in_movies (star_id, movie_id) " +
//                               "SELECT * FROM (SELECT (SELECT id from stars where first_name = ? AND last_name = ? LIMIT 1) AS star_id, (SELECT id from movies where title = ? LIMIT 1) AS movie_id ) AS tmp " + 
//                               "WHERE NOT EXISTS (  SELECT star_id FROM stars_in_movies WHERE star_id in (SELECT id from stars where first_name = ? AND last_name= ?) AND movie_id in (SELECT id from movies where title = ?)) LIMIT 1; ";
   String queryStarMovie =   "INSERT INTO stars_in_movies (star_id, movie_id) values ( (SELECT id from stars where first_name = ? AND last_name = ? LIMIT 1),(SELECT id from movies where title = ? LIMIT 1) );";   
   
   String queryMovie =  "INSERT INTO movies (title, year, director) " +
                        "SELECT * FROM (SELECT ? AS title, 1111 AS year, 'unknown' AS director) AS tmp " + 
                        "WHERE NOT EXISTS (  SELECT * FROM movies WHERE title = ? ) LIMIT 1; ";
   
   public CastSaxHandler(Connection conn){
      connection = conn;
   }

   private void insertSQL(Connection conn) throws Exception{
     // check if the table stars has the star, if not, add the star to table stars
     PreparedStatement insertStar= connection.prepareStatement(queryStar); 
     // check if the table movies has the movie, if not, add the star to table movies
     PreparedStatement insertMovie = connection.prepareStatement(queryMovie);
     // check if the table stars_in_movies has the starid-movieid pair, if not, add the starID-movieID pair to table stars_in_movies
     PreparedStatement insertStarMovie = connection.prepareStatement(queryStarMovie);
     System.out.println("-making-progress-");
     for(int i=0;i<starMoviesList.size();i+=2){ // use batch to optimize
         String[] fnln = starMoviesList.get(i).split(" ",-1);
         String fn = fnln[0].trim();
         String ln = fnln[fnln.length-1].trim();
     
         insertStar.setString(1,fn);
         insertStar.setString(2,ln);
         insertStar.setString(3,fn);
         insertStar.setString(4,ln);
         //insertStar.executeUpdate();
         insertStar.addBatch();  
         
         insertMovie.setString(1,starMoviesList.get(i+1));
         insertMovie.setString(2,starMoviesList.get(i+1));
         //insertMovie.executeUpdate();
         insertMovie.addBatch();
         
         insertStarMovie.setString(1,fn);  
         insertStarMovie.setString(2,ln);
         insertStarMovie.setString(3,starMoviesList.get(i+1));
         //insertStarMovie.setString(4,fn);
         //insertStarMovie.setString(5,ln);
         //insertStarMovie.setString(6,starMoviesList.get(i+1));
         //insertStarMovie.executeUpdate();
         insertStarMovie.addBatch();
          
     }
     
     // execute batches
     insertMovie.executeBatch();
     insertStar.executeBatch();
     insertStarMovie.executeBatch();
     
     starMoviesList = new ArrayList<String>();
     insertStar.close();
     insertMovie.close();
     insertStarMovie.close();
     Counter=0;
   }

	public void warning(SAXParseException e) throws SAXException {
		System.out.println("WARNING : " + e.getMessage()); // do nothing
	}

	public void error(SAXParseException e) throws SAXException {
		System.out.println("ERROR : " + e.getMessage());
		throw e;
	}

	public void fatalError(SAXParseException e) throws SAXException {
		System.out.println("FATAL : " + e.getMessage());
		throw e;
	}

	@Override
	public void startElement(String uri, 
			String localName, String qName, Attributes attributes)
					throws SAXException {
      if (qName.equalsIgnoreCase("casts")) {
			readingFile = true;
      } else if (qName.equalsIgnoreCase("m")) {
         readingStarFilm = true;
	   } else if (qName.equalsIgnoreCase("t")) {
			btitle = true;
		} else if (qName.equalsIgnoreCase("a")) {
			bstar = true;
	   } 
	}

	@Override
	public void endElement(String uri, 
			String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("casts")) {
      // if get to the end of the file, send all data in the list to the database
			readingFile = false;
         if (starMoviesList.size()>0){
           try{insertSQL(connection);}catch(Exception e){System.out.println(e);};
         }
         System.out.println(total);
         System.out.println("-----Finished parsing cast-----");
	   } else if (qName.equalsIgnoreCase("t")) {
			btitle = false;
		} else if (qName.equalsIgnoreCase("a")){
         bstar = false;
      } else if (qName.equalsIgnoreCase("m")) {
         readingStarFilm = false;
         if(fstar!=null & ftitle!=null)
            starMoviesList.add(fstar); // add movie title to the list
            starMoviesList.add(ftitle); // add star name to the list
            Counter+=1;
         }
         total += 1;
         if (Counter >= 3000){ // execute batch every 3000 items if doesn't reach the end of the file
            try{insertSQL(connection);}catch(Exception e){System.out.println(e);};
	      } 
	}
   
	@Override
	public void characters(char ch[], 
			int start, int length) throws SAXException {
		if (bstar) { // take star name from xml
         fstar = new String(ch, start, length);
         if(fstar.equals("n.a.") || fstar.equals("")){
            fstar = null;
         }
		} else if (btitle) { // take movie title from xml
         ftitle = new String(ch, start, length);
         if(ftitle.equals("n.a.") || ftitle.equals("")){
            ftitle = null;
         }
		}
	}
}