<%@ include file="header.jsp" %>  
<%@ page import="moviedb_model.Star"%>
<%@ page import="moviedb_model.Movie"%>
<h1>${star.first_name()} ${star.last_name()}</h1>
<figure>
  <img src="${star.photo_url()}" alt="No photo available" onerror="this.onError=null;this.src='${context}/images/no_photo.png';"/>
  <figcaption>Photo</figcaption>
</figure>
<table class="browse">
  <tr>
    <td>ID</td>
    <td>${star.id()}</td>
  </tr>
  <tr>
    <td>FIRST NAME</td>
    <td>${star.first_name()}</td>
  </tr>
  <tr>
    <td>LAST NAME</td>
    <td>${star.last_name()}</td>
  </tr>
  <tr>
    <td>MOVIES</td>
    <td>
      <ul style="list-style: none;">
        <c:forEach var="movie" items="${star.movies()}">
          <span class="anchor" >
            <p><a  onmouseover="showbox('${context}','${movie.id()}')" onmouseout="hidebox('${movie.id()}')" href="${context}/customer/single_movie?movie_id=${movie.id()}">${movie.title()} </a></p>
            <span id="${movie.id()}" class="hide"></span>
          </span>
         </c:forEach>
      </ul>
    </td>
  </tr>
</table>
<%@ include file="footer.jsp" %>  
