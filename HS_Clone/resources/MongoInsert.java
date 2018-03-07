import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.WriteConcern;
//import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Filters.*;
import org.bson.Document;
import org.bson.BsonDocument;
import com.mongodb.MongoException;
//import com.mongodb.WriteConcern;
//import static com.mongodb.client.model.Sorts.ascending;
//import static com.mongodb.client.model.Sorts.descending;

//import org.bson.conversions.Bson;
//import com.mongodb.client.model.Filters;

//Log message control imports
import java.util.logging.Logger;
import java.util.logging.Level;

//Java imports

import java.util.Arrays;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class MongoInsert extends JFrame {

    JTextField msg;
    JTextField ipId;
    JTextField ipFromUser;
    JTextField ipFromUserName;
    JTextField ipText;
    JTextField ipTweetCnt;

    MongoDatabase sampleDB = null;
    MongoClient client = null;
    MongoCollection<Document> collection = null;
    MongoCursor<Document> cursor = null;
    WindowListener exitListener = null;

    public MongoInsert() {
        setSize(600, 200);
        setLocation(400, 500);
        setTitle("Access MongoDB");

        Container cont = getContentPane();
        cont.setLayout(new BorderLayout() );

        JButton insert = new JButton("Insert");
        JButton connect = new JButton("Connect");
        JButton clear = new JButton("Clear");

        ipId = new JTextField(20);
        ipFromUser = new JTextField(20);
        ipFromUserName = new JTextField(20);
        ipText = new JTextField(20);
        ipTweetCnt = new JTextField(20);
        msg = new JTextField(20);

        JLabel lblId = new JLabel("id:", JLabel.RIGHT);
        JLabel lblFromUser = new JLabel("fromUser:", JLabel.RIGHT);
        JLabel lblFromUserName = new JLabel("fromUserName:", JLabel.RIGHT);
        JLabel lblText = new JLabel("text:", JLabel.RIGHT);
        JLabel lblTweetCnt = new JLabel("tweetCnt:", JLabel.RIGHT);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());
        northPanel.add(connect);
        northPanel.add(msg);
        northPanel.add(insert);
        northPanel.add(clear);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(0, 2));
        centerPanel.add(lblId);
        centerPanel.add(ipId);
        centerPanel.add(lblFromUser);
        centerPanel.add(ipFromUser);
        centerPanel.add(lblFromUserName);
        centerPanel.add(ipFromUserName);
        centerPanel.add(lblText);
        centerPanel.add(ipText);
        centerPanel.add(lblTweetCnt);
        centerPanel.add(ipTweetCnt);

        cont.add(northPanel, BorderLayout.NORTH);
        cont.add(centerPanel, BorderLayout.CENTER);

        connect.addActionListener(new ConnectMongo());
        insert.addActionListener(new InsertMongo());
        clear.addActionListener(new ClearMongo());

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(
                        null, "Are You Sure to Close Application?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);

                if (confirm == 0) {
                	
                	if(client != null) {
                        client.close();
                	}
                    System.exit(0);
                }
            }
        };

        addWindowListener(exitListener);

        setVisible(true);


    } //AccessMongo

    public static void main (String [] args) {

        MongoInsert runIt = new MongoInsert();

    }//main

    class ConnectMongo implements ActionListener {
        public void actionPerformed (ActionEvent event) {
            
            //in this section open the connection to MongoDB.
            //You should enter the code to connect to the database here
            //Remember to connect to MongoDB, connect to the database and connect to the
            //    desired collection
        	
        	try {
        		
        		client = new MongoClient("localhost", 27017);
        		
        		MongoCursor<String> mc = client.listDatabaseNames().iterator();
        		
        		//Print out databases
        		try {
        			
        			while(mc.hasNext()) {
        				System.out.println(mc.next());
        			}
        			
        		} catch(MongoException e) {
        			System.out.println(e.getMessage());
            		System.out.println(e.toString());
            		e.printStackTrace();
        		} finally {
        			mc.close();
        		}
        		
        		sampleDB = client.getDatabase("SampleSocial");
        		
        		System.out.println("We are in the database: " + sampleDB.getName());
        		
        		collection = sampleDB.getCollection("Tweets");
        		
        		System.out.println("Collection count: " + collection.count());
        		
        	} catch(Exception e) {
        		System.out.println(e.getMessage());
        		System.out.println(e.toString());
        		e.printStackTrace();
        	}
        	

        }//actionPerformed


    }//class ConnectMongo

    class InsertMongo implements ActionListener {
        public void actionPerformed (ActionEvent event) {
            // In this section you should insert data from user input

        	int id = Integer.parseInt(ipId.getText());
        	int tweetCount = Integer.parseInt(ipTweetCnt.getText());
        	
        	Document doc = new Document("id", id)
        					.append("fromUser", ipFromUser.getText())
        					.append("fromUserName", ipFromUserName.getText())
        					.append("text", ipText.getText())
        					.append("tweetCnt", tweetCount);
        	
        	collection.insertOne(doc);
        	
        	
        	
        }//actionPerformed
    }//class InsertMongo

    class ClearMongo implements ActionListener {
        public void actionPerformed (ActionEvent event) {
        	
        	//Get document
        	cursor = collection.find(eq("fromUserName", "Doe")).iterator();
        	
        	try {
        		while(cursor.hasNext()) {
        			Document doc = cursor.next();
        			System.out.println(doc);
        			System.out.println(doc.toJson());
        			
        		}
        	} catch(MongoException e) {
        		System.out.println(e.getMessage());
        		System.out.println(e.toString());
        		e.printStackTrace();
        	}
        	
        	
        	//Clear action
            msg.setText("");
            ipId.setText("");
            ipFromUser.setText("");
            ipFromUserName.setText("");
            ipText.setText("");
            ipTweetCnt.setText("");

        }//actionPerformed

    }//class ClearMongo

} //class
