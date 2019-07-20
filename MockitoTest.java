
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MockitoTest {

    @InjectMocks
    ThreadClass threadClass;

    @Mock
    JDBCExample jdbcExample;

    @Test
    public void Test() throws ParserConfigurationException, TransformerException, SAXException, IOException, SQLException {
        when(jdbcExample.update((isNull()), any(int.class), any(int.class))).thenReturn(true);
        when(jdbcExample.printRecord((isNull()), any(int.class))).thenReturn(0);
        when(jdbcExample.printNews(any(int.class), (isNull()))).thenReturn(true);
        Statement stmt = null;
        threadClass = new ThreadClass(stmt, jdbcExample);
        threadClass.run();
        verify(jdbcExample, times(1)).update(isNull(), any(int.class), any(int.class));
        verify(jdbcExample, times(1)).printRecord((isNull()), any(int.class));
        verify(jdbcExample, times(1)).printNews(any(int.class), (isNull()));

    }


}
