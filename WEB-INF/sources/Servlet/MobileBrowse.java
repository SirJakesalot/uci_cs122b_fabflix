import moviedb_model.Star;
import moviedb_model.Genre;
import moviedb_model.Movie;
import moviedb_model.DataModel;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

// Builds a query based on the user's search specifications, then shows those movies
public class MobileBrowse extends HttpServlet {

    public String getServletInfo() {
       return "Mobile Browse";
    }

    // Get all of the search attributes and construct a query based on those attributes. Then show movies that are relevant to that query.
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        try {
            // Get parameters
            String title = request.getParameter("title");
            String pg = request.getParameter("page");

            // Set default page and page size
            int page;
            int page_size = 5;
            try { page = Integer.parseInt(pg); } catch (Exception e) { page = 1; }

            // Base query to get the movies for the given page attributes
            String movie_query = "SELECT * FROM movies WHERE id IN (SELECT id FROM movies WHERE title LIKE ?";
            String movie_count_query = "SELECT COUNT(*) FROM movies WHERE id IN (SELECT id FROM movies WHERE title LIKE ?";
            ArrayList<String> statement_parameters = new ArrayList<String>();
            statement_parameters.add("%" + title + "%");
            for (int i = title.length(); i > 5; --i) {
                movie_query += " OR title LIKE ?";
                movie_count_query += " OR title LIKE ?";
                statement_parameters.add("%" + title.substring(0, i-1) + "%");
            }
            movie_query += ")";
            movie_count_query += ")";

            DataModel dm = new DataModel();
            int movie_count = dm.getQueryCount(movie_count_query, statement_parameters);
            dm.closeStatement();

            // Get movies for the current page
            int page_count = getPageCount(movie_count, page_size);
            if (page > page_count) {
                page = page_count;
            }
            int current_page = getCurrentPage(page, page_count);
            int previous_page = getPreviousPage(page);
            int next_page = getNextPage(page, page_count);

            // Add limit and offset to get the movies for this page
            movie_query += movieIdsRange(page, page_size, movie_count);

            // Get the movies for the page and their associated stars
            ArrayList<Movie> movies = dm.getMoviesForQuery(movie_query, statement_parameters);
            dm.closeConnection();
            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();
            String json = "[{\"movie_count\":\"" + movie_count + "\"}";
            for(Movie movie: movies) {
                json += "," + movie.toJSON();
            }
            json += "]";
            writer.print(json);
            
        } catch (Exception e) {
            request.setAttribute("error", e.toString());
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }

    // Calculate page count
    private static int getPageCount(int movie_count, int page_size) {
        int page_count = (int)Math.ceil((float)movie_count/page_size);
        // Must have at least 1 page
        if (page_count == 0) {
            page_count = 1;
        }
        return page_count;
    }
    // Ensure 1 < page < page_count
    private static int getCurrentPage(int page, int page_count) {
        if (page > page_count) {
            return page_count;
        } else if(page <= 0) {
            return 1;
        }
        return page;
    }
    // Ensure previous page > 1
    private static int getPreviousPage(int page) {
        int prev_page = page - 1;
        if(prev_page <= 1){
            return page;
        }
        return prev_page;
    }
    // Ensure next page <= page_count
    private static int getNextPage(int page, int page_count) {
        int next_page = page + 1;
        if (next_page > page_count) {
            return page;
        }
        return next_page;
    }

    private static String movieIdsRange(int page, int page_size, int movie_count) {
        // Can assume that numbers are sql injection safe
        if (movie_count > 0) {
            return " LIMIT " + Integer.toString(page_size) + " OFFSET " + Integer.toString((page - 1) * page_size);
        }
        return "";
    }
}
