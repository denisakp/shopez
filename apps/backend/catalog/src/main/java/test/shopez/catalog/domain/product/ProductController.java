package test.shopez.catalog.domain.product;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.shopez.catalog.domain.product.dto.ProductCreateDTO;
import test.shopez.catalog.domain.product.dto.ProductResponseDTO;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "${apiPrefix}/${apiVersion}/products", produces = "application/json")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAllHandler() {
        return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createHandler(@Valid @RequestBody ProductCreateDTO payload) {
        return new ResponseEntity<>(productService.create(payload), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ProductResponseDTO>> findByIdHandler(@PathVariable String id) {
        return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateHandler(@PathVariable String id, @Valid @RequestBody ProductCreateDTO payload) {
        return new ResponseEntity<>(productService.update(id, payload), HttpStatus.OK);
    }
}
