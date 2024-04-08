package com.epam.esm.controller.entity;
import com.epam.esm.controller.MainController;
import com.epam.esm.controller.error.PageNotFoundException;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.GiftTag;
import com.epam.esm.model.entity.Tag;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@RestController
@RequestMapping("/SpringRESTBoot/api/v1/gifts")
public class GiftController extends MainController {

    @Autowired
    private GiftService giftService;
    private TagService tagService;
    @Autowired
    private GiftTagService gtService;
    @GetMapping("/{pageNumber}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<CollectionModel<EntityModel<GiftCertificate>>> getOneGiftPage(@PageableDefault(size = 10, sort = "id") Pageable pageable,
                                                                                        @PathVariable(value = "pageNumber" ) Integer currentPage){
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

    @GetMapping("/")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<CollectionModel<EntityModel<GiftCertificate>>> getFirstGiftPage(){
        return getOneGiftPage(PageRequest.of(1, 5),1);
    }

    //@PreAuthorize("hasRole('ADMIN')") // ROLE_ADMIN
    //@PreAuthorize("hasAuthority('ADMIN')") // ADMIN
    @PostMapping("/")
    @CrossOrigin(origins = "http://localhost:3000")
    public HttpEntity<EntityModel<GiftCertificate>> createGift(@RequestBody GiftCertificate giftCertificate) {
        GiftCertificate savedGift = giftService.createGift(giftCertificate);

        URI location =ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedGift.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Object> updateGift(@PathVariable("id") Integer id, @RequestBody GiftCertificate updatedGiftCertificate) {
        Optional<GiftCertificate> giftOptional = Optional.of(giftService.getGiftById(id));

        if (giftOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        giftService.updateGift(id, updatedGiftCertificate);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Void> deleteGift(@PathVariable("id")  Integer id) {
        //Delete all records in GiftTag with the specified id for Gift Certificate, then delete the Gift Certificate
        gtService.deleteGTByGift(id);
        giftService.deleteGift(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/tags/{pageNumber}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<CollectionModel<EntityModel<GiftCertificate>>> searchGiftsByTags(
            @RequestParam(value="tag[]") String[] tags,
            @PathVariable int pageNumber,
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {

        List<String> stringList = Arrays.stream(tags).toList();
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
    @GetMapping("/tags")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<CollectionModel<EntityModel<GiftCertificate>>> searchGiftsByTags(
            @RequestParam(value="tag[]") String[] tags,
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {

        // Call the method for the first page
        return searchGiftsByTags(tags, 0, pageable);
    }

    @GetMapping("/searchByName/{name}/{pageNumber}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<CollectionModel<EntityModel<GiftCertificate>>> searchGiftsByName(
            @PathVariable String name,
            @PathVariable(value = "pageNumber" ) Integer currentPage,
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {

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
        List<GiftCertificate> gifts = page.getContent().stream()
                .filter(e -> name.contains(e.getName()))
                .toList();
        List<Link> links = new ArrayList<>();


        // Self link
        links.add(WebMvcLinkBuilder.linkTo(methodOn(GiftController.class).searchGiftsByName(name, numPage, pageable)).withSelfRel());

        // First page link
        links.add(WebMvcLinkBuilder.linkTo(methodOn(GiftController.class).searchGiftsByName(name,1,PageRequest.of(1, pageable.getPageSize(), pageable.getSort()))).withRel("first"));

        // Last page link
        links.add(WebMvcLinkBuilder.linkTo(methodOn(GiftController.class).searchGiftsByName(name,1,PageRequest.of(totalPages - 1, pageable.getPageSize(), pageable.getSort()))).withRel("last"));

        // Next page link
        if (page.hasNext()) {
            links.add(WebMvcLinkBuilder.linkTo(methodOn(GiftController.class).searchGiftsByName(name,numPage + 1, page.nextPageable())).withRel("next"));
        }

        // Previous page link
        if (page.hasPrevious()) {
            links.add(WebMvcLinkBuilder.linkTo(methodOn(GiftController.class).searchGiftsByName(name,numPage - 1, page.previousPageable())).withRel("prev"));
        }

        List<EntityModel<GiftCertificate>> giftResources = gifts.stream()
                .map(gift -> EntityModel.of(gift,
                        WebMvcLinkBuilder.linkTo(methodOn(GiftDetailController.class).getGiftOneTagPageById(gift.getId(), numPage)).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<GiftCertificate>> response = CollectionModel.of(giftResources, links);

        return ResponseEntity.ok(response);
    }

}
