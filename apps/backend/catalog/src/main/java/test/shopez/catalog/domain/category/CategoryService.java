package test.shopez.catalog.domain.category;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test.shopez.catalog.domain.category.dto.CategoryCreateDTO;
import test.shopez.catalog.domain.category.dto.CategoryResponseDTO;
import test.shopez.catalog.domain.category.mapper.CategoryDTOMapper;
import test.shopez.catalog.error.exception.ConflictException;
import test.shopez.catalog.error.exception.InternalServerException;
import test.shopez.catalog.error.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryDTOMapper categoryDTOMapper;

    // TODO: Implement the pagination
    public List<CategoryResponseDTO> findAll() {
        try {
            return categoryRepository.findAll().stream().map(categoryDTOMapper).collect(Collectors.toList());
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

    public Optional<CategoryResponseDTO> findById(String id) {
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

    public CategoryResponseDTO update(String id, CategoryCreateDTO payload) {
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

    public void deleteById(String id) {
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
