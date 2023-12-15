package com.epam.esm.model.request;

import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.*;

public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private @Getter @Setter String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private @Getter @Setter String email;

    private @Getter @Setter Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private @Getter @Setter String password;
}