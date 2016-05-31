import moviedb_model.CreditCard;
import moviedb_model.Customer;
import moviedb_model.CartItem;
import moviedb_model.DataSource;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Purchase extends HttpServlet {
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        ArrayList<CartItem> shopping_cart = (ArrayList<CartItem>) session.getAttribute("shopping_cart");
        
        if (shopping_cart == null || shopping_cart.size() == 0) {
            session.setAttribute("error", "Nothing in your shopping cart");
            response.sendRedirect("shopping_cart");
            return;
        }

        String customer_id = ((Customer) session.getAttribute("customer")).id();
        String first_name = request.getParameter("first_name");
        String last_name = request.getParameter("last_name");
        String exp_date = request.getParameter("exp_date");
        String cc_id = request.getParameter("cc_id");

        boolean payment_accepted = CreditCard.check(first_name, last_name, exp_date, cc_id);
        if (!payment_accepted) {
            request.setAttribute("error", "Invalid creditcard information");
            request.getRequestDispatcher("checkout").forward(request, response);
            return;
        }

        // Get Timestamp
        Calendar calendar = Calendar.getInstance();
        int current_year = calendar.get(Calendar.YEAR),
        current_day = calendar.get(Calendar.DAY_OF_MONTH),
        current_month = calendar.get(Calendar.MONTH) + 1; // month start from 0 to 11
        String current_date = current_year + "/" + current_month + "/" + current_day;

        DataSource ds = new DataSource();

        try {
            ArrayList<String> statement_parameters;
            for (CartItem item : shopping_cart) {
                statement_parameters = new ArrayList<String>();
                for (int i = 0; i < item.quantity(); i++) {
                    String insert = "INSERT INTO sales (customer_id, movie_id, sale_date) VALUES (?, ?, " + current_date + ");";
                    statement_parameters.add(customer_id);
                    statement_parameters.add(item.movie_id());
                    ds.executeUpdate(insert, statement_parameters);
                }
            }
            session.setAttribute("shopping_cart", null);
            session.setAttribute("success", "Enjoy your movie purchases!");
            response.sendRedirect("main");
        } catch (Exception e) {
            session.setAttribute("error", "Unable to process your order, please try again later\n" + e.toString());
            response.sendRedirect("checkout");
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                
        doPost(request, response);
    }
}
