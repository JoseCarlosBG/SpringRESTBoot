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
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@RestController
@RequestMapping("/SpringRESTBoot/gifts")
public class GiftController extends MainController{

    @Autowired
    private GiftService giftService;
    private TagService tagService;
    @Autowired
    private GiftTagService gtService;
    @GetMapping("/{pageNumber}")
    public ResponseEntity<CollectionModel<EntityModel<GiftCertificate>>> getOneGiftPage(@PageableDefault(size = 10, sort = "id") Pageable pageable,
                                                                                        @PathVariable("pageNumber") Integer currentPage){
        Page<GiftCertificate> page;
        int numPage;
        if(currentPage <= 0){
            throw new PageNotFoundException("Page not found", currentPage);
        } else {
            numPage = currentPage;
        }
        page = giftService.findPage(currentPage);

        int totalPages = page.getTotalPages();
        if (totalPages < currentPage){
            throw new PageNotFoundException("Page not found", currentPage);
        }
        List<GiftCertificate> gifts = page.getContent();
        List<Link> links = new ArrayList<>();

        // Self link
        links.add(WebMvcLinkBuilder.linkTo(methodOn(GiftController.class).getOneGiftPage(pageable, numPage)).withSelfRel());

        // First page link
        links.add(WebMvcLinkBuilder.linkTo(methodOn(GiftController.class).getOneGiftPage(PageRequest.of(1, pageable.getPageSize(), pageable.getSort()), 1)).withRel("first"));

        // Last page link
        links.add(WebMvcLinkBuilder.linkTo(methodOn(GiftController.class).getOneGiftPage(PageRequest.of(totalPages - 1, pageable.getPageSize(), pageable.getSort()), totalPages)).withRel("last"));

        // Next page link
        if (page.hasNext()) {
            links.add(WebMvcLinkBuilder.linkTo(methodOn(GiftController.class).getOneGiftPage(page.nextPageable(), numPage + 1)).withRel("next"));
        }

        // Previous page link
        if (page.hasPrevious()) {
            links.add(WebMvcLinkBuilder.linkTo(methodOn(GiftController.class).getOneGiftPage(page.previousPageable(),numPage -1)).withRel("prev"));
        }

        List<EntityModel<GiftCertificate>> giftResources = gifts.stream()
                .map(gift -> EntityModel.of(gift,
                        WebMvcLinkBuilder.linkTo(methodOn(GiftDetailController.class).getGiftOneTagPageById(gift.getId(), numPage)).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<GiftCertificate>> response = CollectionModel.of(giftResources, links);

        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<GiftCertificate>>> getFirstGiftPage(){
        return getOneGiftPage(PageRequest.of(1, 5),1);
    }

    @PostMapping("/createGift")
    public HttpEntity<EntityModel<GiftCertificate>> createGift(@RequestBody GiftCertificate giftCertificate) {
        GiftCertificate savedGift = giftService.createGift(giftCertificate);

        URI location =ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedGift.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/updateGift/{id}")
    public ResponseEntity<Object> updateGift(@PathVariable("id") Integer id, @RequestBody GiftCertificate updatedGiftCertificate) {
        Optional<GiftCertificate> giftOptional = Optional.of(giftService.getGiftById(id));

        if (giftOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        giftService.updateGift(id, updatedGiftCertificate);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/deleteGift/{id}")
    public ResponseEntity<Void> deleteGift(@PathVariable("id")  Integer id) {
        //Delete all records in GiftTag with the specified id for Gift Certificate, then delete the Gift Certificate
        gtService.deleteGTByGift(id);
        giftService.deleteGift(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/filterByTag/{tags}/{pageNumber}")
    public ResponseEntity<CollectionModel<EntityModel<GiftCertificate>>> searchGiftsByTags(
            @PathVariable String tags,
            @PathVariable int pageNumber,
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {

        List<String> stringList = Arrays.asList(tags.split("-"));
        List<Tag> tagSearch = tagService.getAllTags().stream()
                .filter(e -> stringList.contains(e.getName()))
                .collect(Collectors.toList());
        List<GiftTag> GTList = gtService.getGTByTagList(tagSearch);

        Page<GiftCertificate> giftPage = giftService.findGiftsByTagIds(
                GTList.stream().map(GiftTag::getIdGift).toList(),
                PageRequest.of(pageNumber, pageable.getPageSize(), pageable.getSort())
        );

        List<EntityModel<GiftCertificate>> giftResources = giftPage.getContent().stream()
                .map(gift -> EntityModel.of(gift,
                        linkTo(methodOn(GiftController.class).searchGiftsByTags(tags, pageNumber, pageable)).withSelfRel()))
                .collect(Collectors.toList());

        List<Link> links = new ArrayList<>();

        // Self link
        links.add(linkTo(methodOn(GiftController.class).searchGiftsByTags(tags, pageNumber, pageable)).withSelfRel());

        // First page link
        links.add(linkTo(methodOn(GiftController.class).searchGiftsByTags(tags, 0, pageable)).withRel("first"));

        // Last page link
        links.add(linkTo(methodOn(GiftController.class).searchGiftsByTags(tags,
                giftPage.getTotalPages() - 1, pageable)).withRel("last"));

        // Next page link
        if (giftPage.hasNext()) {
            links.add(linkTo(methodOn(GiftController.class).searchGiftsByTags(tags,
                    pageNumber + 1, pageable)).withRel("next"));
        }

        // Previous page link
        if (giftPage.hasPrevious()) {
            links.add(linkTo(methodOn(GiftController.class).searchGiftsByTags(tags,
                    pageNumber - 1, pageable)).withRel("prev"));
        }

        CollectionModel<EntityModel<GiftCertificate>> response = CollectionModel.of(giftResources, links);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/filterByTag/{tags}")
    public ResponseEntity<CollectionModel<EntityModel<GiftCertificate>>> searchGiftsByTags(
            @PathVariable String tags,
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {

        // Call the method for the first page
        return searchGiftsByTags(tags, 0, pageable);
    }
}
