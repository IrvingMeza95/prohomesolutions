package com.EF2.prohomesolutions.controllers;

import com.EF2.prohomesolutions.enums.FeeTypes;
import com.EF2.prohomesolutions.enums.ProviderType;
import com.EF2.prohomesolutions.enums.Role;
import com.EF2.prohomesolutions.enums.UserType;
import com.EF2.prohomesolutions.exceptions.MyException;
import com.EF2.prohomesolutions.models.Customer;
import com.EF2.prohomesolutions.models.Provider;
import com.EF2.prohomesolutions.services.CustomerService;
import com.EF2.prohomesolutions.services.ProviderService;
import com.EF2.prohomesolutions.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProviderService providerService;

    @GetMapping("/dashboard")
    public String dashboard(ModelMap model) {
        model.addAttribute("users", userService.getAll());
        model.put("roles", Role.values());
        model.put("userTypes", UserType.values());
        return "Admin_dashboard";
    }

    @GetMapping("/dashbpard/filter")
    public String filter(@RequestParam String filter, @RequestParam(required = false) String name,
                         @RequestParam(required = false) String email, @RequestParam(required = false) String tel, ModelMap model){

        if (filter.equals("name")){
            model.addAttribute("users", userService.findByName(name));
        }
        if (filter.equals("email")){
            model.addAttribute("users", List.of(userService.findByEmail(email)));
        }
        if (filter.equals("tel")){
            model.addAttribute("users", userService.findByTel(tel));
        }

        model.put("roles", Role.values());
        model.put("userTypes", UserType.values());
        return "Admin_dashboard";
    }

    @GetMapping("/users-table/filter-by-role/{role}")
    public String getAllFilterByRole(@PathVariable("role") String role, ModelMap model) {
        model.addAttribute("users", userService.getAllFilterByRole(role));
        model.put("roles", Role.values());
        model.put("userTypes", UserType.values());
        return "User_table";
    }

    @GetMapping("/users-table/filter-by-user-type/{userType}")
    public String getAllFilterByUserType(@PathVariable("userType") String userType, ModelMap model) {
        model.addAttribute("users", userService.getAllFilterByUserType(userType));
        model.put("roles", Role.values());
        model.put("userTypes", UserType.values());
        return "User_table";
    }

    @GetMapping("/v1/change-role/{id}/{role}")
    public String changeRole(@PathVariable("id") String id, @PathVariable("role") Role role, ModelMap model){
        if (role.name().equals("ADMIN")){
            role = Role.USER;
        }else{
            role = Role.ADMIN;
        }
        userService.changeRole(id, role);
        model.put("users", userService.getAll());
        model.put("roles", Role.values());
        return "User_table";
    }

    @GetMapping("/user/change-status/{id}")
    public String chanteStatus(@PathVariable("id") String id, ModelMap model) {
        userService.chanteStatus(id);
        model.put("users", userService.getAll());
        model.put("roles", Role.values());
        return "Admin_dashboard";
    }

    @GetMapping("/v1/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isPresent()) {
            model.addAttribute("customer", customer.get());
            return "Edit_c_form";
        }
        Optional<Provider> provider = providerService.findById(id);
        if (provider.isPresent()) {
            model.addAttribute("providerTypes", ProviderType.values());
            model.addAttribute("feeTypes", FeeTypes.values());
            model.addAttribute("provider", provider.get());
            return "Edit_p_form";
        }
        return "User_table";
    }

    @PostMapping("/v1/update-customer")
    public String updateCustomer(Customer customer, @RequestParam String confirmPassword, ModelMap model) {
        try {
            customerService.update(customer, confirmPassword);
        } catch (MyException e) {
            model.put("error", e.getMessage());
            model.addAttribute("customer", customer);
            return "Edit_c_form";
        }
        return "User_table";
    }

    @PostMapping("/v1/update-provider")
    public String updateProvider(Provider provider, @RequestParam String confirmPassword, ModelMap model) {
        try {
            providerService.update(provider, confirmPassword);
        } catch (MyException e) {
            model.put("error", e.getMessage());
            model.addAttribute("provider", provider);
            return "Edit_p_form";
        }
        return "User_table";
    }

    @GetMapping("/v1/delete/{id}")
    public String deleteUser(@PathVariable("id") String id, ModelMap model){
        userService.delete(id);
        model.put("users", userService.getAll());
        model.put("roles", Role.values());
        return "User_table";
    }

}
