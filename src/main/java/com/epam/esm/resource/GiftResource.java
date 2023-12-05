package com.epam.esm.resource;

import com.epam.esm.model.GiftCertificate;
import org.springframework.hateoas.EntityModel;

public class GiftResource extends EntityModel<GiftCertificate> {
    public GiftResource(GiftCertificate content) {
        super(content);
    }
}
