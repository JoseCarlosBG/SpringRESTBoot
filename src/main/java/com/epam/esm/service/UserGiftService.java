package com.epam.esm.service;

import com.epam.esm.model.entity.UserGift;
import com.epam.esm.repo.UserGiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserGiftService {
    @Autowired
    private UserGiftRepository ugRepository;
    public List<UserGift> getAllUGs() {
        return ugRepository.findAll();
    }

    public List<UserGift> getUGByGiftId(Integer id) {
        return ugRepository.findAll().stream().filter(ug -> ug.getIdGift().equals(id)).toList();
    }

    public List<UserGift> getUGByUserId(Integer id) {
        return ugRepository.findAll().stream().filter(ug -> ug.getIdUser().equals(id)).toList();
    }
    public UserGift getUGByGiftAndTag(Integer idGift, Integer idTag){
        //Obtain a list of all the records with the specified idTag
        List<UserGift> listGifts = ugRepository.findAll().stream().filter(ug -> ug.getIdUser().equals(idTag)).toList();
        //Return the record of the previous list with the specified idGift
        return listGifts.stream().filter(ug->ug.getIdGift().equals(idGift)).toList().get(0);
    }
    public UserGift createUG(UserGift ug) {
        return ugRepository.save(ug);
    }

    public void deleteUGByUser(Integer id) { //Deletes all records with the specified Tag ID
        List<UserGift> ugs = ugRepository.findAll().stream().filter(ug -> ug.getIdUser().equals(id)).toList();
        for (UserGift ug: ugs) {
            ugRepository.delete(ug);
        }
    }
    public void deleteUGByGift(Integer id) { //Deletes all records with the specified Gift ID
        List<UserGift> ugs = ugRepository.findAll().stream().filter(ug -> ug.getIdGift().equals(id)).toList();
        for (UserGift ug: ugs) {
            ugRepository.delete(ug);
        }
    }

    public Page<UserGift> findPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return ugRepository.findAll(pageable);
    }


}
