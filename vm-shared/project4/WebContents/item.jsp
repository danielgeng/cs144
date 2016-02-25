<%@ page import="edu.ucla.cs.cs144.EbayItem" %>

<html>
  <head>
    <title><%= request.getAttribute("title") %></title>
  </head>
  <body>
  <% 
    EbayItem ei = (EbayItem) request.getAttribute("item");
  %>
  <p><%= ei.data %></p>
  </body>
</html>
