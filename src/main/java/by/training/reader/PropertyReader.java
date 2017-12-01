package by.training.reader;

import by.training.reader.exception.PropertyReaderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Reader of properties file witch contains data for
 * initializing restaurant application.
 */
public class PropertyReader {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Lock LOCK = new ReentrantLock(true);
    private static final String FILE_NAME = "src/main/resources/applicationConfig.properties";
    private static PropertyReader reader;
    private static AtomicBoolean isInitialized = new AtomicBoolean(false);
    private Properties properties;

    private PropertyReader() throws PropertyReaderException {
        initProperties();
    }

    public static PropertyReader getInstance() {
        if (!isInitialized.get()) {
            LOCK.lock();
            try {
                if (reader == null) {
                    reader = new PropertyReader();
                    isInitialized.set(true);
                }
            } finally {
                LOCK.unlock();
            }
        }
        return reader;
    }

    public int getCashierNumber() {
        String cashierNumber = PropertyEnum.CASHIER_NUMBER.name();
        String cashierNumberValue = properties.getProperty(cashierNumber);
        return Integer.valueOf(cashierNumberValue);
    }

    public int getTotalClientNumber() {
        String totalClientNumber = PropertyEnum.TOTAL_CLIENT_NUMBER.name();
        String totalClientNumberValue = properties.getProperty(totalClientNumber);
        return Integer.valueOf(totalClientNumberValue);
    }

    public int getCashierWorkTime() {
        String cashierWorkTime = PropertyEnum.CASHIER_WORK_TIME.name();
        String cashierWorkTimeValue = properties.getProperty(cashierWorkTime);
        return Integer.valueOf(cashierWorkTimeValue);
    }

    private void initProperties() {

        properties = new Properties();
        try {
            properties.load(new FileReader(new File(FILE_NAME)));
        } catch (IOException e) {
            LOGGER.error("Exception while reading file.");
            throw new PropertyReaderException(e.getMessage(), e.getCause());
        }
    }
}
