package test.shopez.catalog.domain.product.mapper;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import test.shopez.catalog.domain.category.Category;
import test.shopez.catalog.domain.category.CategoryRepository;
import test.shopez.catalog.domain.product.Product;
import test.shopez.catalog.domain.product.dto.ProductCategoryResponseDTO;
import test.shopez.catalog.domain.product.dto.ProductResponseDTO;
import test.shopez.catalog.error.exception.NotFoundException;

import java.util.function.Function;

@Service
public class ProductDTOMapper implements Function<Product, ProductResponseDTO> {
    private final CategoryRepository repository;
    @Value("${server.address}")
    private String host;
    @Value("${server.port}")
    private String port;
    @Value("${apiVersion}")
    private String apiVersion;
    @Value("${apiPrefix}")
    private String apiPrefix;

    public ProductDTOMapper(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductResponseDTO apply(@NotNull Product product) {
        // TODO: add a load test to check if this is a bottleneck
        Category category = repository.findById(product.getCategory()).orElseThrow(() -> new NotFoundException("Category", product.getCategory()));
        ProductCategoryResponseDTO categoryResponseDTO = new ProductCategoryResponseDTO(category.getId(), category.getName());

        String reviewURI = String.format("http://%s:%s/%s/%s/reviews/%s", host, port, apiPrefix, apiVersion, product.getId());
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                categoryResponseDTO,
                product.getSku(),
                product.getSubDescription(),
                product.getFullDescription(),
                product.getImages(),
                product.getStock(),
                product.getPrice(),
                product.getColors(),
                product.getSizes(),
                product.getTags(),
                product.getStatus(),
                reviewURI
        );
    }
}
