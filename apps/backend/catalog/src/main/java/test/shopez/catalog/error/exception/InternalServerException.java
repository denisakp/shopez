package test.shopez.catalog.error.exception;

import test.shopez.catalog.error.ShopezException;

public class InternalServerException extends ShopezException {

    public InternalServerException(String message) {
        super("INTERNAL", message );
    }

    public InternalServerException() {
        super("INTERNAL", "internal server error occurred");
    }
}
