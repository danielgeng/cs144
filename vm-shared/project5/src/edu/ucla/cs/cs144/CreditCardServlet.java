package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreditCardServlet extends HttpServlet implements Servlet {
       
    public CreditCardServlet() {}


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
      String id = request.getParameter("id");
      String res = AuctionSearchClient.getXMLDataForItemId(id);
      EbayItem ei = new EbayItem(res);
      request.setAttribute("item", ei);
      request.getRequestDispatcher("/creditcard.jsp").forward(request, response);
    }
}
