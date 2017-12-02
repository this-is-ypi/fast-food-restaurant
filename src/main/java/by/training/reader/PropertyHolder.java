package by.training.reader;

import by.training.reader.enumeration.PropertyEnum;

import java.util.Properties;

/**
 * Holder of initializing info.
 */
public class PropertyHolder {

    private Properties properties;

    public PropertyHolder(PropertyReader reader, String fileName) {
        properties = reader.initProperties(fileName);
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
}
