package com.epam.esm.controller;

import com.epam.esm.model.*;
import com.epam.esm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/SpringRESTBoot")
public class MainController {
    //Constants containing the common endpoints for the methods defined
    public Integer currentUserId=0; //to keep track of our current User ID, in order to correctly add the records
    @GetMapping("/")
    public ResponseEntity<EntityModel<Map<String, Object>>> mainPage() {
        Map<String, Object> mainPageInfo = new HashMap<>();
        mainPageInfo.put("message", "Welcome to the main page!");

        Link usersLink = linkTo(methodOn(UserController.class).getOneUserPage(PageRequest.of(1, 10),1)).withRel("users");
        Link giftsLink = linkTo(methodOn(GiftController.class).getOneGiftPage(PageRequest.of(1, 10),1)).withRel("gifts");
        Link tagsLink = linkTo(methodOn(TagController.class).getOneTagPage(PageRequest.of(1, 10),1)).withRel("tags");

        EntityModel<Map<String, Object>> mainPageResource = EntityModel.of(mainPageInfo);
        mainPageResource.add(usersLink, giftsLink, tagsLink);

        return ResponseEntity.ok(mainPageResource);
    }
}