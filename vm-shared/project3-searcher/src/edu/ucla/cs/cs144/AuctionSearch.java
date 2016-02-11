package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
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
      parser = parser = new QueryParser("content", new StandardAnalyzer());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public SearchResult[] basicSearch(String query, int numResultsToSkip, 
      int numResultsToReturn) {

    SearchResult[] res = new SearchResult[numResultsToReturn];
    ScoreDoc[] hits = null;

    // Get the TopDocs from lucene
    try {
      Query q = parser.parse(query);
      hits = searcher.search(q, numResultsToSkip + numResultsToReturn).scoreDocs;
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Skip some, and construct SearchResults for the rest
    for (int i = numResultsToSkip; i < res.length; i++) {
      try {
	Document doc = searcher.doc(i);
	res[i] = new SearchResult(doc.get("ItemID"), doc.get("Name"));
      } catch (IOException e) {
	e.printStackTrace();
      }
    }

    return res;
  }

  public SearchResult[] spatialSearch(String query, SearchRegion region,
      int numResultsToSkip, int numResultsToReturn) {
    // TODO: Your code here!
    return new SearchResult[0];
  }

  public String getXMLDataForItemId(String itemId) {
    // TODO: Your code here!
    return "";
  }

  public String echo(String message) {
    return message;
  }

}
