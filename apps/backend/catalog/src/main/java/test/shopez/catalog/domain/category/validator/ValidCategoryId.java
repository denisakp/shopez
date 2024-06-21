package test.shopez.catalog.domain.category.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CategoryIdValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCategoryId {
    String message() default "Invalid category id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
