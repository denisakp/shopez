package test.shopez.catalog.domain.tag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateTagDTO {
    @NotBlank
    @Size(min = 3, message = "Name should have at least 3 characters")
    private String name;
}
