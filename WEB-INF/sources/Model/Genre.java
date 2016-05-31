package moviedb_model;

import java.sql.*;
import java.util.ArrayList;

public class Genre {
    // May need to may the id an int
    private String id;
    private String name;

    public Genre(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public Genre(ResultSet rs) {
        try {
            this.id = rs.getString("id");
            this.name = rs.getString("name");
        } catch (SQLException se) {
            DataSource.logError("ERROR: Genre", se);
        }
    }

    // Genre get functions
    public String id() { return id; }
    public String name() { return name; }

    // Genre set functions
    public String id(String new_id) { id = new_id; return id(); }
    public String name(String new_name) { name = new_name; return name(); }

/*    
    public static void main(String[] args) {
        Genre g = new Genre("1", "Comedy");
        System.out.println(g.name());
        s.name("Horror");
        System.out.println(g.name());
        System.out.println(g.getTableEntry());
    }
*/
}
