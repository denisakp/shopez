package test.shopez.catalog.domain.product.mapper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.validation.constraints.NotNull;

import test.shopez.catalog.domain.category.Category;
import test.shopez.catalog.domain.category.CategoryRepository;
import test.shopez.catalog.domain.product.Product;
import test.shopez.catalog.domain.product.dto.relations.ProductCategoryResponseDTO;
import test.shopez.catalog.domain.product.dto.ProductResponseDTO;
import test.shopez.catalog.domain.product.dto.relations.ProductTagResponseDTO;
import test.shopez.catalog.domain.tag.TagRepository;
import test.shopez.catalog.error.exception.NotFoundException;

@Service
public class ProductDTOMapper implements Function<Product, ProductResponseDTO> {
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Value("${server.address}")
    private String host;
    @Value("${server.port}")
    private String port;
    @Value("${apiVersion}")
    private String apiVersion;
    @Value("${apiPrefix}")
    private String apiPrefix;

    public ProductDTOMapper(CategoryRepository repository, TagRepository tagRepository) {
        this.categoryRepository = repository;
        this.tagRepository = tagRepository;
    }

    @Override
    public ProductResponseDTO apply(@NotNull Product product) {
        // TODO: add a load test to check if this is a bottleneck
        ProductCategoryResponseDTO categoryResponseDTO = mapCategory(product.getCategory());
        List<ProductTagResponseDTO> tags = mapTags(product.getTags());

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
                tags,
                product.getStatus(),
                reviewURI
        );
    }

    protected List<ProductTagResponseDTO> mapTags(@NotNull List<String> tags) {
        return tags
                .stream()
                .map(tagId -> tagRepository.findById(tagId).orElseThrow(() -> new NotFoundException("Tag", tagId)))
                .map(tag -> new ProductTagResponseDTO(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    }

    protected ProductCategoryResponseDTO mapCategory(@NotNull String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category", categoryId));
        return new ProductCategoryResponseDTO(category.getId(), category.getName());
    }
}
