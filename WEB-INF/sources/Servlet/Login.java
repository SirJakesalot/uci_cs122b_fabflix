import moviedb_model.Customer;
import moviedb_model.Employee;
import moviedb_model.DataModel;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Login extends HttpServlet {

    public String getServletInfo() {
        return "Login authentication";
    }

    // Redirects because GET requests aren't allowed for login verification
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("").forward(request, response);
    }

    // Authenticates user input from login.jsp
    // If correct login information, create a session with a customer/employee object and redirect
    // If incorrect, redirect back to the login page with an error message
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Grab POST information
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String is_employee = request.getParameter("is_employee");
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        //boolean valid = VerifyUtils.verify(gRecaptchaResponse);
        boolean valid = true;

        if (!valid) {
            request.setAttribute("error", "Recaptcha Error");
            request.getRequestDispatcher("").forward(request, response);
            return;
        }
        
        try {

            /*
            IF  the user signaled they were an employee
                - verify employee credentials
                - forward based on verification
            ELSE same thing but for customer
            */
            if ("yes".equals(is_employee)) {
                Employee employee = Employee.login(email, password);
                // Returns null if invalid credentials
                if (employee == null) {
                    request.setAttribute("error", "Invalid Username or Password");
                    request.getRequestDispatcher("").forward(request, response);
                } else {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("employee", employee);
                    response.sendRedirect(request.getContextPath() + "/employee/_dashboard");
                }
            } else {
                // Returns null if invalid credentials
                Customer customer = Customer.login(email, password);

                if (customer == null) {
                    request.setAttribute("error", "Invalid Username or Password");
                    request.getRequestDispatcher("").forward(request, response);
                } else {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("customer", customer);
                    response.sendRedirect(request.getContextPath() + "/customer/main");
                }
            }
        } catch (Exception e) { // in case?
            request.setAttribute("error", e.toString());
            request.getRequestDispatcher("").forward(request, response);
        }
    }
}
