import moviedb_model.CartItem;
import moviedb_model.Employee;
import moviedb_model.DataModel;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Checkout extends HttpServlet {

    public String getServletInfo() {
        return "Checkout authentication";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            // Get the session if it already exist
            HttpSession session = request.getSession(false);
            // Get the shopping cart from the session
            ArrayList<CartItem> shopping_cart = (ArrayList<CartItem>)session.getAttribute("shopping_cart");

            if (shopping_cart == null || shopping_cart.size() == 0) {
                session.setAttribute("error", "You have no items in your cart!");
                response.sendRedirect("shopping_cart");
                return;
            }

            request.getRequestDispatcher("/customer/checkout.jsp").forward(request,response);

        } catch (Exception e) {
            request.setAttribute("error", e.toString());
            request.getRequestDispatcher("").forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}
