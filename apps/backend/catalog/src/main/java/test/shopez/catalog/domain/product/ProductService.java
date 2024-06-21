package test.shopez.catalog.domain.product;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test.shopez.catalog.domain.product.dto.ProductCreateDTO;
import test.shopez.catalog.domain.product.dto.ProductResponseDTO;
import test.shopez.catalog.domain.product.mapper.ProductDTOMapper;
import test.shopez.catalog.error.exception.ConflictException;
import test.shopez.catalog.error.exception.InternalServerException;
import test.shopez.catalog.error.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDTOMapper productDTOMapper;

    // TODO: Implement the pagination and data filtering
    public List<ProductResponseDTO> findAll() {
        try {
            return productRepository.findAll().stream().map(productDTOMapper).collect(Collectors.toList());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public ProductResponseDTO create(@NotNull ProductCreateDTO payload) {
        if (productRepository.findBySku(payload.getSku()).isPresent()) {
            throw new ConflictException("Product", payload.getSku());
        }

        try {
            Product product = Product
                    .builder()
                    .name(payload.getName())
                    .sku(payload.getSku())
                    .category(payload.getCategory())
                    .subDescription(payload.getSubDescription())
                    .fullDescription(payload.getFullDescription())
                    .images(payload.getImages())
                    .stock(payload.getStock())
                    .price(payload.getPrice())
                    .colors(payload.getColors())
                    .sizes(payload.getSizes())
                    .tags(payload.getTags())
                    .status(payload.getStatus())
                    .build();

            productRepository.save(product);

            return productDTOMapper.apply(product);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public Optional<ProductResponseDTO> findById(String id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new NotFoundException("Product", id);
        }

        try {
            return product.map(productDTOMapper);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public ProductResponseDTO update(String id, @NotNull ProductCreateDTO payload) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new NotFoundException("Product", id);
        }

        try {
            product.get().setName(payload.getName());
            product.get().setSku(payload.getSku());
            product.get().setSubDescription(payload.getSubDescription());
            product.get().setFullDescription(payload.getFullDescription());
            product.get().setImages(payload.getImages());
            product.get().setStock(payload.getStock());
            product.get().setPrice(payload.getPrice());
            product.get().setColors(payload.getColors());
            product.get().setSizes(payload.getSizes());
            product.get().setTags(payload.getTags());
            product.get().setStatus(payload.getStatus());

            productRepository.save(product.get());

            return productDTOMapper.apply(product.get());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
