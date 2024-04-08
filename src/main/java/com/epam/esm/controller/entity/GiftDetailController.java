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
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@RestController
@RequestMapping("/SpringRESTBoot/api/v1/giftDetails")
public class GiftDetailController extends MainController {
    @Autowired
    private GiftService giftService;
    @Autowired
    private TagService tagService;
    @Autowired
    private GiftTagService gtService;
    @GetMapping("/{id}/{page}")
    @CrossOrigin(origins = "http://localhost:3000")
    ResponseEntity<EntityModel<Map<String,List<Tag>>>> getGiftOneTagPageById(@PathVariable("id") Integer id, @PathVariable("page") Integer currentPage) {
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
        links.add(linkTo(methodOn(GiftDetailController.class).getGiftOneTagPageById(id, numPage)).withSelfRel());

        // First page link
        links.add(linkTo(methodOn(GiftDetailController.class).getGiftOneTagPageById(id, 1)).withRel("first"));

        // Last page link
        links.add(linkTo(methodOn(GiftDetailController.class).getGiftOneTagPageById(id, totalPages)).withRel("last"));

        // Next page link
        if (page.hasNext()) {
            links.add(linkTo(methodOn(GiftDetailController.class).getGiftOneTagPageById(id, numPage + 1)).withRel("next"));
        }

        // Previous page link
        if (page.hasPrevious()) {
            links.add(linkTo(methodOn(GiftDetailController.class).getGiftOneTagPageById(id, numPage - 1)).withRel("prev"));
        }
        Map<String, List<Tag>> tagDetails = new HashMap<>();
        tagDetails.put(gift.getName(), listTag);
        EntityModel<Map<String,List<Tag>>> giftResource = EntityModel.of(tagDetails, links);

        giftResource.add(linkTo(methodOn(GiftController.class).getOneGiftPage(PageRequest.of(1, 5), numPage)).withRel("gifts"));

        return ResponseEntity.ok(giftResource);
    }
    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<EntityModel<Map<String,List<Tag>>>> getGiftFirstTagPageById(@PathVariable("id") Integer id){
        return getGiftOneTagPageById(id, 1);
    }
}
