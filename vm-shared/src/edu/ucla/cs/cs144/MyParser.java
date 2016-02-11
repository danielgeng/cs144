/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        
	String[] moneyFields = {"Currently", "Buy_Price", "First_Bid"};
	String[] timeFields = {"Started", "Ends"};
	SimpleDateFormat inFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
	SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	try {
	  PrintWriter itemFile = new PrintWriter(new FileOutputStream(new File("Item.dat"),true));
	  PrintWriter userFile = new PrintWriter(new FileOutputStream(new File("User.dat"),true));
	  PrintWriter itemCategoryFile = new PrintWriter(new FileOutputStream(new File("ItemCategory.dat"),true));
	  PrintWriter bidFile = new PrintWriter(new FileOutputStream(new File("Bid.dat"),true));
	  
	  for (Element item : getElementsByTagNameNR(doc.getDocumentElement(), "Item")) {
	    StringBuilder sb = new StringBuilder();

	    // ItemID, Name, and ItemCategory
	    String itemID = item.getAttribute("ItemID");
	    for (Element category : getElementsByTagNameNR(item, "Category"))
	      itemCategoryFile.println(itemID + columnSeparator + getElementText(category));
	    sb.append(item.getAttribute("ItemID") + columnSeparator);
	    sb.append(getElementTextByTagNameNR(item, "Name") + columnSeparator);
	    
	    // Currently, Buy_Price, First_Bid, Number_of_Bids
	    for (String field : moneyFields) {
	      String optional = getElementTextByTagNameNR(item, field);
	      if (optional.equals(""))
		sb.append("\\N" + columnSeparator);
	      else
		sb.append(strip(optional) + columnSeparator);
	    }
	    sb.append(getElementTextByTagNameNR(item, "Number_of_Bids") + columnSeparator);

	    // Started, Ends
	    for (String field: timeFields) {
	      Date date = inFormat.parse(getElementTextByTagNameNR(item, field));
	      sb.append(outFormat.format(date) + columnSeparator); 
	    }

	    // Seller
	    Element seller = getElementByTagNameNR(item, "Seller");
	    String sellerID = seller.getAttribute("UserID");
	    sb.append(sellerID + columnSeparator);
	    
	    // Location
	    Element location = getElementByTagNameNR(item, "Location");
	    String[] latlon = {location.getAttribute("Latitude"), location.getAttribute("Longitude")};
	    /*
	    for (String s : latlon) {
	      if (s.equals("")) 
		sb.append("\\N" + columnSeparator);
	      else
		sb.append(s + columnSeparator);
	    }
	    */

	    // write seller info to user file
	    userFile.println(sellerID + columnSeparator + seller.getAttribute("Rating") + columnSeparator + getElementText(location) + columnSeparator + getElementTextByTagNameNR(item, "Country"));

	    // Description
	    String description = getElementTextByTagNameNR(item, "Description");
	    sb.append(description.substring(0, Math.min(description.length(), 4000)));
	    itemFile.println(sb.toString());

	    // Bids
	    for (Element bid : getElementsByTagNameNR(getElementByTagNameNR(item, "Bids"), "Bid")) {
	      // User
	      Element bidder = getElementByTagNameNR(bid, "Bidder");
	      String bidderID = bidder.getAttribute("UserID");
	      userFile.println(bidderID + columnSeparator + bidder.getAttribute("Rating") + columnSeparator + getElementTextByTagNameNR(bidder, "Location") + columnSeparator + getElementTextByTagNameNR(bidder, "Country"));

	      // Time & Amount
	      Date date = inFormat.parse(getElementTextByTagNameNR(bid, "Time"));
	      String time = outFormat.format(date);
	      String amount = strip(getElementTextByTagNameNR(bid, "Amount"));
	      bidFile.println(itemID + columnSeparator + bidderID + columnSeparator + time + columnSeparator + amount);
	    }
	  }
	
	  itemFile.close();
	  userFile.close();
	  itemCategoryFile.close();
	  bidFile.close();
	}
	catch (Exception e) {
	  e.printStackTrace();
	} 
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
