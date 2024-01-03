package com.EF2.prohomesolutions.controllers;

import com.EF2.prohomesolutions.enums.ProviderType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping
public class IndexController {

    @GetMapping
    public String index(HttpSession session, ModelMap model){
        model.put("providerTypes", ProviderType.values());
        return "Principal";
    }

    @GetMapping("/contact-us")
    public String contactUs(HttpSession session, ModelMap model){
        return "Contact_us";
    }

}
