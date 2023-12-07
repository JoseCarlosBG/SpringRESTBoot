package com.epam.esm.controller;

import com.epam.esm.model.GiftTag;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftTagService;
import com.epam.esm.service.TagService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/SpringRESTBoot/tags")
public class TagController {
    @Autowired
    private TagService tagService;
    @Autowired
    private GiftTagService gtService;
    @GetMapping("/{pageNumber}")
    public ResponseEntity<CollectionModel<EntityModel<Tag>>> getOneTagPage(@PageableDefault(size = 10, sort = "id") Pageable pageable,
                                                                           @PathVariable("pageNumber") Integer currentPage){

        // Find the tag page using the provided Pageable
        Page<Tag> page;

        int numPage;
        if(currentPage <= 0){
            throw new PageNotFoundException("Page not found", currentPage);
        } else {
            numPage = currentPage;
        }
        page = tagService.findPage(currentPage);

        List<Tag> tags = page.getContent();

        int totalPages = page.getTotalPages();
        if (totalPages < currentPage){
            throw new PageNotFoundException("Page not found", currentPage);
        }

        List<Link> links = new ArrayList<>();

        // Self link
        links.add(WebMvcLinkBuilder.linkTo(methodOn(TagController.class).getOneTagPage(pageable, numPage)).withSelfRel());

        // First page link
        links.add(WebMvcLinkBuilder.linkTo(methodOn(TagController.class).getOneTagPage(PageRequest.of(1, pageable.getPageSize(), pageable.getSort()), 1)).withRel("first"));

        // Last page link
        links.add(WebMvcLinkBuilder.linkTo(methodOn(TagController.class).getOneTagPage(PageRequest.of(totalPages - 1, pageable.getPageSize(), pageable.getSort()), totalPages)).withRel("last"));

        // Next page link
        if (page.hasNext()) {
            links.add(WebMvcLinkBuilder.linkTo(methodOn(TagController.class).getOneTagPage(page.nextPageable(), numPage + 1)).withRel("next"));
        }

        // Previous page link
        if (page.hasPrevious()) {
            links.add(WebMvcLinkBuilder.linkTo(methodOn(TagController.class).getOneTagPage(page.previousPageable(),numPage -1)).withRel("prev"));
        }

        List<EntityModel<Tag>> tagResources = tags.stream()
                .map(tag -> EntityModel.of(tag,
                        WebMvcLinkBuilder.linkTo(methodOn(GiftDetailController.class).getGiftOneTagPageById(tag.getId(), numPage)).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Tag>> response = CollectionModel.of(tagResources, links);

        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<Tag>>> getFirstTagPage(){
        return getOneTagPage(PageRequest.of(1, 10),1);
    }

    @PostMapping("/saveTag/{id}")
    public HttpEntity<EntityModel<Tag>> createTag(@RequestBody Tag tag, @PathVariable("id")  Integer id) {
        List<Tag> tagList = tagService.getAllTags().stream().filter(t -> t.getName().equals(tag.getName())).toList();
        Tag tag1;

        if (tagList.isEmpty() || tagList.get(0) == null) {
            tagService.createTag(tag);
            tag1 = tagService.getAllTags().stream().filter(t -> t.getName().equals(tag.getName())).toList().get(0);
        } else {
            tag1 = tagList.get(0);
        }

        GiftTag gt = new GiftTag(id, tag1.getId());

        gtService.createGT(gt);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{idGift}/{idTag}").buildAndExpand(gt.getIdGift(),gt.getIdTag()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/deleteTag/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable("id") Integer id) {
        gtService.deleteGTByTag(id);
        tagService.deleteTag(id);

        return ResponseEntity.noContent().build();
    }
}
