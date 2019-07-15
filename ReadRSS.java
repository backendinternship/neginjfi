
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class ReadRSS {

    public static void main(String[] args) {
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
        ThreadClass threadClass = new ThreadClass();
        threadClass.start();


    }

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
