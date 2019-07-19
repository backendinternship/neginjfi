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
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/test";
    static final String USER = "sa";
    static final String PASS = "";
    static final String ITEM = "item";
    static final String SQL1 = "create table rss11 ( " + "   id INT  , news VARCHAR(7000) , title VARCHAR (7000)) ";
    static final String SQL2 = "create table rss22 ( " + "   id INT  , views INT) ";
    static final String FILE_NAME = "/Users/Nefario/neginjfi/out/rss";
    static final String query1 = "insert into rss11(id,news,title) VALUES (?,?,?)";
    static final String query2 = "insert into rss22(id,views) VALUES (?,?)";
    public static NodeList nList2;
    private transient static JDBCExample jd = new JDBCExample();

    public void main(String[] args) {
        File output_file = new File(FILE_NAME);
        Connection conn;
        Statement stmt;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            // stmt.executeUpdate(SQL1); // table should be created only once
            // stmt.executeUpdate(SQL2);
            Document doc = getDocument(output_file);
            //InsertData(conn, doc, query1, query2);
            ThreadClass threadClass = new ThreadClass(stmt , jd);
            threadClass.start();
            System.out.println("thread cla");
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public  void InsertData(Connection conn, Document doc, String query1, String query2) throws SQLException {
        NodeList nList = doc.getElementsByTagName(ITEM);
        PreparedStatement statement = conn.prepareStatement(query1);
        PreparedStatement statement2 = conn.prepareStatement(query2);
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

    public  Document getDocument(File output_file) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(output_file);
        doc.getDocumentElement().normalize();
        nList2 = doc.getElementsByTagName("comments");
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

    public boolean update(Statement stmt, int number, int view) throws SQLException {
        System.out.println("fuck me");
        String query1 = "UPDATE rss22 SET views=" + (view) + " " + "WHERE id = (" + number + ")";
        stmt.executeUpdate(query1);
        return true;
    }


    public static JDBCExample getInstance() {
        return jd;
    }

    public int printRecord(Statement statement, int number) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM rss22 ORDER by id LIMIT 1 OFFSET " + number);
        while (resultSet.next()) {
            int view = resultSet.getInt("views");
            view++;
            System.out.println("VIEWS  " + view);
            return view;
        }
        return 0;
    }

    public boolean printNews(int number, Statement statement) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM rss11 LIMIT 1 OFFSET " + number);
        if (rs.next()) {
            System.out.println("TITLE :  " + rs.getString("title"));
            System.out.println("NEWS  :  " + rs.getString("news"));
            return true;
        }
        return false;
    }
}