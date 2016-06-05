import moviedb_model.Customer;
import moviedb_model.DataModel;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AutocompleteSearch extends HttpServlet {

    private final String[] stop_words_array = new String[] {"a","an","as","at","b","be","by","de", "e","en","i","in","is","it","la","o","of","on","or","t","to"};
    private final Set<String> stop_words_set = new HashSet<String>(Arrays.asList(stop_words_array)); 

    public String getServletInfo() {
        return "Autocomplete";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String autocomplete_title = request.getParameter("autocomplete_title");
        System.out.println("AutocompleteTitle: " + autocomplete_title);
        if (autocomplete_title == null || "".equals(autocomplete_title)) { return; }

        String[] tokens = autocomplete_title.split("\\s+");

        String query = "SELECT title from movies WHERE id IN (SELECT id from movies WHERE ";
        ArrayList<String> statement_parameters = new ArrayList<String>();

        // Build the query and count the number of parameters
        int param_count = 0;
        for(String token : tokens){
            if(stop_words_set.contains(token.toLowerCase())){
                query += " (title LIKE ? OR title LIKE ?) AND ";
                param_count += 2;
            } else {
                query += " MATCH (title) AGAINST (? IN BOOLEAN MODE) AND ";
                param_count += 1;  
            }      
        }
        // Remove the trailing ' AND '
        if (tokens.length > 0){
            query = query.substring(0,query.length()-5);
            query += ") LIMIT 5;";
        } else {
            return;
        }

        int counter = 1;
        for (String token : tokens) {
            if (stop_words_set.contains(token.toLowerCase())) {
                if (counter == param_count - 1){
                    statement_parameters.add("% " + token + "%");
                    statement_parameters.add(token + "%");
                } else {
                    statement_parameters.add("% " + token + " %");
                    statement_parameters.add(token + " %");
                }
                counter += 2;
            } else {
                if (counter == param_count){
                    statement_parameters.add(token + "*");
                } else {
                    statement_parameters.add(token);
                }
                counter += 1;
            }
        }
        DataModel dm = new DataModel();
        ArrayList<String> autocomplete_titles = dm.getStringsForQuery(query, statement_parameters, "title");
        dm.closeConnection();

        request.setAttribute("autocomplete_titles", autocomplete_titles);
        request.getRequestDispatcher("autocomplete_search.jsp").forward(request,response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}

