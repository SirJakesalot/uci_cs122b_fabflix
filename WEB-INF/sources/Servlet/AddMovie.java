/* A servlet to display the contents of the MySQL movieDB database */

import moviedb_model.DataModel;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AddMovie extends HttpServlet {

    public String getServletInfo() {
        return "AddMovie";
    }

    // If http GET, populate an ArrayList with all Genre objects and redirect to the main.jsp page
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        try {
            // Create sql storage procedure
            String query = "CALL add_movie(?,?,?,?,?,?)";

            // Can assume these are there
            String movie_title = request.getParameter("movie_title");
            String movie_year_str = request.getParameter("movie_year");
            String movie_director = request.getParameter("movie_director");
            String star_first_name = request.getParameter("star_first_name");
            String star_last_name = request.getParameter("star_last_name");
            String genre_name = request.getParameter("genre_name");

            if (star_first_name == null || star_first_name.length() ==0) {
                star_first_name = "";
            }

            int movie_year = 0;

            try {
                movie_year = Integer.parseInt(movie_year_str);
            } catch (Exception e) {
                // Invalid Year
            }
            
            DataModel dm = new DataModel();
            Connection conn = dm.getConnection();

            CallableStatement call = conn.prepareCall("{CALL add_movie(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            call.setString(1, movie_title);
            call.setInt(2, movie_year);
            call.setString(3, movie_director);
            call.setString(4, star_first_name);
            call.setString(5, star_last_name);
            call.setString(6, genre_name);

            call.registerOutParameter(7, Types.INTEGER); // movie count
            call.registerOutParameter(8, Types.INTEGER); // star count
            call.registerOutParameter(9, Types.INTEGER); // genre count
            call.registerOutParameter(10, Types.INTEGER); // movie id
            call.registerOutParameter(11, Types.INTEGER); // star id
            call.registerOutParameter(12, Types.INTEGER); // genre id
            call.registerOutParameter(13, Types.INTEGER); // stars in movie
            call.registerOutParameter(14, Types.INTEGER); // genres in movies
        
            call.executeUpdate();

            session.setAttribute("movie_insert", call.getInt(7) > 0 ? "Already Existed" : "Added");
            session.setAttribute("star_insert", call.getInt(8) > 0 ? "Already Existed" : "Added");
            session.setAttribute("genre_insert", call.getInt(9) > 0 ? "Already Existed" : "Added");

            session.setAttribute("movie_id", call.getInt(10));
            session.setAttribute("star_id", call.getInt(11));
            session.setAttribute("genre_id", call.getInt(12));

            session.setAttribute("star_movie_link", call.getInt(13) != 0 ? "Star already linked to Movie" : "Added Star to Movie link");
            session.setAttribute("genre_movie_link", call.getInt(14) != 0 ? "Genre already linked to Movie" : "Added Genre to Movie link");

            dm.closeConnection();
            response.sendRedirect("employee/_dashboard");

        } catch (Exception e) {
            session.setAttribute("error", "Error: " + e.toString());
        } 
    }

    // Same as http GET
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}
