package com.EF2.prohomesolutions.controllers;

import com.EF2.prohomesolutions.enums.Country;
import com.EF2.prohomesolutions.exceptions.MyException;
import com.EF2.prohomesolutions.models.Customer;
import com.EF2.prohomesolutions.models.Job;
import com.EF2.prohomesolutions.models.Notification;
import com.EF2.prohomesolutions.models.Post;
import com.EF2.prohomesolutions.services.CustomerService;
import com.EF2.prohomesolutions.services.JobService;
import com.EF2.prohomesolutions.services.NotificationService;
import com.EF2.prohomesolutions.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerservice;
    @Autowired
    private PostService postService;
    @Autowired
    private JobService jobService;
    @Autowired
    private NotificationService notificationService;

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/profile")
    public String profile(HttpSession session, ModelMap model){
        Customer customer = (Customer) session.getAttribute("userSession");
        customer = customerservice.findById(customer.getId()).get();
        model.addAttribute("customer", customer);
        model.put("countries", Country.values());
        List<Notification> notifications = notificationService.getNotifications(session);
        model.put("notifications", notifications);
        return "Customer_edit_profile";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/profile/update")
    public String update(HttpSession session, @RequestParam String name, @RequestParam String alias,
                         @RequestParam String tel, @RequestParam String country, @RequestParam String about,
                        MultipartFile file, ModelMap model){
        Customer customer = (Customer) session.getAttribute("userSession");
        try {
            customer = customerservice.updateProfile(customer, name, alias, tel, country, about, file);
        } catch (MyException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/customer/profile";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/profile/{id}")
    public String profile(HttpSession session, @PathVariable("id") String id, ModelMap model){
        Customer customer = customerservice.findById(id).get();
        model.addAttribute("customer", customer);
        customer.setJobs(jobService.orderByDateDes(customer.getJobs()));
        model.put("jobs", customer.getJobs());
        model.put("id", id);
        List<Notification> notifications = notificationService.getNotifications(session);
        model.put("notifications", notifications);
        return "Customer_profile";
    }

    @GetMapping("/like-post/{id}")
    public String like(HttpSession session, @PathVariable("id") String id, ModelMap model){
        Customer customer = (Customer) session.getAttribute("userSession");
        Post post = postService.getOne(id);
        boolean fav = customerservice.likePost(post, customer);
        model.addAttribute("post", post);
        model.addAttribute("fav", fav);
        List<Notification> notifications = notificationService.getNotifications(session);
        model.put("notifications", notifications);
        return "Post_view";
    }

}


