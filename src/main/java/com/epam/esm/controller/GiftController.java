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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@RestController
@RequestMapping("/SpringRESTBoot")
public class GiftController {

    @Autowired
    private GiftService giftService;
    private TagService tagService;
    @Autowired
    private GiftTagService gtService;
    private Integer currentUserId=0; //to keep track of our current User ID, in order to correctly add the records
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
        links.add(WebMvcLinkBuilder.linkTo(methodOn(MainController.class).getOneGiftPage(pageable, numPage)).withSelfRel());

        // First page link
        links.add(WebMvcLinkBuilder.linkTo(methodOn(MainController.class).getOneGiftPage(PageRequest.of(1, pageable.getPageSize(), pageable.getSort()), 1)).withRel("first"));

        // Last page link
        links.add(WebMvcLinkBuilder.linkTo(methodOn(MainController.class).getOneGiftPage(PageRequest.of(totalPages - 1, pageable.getPageSize(), pageable.getSort()), totalPages)).withRel("last"));

        // Next page link
        if (page.hasNext()) {
            links.add(WebMvcLinkBuilder.linkTo(methodOn(MainController.class).getOneGiftPage(page.nextPageable(), numPage + 1)).withRel("next"));
        }

        // Previous page link
        if (page.hasPrevious()) {
            links.add(WebMvcLinkBuilder.linkTo(methodOn(MainController.class).getOneGiftPage(page.previousPageable(),numPage -1)).withRel("prev"));
        }

        List<EntityModel<GiftCertificate>> giftResources = gifts.stream()
                .map(gift -> EntityModel.of(gift,
                        WebMvcLinkBuilder.linkTo(methodOn(MainController.class).getGiftOneTagPageById(gift.getId(), numPage)).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<GiftCertificate>> response = CollectionModel.of(giftResources, links);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<EntityModel<Map<String,List<Tag>>>> getGiftOneTagPageById(@PathVariable("id") Integer id, @PathVariable("page") Integer currentPage) {
        GiftCertificate gift= giftService.getGiftById(id);
        if (gift==null){
            throw new PageNotFoundException("Page not found", id);
        }
        Page<Tag> page;

        int numPage;
        if(currentPage <= 0){
            throw new PageNotFoundException("Page not found", currentPage);
        } else {
            numPage = currentPage;
        }
        page = tagService.findPage(currentPage);
        int totalPages = page.getTotalPages();
        if (totalPages < currentPage){
            throw new PageNotFoundException("Page not found", currentPage);
        }
        List<GiftTag> listGt = gtService.getGTByGiftId(id);
        List<Tag> listTag = page.getContent().stream()
                .filter(tag -> listGt.stream().anyMatch(gt
                        -> tag.getId().equals(gt.getIdTag()))).toList();

        currentUserId=id;
        List<Link> links = new ArrayList<>();

        // Self link
        links.add(linkTo(methodOn(MainController.class).getGiftOneTagPageById(id, numPage)).withSelfRel());

        // First page link
        links.add(linkTo(methodOn(MainController.class).getGiftOneTagPageById(id, 1)).withRel("first"));

        // Last page link
        links.add(linkTo(methodOn(MainController.class).getGiftOneTagPageById(id, totalPages)).withRel("last"));

        // Next page link
        if (page.hasNext()) {
            links.add(linkTo(methodOn(MainController.class).getGiftOneTagPageById(id, numPage + 1)).withRel("next"));
        }

        // Previous page link
        if (page.hasPrevious()) {
            links.add(linkTo(methodOn(MainController.class).getGiftOneTagPageById(id, numPage - 1)).withRel("prev"));
        }
        Map<String, List<Tag>> tagDetails = new HashMap<>();
        tagDetails.put(gift.getName(), listTag);
        EntityModel<Map<String,List<Tag>>> giftResource = EntityModel.of(tagDetails, links);

        giftResource.add(linkTo(methodOn(MainController.class).getOneGiftPage(PageRequest.of(1, 5), numPage)).withRel("gifts"));

        return ResponseEntity.ok(giftResource);
    }

    public HttpEntity<EntityModel<GiftCertificate>> createGift(@RequestBody GiftCertificate giftCertificate) {
        GiftCertificate savedGift = giftService.createGift(giftCertificate);

        URI location =ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedGift.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    public ResponseEntity<Object> updateGift(@PathVariable("id") Integer id, @RequestBody GiftCertificate updatedGiftCertificate) {
        Optional<GiftCertificate> giftOptional = Optional.of(giftService.getGiftById(id));

        if (giftOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        giftService.updateGift(id, updatedGiftCertificate);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> deleteGift(@PathVariable("id")  Integer id) {
        //Delete all records in GiftTag with the specified id for Gift Certificate, then delete the Gift Certificate
        gtService.deleteGTByGift(id);
        giftService.deleteGift(id);

        return ResponseEntity.ok().build();
    }

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
                        linkTo(methodOn(MainController.class).searchGiftsByTags(tags, pageNumber, pageable)).withSelfRel()))
                .collect(Collectors.toList());

        List<Link> links = new ArrayList<>();

        // Self link
        links.add(linkTo(methodOn(MainController.class).searchGiftsByTags(tags, pageNumber, pageable)).withSelfRel());

        // First page link
        links.add(linkTo(methodOn(MainController.class).searchGiftsByTags(tags, 0, pageable)).withRel("first"));

        // Last page link
        links.add(linkTo(methodOn(MainController.class).searchGiftsByTags(tags,
                giftPage.getTotalPages() - 1, pageable)).withRel("last"));

        // Next page link
        if (giftPage.hasNext()) {
            links.add(linkTo(methodOn(MainController.class).searchGiftsByTags(tags,
                    pageNumber + 1, pageable)).withRel("next"));
        }

        // Previous page link
        if (giftPage.hasPrevious()) {
            links.add(linkTo(methodOn(MainController.class).searchGiftsByTags(tags,
                    pageNumber - 1, pageable)).withRel("prev"));
        }

        CollectionModel<EntityModel<GiftCertificate>> response = CollectionModel.of(giftResources, links);

        return ResponseEntity.ok(response);
    }
}
