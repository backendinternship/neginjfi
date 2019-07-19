import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockitoTest {

    @InjectMocks
    ThreadClass threadClass;

    @Mock
    JDBCExample jdbcExample;


    @Test
    public void Test() throws ParserConfigurationException, TransformerException, SAXException, IOException, SQLException {
        when(jdbcExample.update(null, 0, 0)).thenReturn(true);
        when(jdbcExample.printRecord(null, 0)).thenReturn(0);
        when(jdbcExample.printNews(0,null)).thenReturn(true);
        threadClass = new ThreadClass(null, jdbcExample);
        threadClass.run();
        verify(jdbcExample, times(1)).update(null, 0, 0);
        verify(jdbcExample, times(1)).printRecord(null, 0);
        verify(jdbcExample, times(1)).printNews(0,null);

    }

}
