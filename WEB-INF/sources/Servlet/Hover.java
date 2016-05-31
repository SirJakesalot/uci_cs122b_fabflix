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
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Movie movie = new Movie(request.getParameter("movie_id"));
            System.out.println("movie_id = " + movie.id());
            movie.getGenres();
            movie.getStars();

            request.setAttribute("movie", movie);
            request.getRequestDispatcher("hover.jsp").forward(request, response);

        } catch (Exception e) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<HTML><HEAD><TITLE>Main Error</TITLE></HEAD>");
            out.println(e.toString());
            DataSource.logError("SingleStar doGet", e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }
}
