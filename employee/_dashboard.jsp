<%@ include file="header.jsp" %>  
<h1>Hello ${employee.fullname()}!</h1>

<% if (session.getAttribute("movie_id") != null) { %>
    <!--<a href="single_movie?movie_id=${movie_id}">Movie ${movie_insert}</a>
    <a href="single_star?star_id=${star_id}">Star ${movie_insert}</a>-->
    <center>
    <p>Movie ${movie_insert}</p>
    <p>Star ${star_insert}</p>
    <p>Genre ${genre_insert}</p>
    <p>${star_movie_link}</p>
    <p>${genre_movie_link}</p></center>
<%
        session.setAttribute("movie_id", null);
    }
%>
    
<table style="margin:auto;width:50%;">
  <tr>
    <td style="width:50%;">
      <form action="add_star" method="get">
        <fieldset class="field_entry">
          <legend>Add Star</legend>
          <table>
            <tr>
              <th>Star First Name</th>
              <td><input type="text" name="first_name"/></td>
            </tr>
            <tr>
              <th>Star Last Name (or full name)</th>
              <td><input type="text" name="last_name" required /></td>
            </tr>
          </table>
          <input type="submit" value="Add" class="submit"/>
        </fieldset>
      </form>
    </td>
  </tr>
  <tr>
    <td style="width:50%;">
      <form action="add_movie" method="get">
        <fieldset class="field_entry">
          <legend>Add Movie</legend>
          <table>
            <tr>
              <th>Movie Title</th>
              <td><input type="text" name="movie_title" required /></td>
            </tr>
            <tr>
              <th>Movie Year</th>
              <td><input type="number" name="movie_year" value="2016" required min="1" max="2016" /></td>
            </tr>
            <tr>
              <th>Movie Director</th>
              <td><input type="text" name="movie_director" required /></td>
            </tr>
            <tr>
              <th>Star First Name</th>
              <td><input type="text" name="star_first_name"/></td>
            </tr>
            <tr>
              <th>Star Last Name (or full name)</th>
              <td><input type="text" name="star_last_name" required /></td>
            </tr>
            <tr>
              <th>Genre Name</th>
              <td><input type="text" name="genre_name" required /></td>
            </tr>
          </table>
          <input type="submit" value="Add" class="submit"/>
        </fieldset>
      </form>
    </td>
  </tr>
</table>
<%@ include file="/customer/footer.jsp" %>  
