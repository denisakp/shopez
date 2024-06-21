package test.shopez.catalog.domain.product.dto;

import test.shopez.catalog.domain.product.ProductStatus;

import java.util.Collections;
import java.util.List;

public record ProductResponseDTO(
        String id,
        String name,
        ProductCategoryResponseDTO category,
        String sku,
        String subDescription,
        String fullDescription,
        List<String> images,
        int stock,
        double price,
        List<String> colors,
        List<String> sizes,
        List<String> tags,
        ProductStatus status,
        String reviews
) {

    public ProductResponseDTO {
        if (images == null) {
            images = Collections.emptyList();
        }

        if (colors == null) {
            colors = Collections.emptyList();
        }

        if (sizes == null) {
            sizes = Collections.emptyList();
        }

        if (tags == null) {
            tags = Collections.emptyList();
        }
    }
}
