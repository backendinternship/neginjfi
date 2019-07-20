import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.w3c.dom.Document;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.Silent.class)
public class MockMain {


    @InjectMocks
    Main main;

    @InjectMocks

    JDBCExample jdbcExample;

    @Mock
    DataInsertion dataInsertion;

    @Before
    public void Maintest() {
        try {
            when(dataInsertion.InsertData(any(Connection.class), any(Document.class), any(String.class), any(String.class))).thenReturn(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws SQLException {
        main = new Main(jdbcExample);
        main.main(null);
        verify(dataInsertion, times(1)).InsertData(any(Connection.class), any(Document.class), any(String.class), any(String.class));
    }
}
