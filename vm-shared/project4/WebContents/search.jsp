<%@ page import="edu.ucla.cs.cs144.SearchResult" %>

<html>
  <head>
    <title><%= request.getAttribute("title") %></title>
  </head>
  <body>
  <form action="search">
    Query:
    <input type="text" name="q"/>
    <input type="hidden" name="numResultsToSkip" value="0"/>
    <input type="hidden" name="numResultsToReturn" value="100"/>
    <input type="submit" value="Submit"/>
  </form>
  <p>
    <%
      SearchResult[] res = (SearchResult[]) request.getAttribute("results");
      for (int i = 0; i < res.length; i++) 
	out.println("<li><a href=\"/eBay/item?id=" + res[i].getItemId() + "\">" + res[i].getName() + "</a></li>");
    %>
  </p>
    <%
      // Previous
      String skip = request.getParameter("numResultsToSkip");
      String ret = request.getParameter("numResultsToReturn");
      int resultsSkipped = (skip == null) ? 0 : Integer.parseInt(skip);
      int resultsReturned = (ret == null) ? 100 : Integer.parseInt(ret);
      String query = request.getParameter("q");
      if (resultsSkipped > 0) {
	int resultsToSkip = resultsSkipped - resultsReturned;
	resultsToSkip = (resultsToSkip < 0) ? 0 : resultsToSkip;
	out.print("<a href=\"/eBay/search?q=" + query);
	out.print("&numResultsToSkip=" + resultsToSkip);
	out.print("&numResultsToReturn=" + resultsReturned);
	out.println("\">Previous</a>");
      }
      // Next
      if (res.length == resultsReturned) {
	out.print("<a href=\"/eBay/search?q=" + query);
	int nextSkip = resultsSkipped + resultsReturned;
	out.print("&numResultsToSkip=" + nextSkip);
	out.print("&numResultsToReturn=" + resultsReturned);
	out.println("\">Next</a>");
      }
    %>
  </body>
</html>
