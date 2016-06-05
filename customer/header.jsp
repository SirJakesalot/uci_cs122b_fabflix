<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page session="true" %>
<%@ page isELIgnored="false" %>

<c:set var="context" value="${pageContext.request.contextPath}" />

<%@ page import="moviedb_model.Customer"%>
<%@ page import="moviedb_model.Movie"%>
<%@ page import="moviedb_model.Star"%>
<%@ page import="moviedb_model.Genre"%>
<%@ page import="moviedb_model.CartItem"%>
<%
    Customer customer = (Customer) request.getSession().getAttribute("customer");
    if (customer == null) {
        response.sendRedirect(request.getContextPath() + "/");
    }
%>

<html>
<head>
  <link rel="stylesheet" href="${context}/css/style.css" type="text/css" media="screen, projection"/>
  <script type='text/javascript' language="javascript" src="${context}/js/jquery.min.js"></script>
  <script type="text/javascript" language="javascript" src="${context}/js/jquery.dropdown.js"></script>
  <script type="text/javascript" language="javascript" src="${context}/js/hover.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
  <title>fabflix</title>
</head>
<body>
  <ul class="navigation">
    <li><a href="main">Main</a></li>
    <li style="float:right"><a href="${context}/logout">Logout</a></li>
    <li style="float:right"><a href="${context}/customer/shopping_cart">Shopping Cart</a></li>
    <li>
      <span class="anchor">
        <form action="browse" method="POST">
          <input type="text" name='title' class="search_box" onfocus="updatePage('${context}' + '/customer/autocomplete_search', 'autocomplete_title', 'autocomplete_box');" onkeyup="updatePage('${context}' + '/customer/autocomplete_search', 'autocomplete_title', 'autocomplete_box');" id="autocomplete_title" placeholder="movie title" autocomplete="off" style="min-width:260px;" onfocusout="hidebox('autocomplete_box')"/>
          <input type="submit" value="Search" class="search_box_submit" style="border-radius: 15px;font-size:1.1em;padding:3px;margin-left:5px;"/>
        </form>
          <span id="autocomplete_box" class="hide"  style="min-width: 260px; max-width:260px; position : absolute;background-color: #f9f9f9; box-shadow: 0px 8px 16px 8px rgba(0,0,0,0.2);margin-left: 10px;" ></span>
      </span>
    </li>
  </ul>
  <h3 class="success">${success}</h3>
  <h3 class="error">${error}</h3>
  <% session.setAttribute("success",""); %>
  <% session.setAttribute("error",""); %>
