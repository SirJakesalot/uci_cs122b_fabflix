import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import java.sql.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class MainSaxHandler extends DefaultHandler implements ErrorHandler {

	boolean bSameDir = false;
	boolean bDirName = false;
	boolean bFilmTitle = false;
	boolean bYear = false;
   boolean bCats = false;
   boolean bCat = false;
   boolean brelease = false;
   boolean readingFile = false;
   boolean readingFilm = false;
   String dirn = "";  // store director's name
   String ftitle= ""; // store movie title
   String fyear= ""; // store movie year
   String fcat=""; // store genre
   ArrayList<String> fcats= new ArrayList<String>(); // store genres for a movie
   ArrayList<Movies> moviesList = new ArrayList<Movies>(); // store list of movie informations
   int movieCounter = 0;
   int total =0;
   Connection connection=null;

   // prepared statements
   String queryMovie =  "INSERT INTO movies (title, year, director) " +
                        "SELECT * FROM (SELECT ? AS title, ? AS year, ? AS director) AS tmp " + 
                        "WHERE NOT EXISTS (  SELECT * FROM movies WHERE title = ? ) LIMIT 1; ";
   String queryGenre =  "INSERT INTO genres (name) " +
                        "SELECT * FROM (SELECT ? AS name) AS tmp " + 
                        "WHERE NOT EXISTS (  SELECT * FROM genres WHERE name = ? ) LIMIT 1; ";
   String queryMovieGenre =   "INSERT INTO genres_in_movies (genre_id, movie_id) " +
                              "SELECT * FROM (SELECT (SELECT id from genres where name = ? LIMIT 1) AS genre_id, (SELECT id from movies where title = ? LIMIT 1) AS movie_id ) AS tmp " + 
                              "WHERE NOT EXISTS (  SELECT * FROM genres_in_movies WHERE genre_id in (SELECT id from genres where name = ?) AND movie_id in (SELECT id from movies where title = ?)) LIMIT 1; ";

   public MainSaxHandler(Connection conn){
      connection = conn;
   }
   
   
   // used to turn year (from the xml) to valid year(for instance, 197x -> 1971/1/1, 1945+ -> 1945/1/1)
   private String convertToValidYear(String dob){
      if(dob.length()>4){
         dob = dob.substring(0,4);
      } else if(dob.length()<=3){
         return null;
      }
      String ndob="";
      for(int i=0;i<4;i++){
         if(dob.charAt(i)<'0' || dob.charAt(i)>'9'){
            ndob += '1';
         } else {
            ndob += dob.charAt(i);
         }
      }
      return ndob;
   }

   private void insertSQL(Connection conn) throws Exception{
      // check if the table movies has the movie, if not, add the movie to table movies
     PreparedStatement insertMovie= connection.prepareStatement(queryMovie);
     // check if the table genres has the genre, if not, add the genre to table genres
     PreparedStatement insertGenre = connection.prepareStatement(queryGenre);
     // check if the table genres_in_movies has the genreid-movieid pair, if not, add the genreID-movieID pair to table genres_in_movies
     PreparedStatement insertGenreMovie = connection.prepareStatement(queryMovieGenre);
     System.out.println("-making-progress-");
     for(int i=0;i<movieCounter ;i++){ // implement the batch insert
         int iyear = 0;
         if(moviesList.get(i).getfyear()!=null){
            iyear = Integer.parseInt(moviesList.get(i).getfyear());
         }
         
         insertMovie.setString(1,moviesList.get(i).getftitle());
         insertMovie.setInt(2,iyear);
         insertMovie.setString(3,moviesList.get(i).getdirn());
         insertMovie.setString(4,moviesList.get(i).getftitle());
         insertMovie.addBatch();

         for(int j=0;j< moviesList.get(i).getmoviesList().size(); j++){
            insertGenre.setString(1,moviesList.get(i).getmoviesList().get(j));
            insertGenre.setString(2,moviesList.get(i).getmoviesList().get(j));
            insertGenre.addBatch();
            
            insertGenreMovie.setString(1,moviesList.get(i).getmoviesList().get(j));
            insertGenreMovie.setString(2,moviesList.get(i).getftitle());
            insertGenreMovie.setString(3,moviesList.get(i).getmoviesList().get(j));
            insertGenreMovie.setString(4,moviesList.get(i).getftitle());
            insertGenreMovie.addBatch();
         }
           
     }
     
     // execute batches
     insertGenre.executeBatch();
     insertMovie.executeBatch();
     insertGenreMovie.executeBatch();
     
     moviesList.clear();
     insertGenreMovie.close();
     insertGenre.close();
     insertMovie.close();
     movieCounter=0;
   }

//    public void setConnection(Connection conn) throws Exception{
//       connection = conn; 
//    }

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
      if (qName.equalsIgnoreCase("movies")) {
			readingFile = true;
      } else if (qName.equalsIgnoreCase("film")) {
         readingFilm = true;
	   } else if (qName.equalsIgnoreCase("directorfilms")) {
			bSameDir = true;
		} else if (qName.equalsIgnoreCase("dirname")) {
			bDirName = true;
	   } else if (qName.equalsIgnoreCase("released")) {
		   brelease = true;
	   } else if (qName.equalsIgnoreCase("cats")) {
		   bCats = true;
         fcats = new ArrayList<String>();
	   } else if (qName.equalsIgnoreCase("t")) {
			bFilmTitle = true;
		} else if (qName.equalsIgnoreCase("year")) {
			bYear = true;
		} else if (qName.equalsIgnoreCase("cat")){
         bCat = true;
      }
	}

	@Override
	public void endElement(String uri, 
			String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("movies")) {
      // if get to the end of the file, send all data in the list to the database
			readingFile = false;
         if (moviesList.size()>0){
            try{insertSQL(connection);}catch(Exception e){System.out.println(e);};
         }
         System.out.println("-----Finished parsing main-----");
	   } else if (qName.equalsIgnoreCase("directorfilms")) {
			bSameDir = false;
         dirn=null;
		} else if (qName.equalsIgnoreCase("dirname")) {
			bDirName = false;
		} else if (qName.equalsIgnoreCase("t")) {
			bFilmTitle = false;
		} else if (qName.equalsIgnoreCase("year")) {
			bYear = false;
      } else if (qName.equalsIgnoreCase("released")) {
         brelease = false;
		} else if (qName.equalsIgnoreCase("cat")){
         bCat = false;
      } else if (qName.equalsIgnoreCase("cats")){
         bCats = false;
      } else if (qName.equalsIgnoreCase("film")) {
         readingFilm = false;
         
         // store movie informations into a class called Movies
         Movies movieObj = new Movies(dirn,ftitle,convertToValidYear(fyear),fcats);
         if(ftitle!=null && dirn!=null){
            moviesList.add(movieObj); // add Movies to the list
            movieCounter+=1;}
         if (movieCounter >= 3000){ // execute batch every 3000 items if doesn't reach the end of the file
            try{insertSQL(connection);}catch(Exception e){System.out.println(e);};
         }
         
         // reset all variables
         ftitle= null;
         fyear= null;
         fcat=null;
	   } 
	}
   
	@Override
	public void characters(char ch[], 
			int start, int length) throws SAXException {
		if (bDirName) { // obtain director's name from xml
         dirn = new String(ch, start, length);
		} else if (bFilmTitle) {// obtain file title from xml
         ftitle = new String(ch, start, length);
		} else if (bYear && !brelease) {// obtain year from xml
         fyear = new String(ch, start, length);
		} else if (bCat) {// obtain film genre from xml
         fcat = new String(ch, start, length);
         if(fcat.equals("n.a.")){fcat=null;}
         fcats.add(fcat);
		}
	}
}