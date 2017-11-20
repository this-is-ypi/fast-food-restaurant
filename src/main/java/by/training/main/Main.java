package by.training.main;

import by.training.model.Restaurant;
import by.training.model.exception.RestaurantException;

public class Main {

    public static void main(String[] args) {
        try {
            Restaurant restaurant = new Restaurant();
            restaurant.startRestaurant();
        } catch (RestaurantException e) {
            e.printStackTrace();
        }
    }
}
