package test.shopez.catalog.domain.product.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProductIdValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProductId {
    String message() default "Invalid product id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
