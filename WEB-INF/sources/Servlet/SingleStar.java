import moviedb_model.Star;
import moviedb_model.Movie;
import moviedb_model.DataSource;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SingleStar extends HttpServlet {
    public String getServletInfo() {
       return "Single Star";
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Star star = new Star(request.getParameter("star_id"));
            star.getMovies();

            request.setAttribute("star", star);
            request.getRequestDispatcher("single_star.jsp").forward(request,response);

        } catch (Exception e) {

            response.setContentType("text/html");    // Response mime type

            // Output stream to STDOUT
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
