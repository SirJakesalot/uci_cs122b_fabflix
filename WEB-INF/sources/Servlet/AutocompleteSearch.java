import moviedb_model.Customer;
import moviedb_model.DataSource;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AutocompleteSearch extends HttpServlet {
   
    public String[] stopWords = new String[] {"a","an","as","at","b","be","by","de",
                              "e","en","i","in","is","it","la","o","of","on","or","t","to"};

    public String getServletInfo() {
        return "ajax";
    }

    // Redirects the user to the login page
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Customer customer = (Customer) request.getSession().getAttribute("customer");
        if (customer == null) {
            response.sendRedirect("");
        }
        String loginUser = "root";
        String loginPasswd = "root";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb_project4";
        response.setContentType("text/html"); 
        PrintWriter out = response.getWriter();
        
        try {
              Class.forName("com.mysql.jdbc.Driver").newInstance();
              Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
              String preparestat = "SELECT title from movies WHERE id IN ( SELECT id from movies WHERE ";
              
              String outputStr = "";
              
              String title = request.getParameter("title");
              
              if(title==null||"".equals(title)){out.print("");return;}
              
              String[] ptitle = title.split("\\s+");
              int lengthTokens = ptitle.length;
              //out.print(Integer.toString(lengthTokens));
              int total = 0;
              for(String token : ptitle){
                  if(Arrays.asList(stopWords).contains(token.toLowerCase())){
                     preparestat += " (title LIKE ? OR title LIKE ?) AND ";
                     total += 2;
                  } else {
                     preparestat += " MATCH (title) AGAINST (? IN BOOLEAN MODE) AND ";
                     total += 1;  
                  }      
              }
              if (lengthTokens > 0){
                  preparestat = preparestat.substring(0,preparestat.length()-5);
                  preparestat += ") LIMIT 5;";
              } else {
                  out.print("");return;
              }
              PreparedStatement statement = dbcon.prepareStatement(preparestat);
              
              //out.println(preparestat);
              
              int counter = 1;
              for(String token : ptitle){
                  if(Arrays.asList(stopWords).contains(token.toLowerCase())){
                     if(counter==total-1){
                        statement.setString(counter,"% " + token + "%");
                        statement.setString(counter+1, token + "%");
                        //out.print("% " + token + "%" + "<br>");
                        //out.print(token + "%" + "<br>");
                     } else {
                        statement.setString(counter,"% " + token + " %");
                        statement.setString(counter+1, token + " %");
                        //out.print("% " + token + "%" + "<br>");
                        //out.print("%" +token + "%" + "<br>");
                     }
                     counter += 2;
                  }
                  else {
                     if(counter==total){
                        statement.setString(counter,token+"*");
                        //out.print(token+"*" + "<br>");
                     } else {
                        statement.setString(counter,token);
                        //out.print(token + "<br>");
                     }
                     counter += 1;
                  }
              }
              //out.println(Integer.toString());
              ResultSet rs = statement.executeQuery();
                         
              while(rs.next()){
                  outputStr += rs.getString("title") + "_!_";
              }
              if(outputStr.length() >= 3){
                  outputStr = outputStr.substring(0,outputStr.length()-3);
              }
              out.print(outputStr);
              statement.close();
              dbcon.close();
              
        }catch (SQLException ex) {
              while (ex != null) {
                    System.out.println ("SQL Exception:  " + ex.getMessage ());
                    ex = ex.getNextException ();
                }  // end while
        }  // end catch SQLException

        catch(java.lang.Exception ex)
        {
                out.println("<HTML>" +
                            "<HEAD><TITLE>" +
                            "MovieDB: Error" +
                            "</TITLE></HEAD>\n<BODY>" +
                            "<P>SQL error in doGet: " +
                            ex.getMessage() + "</P></BODY></HTML>");
                return;
        }
        out.close();

    }
}

