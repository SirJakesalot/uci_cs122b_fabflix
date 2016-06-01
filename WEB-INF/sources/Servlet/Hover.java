import moviedb_model.Movie;
import moviedb_model.Genre;
import moviedb_model.Star;
import moviedb_model.DataSource;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Hover extends HttpServlet
{
    public String getServletInfo()
    {
       return "Hover";
    }
   
    // Get all the Movie information and forward to hover.jsp 
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String movie_id = request.getParameter("movie_id");
            ArrayList<String> statement_parameters = new ArrayList<String>();
            statement_parameters.add(movie_id);

            ArrayList<Movie> movies = DataSource.getMoviesForQuery("SELECT * FROM movies WHERE id=?;",statement_parameters);

            request.setAttribute("movie", movies.get(0));
            request.getRequestDispatcher("hover.jsp").forward(request, response);

        } catch (Exception e) {
            // Need a better Error Management system
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<HTML><HEAD><TITLE>Main Error</TITLE></HEAD>");
            out.println(e.toString());
            DataSource.logError("Hover doGet", e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}
