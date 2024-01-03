package com.EF2.prohomesolutions.services;

import com.EF2.prohomesolutions.dtos.UserLoginDTO;
import com.EF2.prohomesolutions.enums.*;
import com.EF2.prohomesolutions.exceptions.MyException;
import com.EF2.prohomesolutions.models.Customer;
import com.EF2.prohomesolutions.models.Provider;
import com.EF2.prohomesolutions.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private FileService fileService;

    public User create(String email, String password, String confirmPassword, String name,
                       String alias, String tel, UserType userType, ProviderType providerType, FeeTypes feeTypes,
                       String price, String country) throws MyException {

        validate(email,password,confirmPassword,name,alias ,tel);

        if (userType.name().equals("CUSTOMER")){
            Customer customer = new Customer();
            create(customer,email,password,confirmPassword,name,alias,tel,userType, country);
            return customerService.save(customer);
        }
        if (userType.name().equals("PROVIDER")){
            Provider provider = new Provider();
            create(provider,email,password,confirmPassword,name,alias,tel,userType, country);
            provider.setProviderType(providerType);
            provider.setFeeTypes(feeTypes);
            if (price == null){
                price = "0";
            }
            provider.setPrice(Double.valueOf(price));
            return providerService.save(provider);
        }
        return null;
    }

    private void create(User user, String email, String password, String confirmPassword, String name, String alias, String tel,
                        UserType userType, String country){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setName(name);
        user.setAlias(alias);
        user.setTel(tel);
        user.setRole(Role.USER);
        user.setUserType(userType);
        user.setEnable(true);
        user.setCountry(Country.valueOf(country));
    }

    public void chanteStatus(String id){
        Optional<Customer> customer = customerService.findById(id);
        customer.ifPresent(cust -> customerService.chanteStatus(cust));

        Optional<Provider> provider = providerService.findById(id);
        provider.ifPresent(prov -> providerService.chanteStatus(prov));
    }

    private void validate(String email, String password, String confirmPassword, String name,String alias , String tel) throws MyException {
        if (email == null || email.isEmpty()){
            throw new MyException("Email must not be empty.");
        }
        if (password == null || password.isEmpty()){
            throw new MyException("Password must not be empty.");
        }
        if (!password.equals(confirmPassword)){
            throw new MyException("Passwords does not matched.");
        }
        if (name == null       || name.isEmpty()){
            throw new MyException("Name must not be empty.");
        }
        if (alias == null       || alias.isEmpty()){
            throw new MyException("Name must not be empty.");
        }
        if (tel == null       || tel.isEmpty()){
            throw new MyException("Telefone must not be empty.");
        }
    }

    public List<User> getAll(){
        List<User> users =new ArrayList<>();
        customerService.getAll().forEach((u) -> users.add(u));
        providerService.getAll().forEach((u) -> users.add(u));
        return users;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserLoginDTO user = loadUserDTOByEmail(email);
        if (user != null){
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("userRole", user.getRole().name());
            if (user.getUserType().name().equals("CUSTOMER")){
                Customer customer = (Customer) findByEmail(user.getEmail());
                session.setAttribute("userSession", customer);
                session.setAttribute("userType", customer.getUserType().name());
            }
            if (user.getUserType().name().equals("PROVIDER")){
                Provider provider = (Provider) findByEmail(user.getEmail());
                session.setAttribute("userSession", provider);
                session.setAttribute("userType", provider.getUserType().name());
            }
            if (user.getEnable()){
                return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
            }else{
                throw new UsernameNotFoundException("User not found.");
            }
        }else {
            throw new UsernameNotFoundException("User not found.");
        }
    }

    private UserLoginDTO loadUserDTOByEmail(String email) {
        UserLoginDTO user = customerService.loadUserDTOByEmail(email);
        if (user != null){
            return user;
        }
        user = providerService.loadUserDTOByEmail(email);
        if (user != null){
            return user;
        }
        return null;
    }

    public UserLoginDTO loadUserDTOById(String id) {
        UserLoginDTO user = customerService.loadUserDTOById(id);
        if (user != null){
            return user;
        }
        user = providerService.loadUserDTOById(id);
        if (user != null){
            return user;
        }
        return null;
    }

    public void delete(String id) {
        Optional<Customer> customer = customerService.findById(id);
        customer.ifPresent(cust -> customerService.delete(cust));
        Optional<Provider> provider = providerService.findById(id);
        provider.ifPresent(prov -> providerService.delete(prov));
    }

    public User findByEmail(String email) {
        Optional<Customer> customer = customerService.findByEmail(email);
        if (customer.isPresent()){
            return customer.get();
        }
        Optional<Provider> provider = providerService.findByEmail(email);
        if (provider.isPresent()){
            return provider.get();
        }
        return null;
    }

    public void changeRole(String id, Role role) {
        Optional<Customer> customer = customerService.findById(id);
        customer.ifPresent(cust -> customerService.changeRole(cust, role));
        Optional<Provider> provider = providerService.findById(id);
        provider.ifPresent(prov -> providerService.changeRole(prov, role));
    }

    public List<User> getAllFilterByRole(String role) {
        List<User> users = new ArrayList<>();
        customerService.getAllFilterByRole(role).forEach(c -> users.add(c));
        providerService.getAllFilterByRole(role).forEach(p -> users.add(p));
        return users;
    }

    public List<User> getAllFilterByUserType(String userType) {
        List<User> users = new ArrayList<>();
        customerService.getAllFilterByUserType(userType).forEach(c -> users.add(c));
        providerService.getAllFilterByUserType(userType).forEach(p -> users.add(p));
        return users;
    }

    public List<User> findByName(String name) {
        List<User> users = new ArrayList<>();
        users.addAll(customerService.findByName(name));
        users.addAll(providerService.findByName(name));
        return users;
    }

    public List<User> findByTel(String tel) {
        List<User> users = new ArrayList<>();
        users.addAll(customerService.findByTel(tel));
        users.addAll(providerService.findByTel(tel));
        return users;
    }
}
