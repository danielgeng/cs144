<%@ page import="edu.ucla.cs.cs144.EbayItem" %>

<html>
  <head>
    <% EbayItem ei = (EbayItem) request.getAttribute("item"); %>
    <title>Payment</title>
  </head>
  <table>
    <tr>
      <td>ItemID:</td>
      <td><%= request.getParameter("id") %></td>
    </tr>
    <tr>
      <td>ItemName:</td>
      <td><%= ei.name %></td>
    </tr>
    <tr>
      <td>Buy_Price:</td>
      <td><%= ei.buy_price %></td>
    </tr>
    <tr>
      <td>Credit Card:</td>
      <td>
	<form action='confirm' style='display:inline'>
	  <input type="text"/>
	  <input type='submit' value='Submit'/>
	</form>
      </td>
    </tr>
  </table>
</html>
