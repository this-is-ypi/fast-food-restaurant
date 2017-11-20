package by.training.model.exception;

public class RestaurantException extends Exception {

    public RestaurantException() {
        super();
    }

    public RestaurantException(String message) {
        super(message);
    }

    public RestaurantException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestaurantException(Throwable cause) {
        super(cause);
    }
}
