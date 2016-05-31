/* A servlet to display the contents of the MySQL movieDB database */

import moviedb_model.Genre;
import moviedb_model.DataSource;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AddStar extends HttpServlet {

    public String getServletInfo() {
        return "AddStar";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        try {
            // Create sql insert statement
            String update = "INSERT INTO stars(first_name,last_name)values(?,?)";

            // Read new star's first_name and last_name
            String first_name = request.getParameter("first_name");
            String last_name = request.getParameter("last_name");

            ArrayList<String> statement_parameters = new ArrayList<String>();
            statement_parameters.add(first_name);
            statement_parameters.add(last_name);

            DataSource ds = new DataSource();
            int rows_affected = ds.executeUpdate(update, statement_parameters);
            switch (rows_affected) {
                case 0:
                    session.setAttribute("error", "Invalid Star Information");
                    break;
                case 1:
                    session.setAttribute("success", "Successfully added " + first_name + " " + last_name);
                    break;
                default:
                    session.setAttribute("success", "Number of rows were affected: " + rows_affected);
            }
            ds.closeQuery();
            response.sendRedirect("employee/_dashboard");
        } catch (Exception e) {
            session.setAttribute("error", "Error: " + e.toString());
            e.printStackTrace();
            response.sendRedirect("employee/_dashboard");
        } 
    }

    // Same as http GET
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}
