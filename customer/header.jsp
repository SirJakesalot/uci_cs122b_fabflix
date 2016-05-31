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
        response.sendRedirect("");
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
    <form action="browse" method="get">
      <div style="float:left; position : relative;" onfocusout="focusOut();" onfocus="focusOn();">
        <input type="text" name='title' class="search_box" onfocus="ajaxFunction();" onkeyup="ajaxFunction();" id="autocomplete" placeholder="movie title" autocomplete="off" style="min-width:260px;"/>
        <div class="dropContents" style="min-width: 260px; max-width:260px; position : absolute;background-color: #f9f9f9; box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);" id="dropdown_content"></div>
        <style id='dropdown_display'>
          .dropContents{display:none;}
        </style>
        <style>
          .dropContents p:hover {background-color: orange;}
          .select{ cursor: pointer; padding: 10px 0px 10px 0px; margin : 0cm 0cm 0cm 0cm}
        </style>
      </div>
      <input type="submit" value="Search" class="search_box_submit" style="border-radius: 15px;font-size:1.1em;padding:3px;margin-left:5px;"/>
    </form>
    </li>
            
    <script language="javascript" type="text/javascript">
    //Browser Support Code

    function ajaxFunction(){
        var ajaxRequest;  // The variable that makes Ajax possible!
        try{
            // Opera 8.0+, Firefox, Safari
            ajaxRequest = new XMLHttpRequest();
        } catch (e){
            // Internet Explorer Browsers
            try{
                ajaxRequest = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (e) {
                try{
                    ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
                } catch (e){
                    // Something went wrong
                    alert("Your browser broke!");
                    return false;
                }
            }
        }
        // Create a function that will receive data sent from the server
        var input = document.getElementById("autocomplete");
        ajaxRequest.onreadystatechange = function(){
            if(ajaxRequest.readyState == 4 && ajaxRequest.status == 200){
                var str = ajaxRequest.responseText;
                //document.getElementById('p').innerHTML = str;
                var res = str.split("_!_");
                    var num = res.length;
                var text = "";
                    var i;
                    for(i = 0 ; i < num ; i++){
                    text += "<p class=\"select\" onmouseover=\"mOver(this.innerHTML);\">" + res[i] + "</p>";
                    }
                if(input.value.length > 0 && str){
                    document.getElementById("dropdown_display").innerHTML = ".dropContents{display:block;}";
                } else {
                    document.getElementById("dropdown_display").innerHTML = ".dropContents{display:none;}";
                }
                document.getElementById('dropdown_content').innerHTML = text;
                //document.getElementById('p').innerHTML = "===" + window.location.pathname + '===';
            }
        }
        ajaxRequest.open("GET",  "/" + window.location.pathname.split("/")[1] + "/customer/autocomplete_search?title="+input.value, true);
        ajaxRequest.send();
    }
    </script>
    <script>
        function mOver(str){    
            document.getElementById('autocomplete').value=str;
        }
    </script>
    <script>
        function focusOut(){
            document.getElementById("dropdown_display").innerHTML = ".dropContents{display:none;}";
        }   
    </script>
    <script>
        function focusOn(){
            document.getElementById("dropdown_display").innerHTML = ".dropContents{display:block;}";
        }
    </script>
  </ul>
  <h3 class="success">${success}</h3>
  <h3 class="error">${error}</h3>
  <% session.setAttribute("success",""); %>
  <% session.setAttribute("error",""); %>