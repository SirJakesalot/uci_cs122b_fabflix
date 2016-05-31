package moviedb_model;
import java.sql.*;
import java.util.ArrayList;

public class CreditCard {
    // Checks for valid creditcard information
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
        // If an entry exists with that information, payment is acceptable
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
}
