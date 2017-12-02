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
    private static PropertyReader reader;
    private static AtomicBoolean isInitialized = new AtomicBoolean(false);

    private PropertyReader() {
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

    public Properties initProperties(String fileName) {
        Properties properties = new Properties();
        try {
            File file = new File(fileName);
            FileReader fileReader = new FileReader(file);
            properties.load(fileReader);
            return properties;
        } catch (IOException e) {
            LOGGER.error("Exception while reading file.");
            throw new PropertyReaderException(e.getMessage(), e.getCause());
        }
    }
}
