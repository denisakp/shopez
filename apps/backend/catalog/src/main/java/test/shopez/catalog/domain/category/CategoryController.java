package test.shopez.catalog.domain.category;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.shopez.catalog.domain.category.dto.CategoryCreateDTO;
import test.shopez.catalog.domain.category.dto.CategoryResponseDTO;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping(path = "${apiPrefix}/${apiVersion}/categories", produces = "application/json")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAllHandler() {
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createHandler(@Valid @RequestBody CategoryCreateDTO payload) {
        return new ResponseEntity<>(categoryService.create(payload), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<CategoryResponseDTO>> findByIdHandler(@PathVariable String id) {
        return new ResponseEntity<>(categoryService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateHandler(@PathVariable String id, @Valid @RequestBody CategoryCreateDTO payload) {
        return new ResponseEntity<>(categoryService.update(id, payload), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHandler(@PathVariable String id) {
        categoryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
