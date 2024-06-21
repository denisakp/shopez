package test.shopez.catalog.domain.category.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import test.shopez.catalog.domain.category.CategoryRepository;

public class CategoryIdValidator implements ConstraintValidator<ValidCategoryId, String> {
    @Autowired
    private CategoryRepository repository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isEmpty()) {
            return false;
        }

        return repository.existsById(s);
    }
}
