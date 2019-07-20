import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataInsertion {

    private transient static DataInsertion dataInsertion = new DataInsertion();

    public boolean InsertData(Connection conn, Document doc, String query1, String query2) throws SQLException {
        NodeList nList = doc.getElementsByTagName("item");
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
        return true;
    }

    public static DataInsertion getInstance() {
        return dataInsertion;
    }
}
