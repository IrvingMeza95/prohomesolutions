package com.EF2.prohomesolutions.controllers;

import com.EF2.prohomesolutions.enums.Country;
import com.EF2.prohomesolutions.enums.FeeTypes;
import com.EF2.prohomesolutions.enums.ProviderType;
import com.EF2.prohomesolutions.enums.UserType;
import com.EF2.prohomesolutions.exceptions.MyException;
import com.EF2.prohomesolutions.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/app/v1")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/sign-up")
    public String signUpForm(ModelMap model){
        model.put("providerTypes", ProviderType.values());
        model.put("feeTypes", FeeTypes.values());
        model.put("countries", Country.values());
        return "Sign_up_form";
    }

    @PostMapping("/sign-up")
    public String signUp(@RequestParam String name, @RequestParam String alias,
                         @RequestParam String tel, @RequestParam String email, @RequestParam String password,
                         @RequestParam String confirmPassword, @RequestParam String userType, @RequestParam String price,
                         @RequestParam String providerType, @RequestParam String feeType, ModelMap model,
                         @RequestParam String country){
        try {
            userService.create(email,password,confirmPassword,name,alias,tel,
                    UserType.valueOf(userType), ProviderType.valueOf(providerType),
                    FeeTypes.valueOf(feeType), price, country);
            model.put("success", "User created successfully.");
        } catch (MyException e) {
            model.put("providerTypes", ProviderType.values());
            model.put("feeTypes", FeeTypes.values());
            model.put("error", e.getMessage());
            model.addAttribute("name", name);
            model.addAttribute("alias", alias);
            model.addAttribute("tel", tel);
            model.addAttribute("email", email);
            return "Sign_up_form";
        }
        return "redirect:/app/v1/login";
    }

    @GetMapping("/login")
    public String loginForm(){
        return "Login_form.html";
    }

}
