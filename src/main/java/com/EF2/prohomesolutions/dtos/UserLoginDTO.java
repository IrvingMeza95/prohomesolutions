package com.EF2.prohomesolutions.dtos;

import com.EF2.prohomesolutions.enums.Role;
import com.EF2.prohomesolutions.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginDTO {
    private String id;
    private String email;
    private String password;
    private Role role;
    private UserType userType;
    private Boolean enable;
}
