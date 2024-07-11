package test.shopez.catalog.domain.tag.mapper;

import org.springframework.stereotype.Service;
import test.shopez.catalog.domain.tag.Tag;
import test.shopez.catalog.domain.tag.dto.TagResponseDTO;

import java.util.function.Function;

@Service
public class TagDTOMapper implements Function<Tag, TagResponseDTO> {
    @Override
    public TagResponseDTO apply(Tag tag) {
        return new TagResponseDTO(tag.getId(), tag.getName());
    }
}
