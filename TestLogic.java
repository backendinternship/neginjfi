import static org.junit.Assert.*;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.sql.*;

public class TestLogic {

    static final String USER = "root";
    static final String PASS = "qwer1234";
    static final int nodeListSize = 30;

    @Test
    public void testDocumentContent() throws ParserConfigurationException, TransformerException, SAXException, IOException {
        File output_file = new File("/Users/Nefario/RSS/src/rss");
        Document doc = JDBCExample.getDocument(output_file);
        NodeList nList = doc.getElementsByTagName("item");
        boolean itemExistance = false, titleExistance = false, descriptionExistance = false;
        if (nList.getLength() > 0)
            itemExistance = true;
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            Element eElement = (Element) nNode;
            eElement.getElementsByTagName("description").item(0).getTextContent();
            if (eElement.getElementsByTagName("description") != null)
                descriptionExistance = true;
            eElement.getElementsByTagName("title").item(0).getTextContent();
            if (eElement.getElementsByTagName("title") != null)
                titleExistance = true;
        }
        assertTrue(itemExistance);
        assertTrue(descriptionExistance);
        assertTrue(titleExistance);
    }

    @Test
    public void viewIncrement() throws ParserConfigurationException, TransformerException, SAXException, IOException {
        File output_file = new File("/Users/Nefario/RSS/src/rss");
        Document doc = JDBCExample.getDocument(output_file);
        NodeList nList = doc.getElementsByTagName("item");
        Connection conn;
        Statement stmt;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(JDBCExample.DB_URL, USER, PASS);
            stmt = conn.createStatement();
            for (int i = 0; i < nList.getLength(); i++) {
                int view = JDBCExample.printRecord(stmt, i);
                assertEquals(view++, JDBCExample.printRecord(stmt, i));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertDataToTableTest() throws ParserConfigurationException, TransformerException, SAXException, IOException {
        String SQL1 = "create table testTable1 ( " + "   id INT  , news VARCHAR(7000) , title VARCHAR (7000)) ";
        String SQL2 = "create table testTable2 ( " + "   id INT  , views INT) ";
        String query1 = "insert into testTable1(id,news,title) VALUES (?,?,?)";
        String query2 = "insert into testTable2(id,views) VALUES (?,?)";
        File output_file = new File("/Users/Nefario/RSS/src/rss");
        Connection conn;
        Statement stmt;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(JDBCExample.DB_URL, USER, PASS);
            stmt = conn.createStatement();
            stmt.executeUpdate(SQL1); // table should be created only once
            stmt.executeUpdate(SQL2);
            Document doc = JDBCExample.getDocument(output_file);
            JDBCExample.InsertData(conn, doc, query1, query2);
            NodeList nList = doc.getElementsByTagName("item");
            for (int i = 0; i < nList.getLength(); i++) {
                ResultSet resultSet = stmt.executeQuery("SELECT * FROM testTable2 LIMIT 1 OFFSET " + i);
                while (resultSet.next()) {
                    int view = resultSet.getInt("views");
                    Node nNode = nList.item(i);
                    Element eElement = (Element) nNode;
                    assertEquals(view, Integer.parseInt(eElement.getElementsByTagName("newNode").item(0).getTextContent()));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void printTest() throws Exception {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        Connection conn;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(JDBCExample.DB_URL, USER, PASS);
            stmt = conn.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 0; i <nodeListSize ; i++) {
            ThreadClass.printNews(i, stmt);
            System.setOut(oldOut);
            String output = new String(baos.toByteArray());
            assertTrue(output.contains("TITLE"));

        }
    }

    @Test
    public void TestUpdatingData() {
        Connection conn;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(JDBCExample.DB_URL, USER, PASS);
            stmt = conn.createStatement();
            for (int i = 0; i < nodeListSize; i++) {
                int view = JDBCExample.printRecord(stmt, i);
                JDBCExample.update(stmt, i, view);
                ResultSet resultSet = stmt.executeQuery("SELECT * FROM rssDB2 LIMIT 1 OFFSET " + i);
                if (resultSet.next()) {
                    int view2 = resultSet.getInt("views");
                    assertEquals(view2, view);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}