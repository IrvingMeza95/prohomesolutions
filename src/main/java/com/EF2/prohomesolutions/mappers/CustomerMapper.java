package com.EF2.prohomesolutions.mappers;

import com.EF2.prohomesolutions.dtos.UserLoginDTO;
import com.EF2.prohomesolutions.enums.Role;
import com.EF2.prohomesolutions.enums.UserType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CustomerMapper {
    public UserLoginDTO customerToUserLoginDTO(List<Object[]> customer){
		UserLoginDTO userLoginDTO = UserLoginDTO.builder()
				.id((String) customer.get(0)[0])
				.email((String) customer.get(0)[1])
        		.password((String) customer.get(0)[2])
        		.role(Role.valueOf((String)customer.get(0)[3]))
				.userType(UserType.valueOf((String) customer.get(0)[4]))
				.enable((Boolean) customer.get(0)[5])
        		.build();
        return userLoginDTO;

    }

}
