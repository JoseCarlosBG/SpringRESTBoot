package com.epam.esm;

import com.epam.esm.controller.MainController;
import com.epam.esm.controller.PageNotFoundException;
import com.epam.esm.model.*;
import com.epam.esm.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private GiftService giftService;
    @Mock
    private TagService tagService;
    @Mock
    private GiftTagService gtService;
    @Mock
    private UserGiftService ugService;

    @InjectMocks
    private MainController mainController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMainPage() {
        ResponseEntity<EntityModel<Map<String, Object>>> response = mainController.mainPage();
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        // Add more assertions based on your specific implementation
    }

    // Add more test methods for other endpoints in MainController

    // For example:
    @Test
    void testGetOneUserPage() {
        // Mock data and behavior for UserService
        Page<User> mockPage = mock(Page.class);
        when(mockPage.getContent()).thenReturn(Collections.singletonList(new User()));
        when(userService.findPage(anyInt())).thenReturn(mockPage);

        // Test the controller method
        ResponseEntity<CollectionModel<EntityModel<User>>> response = mainController.getOneUserPage(PageRequest.of(1, 10), 1);

        // Assertions
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        // Add more assertions based on your specific implementation
    }

    // Add more test methods for other controller methods

    // For example:
    @Test
    void testCreateUser() {
        // Mock data and behavior for UserService
        User mockUser = new User();
        when(userService.createUser(any(User.class))).thenReturn(mockUser);

        // Test the controller method
        ResponseEntity<EntityModel<User>> response = (ResponseEntity<EntityModel<User>>) mainController.createUser(new User());

        // Assertions
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        // Add more assertions based on your specific implementation
    }

    // Add more test methods for other controller methods
}
