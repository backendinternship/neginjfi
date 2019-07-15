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
import java.util.Scanner;

public class ThreadClass extends Thread {

    int[] views = new int[30];

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        File output_file = new File("/Users/Nefario/RSS/src/rss");
        while (true) {


            String s = getString(scanner);
            if (s.equals("exit"))
                return;
            if (s.matches("^[0-9]+")) {
                int number = Integer.parseInt(s);
                doo( number, output_file);
            }
        }
    }

    private synchronized void doo( int number, File o) {
        Document doc = getDoc(o);
        NodeList nList = getNodeList(doc);
        Node nNode = nList.item(number);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            add(doc, (Element) nNode, number, o);
        }
    }

    private synchronized String getString(Scanner scanner) {
        return scanner.nextLine();
    }

    private synchronized NodeList getNodeList(Document doc) {
        return doc.getElementsByTagName("item");
    }

    private synchronized Document getDoc(File output_file) {
        Document doc = null;
        try {
            doc = addElement(output_file);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return doc;
    }

    private synchronized void add(Document doc, Element nNode, int number, File o) {
        Element eElement = nNode;
        int views = Integer.parseInt(eElement.getElementsByTagName("newNode").item(0).getTextContent());
        views++;
        eElement.getElementsByTagName("newNode").item(0).setTextContent(String.valueOf(views));
        saveInFile(doc, o);
        System.out.println(views + " views" + "   NEWS : " + eElement.getElementsByTagName("description").item(0).getTextContent());
    }

    private synchronized void saveInFile(Document doc, File o) {
        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        Result output = new StreamResult(o);
        Source input = new DOMSource(doc);

        try {
            transformer.transform(input, output);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private synchronized Document addElement(File output_file) throws ParserConfigurationException, SAXException, IOException, TransformerException {
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
            Result output = new StreamResult(new File("/Users/Nefario/RSS/src/rss"));
            Source input = new DOMSource(doc);
            transformer.transform(input, output);
        }
        return doc;
    }
}
