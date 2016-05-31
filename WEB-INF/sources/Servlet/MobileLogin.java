import moviedb_model.Customer;
import moviedb_model.DataSource;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class MobileLogin extends HttpServlet {
    private static final String failure = "[{\"success\":\"false\"}]";
    private static final String success = "[{\"success\":\"true\"}]";

    public String getServletInfo() {
        return "Mobile Login authentication";
    }

    // Redirects because GET requests aren't allowed for login verification
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.print(failure);
        System.out.println("did a get response");
    }

    // Authenticates user input from login.jsp
    // If correct login information, create a session with a customer/employee object and redirect
    // If incorrect, redirect back to the login page with an error message
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Grab POST information
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        /*
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        boolean valid = VerifyUtils.verify(gRecaptchaResponse);
        if (!valid) {
            request.setAttribute("error", "Recaptcha Error");
            request.getRequestDispatcher("").forward(request, response);
            return;
        }*/
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        
        try {

            /*
            IF  the user signaled they were an employee
                - verify employee credentials
                - forward based on verification
            ELSE same thing but for customer
            */

            // Returns null if invalid credentials
            Customer customer = Customer.login(email, password);

            if (customer == null) {
                writer.print(failure);
            } else {
                writer.print(success);
            }
        } catch (Exception e) { // in case?
            writer.print(failure);
        }
    }
}
