package test.shopez.catalog.domain.tag.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TagIdsValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTagIds {
    String message() default "Invalid tag id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
