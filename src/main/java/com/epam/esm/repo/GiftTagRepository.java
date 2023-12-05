package com.epam.esm.repo;

import com.epam.esm.model.GiftTag;
import com.epam.esm.model.GiftTagID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftTagRepository extends JpaRepository<GiftTag, GiftTagID> {
}