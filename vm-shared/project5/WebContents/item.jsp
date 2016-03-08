<%@ page import="edu.ucla.cs.cs144.EbayItem" %>
<%@ page import="java.util.ArrayList" %>

<html>
  <head>
  <% EbayItem ei = (EbayItem) request.getAttribute("item"); %>
    <title><%= request.getAttribute("title") %></title>
    <script type="text/javascript" src="http://maps.google.com/maps/api/js"></script> 
    <script type="text/javascript"> 
      function process_geocode(results, status) {
	if (results.length > 0) {
	  var myOptions = { 
	      zoom: 14,
	      center: results[0].geometry.location, 
	      mapTypeId: google.maps.MapTypeId.ROADMAP 
	  }; 
	  var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
	}
      }

      function initialize() {
	var lat = "<%= ei.lat %>";
	var lon = "<%= ei.lon %>";
	if (lon === "") {
	  var loc = "<%= ei.location %>";
	  geo_req = {
	    address: loc
	  }
	  var geocoder = new google.maps.Geocoder();
	  results = geocoder.geocode(geo_req, process_geocode);
	}
	else {
	  latlng = new google.maps.LatLng(lat, lon);
	  console.log("default");
	  console.log(latlng);
	  var myOptions = { 
	      zoom: 14, // default is 8  
	      center: latlng, 
	      mapTypeId: google.maps.MapTypeId.ROADMAP 
	  }; 
	  var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions); 
	}
      } 

    </script> 
  </head>
  <body onload="initialize()">
    <form action="item">
      Item ID:
      <input type="text" name="id"/>
      <input type="submit" value="Submit"/>
    </form>
    <hr>
    <% if (ei.name == null){ %>
   	<h1>Invalid item ID!</h1>
	<% } else { %>
    <h1><%= ei.name %> (<%= request.getParameter("id") %>)</h1>
    <h2>Seller: <%= ei.user_id + " (" + ei.rating + ")" %></h2>
    <h3>Location: 
    <%= (ei.lon.equals("")) ? 
	  ei.location + " - " + ei.country :
	  ei.location + " (" + ei.lat + ", " + ei.lon + ") - " +
	  ei.country %>
    </h3>
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
	<% if (ei.buy_price != ""){ %>
	<td><b>Buy it Now:</b></td>
	<td>
	  <b><%= ei.buy_price %></b> 
	  <form action='creditcard' style='display:inline'>
	    <input type='submit' value='Pay Now'/>
	  </form>
	</td>
	<%  } %>
      </tr>
    </table>
    <h2>Bids (<%= ei.number_of_bids %>):</h2>
    <%
      for (EbayItem.Bid b : ei.bids) {
	out.println("<p>");
	out.println(b.time + " - <b>" + b.amount + "</b></br>");
	out.print(b.user_id + " (" + b.rating + ") - ");
	out.println(b.location + " - " + b.country);
	out.println("</p>");
      }
    %>
    <h3>Categories:</h3>
    <%
    if (ei.categories.length > 0)
      for (int i = 0; i < ei.categories.length; i++)
	out.println("<li>" + ei.categories[i] + "</li>"); 
    %>
    <h2>Description:</h2>
    <p><%= ei.description %></p>
    <div id="map_canvas" style="width:30em; height:30em"></div>
    <% } %>
  </body>
</html>
