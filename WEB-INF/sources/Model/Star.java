package moviedb_model;

import java.sql.*;
import java.util.ArrayList;

public class Star {
    // May need to may the id an int
    private String id;
    private String first_name;
    private String last_name;
    private String dob;
    private String photo_url;

    private ArrayList<Movie> movies;

    public Star(String id, String first_name, String last_name, String dob, String photo_url) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.dob = dob;
        this.photo_url = photo_url;
    }

    public Star(ResultSet rs) {
        try {
            this.id = rs.getString("id");
            this.first_name = rs.getString("first_name");
            this.last_name = rs.getString("last_name");
            this.dob = rs.getString("dob");
            this.photo_url = rs.getString("photo_url");
        } catch (SQLException se) {
            DataSource.logError("ERROR: Star", se);
        }
    }

    public Star(String star_id) {
        String query = "SELECT * FROM stars WHERE id=?;";
        ArrayList<String> statement_parameters = new ArrayList<String>();
        statement_parameters.add(star_id);

        // Manages opening/closing the connections to the database
        DataSource ds = new DataSource();
        // Open a connection and execute the query
        ds.executeQuery(query, statement_parameters);
        
        try {
            // If the query was not empty
            if (ds.rs.isBeforeFirst()) {
                ds.rs.next();
                this.id = ds.rs.getString("id");
                this.first_name = ds.rs.getString("first_name");
                this.last_name = ds.rs.getString("last_name");
                this.dob = ds.rs.getString("dob");
                this.photo_url = ds.rs.getString("photo_url");
            }
        } catch (SQLException se) {
            DataSource.logError("ERROR: Star star_id", se);
        } finally {
            ds.closeQuery();
        }
    }

    // Star get functions
    public String id() { return id; }
    public String first_name() { return first_name; }
    public String last_name() { return last_name; }
    public String dob() { return dob; }
    public String photo_url() { return photo_url; }

    public ArrayList<Movie> movies() { return movies; }

    // Star set functions
    public String id(String new_id) { id = new_id; return id(); }
    public String first_name(String new_first_name) { first_name = new_first_name; return first_name(); }
    public String last_name(String new_last_name) { last_name = new_last_name; return last_name(); }
    public String dob(String new_dob) { dob = new_dob; return dob(); }
    public String photo_url(String new_photo_url) { photo_url = new_photo_url; return photo_url(); }

    // Return Star from http GET request

    // Get ArrayList of all Movies a Star has appeared in
    public ArrayList<Movie> getMovies() {
        if (this.id() == null) { return null; }

        this.movies = new ArrayList<Movie>();
        String query = "SELECT * FROM movies WHERE id IN (SELECT movie_id FROM stars_in_movies WHERE star_id=?);";
        ArrayList<String> statement_parameters = new ArrayList<String>();
        statement_parameters.add(this.id());

        // Manages opening/closing the connections to the database
        DataSource ds = new DataSource();
        // Open a connection and execute the query
        ds.executeQuery(query, statement_parameters);

        try {
            // If the query was not empty
            if (ds.rs.isBeforeFirst()) {
                while (ds.rs.next()) {
                    this.movies.add(new Movie(ds.rs));
                }
            }
        } catch (SQLException se) {
            DataSource.logError("ERROR: Star getStarMovies ", se);
        } finally {
            // Close all open connections
            ds.closeQuery();
        }

        return movies;
    }
/*    
    public static void main(String[] args) {
        Star s = new Star("1", "Tom", "Hanks", "123456", "https://website.image");
        System.out.println(s.first_name());
        s.first_name("john");
        System.out.println(s.first_name());
        System.out.println(s.getTableEntry());
    }
*/
}
