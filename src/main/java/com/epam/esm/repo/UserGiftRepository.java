package com.epam.esm.repo;

import com.epam.esm.model.entity.UserGift;
import com.epam.esm.model.entity.UserGiftID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Map;

public interface UserGiftRepository extends JpaRepository<UserGift, UserGiftID> {
    @Query(nativeQuery = true, value =
            "SELECT ug.id_user, ug.id_gift, ug.name, ug.cost, ug.create_date, gc.name as gift_name, gc.description as gift_description " +
                    "FROM UserGift ug " +
                    "JOIN Gift_Certificate gc ON ug.id_gift = gc.id " +
                    "WHERE ug.id_user = :userId")
    Page<Map<String, Object>> getUserOrderInfo(@Param("userId") Integer userId, Pageable pageable);

    @Query(nativeQuery = true, value =
            "SELECT t.name as tag_name, MAX(ug.cost) as max_cost " +
                    "FROM UserGift ug " +
                    "JOIN Gift_Certificate gc ON ug.id_gift = gc.id " +
                    "JOIN Gift_Tag gt ON gc.id = gt.id_gift " +
                    "JOIN Tag t ON gt.id_tag = t.id " +
                    "WHERE ug.id_user = :userId " +
                    "GROUP BY t.name " +
                    "ORDER BY max_cost DESC " +
                    "LIMIT 1")
    String getUserMostUsedTag(@Param("userId") Integer userId);
}
