package com.EF2.prohomesolutions.controllers;

import com.EF2.prohomesolutions.dtos.UserLoginDTO;
import com.EF2.prohomesolutions.models.Customer;
import com.EF2.prohomesolutions.models.Provider;
import com.EF2.prohomesolutions.models.User;
import com.EF2.prohomesolutions.services.CustomerService;
import com.EF2.prohomesolutions.services.FileService;
import com.EF2.prohomesolutions.services.ProviderService;
import com.EF2.prohomesolutions.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProviderService providerService;

    @GetMapping("/profile-picture/{id}")
    public ResponseEntity<byte[]> userProfilePicture (@PathVariable String id){
        UserLoginDTO userDTO = userService.loadUserDTOById(id);
        byte[] profilePicture = null;
        if (userDTO.getUserType().name().equals("CUSTOMER")) {
            Customer customer = customerService.findById(userDTO.getId()).get();
            profilePicture = customer.getProfilePicture().getContent();
        }
        if (userDTO.getUserType().name().equals("PROVIDER")) {
            Provider provider = providerService.findById(userDTO.getId()).get();
            profilePicture = provider.getProfilePicture().getContent();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(profilePicture,headers, HttpStatus.OK);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> loadImage (@PathVariable String id){
        UserLoginDTO userDTO = userService.loadUserDTOById(id);
        byte[] image = fileService.getOne(id).getContent();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(image,headers, HttpStatus.OK);
    }

}
