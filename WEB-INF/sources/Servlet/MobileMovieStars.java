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

public class MobileMovieStars extends HttpServlet
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

            ArrayList<Movie> movies = DataSource.getMoviesForQuery("SELECT * FROM movies WHERE id=?;",statement_parameters);

            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();
            String json = "[";
            for(Star star: movies.get(0).stars()) {
                json += star.toJSON() + ",";
            }
            json = json.substring(0, json.length() - 1);
            json += "]";
            writer.print(json);
            System.out.println("JSON: " + json);

        } catch (Exception e) {
            // Error
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }
}
