package by.training.reader;

import by.training.reader.exception.PropertyReaderException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

public class PropertyReaderTest {

    private static final String WRONG_FILE_NAME = "wrongFile.properties";
    private static final String CORRECT_FILE_NAME = "src/test/resources/applicationConfig.properties";
    private PropertyReader reader;

    @Before
    public void init() {
        reader = PropertyReader.getInstance();
    }

    @Test(expected = PropertyReaderException.class)
    public void shouldThrowExceptionWhenWrongFileName() {
        reader.initProperties(WRONG_FILE_NAME);
    }

    @Test
    public void shouldReturnPropertyInstanceWhenCorrectFileName() {
        Properties result = reader.initProperties(CORRECT_FILE_NAME);
        int resultSize = result.size();
        int expectedSize = 3;
        Assert.assertEquals(expectedSize, resultSize);
    }
}
