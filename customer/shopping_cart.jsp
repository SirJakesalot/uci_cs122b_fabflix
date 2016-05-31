<%@ include file="header.jsp" %>

<h1>Shopping Cart</h1>

<c:forEach var="item" items="${sessionScope.shopping_cart}">
  <form class="shopping_cart" action="shopping_cart" method="GET">
    <!--<fieldset class="field_entry">-->
    <fieldset class="field_entry"> 
      <legend>Cart Item</legend>
      <table>
        <tr>
          <th>Movie Id</th>
          <td><input name="movie_id" readonly type="text" value="${item.movie_id()}"/></td>
        </tr>
        <tr>
          <th>Movie Title</th>
          <td>
          <span class="anchor" >
            <a onmouseover="showbox('${context}','${item.movie_id()}')" onmouseout="hidebox('${item.movie_id()}')" href="${context}/customer/single_movie?movie_id=${item.movie_id()}">${item.movie_title()}</a>
            <span id="${item.movie_id()}" class="hide"></span>
          </span>
          </td>
        </tr>
        <tr>
          <th>Quantity</th>
          <td><input type='number' name='quantity' value="${item.quantity()}" min = 0 /></td>
        </tr>
      </table>
      <input type='submit' value='Update'/>
    </fieldset>
  </form>
</c:forEach>
<h2 style="text-align:center;" class="total">Total: $0</h2>
<a style="text-align:center;" href="checkout"><h2>Checkout</h2></a>

<script>
$(document).ready(function() {
    var total = 0;
    $(".shopping_cart input[type='number']").each( function() {
        total = parseInt($(this).val()) + total;
    });
    $(".total").html("Total: $" + total * 10);
});

$(".shopping_cart input[type='number']").change( function() {
    var total = 0;
    $(".shopping_cart input[type='number']").each( function() {
        total = parseInt($(this).val()) + total;
    });
    $(".total").html("Total: $" + total * 10);
});
</script>

<%@ include file="footer.jsp" %>
