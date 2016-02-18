<%@ page import="edu.ucla.cs.cs144.SearchResult" %>

<html>
  <head>
    <title><%= request.getAttribute("title") %></title>
  </head>
  <body>
  <p><%
    SearchResult[] res = (SearchResult[]) request.getAttribute("results");
    for (int i = 0; i < res.length; i++) out.println("<li>" + res[i].getName() + "</li>");
  %></p>
  </body>
</html>
