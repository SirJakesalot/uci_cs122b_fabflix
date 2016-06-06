package moviedb_model;
import java.sql.*;
import java.util.ArrayList;

public class Customer {
    private String id;
    private String first_name;
    private String last_name;
    private String cc_id;
    private String address;
    private String email;
    private String password;

    public Customer(String id, String fn, String ln, String cc_id, String add, String email, String pass) {
        this.id = id;
        this.first_name = fn;
        this.last_name = ln;
        this.cc_id = cc_id;
        this.address = add;
        this.email = email;
        this.password = pass;
    }

    public Customer(ResultSet rs) {
        try {
            this.id = rs.getString("id");
            this.first_name = rs.getString("first_name");
            this.last_name = rs.getString("last_name");
            this.cc_id = rs.getString("cc_id");
            this.address = rs.getString("address");
            this.email = rs.getString("email");
            this.password = rs.getString("password");
        } catch (SQLException se) {
            DataModel.logError("ERROR: Customer", se);
        }
    }

    // Customer get functions
    public String id() { return id; }
    public String first_name() { return first_name; }
    public String last_name() { return last_name; }
    public String cc_id() { return cc_id; }
    public String address() { return address; }
    public String email() { return email; }
    public String password() { return password; }

    // Customer set functions
    public String id(String new_id) { id = new_id; return id(); }
    public String first_name(String new_fn) { first_name = new_fn; return first_name(); }
    public String last_name(String new_ln) { last_name = new_ln; return last_name(); }
    public String cc_id(String new_cc_id) { cc_id = new_cc_id; return cc_id(); }
    public String address(String new_add) { address = new_add; return address(); }
    public String email(String new_email) { email = new_email; return email(); }
    public String password(String new_pass) { email = new_pass; return password(); }

    public static Customer login(String email, String password) {
        String query = "SELECT * FROM customers WHERE email=? AND BINARY password=? LIMIT 1;";
        ArrayList<String> statement_parameters = new ArrayList<String>();
        statement_parameters.add(email);
        statement_parameters.add(password);
        
        Customer customer = null;
        // Manages opening/closing the connections to the database
        DataModel dm = new DataModel();
        // Open a connection and execute the query
        dm.executeQuery(query, statement_parameters);
        
        try {
            // If the query was not empty
            if (dm.rs.isBeforeFirst()) {
                dm.rs.next();
                customer = new Customer(dm.rs);
            }
        } catch (SQLException se) {
            DataModel.logError("ERROR: Customer login", se);
        }
        dm.closeConnection();
        return customer;
    }

/*    
    public static void main(String[] args) {
        Customer c = new Customer("1", "Tom", "Hanks", "123456", "1234 marry lane", "th@email.com", "keyboard");
        System.out.println(c.first_name());
        c.first_name("john");
        System.out.println(c.first_name());
        System.out.println(c.getTableEntry());
    }
*/
}
