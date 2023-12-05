package com.epam.esm.service;

import com.epam.esm.model.GiftTag;
import com.epam.esm.model.Tag;
import com.epam.esm.repo.GiftTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftTagService {
    @Autowired
    private GiftTagRepository gtRepository;

    public List<GiftTag> getAllGTs() {
        return gtRepository.findAll();
    }

    public List<GiftTag> getGTByGiftId(Integer id) {
        return gtRepository.findAll().stream().filter(gt -> gt.getIdGift().equals(id)).toList();
    }

    public List<GiftTag> getGTByTagId(Integer id) {
        return gtRepository.findAll().stream().filter(gt -> gt.getIdTag().equals(id)).toList();
    }
    public GiftTag getGTByGiftAndTag(Integer idGift, Integer idTag){
        //Obtain a list of all the records with the specified idTag
        List<GiftTag> listGifts = gtRepository.findAll().stream().filter(gt -> gt.getIdTag().equals(idTag)).toList();
        //Return the record of the previous list with the specified idGift
        return listGifts.stream().filter(gt->gt.getIdGift().equals(idGift)).toList().get(0);
    }
    public GiftTag createGT(GiftTag gt) {
        return gtRepository.save(gt);
    }

    public void deleteGTByGift(Integer id) { //Deletes all records with the specified Gift ID
        List<GiftTag> gts = gtRepository.findAll().stream().filter(gt -> gt.getIdGift().equals(id)).toList();
        for (GiftTag gt: gts) {
            gtRepository.delete(gt);
        }
    }

    public void deleteGTByTag(Integer id) { //Deletes all records with the specified Tag ID
        List<GiftTag> gts = gtRepository.findAll().stream().filter(gt -> gt.getIdTag().equals(id)).toList();
        for (GiftTag gt: gts) {
            gtRepository.delete(gt);
        }
    }

    public List<GiftTag> getGTByTagList(List<Tag> tagList){
        return gtRepository.findAll().
                stream().filter(e->
                        tagList.stream().map(Tag::getId).toList().
                                contains(e.getIdTag())).collect(Collectors.toList());
    }

    public Page<GiftTag> findPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return gtRepository.findAll(pageable);
    }
}
