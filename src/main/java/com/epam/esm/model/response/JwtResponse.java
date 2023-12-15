package com.epam.esm.model.response;

import java.util.List;
import lombok.*;

@AllArgsConstructor
public class JwtResponse {
    private @Getter @Setter String token;
    private @Getter @Setter String type = "Bearer";
    private @Getter @Setter Long id;
    private @Getter @Setter String username;
    private @Getter @Setter String email;
    private @Getter List<String> roles;

    public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
