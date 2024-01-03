package com.EF2.prohomesolutions.mappers;

import com.EF2.prohomesolutions.dtos.UserLoginDTO;
import com.EF2.prohomesolutions.enums.Role;
import com.EF2.prohomesolutions.enums.UserType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ProviderMapper {

    public UserLoginDTO providerToUserLoginDTO(List<Object[]> provider){
        UserLoginDTO userLoginDTO = UserLoginDTO.builder()
                .id((String) provider.get(0)[0])
                .email((String) provider.get(0)[1])
                .password((String) provider.get(0)[2])
                .role(Role.valueOf((String)provider.get(0)[3]))
                .userType(UserType.valueOf((String)provider.get(0)[4]))
                .enable((Boolean) provider.get(0)[5])
                .build();
        return userLoginDTO;

    }

}
