package test.shopez.catalog.domain.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import test.shopez.catalog.domain.category.validator.ValidCategoryId;
import test.shopez.catalog.domain.product.ProductStatus;
import test.shopez.catalog.domain.tag.validator.ValidTagIds;

import java.util.List;

@Data
public class ProductCreateDTO {
    @NotBlank
    @Size(min = 3)
    private String name;

    @ValidCategoryId
    private String category;

    @NotBlank
    @Size(min = 3)
    private String sku;

    @NotBlank
    private String subDescription;

    private String fullDescription;

    private List<String> images;

    @Min(1)
    private int stock;

    @DecimalMin(value = "0.1", message = "Price must be greater than 0.1")
    private double price;

    private List<String> colors;

    private List<String> sizes;

    //@ValidTagIds
    private List<String> tags;

    private ProductStatus status;
}
