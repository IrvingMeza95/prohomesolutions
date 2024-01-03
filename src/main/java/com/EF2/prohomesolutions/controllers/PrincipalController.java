package com.EF2.prohomesolutions.controllers;

import com.EF2.prohomesolutions.dtos.UserLoginDTO;
import com.EF2.prohomesolutions.enums.ProviderType;
import com.EF2.prohomesolutions.models.Customer;
import com.EF2.prohomesolutions.models.Notification;
import com.EF2.prohomesolutions.models.Provider;
import com.EF2.prohomesolutions.services.NotificationService;
import com.EF2.prohomesolutions.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/principal")
public class PrincipalController {

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping
    public String princiapl(HttpSession session, ModelMap model){
        model.put("providerTypes", ProviderType.values());
        List<Notification> notifications = notificationService.getNotifications(session);
        model.put("notifications", notifications);
        return "Principal";
    }

}
