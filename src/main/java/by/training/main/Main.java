package by.training.main;

import by.training.model.Restaurant;
import by.training.reader.PropertyHolder;
import by.training.reader.PropertyReader;

public class Main {

    private static final String FILE_NAME = "src/main/resources/applicationConfig.properties";

    public static void main(String[] args) {

        PropertyHolder holder = new PropertyHolder(PropertyReader.getInstance(), FILE_NAME);

        Restaurant restaurant = new Restaurant(holder);
        restaurant.startRestaurant();
    }
}
