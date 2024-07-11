package test.shopez.catalog.domain.tag.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import test.shopez.catalog.domain.tag.TagRepository;

import java.util.List;

public class TagIdsValidator implements ConstraintValidator<ValidTagIds, List<String>> {

    private final TagRepository repository;

    public TagIdsValidator(TagRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isValid(List<String> strings, ConstraintValidatorContext ctx) {
        if (strings == null || strings.isEmpty()) {
            return true;
        }

        return strings.stream().allMatch(repository::existsById);
    }
}
