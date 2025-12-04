package com.skillforge.platform.payloads;

import com.skillforge.platform.constants.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String email;

    private String password;
    private String firstName;
    private String lastName;
    private UserRole role;
}
