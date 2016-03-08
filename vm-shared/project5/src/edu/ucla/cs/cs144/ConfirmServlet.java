package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConfirmServlet extends HttpServlet implements Servlet {
       
    public ConfirmServlet() {}


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
      request.getRequestDispatcher("/confirm.jsp").forward(request, response);
    }

}
