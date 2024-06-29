package test.shopez.catalog.domain.category;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import test.shopez.catalog.domain.category.dto.CategoryCreateDTO;
import test.shopez.catalog.domain.category.dto.CategoryResponseDTO;
import test.shopez.catalog.domain.category.mapper.CategoryDTOMapper;
import test.shopez.catalog.error.exception.ConflictException;
import test.shopez.catalog.error.exception.InternalServerException;
import test.shopez.catalog.error.exception.NotFoundException;

import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryDTOMapper categoryDTOMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryDTOMapper categoryDTOMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryDTOMapper = categoryDTOMapper;
    }

    public Page<CategoryResponseDTO> findAll(Pageable pageable) {
        try {
            return categoryRepository.findAll(pageable).map(categoryDTOMapper);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public CategoryResponseDTO create(@NotNull CategoryCreateDTO payload) {
        if (categoryRepository.findByName(payload.getName()).isPresent()) {
            throw new ConflictException("Category", payload.getName());
        }

        try {
            Category category = Category.builder()
                    .name(payload.getName())
                    .description(payload.getDescription())
                    .build();

            categoryRepository.save(category);

            return categoryDTOMapper.apply(category);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public Optional<CategoryResponseDTO> findById(@NotNull String id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isEmpty()) {
            throw new NotFoundException("Category", id);
        }

        try {
            return category.map(categoryDTOMapper);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public CategoryResponseDTO update(@NotNull String id, @NotNull CategoryCreateDTO payload) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new NotFoundException("Category", id);
        }

        if (category.get().getName().equals(payload.getName())) {
            throw new ConflictException("Category", payload.getName());
        }

        try {
            category.get().setName(payload.getName());
            category.get().setDescription(payload.getDescription());

            categoryRepository.save(category.get());

            return categoryDTOMapper.apply(category.get());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public void deleteById(@NotNull String id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Category", id);
        }

        try {
            categoryRepository.deleteById(id);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
