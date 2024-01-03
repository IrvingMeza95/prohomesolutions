package com.EF2.prohomesolutions.services;

import com.EF2.prohomesolutions.enums.NotificationStatus;
import com.EF2.prohomesolutions.models.*;
import com.EF2.prohomesolutions.repositories.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProviderService providerService;

    public void create(String id, String userType, String title, String details, String url, String imageId){
        Notification notification = Notification.builder()
                .title(title)
                .date(new Date())
                .details(details)
                .status(NotificationStatus.PENDING)
                .url(url)
                .imageUrl("/file/profile-picture/" + imageId)
                .build();
        if (userType.equals("CUSTOMER")){
            Customer customer = customerService.findById(id).get();
            notification.setCustomer(customer);
        }
        if (userType.equals("PROVIDER")){
            Provider provider = providerService.findById(id).get();
            notification.setProvider(provider);
        }
        notificationRepo.save(notification);
    }

    public List<Notification> getNotifications(HttpSession session) {
        String userType = (String) session.getAttribute("userType");
        if (userType.equals("CUSTOMER")){
            Customer customer = (Customer)session.getAttribute("userSession");
            return orderByDateDesc(customerService.findById(customer.getId()).get().getNotifications());
        }
        if (userType.equals("PROVIDER")){
            Provider provider = (Provider) session.getAttribute("userSession");
            return orderByDateDesc(providerService.findById(provider.getId()).get().getNotifications());
        }
        return null;
    }

    public List<Notification> orderByDateDesc(List<Notification> notifications){
        return notifications.stream().sorted(Comparator.comparing(Notification::getDate).reversed())
                .collect(Collectors.toList());
    }

}
