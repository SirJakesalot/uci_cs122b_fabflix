package moviedb_model;
import java.sql.*;
import java.util.ArrayList;

public class CreditCard {
    // May need to may the id an int
    private String id;
    private String first_name;
    private String last_name;
    private String expiration;

    public CreditCard(String id, String first_name, String last_name, String expiration) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.expiration = expiration;
    }

    public CreditCard(ResultSet rs) {
        try {
            this.id = rs.getString("id");
            this.first_name = rs.getString("first_name");
            this.last_name = rs.getString("last_name");
            this.expiration = rs.getString("expiration");
        } catch (SQLException se) {
            DataSource.logError("ERROR: Creditcard", se);
        }
    }
    
    // CreditCard get functions
    public String id() { return id; }
    public String first_name() { return first_name; }
    public String last_name() { return last_name; }
    public String expiration() { return expiration; }

    // CreditCard set functions
    public String id(String new_id) { id = new_id; return id(); }
    public String first_name(String new_first_name) { first_name = new_first_name; return first_name(); }
    public String last_name(String new_last_name) { last_name = new_last_name; return last_name(); }
    public String expiration(String new_expiration) { expiration = new_expiration; return expiration(); }

    public static boolean check(String first_name, String last_name, String experation_date, String cc_id) {
        String query = "SELECT * FROM creditcards WHERE id = ? AND first_name = ? AND last_name = ? AND expiration = ?";
        ArrayList<String> statement_parameters = new ArrayList<String>();
        statement_parameters.add(cc_id);
        statement_parameters.add(first_name);
        statement_parameters.add(last_name);
        statement_parameters.add(experation_date);

        // Manages opening/closing the connections to the database
        DataSource ds = new DataSource();
        // Open a connection and execute the query
        ds.executeQuery(query, statement_parameters);
        boolean payment_accepted = false;
        try {
            // Entry exists
            if (ds.rs.isBeforeFirst()) {
                payment_accepted = true;
            }
        } catch (SQLException se) {
            DataSource.logError("ERROR: CreditCard check", se);
        }
        ds.closeQuery();
        return payment_accepted;
    }
/*    
    public static void main(String[] args) {
        CreditCard c = new CreditCard("1", "Tom", "Hanks", "123456");
        System.out.println(c.first_name());
        c.first_name("John");
        System.out.println(c.first_name());
        System.out.println(c.getTableEntry());
    }
*/
}
