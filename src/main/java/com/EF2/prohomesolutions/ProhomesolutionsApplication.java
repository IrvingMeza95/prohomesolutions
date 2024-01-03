package com.EF2.prohomesolutions;

import com.EF2.prohomesolutions.enums.UserType;
import com.EF2.prohomesolutions.exceptions.MyException;
import com.EF2.prohomesolutions.models.User;
import com.EF2.prohomesolutions.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProhomesolutionsApplication {

    public ProhomesolutionsApplication() throws MyException {
    }

    public static void main(String[] args) {
		SpringApplication.run(ProhomesolutionsApplication.class, args);
	}





}
