package test.shopez.catalog.domain.tag;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.shopez.catalog.domain.tag.dto.CreateTagDTO;
import test.shopez.catalog.domain.tag.dto.TagResponseDTO;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "${apiPrefix}/${apiVersion}/tags", produces = "application/json")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagResponseDTO>> findAllHandler() {
        return new ResponseEntity<>(tagService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TagResponseDTO> createHandler(@Valid @RequestBody CreateTagDTO payload) {
        return new ResponseEntity<>(tagService.create(payload), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<TagResponseDTO>> findByIdHandler(@PathVariable String id) {
        return new ResponseEntity<>(tagService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagResponseDTO> updateHandler(@PathVariable String id, @Valid @RequestBody CreateTagDTO payload) {
        return new ResponseEntity<>(tagService.update(id, payload), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHandler(@PathVariable String id) {
        tagService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
