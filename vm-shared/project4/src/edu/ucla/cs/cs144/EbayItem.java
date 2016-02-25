package edu.ucla.cs.cs144;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.w3c.dom.*;

import java.io.*;

public class EbayItem {
  public EbayItem(String s) {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = null;
    data = "";
    try {
      db = dbf.newDocumentBuilder();
      InputSource is = new InputSource();
      is.setCharacterStream(new StringReader(s));
      Document doc = db.parse(is);
      NodeList categories = doc.getElementsByTagName("Category");
      for (int i = 0; i < categories.getLength(); i++)
	data += categories.item(i).getTextContent() + " ";
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public int id;
  public String data;
};
