package moviedb_model;

import java.sql.*;
import java.io.*;
import java.util.*;
import java.net.*;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DataModel {

    //@Resource(name="jdbc/moviedb", type=javax.sql.DataSource.class)

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
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");
            conn = ds.getConnection(); 
            return conn;
            //return ds.getConnection();
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
    public void closeConnection() {
        try {
            if (rs != null) { rs.close(); }
            if (stmt != null) { stmt.close(); }
            if (conn != null) { conn.close(); }
        } catch (SQLException se) {
            logError("ERROR: DataModel closeConnections", se);
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
    public ArrayList<Movie> getMoviesForQuery(String query, ArrayList<String> statement_parameters) {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        // Open a connection and execute the query
        executeQuery(query, statement_parameters);
        try {
            // If the query was not empty
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    Movie movie = new Movie(rs);
                    closeStatement();
                    getMovieGenres(movie);
                    closeStatement();
                    getMovieStars(movie);
                    movies.add(movie);
                }
            } else {
                return null;
            }
        } catch (SQLException se) {
            logError("ERROR: Movie getMoviesForQuery", se);
        }
        return movies;
    }

    private void getMovieGenres(Movie movie) {
        if (movie.id() == null) { return; }

        ArrayList<Genre> genres = new ArrayList<Genre>();
        String query = "SELECT * FROM genres WHERE id IN (SELECT genre_id FROM genres_in_movies WHERE movie_id=?);";
        ArrayList<String> statement_parameters = new ArrayList<String>();
        statement_parameters.add(movie.id());

        // Execute the query
        executeQuery(query, statement_parameters);

        try {
            // If the query was not empty
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    genres.add(new Genre(rs));
                }
                movie.genres(genres);
            }
        } catch (SQLException se) {
            logError("ERROR: DataModel getMovieGenres", se);
        }
    }
    private void getMovieStars(Movie movie) {
        if (movie.id() == null) { return; }

        ArrayList<Star> stars = new ArrayList<Star>();
        String query = "SELECT * FROM stars WHERE id IN (SELECT star_id FROM stars_in_movies WHERE movie_id=?);";
        ArrayList<String> statement_parameters = new ArrayList<String>();
        statement_parameters.add(movie.id());

        // Open a connection and execute the query
        executeQuery(query, statement_parameters);

        try {
            // If the query was not empty
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    stars.add(new Star(rs));
                }
                movie.stars(stars);
            }
        } catch (SQLException se) {
            logError("ERROR: DataModel getMovieStars", se);
        }
    }

    // Return the Stars for the given query
    public ArrayList<Star> getStarsForQuery(String query, ArrayList<String> statement_parameters) {
        ArrayList<Star> stars = new ArrayList<Star>();
        // Open a connection and execute the query
        executeQuery(query, statement_parameters);
        try {
            // If the query was not empty
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    Star star = new Star(rs);
                    closeStatement();
                    getStarMovies(star);
                    stars.add(star);
                }
            } else {
                return null;
            }
        } catch (SQLException se) {
            logError("ERROR: DataModel getStarsForQuery", se);
        }
        return stars;
    }

    private void getStarMovies(Star star) {
        if (star.id() == null) { return; }

        ArrayList<Movie> movies = new ArrayList<Movie>();
        // Do not need all the information for the SingleStar page, just the id and title
        String query = "SELECT id,title FROM movies WHERE id IN (SELECT movie_id FROM stars_in_movies WHERE star_id=?);";
        ArrayList<String> statement_parameters = new ArrayList<String>();
        statement_parameters.add(star.id());

        // Open a connection and execute the query
        executeQuery(query, statement_parameters);

        try {
            // If the query was not empty
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    movies.add(new Movie(rs));
                }
                star.movies(movies);
            }
        } catch (SQLException se) {
            logError("ERROR: DataModel getStarMovies", se);
        }
    }


    // Return the genres for the given query
    public ArrayList<Genre> getGenresForQuery(String query, ArrayList<String> statement_parameters) {
        ArrayList<Genre> genres = new ArrayList<Genre>();
        // Open a connection and execute the query
        executeQuery(query, statement_parameters);
        try {
            // If the query was not empty
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    genres.add(new Genre(rs));
                }
            } else {
                return null;
            }
        } catch (SQLException se) {
            logError("ERROR: DataModel getGenresForQuery", se);
        }
        return genres;
    }

    // Return the number of table entries for the given query
    public int getQueryCount(String query, ArrayList<String> statement_parameters) {
        int count = 0;
        // Open a connection and execute the query
        executeQuery(query, statement_parameters);
        try {
            // If the query was not empty
            if (rs.isBeforeFirst()) {
                rs.next();
                count = rs.getInt(1);
            }
        } catch (SQLException se) {
            logError("ERROR: DataModel getQueryCount", se);
        }
        return count;
    }

    // Return the genres for the given query
    public ArrayList<String> getStringsForQuery(String query, ArrayList<String> statement_parameters, String column) {
        ArrayList<String> strings = new ArrayList<String>();
        // Open a connection and execute the query
        executeQuery(query, statement_parameters);
        try {
            // If the query was not empty
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    strings.add(rs.getString(column));
                }
            } else {
                return null;
            }
        } catch (SQLException se) {
            logError("ERROR: DataModel getStringsForQuery", se);
        }
        return strings;
    }
}
