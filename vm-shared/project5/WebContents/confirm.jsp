<%@ page import="java.util.Date" %>
<%@ page import="edu.ucla.cs.cs144.EbayItem" %>

<html>
  <head>
    <% EbayItem ei = (EbayItem)session.getAttribute("item"); %>
    <title>Confirmation</title>
  </head>
  <% if (ei != null) { %>
  <h1>Thank you for your Purchase!</h1>
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
      <td><%= request.getParameter("card_number") %></td>
    </tr>
    <tr>
      <td>Time:</td>
      <td><%= new Date(session.getLastAccessedTime()) %></td>
    </tr>
  </table>
 <% } %>
</html>
