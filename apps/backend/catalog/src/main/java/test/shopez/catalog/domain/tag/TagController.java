package test.shopez.catalog.domain.tag;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.shopez.catalog.domain.tag.dto.CreateTagDTO;
import test.shopez.catalog.domain.tag.dto.TagResponseDTO;

import java.util.Optional;

@RestController
@RequestMapping(path = "${apiPrefix}/${apiVersion}/tags", produces = "application/json")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<Page<TagResponseDTO>> findAllHandler(Pageable p) {
        return new ResponseEntity<>(tagService.findAll(p), HttpStatus.OK);
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
