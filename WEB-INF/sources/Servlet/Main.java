import moviedb_model.Genre;
import moviedb_model.DataSource;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

// Shows different search/browing methods for the user
public class Main extends HttpServlet {

    public String getServletInfo() {
        return "Servlet connects to MySQL database and displays result of a SELECT";
    }

    // Populate an ArrayList with all Genre objects and redirect to main.jsp
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ArrayList<Genre> genres = DataSource.getGenresForQuery("SELECT * FROM genres ORDER BY name", new ArrayList<String>());
        request.setAttribute("genres", genres);
        request.getRequestDispatcher("/customer/main.jsp").forward(request,response);
    }

    // Same as http GET
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}
