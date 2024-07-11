package test.shopez.catalog.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShopezException extends RuntimeException {
    private final String type;
    private final String message;
}
