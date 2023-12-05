package com.epam.esm.resource;

import com.epam.esm.model.Tag;
import org.springframework.hateoas.EntityModel;

public class TagResource extends EntityModel<Tag> {
    public TagResource(Tag content) {
        super(content);
    }
}
