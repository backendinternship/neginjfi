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
    static final String SQL = "create table rssDBS ( " + "   id INT  , news VARCHAR(7000) , views INT ) ";
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
            //stmt.executeUpdate(SQL); // table should be created only once
            Document doc = getDocument(output_file);
            InsertData(conn, doc);
            ThreadClass threadClass = new ThreadClass(stmt);
            threadClass.start();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    private static void InsertData(Connection conn, Document doc) throws SQLException {
        NodeList nList = doc.getElementsByTagName(ITEM);
        PreparedStatement statement = conn.prepareStatement("insert into rssDBS(id,news,views) VALUES (?,?,?)");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            Element eElement = (Element) nNode;
            statement.setInt(1, i);
            statement.setString(2, eElement.getElementsByTagName("description").item(0).getTextContent());
            statement.setInt(3, Integer.parseInt(eElement.getElementsByTagName("newNode").item(0).getTextContent()));
            statement.execute();
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
        String query1 = "update rssDBS set views=" + (view) + " " + "where id in(" + number + ")";
        stmt.executeUpdate(query1);
    }

    public static int printRecord(ResultSet rsr) throws SQLException {
        while (rsr.next()) {
            String news = rsr.getString("news");
            int view = rsr.getInt("views");
            view++;
            System.out.println(news);
            System.out.println("views  " + view);
            return view;
        }
        return 1;
    }
}