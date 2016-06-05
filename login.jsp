<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="moviedb_model.Customer"%>
<%@ page import="moviedb_model.Employee"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page session="true" %>
<%@page isELIgnored="false"%>
<html>
<head>
  <link rel="stylesheet" href="css/style.css" type="text/css" media="screen, projection"/>
  <script type='text/javascript' language="javascript" src="js/jquery.min.js"></script>
  <script type="text/javascript" language="javascript" src="js/jquery.dropdown.js"></script>
  <script src='https://www.google.com/recaptcha/api.js'></script>
  <title>fabflix</title>
</head>
<%
    System.out.println("THIS IS THE SLAVE: login.jsp");
    Customer customer = (Customer) request.getSession().getAttribute("customer");
    if (customer != null) {
	System.out.println(request.getContextPath() + "/customer/main");
        response.sendRedirect(request.getContextPath() + "/customer/main");
    }

    Employee employee = (Employee) request.getSession().getAttribute("employee");
    if (employee != null) {
        response.sendRedirect(request.getContextPath() + "/_dashboard");
    }
%>
<body>
  <ul class="navigation">
    <li style="float:right"><a href="">Login</a></li>
  </ul>
  <h3 class="error">${error}</h3>
  <%session.setAttribute("error","");%>
  <c:set var="context" value="${pageContext.request.contextPath}" />
  <form action="${context}/login" method="post">
    <fieldset class="field_entry" style="width:50%">
      <legend>Login</legend>
        <table>
          <tr>
            <th>Email</th>
            <td><input type="text" name="email" required="required" value="jack@ics.com"/></td>
          </tr>
          <tr>
            <th>Password</td>
            <td><input type="password" name="password" required="required" value="jack" /></td>
          </tr>
          <tr>
            <th>Are you an employee?</th>
            <td><input type="checkbox" name="is_employee" value="yes"/></td>
        </table>
        <input type="submit" value="Login" class="submit"/></td>
    </fieldset>
    <br>
    <center><div class="g-recaptcha" data-sitekey="6Lc9OR8TAAAAAJ5bPCSXNOFgxzqFspLgS3cTkMZB"></div></center>
  </form>
</body>
</html>
<%@ include file="customer/footer.jsp" %>  
