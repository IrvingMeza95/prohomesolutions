package com.EF2.prohomesolutions.controllers;

import com.EF2.prohomesolutions.enums.Country;
import com.EF2.prohomesolutions.enums.FeeTypes;
import com.EF2.prohomesolutions.enums.JobStatus;
import com.EF2.prohomesolutions.enums.ProviderType;
import com.EF2.prohomesolutions.exceptions.MyException;
import com.EF2.prohomesolutions.models.Customer;
import com.EF2.prohomesolutions.models.Notification;
import com.EF2.prohomesolutions.models.Provider;
import com.EF2.prohomesolutions.services.JobService;
import com.EF2.prohomesolutions.services.NotificationService;
import com.EF2.prohomesolutions.services.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/provider")
public class ProviderController {

    @Autowired
    private ProviderService providerService;
    @Autowired
    private JobService jobService;
    @Autowired
    private NotificationService notificationService;

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/profile")
    public String editProfile(HttpSession session, ModelMap model){
        Provider provider = (Provider) session.getAttribute("userSession");
        provider = providerService.findById(provider.getId()).get();
        model.addAttribute("provider", provider);
        model.put("countries", Country.values());
        model.put("providerTypes", ProviderType.values());
        model.put("feeTypes", FeeTypes.values());
        List<Notification> notifications = notificationService.getNotifications(session);
        model.put("notifications", notifications);
        return "Provider_edit_profile";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/profile/update")
    public String update(HttpSession session, @RequestParam String name, @RequestParam String alias,
                         @RequestParam String tel, @RequestParam String country, @RequestParam String providerType,
                         @RequestParam String feeType, @RequestParam String price, MultipartFile file, ModelMap model){
        Provider provider = (Provider) session.getAttribute("userSession");
        try {
            provider = providerService.updateProfile(provider, name, alias, tel, country, providerType, feeType, price, file);
        } catch (MyException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/provider/profile";
    }

    @GetMapping("/profile/{id}")
    public String viewProfile(HttpSession session, @PathVariable String id, ModelMap model){
        Provider provider = providerService.findById(id).get();
        model.addAttribute("provider", provider);
        model.put("id", id);
        if (provider.getId().equals(id)){
            model.put("jobStatus", JobStatus.values());
            provider.setJobs(jobService.orderByDateDes(provider.getJobs()));
            model.put("jobs", provider.getJobs());
        }
        List<Notification> notifications = notificationService.getNotifications(session);
        model.put("notifications", notifications);
        return "Provider_profile";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping(value="/type/{type}")
    public String providerTypeTable(HttpSession session, @PathVariable("type") String type, ModelMap model){
        List<Provider> providers = providerService.findByProviderType(type);
        model.put("data", providers);
        model.put("providerTypes", ProviderType.values());
        List<Notification> notifications = notificationService.getNotifications(session);
        model.put("notifications", notifications);
        model.put("type", ProviderType.valueOf(type));
        return"Provider_type_table";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping(value="/type/{type}/{filters}")
    public String providerTypeAndName(HttpSession session, @PathVariable("type") String type,@PathVariable("filters") String filters,
                                      String name, String price1s,
                                      String price2s,ModelMap model){
        try{
            if(filters.equals("name")){
                List<Provider> providers = providerService.findByN(type, name);
                model.put("data", providers);
                model.put("type", type);
            }else if(filters.equals("price")){
                List<Provider> providers = providerService.findByTypeAndPrice(type, price1s, price2s);
                model.put("data", providers);
                model.put("type", type);
            }
        }catch (MyException e){
            model.put("error", e.getMessage());
        }
        List<Notification> notifications = notificationService.getNotifications(session);
        model.put("notifications", notifications);
        model.put("providerTypes", ProviderType.values());
        model.put("type", ProviderType.valueOf(type));
        return "Provider_type_table";
    }

}
