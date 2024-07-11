package test.shopez.catalog.error.exception;

import lombok.Builder;
import lombok.Getter;
import test.shopez.catalog.error.ShopezException;

public class BadRequestException extends ShopezException {

    public BadRequestException(String message) {
        super("BAD_REQUEST", message);
    }
}
