package test.shopez.catalog.domain.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "products")
public class Product {
    @Id
    private String id;

    // details
    @NotNull
    @Size(min = 3)
    private String name;
    @NotNull
    private String category;
    @NotNull
    private String subDescription;
    private String fullDescription;
    private List<String> images;

    // properties
    @NotNull
    private String sku;
    @Min(value = 1, message = "Stock must be greater than 0")
    private int stock;
    private List<String> colors;
    private List<String> sizes;
    private List<String> tags;

    // pricing
    private double price;

    @Builder.Default
    private ProductStatus status = ProductStatus.DRAFT;
}
