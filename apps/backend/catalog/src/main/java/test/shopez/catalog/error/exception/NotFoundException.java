package test.shopez.catalog.error.exception;

import lombok.Getter;
import lombok.Setter;
import test.shopez.catalog.error.ShopezException;

@Getter
@Setter
public class NotFoundException extends ShopezException {
    private final String resource;
    private final String value;

    public NotFoundException(String resource, String value) {
        super("NOT_FOUND", String.format("Resource: %s with value: %s not found", resource, value));
        this.resource= resource;
        this.value = value;
    }
}
