import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class ReadRSS {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        URL url = null;
        try {
            url = new URL("https://www.digitaltrends.com/feed/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String content = readContents(url);
        File output_file = new File("/Users/Nefario/RSS/src/rss");
        if (output_file.length() == 0) {
            writeContents(output_file, content);
        }
        System.out.println("enter number");
        ThreadClass threadClass = new ThreadClass();
        threadClass.start();


    }

  /*  private static void saveInFile(Document doc) {
        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        Result output = new StreamResult(new File("/Users/Nefario/RSS/src/rss"));
        Source input = new DOMSource(doc);

        try {
            transformer.transform(input, output);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private static Document addElement(File output_file) throws ParserConfigurationException, SAXException, IOException, TransformerException {
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
    }*/

    private static void writeContents(File file, String content) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            out.write(content);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private static String readContents(URL url) {
        BufferedReader in = null;
        try {
            URLConnection con = url.openConnection();
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = in.readLine();
            StringBuilder builder = new StringBuilder();
            do {
                builder.append(line + "\n");
            } while ((line = in.readLine()) != null);
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

}
