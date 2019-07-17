import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ThreadClass extends Thread {
    Statement statement;

    public ThreadClass(Statement stmt) {
        this.statement = stmt;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String s = scanner.nextLine();
            if (s.matches("^[0-9]+")) {
                try {
                    int number = Integer.parseInt(s);
                    printNews(number);
                    int vi = JDBCExample.printRecord(statement, number);
                    JDBCExample.update(statement, number, vi);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void printNews(int number) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM rssDB1 LIMIT 1 OFFSET " + number);
        while (rs.next()) {
            System.out.println("TITLE :  " + rs.getString("title"));
            System.out.println("NEWS  :  " + rs.getString("news"));
        }
    }
}