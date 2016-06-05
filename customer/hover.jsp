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
<figure>
  <img src="${movie.banner_url()}" alt="No banner available" onerror="this.onError=null;this.src='${context}/images/no_photo.png';">
  <figcaption>Banner</figcaption>
</figure>
<table class="browse">
  <tr>
    <td>ID</td>
    <td>${movie.id()}</td>
  </tr>
  <tr>
    <td>TITLE</td>
    <td>${movie.title()}</td>
  </tr>
  <tr>
    <td>YEAR</td>
    <td>${movie.year()}</td>
  </tr>
  <tr>
    <td>DIRECTOR</td>
    <td>${movie.director()}</td>
  </tr>
  <tr>
    <td>GENRES</td>
    <td>
      <ul style="list-style: none;">
        <c:forEach var="genre" items="${movie.genres()}">
          <a href="${context}/customer/browse?genre=${genre.name()}">${genre.name()}</a>,
        </c:forEach>
      </ul>
    </td>
  </tr>
  <tr>
    <td>STARS</td>
    <td>
      <ul style="list-style: none;">
        <c:forEach var="star" items="${movie.stars()}">
          <a href="${context}/customer/single_star?star_id=${star.id()}">${star.first_name()} ${star.last_name()}</a>,
        </c:forEach>
      </ul>
    </td>
  </tr>
  <tr>
    <td>TRAILER</td>
    <td><a href="${movie.trailer_url()}">link</a></td>
  </tr>
</table>
<a href="${context}/customer/shopping_cart?movie_id=${movie.id()}&movie_title=${fn:replace(movie.title(),' ','+')}&increment=1">
  <figure class='shopping_cart'>
    <img src='${context}/images/shopping_cart.png'/>
    <figcaption>Add to Cart</figcaption>
  </figure>
</a>

