package edu.ucla.cs.cs144;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {

  public ProxyServlet() {}

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    response.setContentType("text/xml;charset=UTF-8");
    PrintWriter writer = response.getWriter();

    String query = request.getParameter("q");
    if (query == null)
      return;
    URL url = new URL("http://google.com/complete/search?output=toolbar&q=" + query);
    HttpURLConnection conn = (HttpURLConnection)url.openConnection();

    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String inputLine;

    while ((inputLine = in.readLine()) != null) {
      writer.println(inputLine);
    }
  }
}
