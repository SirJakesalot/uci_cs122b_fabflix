package moviedb_model;
import java.sql.*;
import java.util.ArrayList;

public class Employee {
    private String email;
    private String password;
    private String fullname;

    public Employee(String email, String password, String fullname) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
    }

    public Employee(ResultSet rs) {
        try {
            this.email = rs.getString("email");
            this.password = rs.getString("password");
            this.fullname = rs.getString("fullname");
        } catch (SQLException se) {
            DataSource.logError("ERROR: Employee", se);
        }
    }

    // Employee get functions
    public String email() { return email; }
    public String password() { return password; }
    public String fullname() { return fullname; }

    // Employee set functions
    public String email(String new_email) { email = new_email; return email(); }
    public String password(String new_pass) { email = new_pass; return password(); }
    public String fullname(String new_fn) { fullname = new_fn; return fullname(); }

    public static Employee login(String email, String password) {
        String query = "SELECT * FROM employees WHERE email=? AND BINARY password=? LIMIT 1;";
        ArrayList<String> statement_parameters = new ArrayList<String>();
        statement_parameters.add(email);
        statement_parameters.add(password);
        
        Employee e = null;
        // Manages opening/closing the connections to the database
        DataSource ds = new DataSource();
        // Open a connection and execute the query
        ds.executeQuery(query, statement_parameters);
        
        try {
            // If the query was not empty
            if (ds.rs.isBeforeFirst()) {
                //System.out.println("ResultSet is not empty");
                ds.rs.next();
                e = new Employee(ds.rs);
            }
        } catch (SQLException se) {
            DataSource.logError("ERROR: Employee login", se);
        } finally {
            ds.closeQuery();
        }
        
        return e;
    }

/*    
    public static void main(String[] args) {
        Employee c = new Employee("1", "Tom", "Hanks", "123456", "1234 marry lane", "th@email.com", "keyboard");
        System.out.println(c.fullname());
        c.fullname("john");
        System.out.println(c.fullname());
        System.out.println(c.getTableEntry());
    }
*/
}
