package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.text.SimpleDateFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

  public final static String LUCENE_DIR = "/var/lib/lucene";

  private IndexSearcher searcher = null;
  private QueryParser parser = null;

  public AuctionSearch() {
    try {
      searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(LUCENE_DIR + "/index1"))));
      parser = new QueryParser("content", new StandardAnalyzer());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public SearchResult[] basicSearch(String query, int numResultsToSkip, 
      int numResultsToReturn) {

    SearchResult[] res = null;
    ScoreDoc[] hits = null;

    // Get the TopDocs from lucene
    try {
      Query q = parser.parse(query);
      hits = searcher.search(q, numResultsToSkip + numResultsToReturn).scoreDocs;
      res = new SearchResult[hits.length - numResultsToSkip];
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Skip some, and construct SearchResults for the rest
    for (int i = 0; i < res.length; i++) {
      try {
	Document doc = searcher.doc(hits[i+numResultsToSkip].doc);
	res[i] = new SearchResult(doc.get("ItemID"), doc.get("Name"));
      } catch (IOException e) {
	e.printStackTrace();
      }
    }

    return res;
  }

  public SearchResult[] spatialSearch(String query, SearchRegion region,
      int numResultsToSkip, int numResultsToReturn) {
    // Get item ID's that fit the location
    HashSet<String> spatialItems = new HashSet<>();
    Connection conn = null;
    try {
	conn = DbManager.getConnection(true);
	Statement s = conn.createStatement();
	String lx = Double.toString(region.getLx());
	String ly = Double.toString(region.getLy());
	String rx = Double.toString(region.getRx());
	String ry = Double.toString(region.getRy());
	ResultSet rs;
	rs = s.executeQuery("select ItemID from Item_Location" +
			    " where x(Location) > " + lx +
			    " and x(Location) < " + rx +
			    " and y(Location) > " + ly +
			    " and y(Location) < " + ry);
	while (rs.next())
	  spatialItems.add(rs.getString("ItemID"));
    } catch (SQLException e) {
	e.printStackTrace();
    }
    if (spatialItems.size() == 0)
      return new SearchResult[0];
    
    // Get basic search results and filter based on item ID's
    ArrayList<SearchResult> res = new ArrayList<>();
    SearchResult[] basic_res = basicSearch(query, 0, spatialItems.size());
    int count = 0;
    for (SearchResult r : basic_res)  {
      if (spatialItems.contains(r.getItemId())) {
	if (count >= numResultsToSkip)
	  res.add(r);
	count++;
	if (count == numResultsToSkip + numResultsToReturn)
	  break;
      }
    }

    return res.toArray(new SearchResult[res.size()]);
  }

  public String getXMLDataForItemId(String itemId) {
    String res = "<Item ItemID=\"" + itemId + "\">";
    
    String name, currently, buy_price, first_bid, number_of_bids, started, ends, seller, latitude, longitude, description;
    String rating = "";
    String country = "";
    String location = "";

    Connection conn = null;
    try {
      conn = DbManager.getConnection(true);
      Statement s = conn.createStatement();
      ResultSet rs = s.executeQuery("select * from Item" +
				    " where ItemID = " + itemId);

      if (!rs.next())
	return "";
      
      name = rs.getString("Name");
      res += "<Name>" + name + "</Name>";
      
      currently = rs.getString("Currently");
      buy_price = rs.getString("Buy_Price");
      first_bid = rs.getString("First_Bid");
      number_of_bids = rs.getString("Number_of_Bids");
      started = rs.getString("Started");
      ends = rs.getString("Ends");
      seller = rs.getString("Seller");
      latitude = rs.getString("Latitude");
      longitude = rs.getString("Longitude");
      description = rs.getString("Description");
      
      // Seller
      rs = s.executeQuery("select * from User" +
			  " where UserID = \"" + seller + "\"");
      if (rs.next()) {
	rating = rs.getString("Rating");
	location = rs.getString("Location");
	country = rs.getString("Country");
      }

      rs = s.executeQuery("select * from Item_Category" +
			  " where ItemID = \"" + itemId + "\"");
      // Categories
      while (rs.next()) 
	res += "<Category>" + rs.getString("Category") + "</Category>";

      res += "<Currently>" + currently + "</Currently>";
      if (buy_price != null)
	res += "<Buy_Price>" + buy_price + "</Buy_Price>";
      res += "<First_Bid>" + first_bid + "</First_Bid>";
      res += "<Number_of_Bids>" + number_of_bids + "</Number_of_Bids>";
      
      // Bids
      rs = s.executeQuery("select * from Bid" + 
			  " where ItemID = \"" + itemId + "\"");
      if (rs.next()) {
	res += "<Bids>";
	do {
	  // Time and amount
	  String time = rs.getString("Time");
	  String amount = rs.getString("Amount");

	  // Bidder: location, rating, and country 
	  String bidder = rs.getString("Bidder");
	  String b_location = "";
	  String b_country = "";
	  String b_rating = "";
	  
	  Statement user_s = conn.createStatement();
	  ResultSet user_rs = user_s.executeQuery("select * from User" +
						  " where UserID = \"" + bidder + "\"");
	  if (user_rs.next()) {
	    b_location = user_rs.getString("Location");
	    b_country = user_rs.getString("Country");
	    b_rating = user_rs.getString("Rating");
	  }

	  res += "<Bid><Bidder Rating =\"" + b_rating + "\" ";
	  res += "UserID=\"" + bidder + "\">";
	  res += "<Location Latitude=\"" + latitude + "\" ";
	  res += "Longitude=\"" + longitude + "\">";
	  res += b_location + "</Location>";
	  res += "<Country>" + b_country + "</Country></Bidder>";
	  res += "<Time>" + time + "</Time>";
	  res += "<Amount>" + amount + "</Amount></Bid>";
	  
	} while (rs.next());
      }
      else
	res += "<Bids />";
      
      res += "<Location>" + location + "</Location>";
      res += "<Country>" + country + "</Country>";
      res += "<Started>" + started + "</Started>";
      res += "<Ends>" + ends + "</Ends>";
      res += "<Seller Rating=\"" + rating + "\" ";
      res += "UserID=\"" + seller + "\" />";
      res += "<Description>" + description + "</Description></Item>";
      
    } catch (SQLException e) {
	e.printStackTrace();
    }

    return res;
  }

  public String echo(String message) {
    return message;
  }

}
