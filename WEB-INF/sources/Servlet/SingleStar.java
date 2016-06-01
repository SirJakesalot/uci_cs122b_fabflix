import moviedb_model.Star;
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

            String star_id = request.getParameter("star_id");

            ArrayList<String> statement_parameters = new ArrayList<String>();
            statement_parameters.add(star_id);

            ArrayList<Star> stars = DataSource.getStarsForQuery("SELECT * FROM stars WHERE id=? LIMIT 1;",statement_parameters);

            request.setAttribute("star", stars.get(0));
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

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}
