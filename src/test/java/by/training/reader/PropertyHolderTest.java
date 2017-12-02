package by.training.reader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PropertyHolderTest {

    private static final String CORRECT_FILE_NAME = "src/test/resources/applicationConfig.properties";
    private static final int EXPECTED_CASHIER_NUMBER = 3;
    private static final int EXPECTED_TOTAL_CLIENT_NUMBER = 7;
    private static final int EXPECTED_CASHIER_WORK_TIME = 300;
    private PropertyHolder holder;


    @Before
    public void init() {
        holder = new PropertyHolder(PropertyReader.getInstance(), CORRECT_FILE_NAME);
    }

    @Test
    public void shouldReturnCorrectCashierNumber() {
        int resultCashierNumber = holder.getCashierNumber();
        Assert.assertEquals(EXPECTED_CASHIER_NUMBER, resultCashierNumber);
    }

    @Test
    public void shouldReturnCorrectTotalClientNumber() {
        int resultTotalClientNumber = holder.getTotalClientNumber();
        Assert.assertEquals(EXPECTED_TOTAL_CLIENT_NUMBER, resultTotalClientNumber);
    }

    @Test
    public void shouldReturnCorrectCashierWorkTime() {
        int resultCashierWorkTime = holder.getCashierWorkTime();
        Assert.assertEquals(EXPECTED_CASHIER_WORK_TIME, resultCashierWorkTime);
    }
}
