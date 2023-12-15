package com.epam.esm.controller.entity;
import com.epam.esm.controller.MainController;
import com.epam.esm.controller.error.PageNotFoundException;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.entity.UserGift;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@RestController
@RequestMapping("/SpringRESTBoot/users")
public class UserController extends MainController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserGiftService ugService;
    @Autowired
    private GiftService giftService;
    @Autowired
    private GiftTagService gtService;

    @GetMapping("/{pageNumber}")
    public ResponseEntity<CollectionModel<EntityModel<User>>> getOneUserPage(@PageableDefault(size = 10, sort = "id") Pageable pageable,
                                                                             @PathVariable("pageNumber") Integer currentPage) {
        Page<User> page;
        int numPage;
        if(currentPage <= 0){
            throw new PageNotFoundException("Page not found", currentPage);
        } else {
            numPage = currentPage;
        }
        page = userService.findPage(numPage);

        int totalPages = page.getTotalPages();
        if (totalPages < currentPage){
            throw new PageNotFoundException("Page not found", currentPage);
        }
        List<User> users = page.getContent();
        List<Link> links = new ArrayList<>();

        // Self link
        links.add(WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getOneUserPage(pageable, numPage)).withSelfRel());

        // First page link
        links.add(WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getOneUserPage(PageRequest.of(1, pageable.getPageSize(), pageable.getSort()), 1)).withRel("first"));

        // Last page link
        links.add(WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getOneUserPage(PageRequest.of(totalPages - 1, pageable.getPageSize(), pageable.getSort()), totalPages)).withRel("last"));

        // Next page link
        if (page.hasNext()) {
            links.add(WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getOneUserPage(page.nextPageable(), numPage + 1)).withRel("next"));
        }

        // Previous page link
        if (page.hasPrevious()) {
            links.add(WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getOneUserPage(page.previousPageable(), numPage -1 )).withRel("prev"));
        }

        int finalNumPage = numPage;
        List<EntityModel<User>> userResources = users.stream()
                .map(user -> EntityModel.of(user,
                        WebMvcLinkBuilder.linkTo(methodOn(UserDetailController.class).getUserOneGiftPageById(user.getId(), finalNumPage)).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<User>> response = CollectionModel.of(userResources, links);


        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<User>>> getFirstUserPage(){
        return getOneUserPage(PageRequest.of(1, 5),1);
    }

    @PostMapping("/createUser")
    public HttpEntity<EntityModel<User>> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);


        URI location =ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id")  Integer id) {
        // Delete all records in UserGift with the specified id for User, then delete the User
        ugService.deleteUGByUser(id);
        userService.deleteUser(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/userOrderInfo/{userId}/{page}")
    public ResponseEntity<CollectionModel<EntityModel<Map<String, Object>>>> getUserOrderInfo(
            @PathVariable("userId") Integer userId, @PathVariable("page") Integer page,
            @PageableDefault(size = 5, sort = "id_user") Pageable pageable) {

        // Logic to retrieve order information (cost and timestamp) for the given user
        Page<Map<String, Object>> userOrderPage = userService.getUserOrderInfo(userId, pageable);

        int totalPages = userOrderPage.getTotalPages();

        if (page >= totalPages || page < 0) {
            throw new PageNotFoundException("Page not found", page);
        }

        List<Map<String, Object>> userOrders = userOrderPage.getContent();
        List<Link> links = new ArrayList<>();

        // Self link
        links.add(WebMvcLinkBuilder.linkTo(
                methodOn(UserController.class).getUserOrderInfo(userId, page, pageable)).withSelfRel());

        // First page link
        links.add(WebMvcLinkBuilder.linkTo(
                methodOn(UserController.class).getUserOrderInfo(userId, 0, pageable)).withRel("first"));

        // Last page link
        links.add(WebMvcLinkBuilder.linkTo(
                methodOn(UserController.class).getUserOrderInfo(userId, totalPages - 1, pageable)).withRel("last"));

        // Next page link
        if (userOrderPage.hasNext()) {
            links.add(WebMvcLinkBuilder.linkTo(
                    methodOn(UserController.class).getUserOrderInfo(userId, page + 1, pageable)).withRel("next"));
        }

        // Previous page link
        if (userOrderPage.hasPrevious()) {
            links.add(WebMvcLinkBuilder.linkTo(
                    methodOn(UserController.class).getUserOrderInfo(userId, page - 1, pageable)).withRel("prev"));
        }

        List<EntityModel<Map<String, Object>>> userOrderResources = userOrders.stream()
                .map(order -> EntityModel.of(order,
                        WebMvcLinkBuilder.linkTo(
                                methodOn(UserController.class).getUserOrderInfo(userId, page, pageable)).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Map<String, Object>>> response = CollectionModel.of(userOrderResources, links);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/userOrderInfo/{userId}")
    public ResponseEntity<CollectionModel<EntityModel<Map<String, Object>>>> getFirstUserOrderInfo(@PathVariable("userId") Integer userId, @PageableDefault(size = 5, sort = "id_user") Pageable pageable) {
        return getUserOrderInfo(userId, 0, pageable);
    }

    @GetMapping("/userMostUsedTag/{userId}")
    public ResponseEntity<String> getUserMostUsedTag(@PathVariable("userId") Integer userId) {
        // Logic to retrieve the most widely used tag of the user with the highest cost of all orders
        String mostUsedTag = userService.getUserMostUsedTag(userId);

        return ResponseEntity.ok(mostUsedTag);
    }

    @PostMapping("/saveOrder/{id}")
    public ResponseEntity<EntityModel<GiftCertificate>> createCert(@RequestBody GiftCertificate gift, @PathVariable("id")  Integer id) {
        List<GiftCertificate> giftList = giftService.getAllGifts().stream().filter(t -> t.getName().equals(gift.getName())).toList();
        GiftCertificate gift1;

        if (giftList.isEmpty() || giftList.get(0) == null) {
            // Error. There are no Gift Certificates in the database
            return ResponseEntity.badRequest().build();
        } else {
            gift1 = giftList.get(0);
        }

        LocalDateTime date = LocalDateTime.now();
        UserGift ug = new UserGift(id, gift1.getId(), gift1.getName(), gift1.getPrice(), date);
        ugService.createUG(ug);

        URI location =ServletUriComponentsBuilder.fromCurrentRequest().path("/{idUser}/{idGift}").buildAndExpand(ug.getIdUser(),ug.getIdGift()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/deleteOrder/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Integer id) {
        ugService.deleteUGByGift(id);
        giftService.deleteGift(id);

        return ResponseEntity.noContent().build();
    }
}
