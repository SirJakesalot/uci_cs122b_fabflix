import moviedb_model.Movie;
import moviedb_model.Genre;
import moviedb_model.Star;
import moviedb_model.DataModel;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SingleMovie extends HttpServlet
{
    public String getServletInfo()
    {
       return "Single Movie";
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String movie_id = request.getParameter("movie_id");
            ArrayList<String> statement_parameters = new ArrayList<String>();
            statement_parameters.add(movie_id);

            DataModel dm = new DataModel();
            ArrayList<Movie> movies = dm.getMoviesForQuery("SELECT * FROM movies WHERE id=? LIMIT 1;",statement_parameters);
            dm.closeConnection();

            request.setAttribute("movie", movies.get(0));
            request.getRequestDispatcher("single_movie.jsp").forward(request, response);
        } catch (Exception e) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<HTML><HEAD><TITLE>Main Error</TITLE></HEAD>");
            out.println(e.toString());
            DataModel.logError("SingleStar doGet", e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }
}
