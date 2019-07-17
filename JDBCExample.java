import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class JDBCExample {
    static final String DB_URL = "jdbc:mysql://localhost/Rss";
    static final String USER = "root";
    static final String PASS = "qwer1234";
    static final String CLASS_NAME = "com.mysql.jdbc.Driver";
    static final String SQL1 = "create table rssDB1 ( " + "   id INT  , news VARCHAR(7000) , title VARCHAR (7000)) ";
    static final String SQL2 = "create table rssDB2 ( " + "   id INT  , views INT) ";
    static final String ITEM = "item";
    static final String FILE_NAME = "/Users/Nefario/RSS/src/rss";

    public static void main(String[] args) {
        File output_file = new File(FILE_NAME);
        Connection conn;
        Statement stmt;
        try {
            Class.forName(CLASS_NAME);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            //stmt.executeUpdate(SQL1); // table should be created only once
            //stmt.executeUpdate(SQL2);
            Document doc = getDocument(output_file);
            // InsertData(conn, doc);
            ThreadClass threadClass = new ThreadClass(stmt);
            threadClass.start();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    private static void InsertData(Connection conn, Document doc) throws SQLException {
        NodeList nList = doc.getElementsByTagName(ITEM);
        PreparedStatement statement = conn.prepareStatement("insert into rssDB1(id,news,title) VALUES (?,?,?)");
        PreparedStatement statement2 = conn.prepareStatement("insert into rssDB2(id,views) VALUES (?,?)");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            Element eElement = (Element) nNode;
            statement.setInt(1, i);
            statement2.setInt(1, i);
            statement.setString(2, eElement.getElementsByTagName("description").item(0).getTextContent());
            statement.setString(3, eElement.getElementsByTagName("title").item(0).getTextContent());
            statement2.setInt(2, Integer.parseInt(eElement.getElementsByTagName("newNode").item(0).getTextContent()));
            statement.execute();
            statement2.execute();
        }
    }

    private static Document getDocument(File output_file) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(output_file);
        doc.getDocumentElement().normalize();
        NodeList nList2 = doc.getElementsByTagName("comments");
        for (int i = 0; i < nList2.getLength(); i++) {
            Text a = doc.createTextNode("0");
            Element p = doc.createElement("newNode");
            p.appendChild(a);
            nList2.item(i).getParentNode().insertBefore(p, nList2.item(i));
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(new File(FILE_NAME));
            Source input = new DOMSource(doc);
            transformer.transform(input, output);
        }
        return doc;
    }

    public static void update(Statement stmt, int number, int view) throws SQLException {
        String query1 = "update rssDB2 set views=" + (view) + " " + "where id in(" + number + ")";
        stmt.executeUpdate(query1);
    }

    public static int printRecord(Statement statement, int number) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM rssDB2 LIMIT 1 OFFSET " + number);
        while (resultSet.next()) {
            int view = resultSet.getInt("views");
            view++;
            System.out.println("VIEWS  " + view);
            return view;
        }
        return 0;
    }
}