package com.epam.esm.controller;
import com.epam.esm.model.*;
import com.epam.esm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@RestController
@RequestMapping("/SpringRESTBoot/user")
public class UserDetailController extends MainController{
    @Autowired
    private UserService userService;
    @Autowired
    private UserGiftService ugService;
    @GetMapping("/{id}/{page}")
    public ResponseEntity<EntityModel<Map<String,List<UserGift>>>> getUserOneGiftPageById(@PathVariable("id") Integer id, @PathVariable("page") Integer currentPage) {
        User user= userService.getUserById(id);
        if (user==null){
            throw new PageNotFoundException("Page not found", id);
        }
        Page<UserGift> page;
        int numPage;
        if(currentPage<=0){
            throw new PageNotFoundException("Page not found", currentPage);
        } else {
            numPage = currentPage;
        }
        page = ugService.findPage(numPage);
        int totalPages = page.getTotalPages();

        if (totalPages<currentPage){
            throw new PageNotFoundException("Page not found", currentPage);
        }

        List<UserGift> listUg = ugService.getUGByUserId(id);
        List<UserGift> listGifts = page.getContent().stream()
                .filter(gift -> listUg.stream().anyMatch(ug
                        -> gift.getIdGift().equals(ug.getIdGift()))).toList();

        List<Link> links = new ArrayList<>();

        // Self link
        links.add(linkTo(methodOn(UserDetailController.class).getUserOneGiftPageById(id, numPage)).withSelfRel());

        // First page link
        links.add(linkTo(methodOn(UserDetailController.class).getUserOneGiftPageById(id, 1)).withRel("first"));

        // Last page link
        links.add(linkTo(methodOn(UserDetailController.class).getUserOneGiftPageById(id, totalPages)).withRel("last"));

        // Next page link
        if (page.hasNext()) {
            links.add(linkTo(methodOn(UserDetailController.class).getUserOneGiftPageById(id, numPage + 1)).withRel("next"));
        }

        // Previous page link
        if (page.hasPrevious()) {
            links.add(linkTo(methodOn(UserDetailController.class).getUserOneGiftPageById(id, numPage - 1)).withRel("prev"));
        }
        Map<String, List<UserGift>> giftDetails = new HashMap<>();
        giftDetails.put(user.getFirstName() + " " + user.getLastName(), listGifts);
        currentUserId=id;
        EntityModel<Map<String,List<UserGift>>> userResource = EntityModel.of(giftDetails, links);

        userResource.add(linkTo(methodOn(UserController.class).getOneUserPage(PageRequest.of(1, 5), numPage)).withRel("users"));

        return ResponseEntity.ok(userResource);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Map<String,List<UserGift>>>> getUserFirstTagPageById(@PathVariable("id") Integer id){
        return getUserOneGiftPageById(id, 1);
    }
}
