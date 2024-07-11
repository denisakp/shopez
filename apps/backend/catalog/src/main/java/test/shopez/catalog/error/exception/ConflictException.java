package test.shopez.catalog.error.exception;


import lombok.*;
import test.shopez.catalog.error.ShopezException;

@Getter
@Setter
public class ConflictException extends ShopezException {
    private final String resource;
    private final String value;

    public ConflictException(String resource, String value) {
        super("CONFLICT", String.format("Resource: %s with value: %s already exists. ", resource, value));
        this.resource = resource;
        this.value = value;
    }
}
