package test.shopez.catalog.domain.tag;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import test.shopez.catalog.domain.tag.dto.CreateTagDTO;
import test.shopez.catalog.domain.tag.dto.TagResponseDTO;
import test.shopez.catalog.domain.tag.mapper.TagDTOMapper;
import test.shopez.catalog.error.exception.ConflictException;
import test.shopez.catalog.error.exception.InternalServerException;
import test.shopez.catalog.error.exception.NotFoundException;

import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final TagDTOMapper tagDTOMapper;

    public TagService(TagRepository tagRepository, TagDTOMapper tagDTOMapper) {
        this.tagRepository = tagRepository;
        this.tagDTOMapper = tagDTOMapper;
    }

    public Page<TagResponseDTO> findAll(Pageable pageable) {
        try {
            return tagRepository.findAll(pageable).map(tagDTOMapper);
        } catch(Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public TagResponseDTO create(@NotNull CreateTagDTO payload) {
        try {
            Tag tag = Tag.builder()
                    .name(payload.getName())
                    .build();

            tagRepository.save(tag);

            return tagDTOMapper.apply(tag);
        } catch(Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public Optional<TagResponseDTO> findById(@NotNull String id) {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isEmpty()) {
            throw new NotFoundException("Tag", id);
        }

        try {
            return tag.map(tagDTOMapper);
        } catch(Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public TagResponseDTO update(@NotNull String id, @NotNull CreateTagDTO payload) {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isEmpty()) {
            throw new NotFoundException("Tag", id);
        }

        if (tag.get().getName().equals(payload.getName())) {
           throw new ConflictException("Tag", payload.getName());
        }

        try {
            tag.get().setName(payload.getName());

            tagRepository.save(tag.get());
            return tagDTOMapper.apply(tag.get());
        } catch(Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public void deleteById(String id) {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isEmpty()) {
            throw new NotFoundException("Tag", id);
        }

        try {
            tagRepository.deleteById(id);
        } catch(Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
