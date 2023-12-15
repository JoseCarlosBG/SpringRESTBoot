package com.epam.esm.resource;

import com.epam.esm.model.entity.User;
import org.springframework.hateoas.EntityModel;

public class UserResource extends EntityModel<User> {
    public UserResource(User content) {
        super(content);
    }
}