package com.EF2.prohomesolutions.services;

import com.EF2.prohomesolutions.dtos.UserLoginDTO;
import com.EF2.prohomesolutions.enums.Country;
import com.EF2.prohomesolutions.enums.Role;
import com.EF2.prohomesolutions.enums.UserType;
import com.EF2.prohomesolutions.exceptions.MyException;
import com.EF2.prohomesolutions.mappers.CustomerMapper;
import com.EF2.prohomesolutions.models.*;
import com.EF2.prohomesolutions.repositories.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private FileService fileService;

    public Optional<Customer> findById(String id) {
        return customerRepo.findById(id);
    }

    public List<Customer> getAll() {
        return customerRepo.findAll();
    }

    public void chanteStatus(Customer customer) {
        customer.setEnable(!customer.getEnable());
        customerRepo.save(customer);
    }

    public Optional<Customer> findByEmail(String email) {
        return customerRepo.findByEmail(email);
    }

    public Customer save(Customer customer) {
        return customerRepo.save(customer);
    }

    public Customer update(Customer formCustomer, String confirmPassword) throws MyException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Optional<Customer> bdCustomer = findById(formCustomer.getId());
        if (bdCustomer.isEmpty()){
            throw new MyException("User updatin process has failed.");
        }
        Customer updatedCustomer = bdCustomer.get();
        if (!formCustomer.getPassword().equals(confirmPassword)){
            throw new MyException("Passwords does not matched.");
        }
        updatedCustomer.setPassword(bCryptPasswordEncoder.encode(formCustomer.getPassword()));
        updatedCustomer.setName(formCustomer.getName());
        updatedCustomer.setAlias(formCustomer.getAlias());
        updatedCustomer.setTel(formCustomer.getTel());
        return  save(updatedCustomer);
    }

    public void delete(Customer customer) {
        customerRepo.delete(customer);
    }

    public UserLoginDTO loadUserDTOByEmail(String email) {
        List<Object[]> customer = customerRepo.loadUserDTOByEmail(email);
        if (!customer.isEmpty()){
            return customerMapper.customerToUserLoginDTO(customer);
        }
        return null;
    }

    public UserLoginDTO loadUserDTOById(String id) {
        List<Object[]> customer = customerRepo.loadUserDTOById(id);
        if (!customer.isEmpty()){
            return customerMapper.customerToUserLoginDTO(customer);
        }
        return null;
    }

    public void changeRole(Customer customer, Role role) {
        customer.setRole(role);
        save(customer);
    }

    public List<Customer> getAllFilterByRole(String role) {
        return customerRepo.getAllFilterByRole(Role.valueOf(role));
    }

    public List<Customer> getAllFilterByUserType(String userType) {
            return customerRepo.getAllFilterByUserType(UserType.valueOf(userType));
    }

    public boolean likePost(Post post, Customer customer) {
        customer = findById(customer.getId()).get();
        boolean fav = false;
        for (Post p : customer.getLikedPosts()){
            if (p.equals(post)){
                fav = true;
                break;
            }
        }
        if (fav){
            customer.getLikedPosts().remove(post);
            save(customer);
            return false;
        }
        else{
            customer.getLikedPosts().add(post);
            save(customer);
//            String notiTitle = "New like.";
//            String notiDetails = customer.getName() + " liked your post.";
//            String url = "/post/post-view/" + post.getId();
//            notificationService.create(post.getProvider().getId(), post.getProvider().getUserType().name(),
//                    notiTitle, notiDetails, url, customer.getId());
            return true;
        }
    }

    public Customer updateProfile(Customer customer, String name, String alias, String tel,
                                  String country, String about, MultipartFile file) throws MyException {

        customer.setName(name);
        customer.setAlias(alias);
        customer.setTel(tel);
        customer.setAbout(about);
        customer.setCountry(Country.valueOf(country));
        if (!file.isEmpty()){
            if (customer.getProfilePicture() != null){
                fileService.update(file,customer.getProfilePicture().getId());
            }else{
                File file1 = fileService.saveImage(file);
                customer.setProfilePicture(file1);
            }
        }
        return save(customer);
    }

    public List<Customer> findByName(String name) {
        return customerRepo.findByName(name);
    }

    public List<Customer> findByTel(String tel) {
        return customerRepo.findByTel(tel);
    }
}
