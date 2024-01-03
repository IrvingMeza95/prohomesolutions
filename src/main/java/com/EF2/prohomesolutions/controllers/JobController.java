package com.EF2.prohomesolutions.controllers;

import com.EF2.prohomesolutions.enums.JobStatus;
import com.EF2.prohomesolutions.exceptions.MyException;
import com.EF2.prohomesolutions.models.Customer;
import com.EF2.prohomesolutions.models.Job;
import com.EF2.prohomesolutions.models.Notification;
import com.EF2.prohomesolutions.models.Provider;
import com.EF2.prohomesolutions.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
@Controller
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/{idProvider}")
    public String placeOrder(HttpSession session, @PathVariable("idProvider") String idProvider, @RequestParam String message, ModelMap model){
        Customer customer = (Customer) session.getAttribute("userSession");
        Provider provider = providerService.findById(idProvider).get();
        jobService.create(customer,provider, message);
        return "redirect:/customer/profile/" + customer.getId();
    }

    @GetMapping("/change-status/{id}")
    public String changeStatus(HttpSession session, @PathVariable("id") String id, @RequestParam String jobStatus, ModelMap model){
        try {
            jobService.changeStatus(id, jobStatus);
        } catch (MyException e) {
            System.out.println(e.getMessage());
        }
        Provider provider = (Provider) session.getAttribute("userSession");
        return "redirect:/provider/profile/" + provider.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(HttpSession session, @PathVariable("id") String id, ModelMap model){
        try {
            jobService.delete(id);
        } catch (MyException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/customer/profile";
    }

    @GetMapping("/chat-messages/{id}")
    public String chatMessages(HttpSession session, @PathVariable String id, ModelMap model){
        Job job = jobService.findById(id);
        model.put("job", job);
        model.addAttribute("messages", jobService.getChatMessages(job));
        model.put("jobStatus", JobStatus.values());
        List<Notification> notifications = notificationService.getNotifications(session);
        model.put("notifications", notifications);
        return "Chat";
    }

    @GetMapping("/chat-send-message/{id}")
    public String chatSendMessage(HttpSession session, @PathVariable String id, @RequestParam String message, ModelMap model){
        Job job = jobService.findById(id);
        jobService.chatSendMessage(job, session, message);
        return "redirect:/job/chat-messages/" + job.getId();
    }

    @GetMapping("/save-changes/{id}")
    public String saveChanges(HttpSession session, @PathVariable String id,
                              @RequestParam String jobStatus, @RequestParam String totalPrice, ModelMap model){
        Job job = jobService.findById(id);
        System.out.println("--- " + jobStatus);
        System.out.println("--- " + totalPrice);
        try {
            jobService.saveChanges(id, jobStatus, totalPrice);
        } catch (MyException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/job/chat-messages/" + job.getId();
    }

}
