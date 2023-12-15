package com.epam.esm.repo;

import com.epam.esm.model.entity.GiftTag;
import com.epam.esm.model.entity.GiftTagID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftTagRepository extends JpaRepository<GiftTag, GiftTagID> {
}