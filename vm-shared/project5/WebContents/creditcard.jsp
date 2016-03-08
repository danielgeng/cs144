<%@ page import="edu.ucla.cs.cs144.EbayItem" %>

<html>
  <head>
    <% EbayItem ei = (EbayItem)session.getAttribute("item"); %>
    <title>Payment</title>
  </head>
  <% if (ei != null) { %>
  <table>
    <tr>
      <td>ItemID:</td>
      <td><%= ei.id %></td>
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
	<form action='https://<%= request.getServerName() %>:8443<%= request.getContextPath()%>/confirm.jsp' style='display:inline' method='POST'>
	  <input type="text" name='card_number'/>
	  <input type='submit' value='Submit'/>
	</form>
      </td>
    </tr>
  </table>
 <% } %>
</html>
