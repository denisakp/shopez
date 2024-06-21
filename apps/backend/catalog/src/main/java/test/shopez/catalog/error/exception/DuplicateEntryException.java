package test.shopez.catalog.error.exception;


import lombok.Getter;
import lombok.Setter;
import test.shopez.catalog.error.ShopezException;

@Getter
@Setter
public class DuplicateEntryException extends ShopezException {
    private final String value;

    public DuplicateEntryException(String value) {
        super("DUPLICATE", String.format("cannot add more than 1 review for the product %s", value));
        this.value = value;
    }
}
