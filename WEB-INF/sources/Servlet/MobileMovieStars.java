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

    public String getMovieStarsJSON(Star star) {
        return "{\"id\":\"" + star.id() + "\"," +
                "\"first_name\":\"" + star.first_name() + "\"," +
                "\"last_name\":\"" + star.last_name() + "\"," +
                "\"dob\":\"" + star.dob() + "\"," +
                "\"photo_url\":\"" + star.photo_url() + "\"}";
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            
            Movie movie = new Movie(request.getParameter("movie_id"));
            ArrayList<Star> stars = movie.getStars();

            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();
            String json = "[";
            for(Star star: stars) {
                json += getMovieStarsJSON(star) + ",";
            }
            json = json.substring(0, json.length() - 1);
            json += "]";
            writer.print(json);
            System.out.println(json);

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
