package moviedb_model;

import java.sql.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class DataModel {
    // JDBC driver name and database url
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql:///";

    private static final String DB = "moviedb";
    private static final String USR = "root";
    private static final String PASS = "root";

    public Connection conn = null;
    public PreparedStatement stmt = null;
    public ResultSet rs = null;

    public DataModel() {
        this.conn = getConnection(); 
    }
    
    // There should be a logger object to handle these! 
    public static void logError(String title, Exception e) {
        System.out.println(title);
        e.printStackTrace();
    }
   
    // Creates a mysql connection 
    public Connection getConnection() {
        try {
            Class.forName(JDBC_DRIVER).newInstance();

            return DriverManager.getConnection(DB_URL + DB, USR, PASS); 
        } catch (Exception e) {
            logError("ERROR: DataModel getConnection", e);
            return null;
        }
    }

    public void executeQuery(String query, ArrayList<String> statement_parameters) {
        try {
            // Print out the query we are using
            System.out.println("Query: " + query);
            
            // Declare our statement
            stmt = conn.prepareStatement(query);
            // Set and print the parameters for the query
            for (int i = 0; i < statement_parameters.size(); ++i) {
                System.out.println("param: " + statement_parameters.get(i));
                stmt.setString(i + 1, statement_parameters.get(i));
            }

            // Perform the query
            rs = stmt.executeQuery();

        } catch (SQLException se) {
            logError("ERROR: DataModel executeQuery", se);
        }
    }

    public int executeUpdate(String update, ArrayList<String> statement_parameters) {
        try {
            // Print out the query we are using
            System.out.println("Update: " + update);
            
            // Declare our statement
            stmt = conn.prepareStatement(update);
            // Set and print the parameters for the update
            for (int i = 0; i < statement_parameters.size(); ++i) {
                System.out.println("param: " + statement_parameters.get(i));
                stmt.setString(i + 1, statement_parameters.get(i));
            }

            // Perform the update and return the number of rows affected
            return stmt.executeUpdate();

        } catch (SQLException se) {
            logError("ERROR: DataModel executeUpdate", se);
        }
        return 0;
    }

    // Close all open connections
    public void closeQuery() {
        try {
            if (rs != null) { rs.close(); }
            if (stmt != null) { stmt.close(); }
            if (conn != null) { conn.close(); }
        } catch (SQLException se) {
            logError("ERROR: DataModel closeQuery", se);
        }
    }

    // Close the resultset and statment, leave the connection open for another query
    public void closeStatement() {
        try {
            if (rs != null) { rs.close(); }
            if (stmt != null) { stmt.close(); }
        } catch (SQLException se) {
            logError("ERROR: DataModel closeStatement", se);
        }
    }

    // Return the movies for the given query
    public static ArrayList<Movie> getMoviesForQuery(String query, ArrayList<String> statement_parameters) {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        // Manages opening/closing the connections to the database
        DataModel dm = new DataModel();
        // Open a connection and execute the query
        dm.executeQuery(query, statement_parameters);
        try {
            // If the query was not empty
            if (dm.rs.isBeforeFirst()) {
                while (dm.rs.next()) {
                    Movie movie = new Movie(dm.rs);
                    getMovieGenres(dm, movie);
                    getMovieStars(dm, movie);
                    movies.add(movie);
                }
            } else {
                return null;
            }
        } catch (SQLException se) {
            DataModel.logError("ERROR: Movie getMoviesForQuery", se);
        } finally {
            dm.closeQuery();
        }
        return movies;
    }

    private static void getMovieGenres(DataModel dm, Movie movie) {
        if (movie.id() == null) { return; }

        ArrayList<Genre> genres = new ArrayList<Genre>();
        String query = "SELECT * FROM genres WHERE id IN (SELECT genre_id FROM genres_in_movies WHERE movie_id=?);";
        ArrayList<String> statement_parameters = new ArrayList<String>();
        statement_parameters.add(movie.id());

        // Execute the query
        dm.executeQuery(query, statement_parameters);

        try {
            // If the query was not empty
            if (dm.rs.isBeforeFirst()) {
                while (dm.rs.next()) {
                    genres.add(new Genre(dm.rs));
                }
                movie.genres(genres);
            }
        } catch (SQLException se) {
            dm.logError("ERROR: DataModel getMovieGenres", se);
        } finally {
            // Close resultset and statement for genres
            dm.closeStatement();
        }
    }
    private static void getMovieStars(DataModel dm, Movie movie) {
        if (movie.id() == null) { return; }

        ArrayList<Star> stars = new ArrayList<Star>();
        String query = "SELECT * FROM stars WHERE id IN (SELECT star_id FROM stars_in_movies WHERE movie_id=?);";
        ArrayList<String> statement_parameters = new ArrayList<String>();
        statement_parameters.add(movie.id());

        // Open a connection and execute the query
        dm.executeQuery(query, statement_parameters);

        try {
            // If the query was not empty
            if (dm.rs.isBeforeFirst()) {
                while (dm.rs.next()) {
                    stars.add(new Star(dm.rs));
                }
                movie.stars(stars);
            }
        } catch (SQLException se) {
            dm.logError("ERROR: DataModel getMovieStars", se);
        } finally {
            // Close all open connections
            dm.closeStatement();
        }
    }


    // Return the movies for the given query
    public static ArrayList<Star> getStarsForQuery(String query, ArrayList<String> statement_parameters) {
        ArrayList<Star> stars = new ArrayList<Star>();
        // Manages opening/closing the connections to the database
        DataModel dm = new DataModel();
        // Open a connection and execute the query
        dm.executeQuery(query, statement_parameters);
        try {
            // If the query was not empty
            if (dm.rs.isBeforeFirst()) {
                while (dm.rs.next()) {
                    Star star = new Star(dm.rs);
                    getStarMovies(dm, star);
                    stars.add(star);
                }
            } else {
                return null;
            }
        } catch (SQLException se) {
            DataModel.logError("ERROR: DataModel getStarsForQuery", se);
        } finally {
            dm.closeQuery();
        }
        return stars;
    }

    private static void getStarMovies(DataModel dm, Star star) {
        if (star.id() == null) { return; }

        ArrayList<Movie> movies = new ArrayList<Movie>();
        // Do not need all the information for the SingleStar page, just the id and title
        String query = "SELECT id,title FROM movies WHERE id IN (SELECT movie_id FROM stars_in_movies WHERE star_id=?);";
        ArrayList<String> statement_parameters = new ArrayList<String>();
        statement_parameters.add(star.id());

        // Open a connection and execute the query
        dm.executeQuery(query, statement_parameters);

        try {
            // If the query was not empty
            if (dm.rs.isBeforeFirst()) {
                while (dm.rs.next()) {
                    movies.add(new Movie(dm.rs));
                }
                star.movies(movies);
            }
        } catch (SQLException se) {
            dm.logError("ERROR: DataModel getStarMovies", se);
        } finally {
            // Close all open connections
            dm.closeStatement();
        }
    }


    // Return the genres for the given query
    public static ArrayList<Genre> getGenresForQuery(String query, ArrayList<String> statement_parameters) {
        ArrayList<Genre> genres = new ArrayList<Genre>();
        // Manages opening/closing the connections to the database
        DataModel dm = new DataModel();
        // Open a connection and execute the query
        dm.executeQuery(query, statement_parameters);
        try {
            // If the query was not empty
            if (dm.rs.isBeforeFirst()) {
                while (dm.rs.next()) {
                    genres.add(new Genre(dm.rs));
                }
            } else {
                return null;
            }
        } catch (SQLException se) {
            DataModel.logError("ERROR: Movie getGenresForQuery", se);
        } finally {
            dm.closeQuery();
        }
        return genres;
    }

    // Return the number of table entries for the given query
    public static int getQueryCount(String query, ArrayList<String> statement_parameters) {
        int count = 0;
        // Manages opening/closing the connections to the database
        DataModel dm = new DataModel();
        // Open a connection and execute the query
        dm.executeQuery(query, statement_parameters);
        try {
            // If the query was not empty
            if (dm.rs.isBeforeFirst()) {
                dm.rs.next();
                count = dm.rs.getInt(1);
            }
        } catch (SQLException se) {
            DataModel.logError("ERROR: DataModel getQueryCount", se);
        } finally {
            dm.closeQuery();
        }
        return count;
    }
}
