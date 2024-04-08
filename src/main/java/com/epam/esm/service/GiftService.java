package com.epam.esm.service;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.repo.GiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GiftService {

    @Autowired
    private GiftRepository giftRepository;

    public List<GiftCertificate> getAllGifts() {
        return giftRepository.findAll();
    }

    public GiftCertificate getGiftById(Integer id) {
        return giftRepository.findById(id).orElse(null);
    }

    public GiftCertificate createGift(GiftCertificate giftCertificate) {
        return giftRepository.save(giftCertificate);
    }

    public GiftCertificate updateGift(Integer id, GiftCertificate updatedGiftCertificate) {
        GiftCertificate giftCertificate = giftRepository.findById(id).orElse(null);
        if (giftCertificate != null) {
            giftCertificate.setName(updatedGiftCertificate.getName());
            giftCertificate.setDescription(updatedGiftCertificate.getDescription());
            giftCertificate.setPrice(updatedGiftCertificate.getPrice());
            giftCertificate.setDuration(updatedGiftCertificate.getDuration());
            giftCertificate.setCreateDate(updatedGiftCertificate.getCreateDate());
            giftCertificate.setLastUpdateDate(updatedGiftCertificate.getLastUpdateDate());
            return giftRepository.save(giftCertificate);
        }
        return null;
    }

    public void deleteGift(Integer id) {
        giftRepository.deleteById(id);
    }

    public Page<GiftCertificate> findPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return giftRepository.findAll(pageable);
    }

    public Page<GiftCertificate> findGiftsByTagIds(List<Integer> tagIds, Pageable pageable) {
        Set<Integer> uniqueTagIds = tagIds.stream().collect(Collectors.toSet());
        return giftRepository.findByGiftTags_IdTagIn(uniqueTagIds, pageable);
    }

    public Page<GiftCertificate> findGiftsByNameContaining(String namePattern, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return giftRepository.findByNameContaining(namePattern, pageable);
    }
}