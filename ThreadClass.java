import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.sql.*;
import java.util.Scanner;

public class ThreadClass extends Thread {
    Statement statement;
    JDBCExample jdbcExample;

    public ThreadClass(Statement stmt, JDBCExample jdbcExample) {
        this.statement = stmt;
        this.jdbcExample = jdbcExample;
    }

    @Override
    public void run() {
        //Scanner scanner = new Scanner(System.in);
        /*while (true) {
            String s = scanner.nextLine();
            if (s.matches("^[0-9]+")) {*/
        try {
            jdbcExample.printNews(0, statement);
            int vi = jdbcExample.printRecord(statement,0);
            jdbcExample.update(statement, 0, vi);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*    }
        }*/
    }


}