import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import java.sql.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class StarSaxHandler extends DefaultHandler implements ErrorHandler {

	boolean bln = false;
	boolean bfn = false;
	boolean bdob = false;
   boolean readingActor = false;
   boolean readingFile = false;
   boolean bstagen = false;
   String ln = null;
   String fn = null;
   String dob = null;
   String stagen = null;
//    String pln = null;
//    String pfn = null;
   ArrayList<String> starsList = new ArrayList<String>();
   
   int starCounter = 0;
   int total=0;
   Connection connection=null;

   String queryStar =  "INSERT INTO stars (first_name, last_name, dob) " +
                        "SELECT * FROM (SELECT ? AS first_name, ? AS last_name, ? AS dob) AS tmp " + 
                        "WHERE NOT EXISTS (  SELECT id FROM stars WHERE first_name = ? AND last_name = ?) LIMIT 1; ";
   //String queryStar = "INSERT INTO stars (first_name, last_name, dob) values (? , ?, ?);";
   
   public StarSaxHandler(Connection conn){
      connection = conn;
   }

   private void insertSQL(Connection conn){
     try{
         // check if the table stars has the star, if not, add the star to table stars
         PreparedStatement insertStar= connection.prepareStatement(queryStar);
         System.out.println("-making-progress-");
         for(int i=0;i<starsList.size() ;i+=3){ // use batch insert
            insertStar.setString(1,starsList.get(i));
            insertStar.setString(2,starsList.get(i+1));
            insertStar.setString(3,starsList.get(i+2));
            insertStar.setString(4,starsList.get(i));
            insertStar.setString(5,starsList.get(i+1));
            insertStar.addBatch();         
         }
         // execute batch
         insertStar.executeBatch();
         starsList = new ArrayList<String>();;
         insertStar.close();
         starCounter=0;
     }catch(Exception e){
         System.out.println(e);
     } 
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
      return ndob + "/1/1";
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
      if (qName.equalsIgnoreCase("actors")) {
			readingFile = true;
      } else if (qName.equalsIgnoreCase("familyname")) {
         bln = true;
	   } else if (qName.equalsIgnoreCase("firstname")) {
			bfn = true;
		} else if (qName.equalsIgnoreCase("dob")) {
			bdob = true;
	   } else if (qName.equalsIgnoreCase("actor")) {
			readingActor = true;
      } else if (qName.equalsIgnoreCase("stagename")) {
         bstagen = true;
      }
	}

	@Override
	public void endElement(String uri, 
			String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("actors")) {// if get to the end of the file, send all data in the list to the database
			readingFile = false;
         if (starsList.size()>0){
            try{insertSQL(connection);}catch(Exception e){System.out.println(e);};
         }
         System.out.println("-----Finished parsing actors-----");
	   } else if (qName.equalsIgnoreCase("familyname")) {
			bln = false;
		} else if (qName.equalsIgnoreCase("firstname")) {
			bfn = false;
		} else if (qName.equalsIgnoreCase("dob")) {
			bdob = false;
      } else if (qName.equalsIgnoreCase("actor")) {
         readingActor = false;
         if(stagen!=null && fn==null && fn==null){// if an actor does not have a firstname and a lastname, use the stagename
            String[] stagename = stagen.split(" ",-1);
            if(stagename.length==1){
               ln = stagename[0];
               fn = "";
            } else {
               ln = stagename[stagename.length-1];
               fn = stagename[0];
            }
         }
         //total+=1;
         if(fn!=null && ln!=null){
            starsList.add(fn.trim());
            starsList.add(ln.trim());
         
            if (dob != null){
               starsList.add(dob.trim());
            } else {starsList.add(dob);}
            
            starCounter+=1;
         }
         
         
         ln = null;
         fn = null;
         dob = null;
         stagen = null;
         
         
         if (starCounter >= 3000){// execute batch every 3000 items if doesn't reach the end of the file
            try{insertSQL(connection);}catch(Exception e){System.out.println(e);};
         }
         
	   } else if (qName.equalsIgnoreCase("stagename")) {
         bstagen = false;
      } 
	}
   
	@Override
	public void characters(char ch[], 
			int start, int length) throws SAXException {
		if (bfn) {
         //pfn = fn;
         fn = new String(ch, start, length);
         if(fn.equals("n.a.")||fn.equals("")){
            fn = null;
         }
		} else if (bln) {
         //pln = ln;
         ln = new String(ch, start, length);
         if(ln.equals("n.a.")||ln.equals("")){
            ln = null;
         }
         
         
		} else if (bdob) {
         dob = new String(ch, start, length);
         if(dob.equals("n.a.")){
            dob = null;
         }
         if(dob!=null && dob.length()>0){
            dob = convertToValidYear(dob);
         }    
		} else if (bstagen) {
         stagen = new String(ch, start, length);
         if(stagen.equals("n.a.")){
            stagen = null;
         }
      }
	}
}