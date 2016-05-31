<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true" %>
<%@ page isELIgnored="false"%>

<%@ page import="moviedb_model.Employee"%>

<c:set var="context" value="${pageContext.request.contextPath}" />

<html>
<head>
  <link rel="stylesheet" href="${context}/css/style.css" type="text/css" media="screen, projection"/>
  <script type='text/javascript' language="javascript" src="${context}/js/jquery.min.js"></script>
  <script type="text/javascript" language="javascript" src="${context}/js/jquery.dropdown.js"></script>
  <title>fabflix</title>
</head>
<%
    Employee employee = (Employee) request.getSession().getAttribute("employee");
    if (employee == null) {
        response.sendRedirect("");
    }
%>
<body>
  <ul class="navigation">
    <li style="float:right"><a href="${context}/logout">Logout</a></li>
  </ul>
  <h3 class="success">${success}</h3>
  <h3 class="error">${error}</h3>
  <% session.setAttribute("success",""); %>
  <% session.setAttribute("error",""); %>
