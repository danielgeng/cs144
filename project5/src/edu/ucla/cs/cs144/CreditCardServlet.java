package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CreditCardServlet extends HttpServlet implements Servlet {
       
    public CreditCardServlet() {}


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
      request.getRequestDispatcher("/creditcard.jsp").forward(request, response);
    }
}
