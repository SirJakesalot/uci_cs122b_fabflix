import moviedb_model.CartItem;
import moviedb_model.Customer;
import java.util.ArrayList;
import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ShoppingCart extends HttpServlet
{
    public String getServletInfo()
    {
       return "Shopping Cart";
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession();

        // Get attributes
        String movie_id = request.getParameter("movie_id");
        String movie_title = request.getParameter("movie_title");
        String increment = request.getParameter("increment");
        String quantity_str = request.getParameter("quantity");

        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantity_str);
        } catch (Exception e) {
            // Invalid value for quantity_str
            quantity = -1;
        }

        // Create a cart if one does not already exist
        ArrayList<CartItem> shopping_cart = (ArrayList<CartItem>) session.getAttribute("shopping_cart");
        if (shopping_cart == null) {
             shopping_cart = new ArrayList<CartItem>();
        }

        // Only update if there is enough information for a change
        if ((increment != null || quantity >= 0) && movie_id != null && movie_title != null) {
            updateCart(shopping_cart, movie_id, movie_title, increment, quantity);
        }

        if (shopping_cart.isEmpty()) {
            session.setAttribute("error", "No items in cart");
        }
        session.setAttribute("shopping_cart", shopping_cart);
        request.getRequestDispatcher("shopping_cart.jsp").forward(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }

    // Will determine how to update the shopping cart
    //  - Increment their item quantity by one
    //  - Remove their item from the cart
    //  - Overwrite their item quantity
    private ArrayList<CartItem> updateCart(ArrayList<CartItem> shopping_cart, String movie_id, String movie_title, String increment, int quantity){
        // As long as the increment isn't 0 or null it is considered true
        boolean inc = increment != null && !increment.equals("0");

        for(int i = 0; i < shopping_cart.size(); ++i) {
            // If exists in the shopping cart
            if(movie_id.equals(shopping_cart.get(i).movie_id())) {
                // If only incrementing the count
                // Else change the quantity
                if (inc) {
                    shopping_cart.get(i).incrementQuantity();
                } else if (quantity == 0) {
                    shopping_cart.remove(i);
                } else {
                    shopping_cart.get(i).quantity(quantity);
                }
                return shopping_cart;
            }
        }
        // Item has not been seen before, add it with a quantity of 1
        CartItem item = new CartItem(movie_id, movie_title, 1);
        shopping_cart.add(item);
        return shopping_cart;
    }
}
