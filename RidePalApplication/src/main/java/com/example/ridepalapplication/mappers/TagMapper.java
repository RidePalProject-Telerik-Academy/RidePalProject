package com.example.ridepalapplication.mappers;

import com.example.ridepalapplication.dtos.TagDto;
import com.example.ridepalapplication.models.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    public Tag fromDto(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setName(tagDto.getTagName());
        return tag;
    }

}
