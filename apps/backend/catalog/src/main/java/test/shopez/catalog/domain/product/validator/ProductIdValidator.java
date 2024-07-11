package test.shopez.catalog.domain.product.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import test.shopez.catalog.domain.product.ProductRepository;

public class ProductIdValidator implements ConstraintValidator<ValidProductId, String> {

    private final ProductRepository repository;

    public ProductIdValidator(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        return repository.existsById(s);
    }
}
