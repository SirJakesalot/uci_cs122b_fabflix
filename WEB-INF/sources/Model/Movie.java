package moviedb_model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Movie {
    // May need to may the id an int
    private String id;
    private String title;
    private String year;
    private String director;
    private String banner_url;
    private String trailer_url;

    private ArrayList<Star> stars;
    private ArrayList<Genre> genres;

    
    public Movie(String id, String title, String year, String director, String banner_url, String trailer_url) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;
        this.banner_url = banner_url;
        this.trailer_url = trailer_url;
    }

    public Movie(ResultSet rs) {
        try {
            this.id = rs.getString("id");
            this.title = rs.getString("title");
            this.year = rs.getString("year");
            this.director = rs.getString("director");
            this.banner_url = rs.getString("banner_url");
            this.trailer_url = rs.getString("trailer_url");
        } catch (SQLException se) {
            DataSource.logError("ERROR: Movie resultset", se);
        }
    }
    public Movie(String movie_id) {
        String query = "SELECT * FROM movies WHERE id=?;";
        // Construct query execution parameters
        ArrayList<String> statement_parameters = new ArrayList<String>();
        statement_parameters.add(movie_id);

        // Manages opening/closing the connections to the database
        DataSource ds = new DataSource();
        // Open a connection and execute the query
        ds.executeQuery(query, statement_parameters);
        try {
            // If the query was not empty
            if (ds.rs.isBeforeFirst()) {
                ds.rs.next();
                this.id = ds.rs.getString("id");
                this.title = ds.rs.getString("title");
                this.year = ds.rs.getString("year");
                this.director = ds.rs.getString("director");
                this.banner_url = ds.rs.getString("banner_url");
                this.trailer_url = ds.rs.getString("trailer_url");
            }
        } catch (SQLException se) {
            DataSource.logError("ERROR: Movie movie_id", se);
        } finally {
            ds.closeQuery();
        }
    }

    // Movie get functions
    public String id() { return id; }
    public String title() { return title; }
    public String year() { return year; }
    public String director() { return director; }
    public String banner_url() { return banner_url; }
    public String trailer_url() { return trailer_url; }

    public ArrayList<Star> stars() { return stars; }
    public ArrayList<Genre> genres() { return genres; }
    public String toJSON() {
        return "{\"id\":\"" + id() + "\"," +
                "\"title\":\"" + title() + "\"," +
                "\"year\":\"" + year() + "\"," +
                "\"director\":\"" + director() + "\"," +
                "\"banner_url\":\"" + banner_url() + "\"," +
                "\"trailer_url\":\"" + trailer_url() + "\"}";
    }

    // Movie set functions
    public String id(String new_id) { id = new_id; return id(); }
    public String title(String new_fn) { title = new_fn; return title(); }
    public String year(String new_ln) { year = new_ln; return year(); }
    public String director(String new_director) { director = new_director; return director(); }
    public String banner_url(String new_add) { banner_url = new_add; return banner_url(); }
    public String trailer_url(String new_trailer_url) { trailer_url = new_trailer_url; return trailer_url(); }

    public ArrayList<Star> stars(ArrayList<Star> new_stars) { stars = new_stars; return stars; }
    public ArrayList<Genre> genres(ArrayList<Genre> new_genres) { genres = new_genres; return genres; }

/*
    public static void main(String[] args) {
        // Movie2 m = new Movie2("1", "Forrest Gump", "2002", "Unknown", "", "");
        // System.out.println(m.title());
        // m.title("Terminator");
        // System.out.println(m.title());
        // System.out.println(m.getTableEntry());
        Movie.getBrowsePage("genre", "letter", "title", "first_name", "last_name", "director", "year_start", "year_end", 2, 10, "order", "order_by");
        System.out.println("");
        Movie.getBrowsePage(null, null, null, null, null, null, null, null, 2, 10, null, null);
        System.out.println("");
        Movie.getBrowsePage(null, "A", null, null, null, null, null, null, 1, 5, null, null);
    }
*/

}
