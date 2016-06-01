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

    // Star get functions
    public String id() { return id; }
    public String first_name() { return first_name; }
    public String last_name() { return last_name; }
    public String dob() { return dob; }
    public String photo_url() { return photo_url; }

    public ArrayList<Movie> movies() { return movies; }
    public String toJSON() {
        return "{\"id\":\"" + id() + "\"," +
                "\"first_name\":\"" + first_name() + "\"," +
                "\"last_name\":\"" + last_name() + "\"," +
                "\"dob\":\"" + dob() + "\"," +
                "\"photo_url\":\"" + photo_url() + "\"}";
    }

    // Star set functions
    public String id(String new_id) { id = new_id; return id(); }
    public String first_name(String new_first_name) { first_name = new_first_name; return first_name(); }
    public String last_name(String new_last_name) { last_name = new_last_name; return last_name(); }
    public String dob(String new_dob) { dob = new_dob; return dob(); }
    public String photo_url(String new_photo_url) { photo_url = new_photo_url; return photo_url(); }

    public ArrayList<Movie> movies(ArrayList<Movie> new_movies) { movies = new_movies; return movies; }

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
