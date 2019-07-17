import static java.util.jar.Pack200.Packer.PASS;
import static org.junit.Assert.*;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class TestLogic {

    static final String USER = "root";
    static final String PASS = "qwer1234";

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
}