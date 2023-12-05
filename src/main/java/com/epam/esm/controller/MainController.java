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
    public final String USERS="/users", USER="/user", GIFTS="/gifts", GIFT="/gift", TAGS="/tags";

    private UserController userController;
    private GiftController giftController;
    private TagController tagController;

    @GetMapping("/")
    public ResponseEntity<EntityModel<Map<String, Object>>> mainPage() {
        Map<String, Object> mainPageInfo = new HashMap<>();
        mainPageInfo.put("message", "Welcome to the main page!");

        Link usersLink = linkTo(methodOn(MainController.class).getOneUserPage(PageRequest.of(1, 10),1)).withRel("users");
        Link giftsLink = linkTo(methodOn(MainController.class).getOneGiftPage(PageRequest.of(1, 10),1)).withRel("gifts");
        Link tagsLink = linkTo(methodOn(MainController.class).getOneTagPage(PageRequest.of(1, 10),1)).withRel("tags");

        EntityModel<Map<String, Object>> mainPageResource = EntityModel.of(mainPageInfo);
        mainPageResource.add(usersLink, giftsLink, tagsLink);

        return ResponseEntity.ok(mainPageResource);
    }

    @GetMapping(USERS + "/{pageNumber}")
    public ResponseEntity<CollectionModel<EntityModel<User>>> getOneUserPage(@PageableDefault(size = 10, sort = "id") Pageable pageable,
                                                                             @PathVariable("pageNumber") Integer currentPage) {
        return userController.getOneUserPage(pageable,currentPage);
    }
    @GetMapping(USERS)
    public ResponseEntity<CollectionModel<EntityModel<User>>> getFirstUserPage(){
        return getOneUserPage(PageRequest.of(1, 5),1);
    }

    @GetMapping(USER + "/{id}/{page}")
    public ResponseEntity<EntityModel<Map<String,List<UserGift>>>> getUserOneGiftPageById(@PathVariable("id") Integer id, @PathVariable("page") Integer currentPage) {
        return userController.getUserOneGiftPageById(id,currentPage);
    }

    @GetMapping(USER + "/{id}")
    public ResponseEntity<EntityModel<Map<String,List<UserGift>>>> getUserFirstTagPageById(@PathVariable("id") Integer id){
        return getUserOneGiftPageById(id, 1);
    }

    @GetMapping(GIFTS + "/{pageNumber}")
    public ResponseEntity<CollectionModel<EntityModel<GiftCertificate>>> getOneGiftPage(@PageableDefault(size = 10, sort = "id") Pageable pageable,
                                                                                        @PathVariable("pageNumber") Integer currentPage){
        return giftController.getOneGiftPage(pageable,currentPage);
    }
    @GetMapping(GIFTS)
    public ResponseEntity<CollectionModel<EntityModel<GiftCertificate>>> getFirstGiftPage(){
        return getOneGiftPage(PageRequest.of(1, 5),1);
    }

    @GetMapping(GIFT + "/{id}/{page}")
    public ResponseEntity<EntityModel<Map<String,List<Tag>>>> getGiftOneTagPageById(@PathVariable("id") Integer id, @PathVariable("page") Integer currentPage) {
        return giftController.getGiftOneTagPageById(id,currentPage);
    }

    @GetMapping(GIFT + "/{id}")
    public ResponseEntity<EntityModel<Map<String,List<Tag>>>> getGiftFirstTagPageById(@PathVariable("id") Integer id){
        return getGiftOneTagPageById(id, 1);
    }

    @GetMapping(TAGS + "/{pageNumber}")
    public ResponseEntity<CollectionModel<EntityModel<Tag>>> getOneTagPage(@PageableDefault(size = 10, sort = "id") Pageable pageable,
                                                                           @PathVariable("pageNumber") Integer currentPage){
        return tagController.getOneTagPage(pageable,currentPage);
    }
    @GetMapping(TAGS)
    public ResponseEntity<CollectionModel<EntityModel<Tag>>> getFirstTagPage(){
        return getOneTagPage(PageRequest.of(1, 10),1);
    }

    @PostMapping("/createUser")
    public HttpEntity<EntityModel<User>> createUser(@RequestBody User user) {
        return userController.createUser(user);
    }

    @GetMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id")  Integer id) {
        return userController.deleteUser(id);
    }


    @PostMapping("/createGift")
    public HttpEntity<EntityModel<GiftCertificate>> createGift(@RequestBody GiftCertificate giftCertificate) {
        return giftController.createGift(giftCertificate);
    }

    @PutMapping("/updateGift/{id}")
    public ResponseEntity<Object> updateGift(@PathVariable("id") Integer id, @RequestBody GiftCertificate updatedGiftCertificate) {
        return giftController.updateGift(id, updatedGiftCertificate);
    }

    @GetMapping("/deleteGift/{id}")
    public ResponseEntity<Void> deleteGift(@PathVariable("id")  Integer id) {
        return giftController.deleteGift(id);
    }

    @PostMapping("/saveTag/{id}")
    public HttpEntity<EntityModel<Tag>> createTag(@RequestBody Tag tag, @PathVariable("id")  Integer id) {
        return tagController.createTag(tag, id);
    }

    @GetMapping("/deleteTag/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable("id") Integer id) {
        return tagController.deleteTag(id);
    }

    @PostMapping("/saveOrder/{id}")
    public ResponseEntity<EntityModel<GiftCertificate>> createCert(@RequestBody GiftCertificate gift, @PathVariable("id")  Integer id) {
        return userController.createCert(gift, id);
    }

    @GetMapping("/deleteOrder/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Integer id) {
        return userController.deleteOrder(id);
    }

    @GetMapping("/userOrderInfo/{userId}/{page}")
    public ResponseEntity<CollectionModel<EntityModel<Map<String, Object>>>> getUserOrderInfo(
            @PathVariable("userId") Integer userId, @PathVariable("page") Integer page,
            @PageableDefault(size = 5, sort = "id_user") Pageable pageable) {
        return userController.getUserOrderInfo(userId,page,pageable);
    }
    @GetMapping("/userOrderInfo/{userId}")
    public ResponseEntity<CollectionModel<EntityModel<Map<String, Object>>>> getFirstUserOrderInfo(@PathVariable("userId") Integer userId, @PageableDefault(size = 5, sort = "id_user") Pageable pageable) {
        return getUserOrderInfo(userId, 0, pageable);
    }
    @GetMapping("/userMostUsedTag/{userId}")
    public ResponseEntity<String> getUserMostUsedTag(@PathVariable("userId") Integer userId) {
        return userController.getUserMostUsedTag(userId);
    }

    @GetMapping("/filterByTag/{tags}/{pageNumber}")
    public ResponseEntity<CollectionModel<EntityModel<GiftCertificate>>> searchGiftsByTags(
            @PathVariable String tags,
            @PathVariable int pageNumber,
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {
        return giftController.searchGiftsByTags(tags,pageNumber, pageable);
    }

    @GetMapping("/filterByTag/{tags}")
    public ResponseEntity<CollectionModel<EntityModel<GiftCertificate>>> searchGiftsByTags(
            @PathVariable String tags,
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {

        // Call the method for the first page
        return searchGiftsByTags(tags, 0, pageable);
    }
}