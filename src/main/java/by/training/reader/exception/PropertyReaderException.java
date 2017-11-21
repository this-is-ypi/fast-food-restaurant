package by.training.reader.exception;

public class PropertyReaderException extends RuntimeException {

    public PropertyReaderException() {
        super();
    }

    public PropertyReaderException(String message) {
        super(message);
    }

    public PropertyReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyReaderException(Throwable cause) {
        super(cause);
    }
}
