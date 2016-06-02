import moviedb_model.Customer;
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
public class Browse extends HttpServlet {

    public String getServletInfo() {
       return "Browse";
    }

    // Get all of the search attributes and construct a query based on those attributes. Then show movies that are relevant to that query.
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        try {
            // Get parameters
            String genre = request.getParameter("genre");
            String letter = request.getParameter("letter");
            String title = request.getParameter("title");
            String first_name = request.getParameter("first_name");
            String last_name = request.getParameter("last_name");
            String director = request.getParameter("director");
            String year_start = request.getParameter("year_start");
            String year_end = request.getParameter("year_end");
            String pg = request.getParameter("page");
            String pg_size = request.getParameter("page_size");
            String order = request.getParameter("order");
            String order_by = request.getParameter("order_by");

            // Set default page and page size
            int page;
            int page_size;
            try { page = Integer.parseInt(pg); } catch (Exception e) { page = 1; }
            try { page_size = Integer.parseInt(pg_size); } catch (Exception e) { page_size = 5; }

            // Base query to get the movies for the given page attributes
            String movie_query = "SELECT * FROM movies";
            // Base query for getting the count all the movies for the given page attributes
            String movie_count_query = "SELECT COUNT(*) FROM movies";

            // All the search requirements defined by the page GET request
            String search_requirements = "";
            // List containing the values for the query's prepared statement
            ArrayList<String> statement_parameters = new ArrayList<String>();

            // Collects the search requirements
            search_requirements += movieIdsByTitle(statement_parameters, title, letter);
            search_requirements += movieIdsByDirector(statement_parameters, director);
            search_requirements += movieIdsByStar(statement_parameters, first_name,last_name);
            search_requirements += movieIdsByYear(statement_parameters, year_start, year_end);
            search_requirements += movieIdsByGenre(statement_parameters, genre);

            // Will remove trailing " AND id IN "
            if (!statement_parameters.isEmpty()) {
                search_requirements = search_requirements.substring(0, search_requirements.length() - 11);
                // Prepare for search requirements
                movie_query += " WHERE id IN ";
                movie_count_query += " WHERE id IN ";
            }

            movie_count_query += search_requirements;
            // Executes the query to get the count
            DataModel dm = new DataModel();
            int movie_count = dm.getQueryCount(movie_count_query, statement_parameters);
            // Close resultset and statement
            dm.closeStatement();

            // Add the order
            search_requirements += movieIdsOrder(order_by, order);

            // Get movies for the current page
            int page_count = getPageCount(movie_count, page_size);
            if (page > page_count) {
                page = page_count;
            }
            int current_page = getCurrentPage(page, page_count);
            int previous_page = getPreviousPage(page);
            int next_page = getNextPage(page, page_count);

            // Add limit and offset to get the movies for this page
            search_requirements += movieIdsRange(page, page_size, movie_count);

            // Query for all the movies that are to be shown for this page
            movie_query += search_requirements;

            // Get the movies for the page and their associated stars
            ArrayList<Movie> movies = dm.getMoviesForQuery(movie_query, statement_parameters);
            // Close all open connections
            dm.closeConnection();

            // The search requirements given they had no results
            if (movies == null) {
                request.setAttribute("error", "No movie results");
            } else {
                String base_url = getBasePageURL(request);
                request.setAttribute("movies", movies);
                request.setAttribute("first_page", getPageURL(base_url, 1, page_size, order, order_by));
                request.setAttribute("previous_page", getPageURL(base_url, previous_page, page_size, order, order_by));
                request.setAttribute("next_page", getPageURL(base_url, next_page, page_size, order, order_by));
                request.setAttribute("last_page", getPageURL(base_url, page_count, page_size, order, order_by));

                request.setAttribute("five", getPageURL(base_url, 1, 5, order, order_by));
                request.setAttribute("ten", getPageURL(base_url, 1, 10, order, order_by));
                request.setAttribute("twenty", getPageURL(base_url, 1, 20, order, order_by));

                request.setAttribute("title_asc", getPageURL(base_url, page, page_size, "ASC", "title"));
                request.setAttribute("title_desc", getPageURL(base_url, page, page_size, "DESC", "title"));
                request.setAttribute("year_asc", getPageURL(base_url, page, page_size, "ASC", "year"));
                request.setAttribute("year_desc", getPageURL(base_url, page, page_size, "DESC", "year"));
            }
        } catch (Exception e) {
            request.setAttribute("error", e.toString());
            e.printStackTrace();
        }
        request.getRequestDispatcher("browse.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }

    // Used for the pageation feature. For 'First Page', 'Next Page', etc
    // Builds the full url for a given page size and order attributes
    private String getPageURL(String base_url, int page, int page_size, String order, String order_by) {
        // Base URL construct
        String url = base_url;
        // If an order has been chosen
        if(order!=null && order_by!=null){
            url += "order="+order+"&order_by="+order_by+"&";
        }
        url += "page="+page+"&page_size="+page_size;
        return url;
    }

    // Builds the base url for a page, given the search attribute
    private String getBasePageURL(HttpServletRequest request) {
        // Base URL construct
        String url = request.getRequestURI() + "?";
        for(Map.Entry<String, String[]> entry : ((Map<String, String[]>)request.getParameterMap()).entrySet()) {
            String name = entry.getKey();
            String value[] = entry.getValue();
            for (String v : value){
                // Omit anything to do with page size and order
                if(!name.equals("page") && !name.equals("page_size") && !name.equals("order") && !name.equals("order_by")){
                    String val = v.replaceAll(" ", "+");
                    url += name + "=" + val + "&";
                }
            }
        }
        return url;
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

    private static String movieIdsByTitle(ArrayList<String> statement_parameters, String title, String letter) {
        String query = "(SELECT id FROM movies WHERE title LIKE ?";
        // If title exists
        if (title != null && title.length() != 0) {
            statement_parameters.add("%" + title + "%");
            /*
            for (int i = title.length(); i > 5; --i) {
                query += " OR title LIKE ?";
                statement_parameters.add("%" + title.substring(0, i-1) + "%");
            }*/
        } else if (letter != null && letter.length() != 0) {
            statement_parameters.add(letter + "%");
        } else {
            // Neither a title nor a letter was provided
            return "";
        }
        return query + ") AND id IN ";
    }
    private static String movieIdsByDirector(ArrayList<String> statement_parameters, String director) {
        if(director != null && director.length() != 0) {
            statement_parameters.add(director);
            return "(SELECT id FROM movies WHERE director LIKE '%?%') AND id IN ";
        }
        return "";
    }

    private static String movieIdsByStar(ArrayList<String> statement_parameters, String first_name, String last_name) {
        // Getting all the movies for the given star names
        // first name exists
        boolean fn = first_name != null && first_name.length() != 0;
        // last name exists
        boolean ln = last_name != null && last_name.length() != 0;
        // if neither exist, exit
        if (!fn && !ln) { return ""; }
        String query = "(SELECT id FROM movies WHERE id IN (SELECT movie_id FROM stars_in_movies WHERE star_id IN (SELECT id FROM stars WHERE ";
        if (fn) {
            query += "first_name LIKE ?";
            statement_parameters.add("%" + first_name + "%");
        }
        if (fn && ln) {
            query += " AND ";
        }
        if (ln) {
            query += "last_name LIKE ?";
            statement_parameters.add("%" + last_name + "%");
        }
        return query + "))) AND id IN ";
    }

    private static String movieIdsByYear(ArrayList<String> statement_parameters, String year_start, String year_end) {
        // start year exists
        boolean start = year_start != null && year_start.length() != 0;
        // end year exists
        boolean end = year_end != null && year_end.length() != 0;

        // If neither exist, exit
        if (!start && !end) { return ""; }
        String query = "(SELECT id FROM movies WHERE year ";
        if (start) {
            query += " >= ?";
            statement_parameters.add(year_start);
        }
        if (start && end) {
            query += " AND ";
        }
        if (end) {
            query += " <= ?";
            statement_parameters.add(year_end);
        }
        return query + ") AND id IN ";
    }

    private static String movieIdsByGenre(ArrayList<String> statement_parameters, String genre) {
        String query = "(SELECT id FROM movies WHERE id IN (SELECT movie_id FROM genres_in_movies WHERE genre_id in (SELECT id FROM genres WHERE name=";
        boolean genre_exists = genre != null && genre.length() != 0;
        if (genre_exists) {
            statement_parameters.add(genre);
            return  "(SELECT id FROM movies WHERE id IN (SELECT movie_id FROM genres_in_movies WHERE genre_id in (SELECT id FROM genres WHERE name=?))) AND id IN ";
        } else { return ""; }
    }

    private static String movieIdsOrder(String order_by, String order) {
        // Manual sanitation because preparedStatements don't accept columns as strings
        boolean ordr_by = (order_by != null && (order_by.equalsIgnoreCase("title") || order_by.equalsIgnoreCase("year")));
        // Manual sanitation because preparedStatements don't accept orders as strings
        boolean ordr = (order != null && (order.equalsIgnoreCase("asc") || order.equalsIgnoreCase("desc")));

        if (ordr_by && ordr) {
            return " ORDER BY " + order_by + " " + order;
        } else {
            // Error: one is null and the other is not
        }
        return "";
    }

    private static String movieIdsRange(int page, int page_size, int movie_count) {
        // Can assume that numbers are sql injection safe
        if (movie_count > 0) {
            return " LIMIT " + Integer.toString(page_size) + " OFFSET " + Integer.toString((page - 1) * page_size);
        }
        return "";
    }
}
