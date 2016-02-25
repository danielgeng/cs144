<%@ page import="edu.ucla.cs.cs144.EbayItem" %>
<%@ page import="java.util.ArrayList" %>


<html>
  <head>
    <title><%= request.getAttribute("title") %></title>
  </head>
  <body>
    <% 
      EbayItem ei = (EbayItem) request.getAttribute("item"); 
    %>
    <h1><%= ei.name %></h1>
    <h3>
     <%= (ei.lon.equals("")) ? 
	  ei.location + " - " + ei.country :
	  ei.location + " (" + ei.lat + ", " + ei.lon + ") - " +
	  ei.country %>
    </h3>
    <p><b>Seller: </b><%= ei.user_id + " (" + ei.rating + ")" %></p>
    <table>
      <tr>
	<td><b>Currently:</b></td>
	<td><b><%= ei.currently %></b></td>
      </tr>
      <tr>
	<td>First Bid:</td>
	<td><%= ei.first_bid %></td>
      </tr>
      <tr>
	<% 
	  if (ei.buy_price != "")
	    out.println("<td><b>Buy it Now:</b></td>");
	    out.println("<td><b>" + ei.buy_price + "</b></td>");
	%>
      </tr>
    </table>
    <h2>Bids (<%= ei.number_of_bids %>):</h2>
    <%
      for (EbayItem.Bid b : ei.bids) {
	out.println("<p>");
	out.println(b.time + "- <b>" + b.amount + "</b></br>");
	out.print(b.user_id + " (" + b.rating + ") - ");
	out.println(b.location + " - " + b.country);
	out.println("</p>");
      }
    %>
    <h3>Categories:</h3>
    <%
      for (int i = 0; i < ei.categories.length; i++)
	out.println("<li>" + ei.categories[i] + "</li>"); 
    %>
    <h2>Description:</h2>
    <p><%= ei.description %></p>
  </body>
</html>
