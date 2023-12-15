package com.epam.esm.repo;

import com.epam.esm.model.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GiftRepository extends JpaRepository<GiftCertificate, Integer> {
    @Query("SELECT DISTINCT g FROM GiftCertificate g " +
            "JOIN GiftTag gt ON g.id = gt.idGift " +
            "WHERE gt.idTag IN :tagIds")
    Page<GiftCertificate> findByGiftTags_IdTagIn(@Param("tagIds") Set<Integer> tagIds, Pageable pageable);
}