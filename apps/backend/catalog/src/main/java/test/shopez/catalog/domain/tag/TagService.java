package test.shopez.catalog.domain.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test.shopez.catalog.domain.tag.dto.CreateTagDTO;
import test.shopez.catalog.domain.tag.dto.TagResponseDTO;
import test.shopez.catalog.domain.tag.mapper.TagDTOMapper;
import test.shopez.catalog.error.exception.ConflictException;
import test.shopez.catalog.error.exception.InternalServerException;
import test.shopez.catalog.error.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagDTOMapper tagDTOMapper;

    public List<TagResponseDTO> findAll() {
        try {
            return tagRepository.findAll().stream().map(tagDTOMapper).collect(Collectors.toList());
        } catch(Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public TagResponseDTO create(CreateTagDTO payload) {
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

    public Optional<TagResponseDTO> findById(String id) {
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

    public TagResponseDTO update(String id, CreateTagDTO payload) {
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
