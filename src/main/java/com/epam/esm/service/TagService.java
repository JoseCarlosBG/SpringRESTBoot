package com.epam.esm.service;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.repo.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public List<Tag> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags;
    }

    public Tag getTagById(Integer id) {
        return tagRepository.findById(id).orElse(null);
    }

    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag updateTag(Integer id, Tag updatedTag) {
        Tag gift = tagRepository.findById(id).orElse(null);
        if (gift != null) {
            gift.setName(updatedTag.getName());
            return tagRepository.save(gift);
        }
        return null;
    }

    public void deleteTag(Integer id) {
        tagRepository.deleteById(id);
    }

    public Page<Tag> findPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return tagRepository.findAll(pageable);
    }
}
