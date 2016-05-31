
<%@ include file="header.jsp" %>
  <h1>MovieDB</h1>
  <table class="browse">
    <c:forEach var="movie" items="${movies}">
      <tr>
        <td>
          <img src="${movie.banner_url()}" onError='this.onError=null;this.src="${context}/images/no_photo.png;"'/>
          <p>
            <c:set var="movie_title" value="${fn:replace(movie.title(),' ','+')}"/>
            <a href="${context}/customer/shopping_cart?movie_id=${movie.id()}&movie_title=${fn:replace(movie.title(),' ','+')}&increment=1">
              <figure class='shopping_cart'>
                <img src='${context}/images/shopping_cart.png'/>
                <figcaption>Price: $10</br>Add to Cart</figcaption>
              </figure>
            </a>
          </p>
        </td>
        <td class='browse_categories' valign="top">
          <p>Title</p>
          <p>Year</p>
          <p>Director</p>
          <p>Stars</p>
          <p>Genres</p>
        </td>
        <td class='browse_categories' valign="top">
          <span class="anchor" >
            <p><a onmouseover="showbox('${context}','${movie.id()}')" onmouseout="hidebox('${movie.id()}')" href="${context}/customer/single_movie?movie_id=${movie.id()}">${movie.title()} </a></p>
            <span id="${movie.id()}" class="hide"></span>
          </span>
          <p>${movie.year()}</p>
          <p>${movie.director()}</p>
          <p>
            <c:forEach var="star" items="${movie.stars()}">
              <a href="${context}/customer/single_star?star_id=${star.id()}">${star.first_name()} ${star.last_name()}</a>
            </c:forEach>
          </p>
          <p>
            <c:forEach var="genre" items="${movie.genres()}">
              <a href="${context}/customer/browse?genre=${fn:replace(genre.name(), ' ','+')}">${genre.name()}</a>
            </c:forEach>
          </p>
          <p><a href="${movie.trailer_url()}">Trailer</a></p>
        </td>
      </tr>
    </c:forEach>
  </table>
  <div class="browse_footer">
    <p>
      <a href=${first_page}>First Page</a>
      <a href=${previous_page}>Prev Page</a>
      <a href=${next_page}>Next Page</a>
      <a href=${last_page}>Last Page</a>
    </p>
    <p> Page Size:
      <a href=${five}>5</a>
      <a href=${ten}>10</a>
      <a href=${twenty}>20</a>
    </p>
    <p> Order By:
      <a href=${title_asc}>Title ASC</a>
      <a href=${title_desc}>Title DESC</a>
      <a href=${year_asc}>Year ASC</a>
      <a href=${year_desc}>Year DESC</a>
    </p>
  </div>

<%@ include file="footer.jsp" %>
