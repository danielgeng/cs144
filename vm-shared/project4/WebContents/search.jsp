<%@ page import="edu.ucla.cs.cs144.SearchResult" %>

<html>
  <head>
    <title><%= request.getAttribute("title") %></title>
    <script type="text/javascript" src="suggest.js"></script>
    <script type="text/javascript">
    window.onload = function () {
	    var oTextbox = new AutoSuggestControl(document.getElementById("suggest"), new AutoSuggestions());
    }
    </script>
    <link rel="stylesheet" type="text/css" href="style.css"/>
  </head>
  <body>
  <form action="search">
    Query:
    <input type="text" name="q" autocomplete="off" id="suggest"/>
    <input type="hidden" name="numResultsToSkip" value="0"/>
    <input type="hidden" name="numResultsToReturn" value="50"/>
    <input type="submit" value="Submit"/>
  </form>
  <hr>
  <h2>Search results for <%= request.getParameter("q") %></h2>
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
      int resultsReturned = (ret == null) ? 50 : Integer.parseInt(ret);
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
