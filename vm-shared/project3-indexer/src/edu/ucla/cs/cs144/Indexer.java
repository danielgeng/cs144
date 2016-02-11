package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    public final static String LUCENE_DIR = "/var/lib/lucene";
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }

    private IndexWriter indexWriter = null;

    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            try {
                Directory indexDir = FSDirectory.open(new File(LUCENE_DIR + "/index1"));
                IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
                indexWriter = new IndexWriter(indexDir, config);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return indexWriter;
    }

    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            try {
                indexWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
    public void rebuildIndexes() {

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
    	try {
    	    conn = DbManager.getConnection(true);
    	} catch (SQLException e) {
            e.printStackTrace();
    	}

        try {
            getIndexWriter(true);

	    // Query the database
	    Statement s = conn.createStatement();
	    Statement item_s = conn.createStatement();
	    ResultSet item_rs, ic_rs;
	    String item_id, name, category, description, content;

	    // Item
	    item_rs = item_s.executeQuery("select * from Item;");
	    while (item_rs.next()) {
	      Document doc = new Document();

	      item_id     = item_rs.getString("ItemID");
	      name        = item_rs.getString("Name");
	      description = item_rs.getString("Description");
	      doc.add(new StringField("ItemID", item_id, Field.Store.YES));
	      doc.add(new TextField("Name", name, Field.Store.YES));
	      if (description != null)
		doc.add(new TextField("Description", description, Field.Store.YES));
	      content = item_id + " " + name + " " + " " + description;


	      // Item_Categories
	      ic_rs = s.executeQuery("select * from Item_Category where ItemID=" + item_id + ";");
	      while (ic_rs.next()) {
		category = ic_rs.getString("Category");
		doc.add(new TextField("Category", category, Field.Store.YES));
		content += " " + category;
	      }

	      doc.add(new TextField("content", content, Field.Store.NO));
	      indexWriter.addDocument(doc);
	    }


            closeIndexWriter();
            conn.close();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }


        // close the database connection
    	try {
    	    conn.close();
    	} catch (SQLException e) {
    	    e.printStackTrace();
    	}
    }    

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
