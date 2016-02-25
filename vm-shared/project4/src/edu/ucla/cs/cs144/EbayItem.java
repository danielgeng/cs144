package edu.ucla.cs.cs144;

import org.xml.sax.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.sql.Time;
import java.text.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class EbayItem {

  // Bid 
  public class Bid implements Comparable<Bid> {
    // comparable by time
    public int compareTo(Bid other) {
      return -t.compareTo(other.t);
    }

    public String rating;
    public String user_id;
    public String location;
    public String country;
    public String time = "";
    public String amount;
    public Time t;
  }

  public EbayItem(String s) {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = null;
    try {
      db = dbf.newDocumentBuilder();
      InputSource is = new InputSource();
      is.setCharacterStream(new StringReader(s));
      Document doc = db.parse(is);
      DateFormat formatter = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");

      name = doc.getElementsByTagName("Name").item(0).getTextContent();
      currently = doc.getElementsByTagName("Currently").item(0).getTextContent();
      first_bid = doc.getElementsByTagName("First_Bid").item(0).getTextContent();
      number_of_bids = doc.getElementsByTagName("Number_of_Bids").item(0).getTextContent();
      description = doc.getElementsByTagName("Description").item(0).getTextContent();
      Element seller = (Element)doc.getElementsByTagName("Seller").item(0);
      user_id = seller.getAttribute("UserID");
      rating = seller.getAttribute("Rating");
      started = doc.getElementsByTagName("Started").item(0).getTextContent();
      ends = doc.getElementsByTagName("Ends").item(0).getTextContent();
      country = doc.getElementsByTagName("Country").item(0).getTextContent();
      Element loc = (Element)doc.getElementsByTagName("Location").item(0);
      location = loc.getTextContent();
      lat = loc.getAttribute("Latitude");
      lon = loc.getAttribute("Longitude");
      

      // optional buy price
      NodeList nl = doc.getElementsByTagName("Buy_Price");
      if (nl.getLength() > 0)
	buy_price = nl.item(0).getTextContent();

      // bids
      nl = doc.getElementsByTagName("Bid");
      for (int i = 0; i < nl.getLength(); i++) {
	Bid b = new Bid();
	NodeList cl = nl.item(i).getChildNodes();
	Element bidder = (Element)cl.item(0);
	b.rating = bidder.getAttribute("Rating");
	b.user_id = bidder.getAttribute("UserID");
	b.location = bidder.getFirstChild().getTextContent();
	b.country = bidder.getLastChild().getTextContent();
	b.amount = cl.item(2).getTextContent();

	// time conversion
	b.time = cl.item(1).getTextContent();
	b.t = new Time(formatter.parse(b.time).getTime());
	bids.add(b);
      }
      Collections.sort(bids);

      // Categories
      nl = doc.getElementsByTagName("Category");
      categories = new String[nl.getLength()];
      for (int i = 0; i < nl.getLength(); i++)
	categories[i] = nl.item(i).getTextContent() + " ";
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public int id;
  public String name;
  public String started;
  public String ends;
  public String user_id;
  public String rating;
  public String location;
  public String country;
  public String lon = "";
  public String lat = "";
  public String currently;
  public String first_bid;
  public String buy_price = "";
  public String number_of_bids;
  public ArrayList<Bid> bids = new ArrayList<>();
  public String[] categories;
  public String description;
};
