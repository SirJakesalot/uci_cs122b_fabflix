<%@ include file="header.jsp" %>
<%@ page import="moviedb_model.Star"%>
<%@ page import="moviedb_model.Movie"%>
<%@ page import="moviedb_model.Genre"%>
<h1>${movie.title()}</h1>
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
          <li><a href="${context}/customer/browse?genre=${genre.name()}">${genre.name()}</a></li>
        </c:forEach>
      </ul>
    </td>
  </tr>
  <tr>
    <td>STARS</td>
    <td>
      <ul style="list-style: none;">
        <c:forEach var="star" items="${movie.stars()}">
          <li><a href="${context}/customer/single_star?star_id=${star.id()}">${star.first_name()} ${star.last_name()}</a></li>
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
<%@ include file="footer.jsp" %>
