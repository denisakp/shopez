package test.shopez.catalog.domain.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryCreateDTO {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, message = "Name should have at least 3 characters")
    private String name;

    @NotBlank(message = "Description is mandatory")
    @Size(min = 3, message = "Description should have at least 3 characters")
    private String description;
}
