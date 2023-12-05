package com.epam.esm;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.repo.GiftRepository;
import com.epam.esm.service.GiftService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class GiftCertificateServiceTest {

    @InjectMocks
    private GiftService giftService;

    @Mock
    private GiftRepository giftRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllGifts() {
        // Arrange
        List<GiftCertificate> giftCertificateList = new ArrayList<>();
        when(giftRepository.findAll()).thenReturn(giftCertificateList);

        // Act
        List<GiftCertificate> result = giftService.getAllGifts();

        // Assert
        assertNotNull(result);
        assertEquals(giftCertificateList, result);
    }

    @Test
    public void testGetGiftById_ExistingItem() {
        // Arrange
        Integer id = 1;
        GiftCertificate giftCertificate = new GiftCertificate();
        when(giftRepository.findById(id)).thenReturn(Optional.of(giftCertificate));

        // Act
        GiftCertificate result = giftService.getGiftById(id);

        // Assert
        assertNotNull(result);
        assertEquals(giftCertificate, result);
    }

    @Test
    public void testGetGiftById_NonExistingItem() {
        // Arrange
        Integer id = 1;
        when(giftRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        GiftCertificate result = giftService.getGiftById(id);

        // Assert
        assertNull(result);
    }

    @Test
    public void testCreateGift() {
        // Arrange
        GiftCertificate giftCertificate = new GiftCertificate();
        when(giftRepository.save(giftCertificate)).thenReturn(giftCertificate);

        // Act
        GiftCertificate result = giftService.createGift(giftCertificate);

        // Assert
        assertNotNull(result);
        assertEquals(giftCertificate, result);
    }

    @Test
    public void testUpdateGift_ExistingItem() {
        // Arrange
        Integer id = 1;
        GiftCertificate giftCertificate = new GiftCertificate();
        when(giftRepository.findById(id)).thenReturn(Optional.of(giftCertificate));

        // Act
        GiftCertificate result = giftService.updateGift(id, giftCertificate);

        // Assert
        assertNotNull(result);
        assertEquals(giftCertificate, result);
    }

    @Test
    public void testUpdateGift_NonExistingItem() {
        // Arrange
        Integer id = 1;
        when(giftRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        GiftCertificate result = giftService.updateGift(id, new GiftCertificate());

        // Assert
        assertNull(result);
    }

    @Test
    public void testDeleteGift() {
        // Arrange
        Integer id = 1;

        // Act
        giftService.deleteGift(id);

        // Assert
        verify(giftRepository, times(1)).deleteById(id);
    }
}