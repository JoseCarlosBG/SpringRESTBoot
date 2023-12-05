package com.epam.esm.service;

import com.epam.esm.model.User;
import com.epam.esm.repo.UserGiftRepository;
import com.epam.esm.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserGiftRepository userGiftRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public Page<User> findPage(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return userRepository.findAll(pageable);
    }

    public Page<Map<String, Object>> getUserOrderInfo(Integer userId, Pageable pageable) {
        //This method requires custom implementation in the ugRepository
        return userGiftRepository.getUserOrderInfo(userId, pageable);
    }

    public String getUserMostUsedTag(Integer userId) {
        //This method requires custom implementation in the ugRepository
        return userGiftRepository.getUserMostUsedTag(userId);
    }
}
