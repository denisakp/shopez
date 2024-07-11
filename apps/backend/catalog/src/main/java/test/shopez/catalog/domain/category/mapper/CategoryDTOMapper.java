package test.shopez.catalog.domain.category.mapper;

import org.springframework.stereotype.Service;
import test.shopez.catalog.domain.category.Category;
import test.shopez.catalog.domain.category.dto.CategoryResponseDTO;

import java.util.function.Function;

@Service
public class CategoryDTOMapper implements Function<Category, CategoryResponseDTO> {
    @Override
    public CategoryResponseDTO apply(Category category) {
        return new CategoryResponseDTO(category.getId(), category.getName(), category.getDescription());
    }
}
