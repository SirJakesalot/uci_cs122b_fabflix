<%@ include file="header.jsp" %>  
<h1>Hello ${customer.first_name()}!</h1>
<table style="height:500px;table-layout:fixed;margin:auto;">
  <tr>
    <td style="min-width:400px;">
      <form action="browse" method="get">
        <fieldset class="field_entry">
          <legend>Search</legend>
          <table>
            <tr>
              <th>Title</th>
              <td><input type="text" name="title"/></td>
            </tr>
            <tr>
              <th>Year Start</th>
              <td><input type="text" name="year_start" /></td>
            </tr>
            <tr>
              <th>Year End</th>
              <td><input type="text" name="year_end" /></td>
            </tr>
            <tr>
              <th>Director</th>
              <td><input type="text" name="director" /></td>
            </tr>
            <tr>
              <th>Star First Name</th>
              <td><input type="text" name="first_name" /></td>
            </tr>
            <tr>
              <th>Star Last Name</th>
              <td><input type="text" name="last_name" /></td>
            </tr>
          </table>
          <input type="submit" value="Submit Search" class="submit"/>
        </fieldset>
      </form>
    </td>
    <td valign="top">
      <div id="page-wrap">
        <ul class="dropdown">
          <li>
            <a href="#">Browse By Genre</a>
            <ul class="sub_menu">
              <c:forEach var="genre" items="${genres}" >
                <li><a href="${context}/customer/browse?genre=${genre.name()}">${genre.name()}</a></li>
              </c:forEach>
            </ul>
          </li>
          <li>
            <a href="#">Browse By Title</a>
            <ul class="sub_menu">
              <li><a href="${context}/customer/browse?letter=A"><center>A</center></a></li>
              <li><a href="${context}/customer/browse?letter=B"><center>B</center></a></li>
              <li><a href="${context}/customer/browse?letter=C"><center>C</center></a></li>
              <li><a href="${context}/customer/browse?letter=D"><center>D</center></a></li>
              <li><a href="${context}/customer/browse?letter=E"><center>E</center></a></li>
              <li><a href="${context}/customer/browse?letter=F"><center>F</center></a></li>
              <li><a href="${context}/customer/browse?letter=G"><center>G</center></a></li>
              <li><a href="${context}/customer/browse?letter=H"><center>H</center></a></li>
              <li><a href="${context}/customer/browse?letter=I"><center>I</center></a></li>
              <li><a href="${context}/customer/browse?letter=J"><center>J</center></a></li>
              <li><a href="${context}/customer/browse?letter=K"><center>K</center></a></li>
              <li><a href="${context}/customer/browse?letter=L"><center>L</center></a></li>
              <li><a href="${context}/customer/browse?letter=M"><center>M</center></a></li>
              <li><a href="${context}/customer/browse?letter=N"><center>N</center></a></li>
              <li><a href="${context}/customer/browse?letter=O"><center>O</center></a></li>
              <li><a href="${context}/customer/browse?letter=P"><center>P</center></a></li>
              <li><a href="${context}/customer/browse?letter=Q"><center>Q</center></a></li>
              <li><a href="${context}/customer/browse?letter=R"><center>R</center></a></li>
              <li><a href="${context}/customer/browse?letter=S"><center>S</center></a></li>
              <li><a href="${context}/customer/browse?letter=T"><center>T</center></a></li>
              <li><a href="${context}/customer/browse?letter=U"><center>U</center></a></li>
              <li><a href="${context}/customer/browse?letter=V"><center>V</center></a></li>
              <li><a href="${context}/customer/browse?letter=W"><center>W</center></a></li>
              <li><a href="${context}/customer/browse?letter=X"><center>X</center></a></li>
              <li><a href="${context}/customer/browse?letter=Y"><center>Y</center></a></li>
              <li><a href="${context}/customer/browse?letter=Z"><center>Z</center></a></li>
            </ul>
          </li>
        </ul>
      </div>
    </td>
  </tr>
</table>
<%@ include file="footer.jsp" %>  
