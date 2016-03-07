package edu.ucla.cs.cs144;

import java.util.Arrays;
import java.util.List;
import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {

  public SearchServlet() {}

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    String query = request.getParameter("q");
    String skip = request.getParameter("numResultsToSkip");
    String ret  = request.getParameter("numResultsToReturn");

    int numResultsToSkip = (skip == null) ? 0 : Integer.parseInt(skip);
    int numResultsToReturn = (ret == null) ? 100 : Integer.parseInt(ret);

    SearchResult[] res = AuctionSearchClient.basicSearch(query, numResultsToSkip, numResultsToReturn);
    
    request.setAttribute("results", res);
    request.setAttribute("title", "eBay Search");
    request.getRequestDispatcher("/search.jsp").forward(request, response);
  }
}
