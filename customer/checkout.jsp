<%@ include file="header.jsp" %>  
<form action="purchase" method="post">
  <fieldset class="field_entry">
    <legend>Creditcard Information</legend>
    <table>
      <tr>
        <th>First Name</th>
        <td><input type="text" name="first_name" ></td>
      </tr>
      <tr>
        <th>Last Name</th>
        <td><input type="text" name="last_name" ></td>
      </tr>
      <tr>
        <th>Creditcard Number</th>
        <td><input type="text" name="cc_id" ></td>
      </tr>
      <tr>
        <th>Expiration Date</th>
        <td><input type="text" name="exp_date" ></td>
      </tr>
    </table>
    <input type="submit" value="Submit">
  </fieldset>
</form>
<%@ include file="footer.jsp" %>  
