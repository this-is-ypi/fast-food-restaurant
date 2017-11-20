package by.training.reader;

import by.training.reader.exception.PropertyReaderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PropertyReader {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Lock LOCK = new ReentrantLock(true);

    private static final String APPLICATION_CONFIG_PROPERTIES = "applicationConfig.properties";

    private static PropertyReader reader;

    private static AtomicBoolean isInitialized = new AtomicBoolean(false);

    private Properties properties;

    private PropertyReader() throws PropertyReaderException {
        initProperties();
    }

    public static PropertyReader getInstance() throws PropertyReaderException{
        if (!isInitialized.get()) {
            LOCK.lock();
            try {
                if (reader == null) {
                    reader = new PropertyReader();
                }
            } finally {
                LOCK.unlock();
            }
        }
        return reader;
    }

    public int getCashierNumber() {
        return Integer.valueOf(properties.getProperty(PropertyEnum.CASHIER_NUMBER.name()));
    }

    public int getTotalClientNumber() {
        return Integer.valueOf(properties.getProperty(PropertyEnum.TOTAL_CLIENT_NUMBER.name()));
    }

    public int getPreOrderClientNumber() {
        return Integer.valueOf(properties.getProperty(PropertyEnum.PRE_ORDER_CLIENT_NUMBER.name()));
    }

    public int getCashierWorkTime() {
        return Integer.valueOf(properties.getProperty(PropertyEnum.CASHIER_WORK_TIME.name()));
    }

    private void initProperties() throws PropertyReaderException {
        InputStream inputStream = PropertyReader.class
                .getClassLoader()
                .getResourceAsStream(APPLICATION_CONFIG_PROPERTIES);
        if (inputStream == null) {
            LOGGER.error("Config file not found.");
            throw new PropertyReaderException("Config file not found.");
        }

        properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new PropertyReaderException(e.getMessage(), e.getCause());
        }
    }
}
