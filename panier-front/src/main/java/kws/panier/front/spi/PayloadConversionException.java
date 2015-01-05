package kws.panier.front.spi;


public class PayloadConversionException extends RuntimeException {

    public PayloadConversionException() {
    }

    public PayloadConversionException(String message) {
        super(message);
    }

    public PayloadConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayloadConversionException(Throwable cause) {
        super(cause);
    }

    public PayloadConversionException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
