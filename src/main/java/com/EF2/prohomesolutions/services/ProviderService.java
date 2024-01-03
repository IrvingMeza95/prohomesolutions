package com.EF2.prohomesolutions.services;

import com.EF2.prohomesolutions.dtos.UserLoginDTO;
import com.EF2.prohomesolutions.enums.*;
import com.EF2.prohomesolutions.exceptions.MyException;
import com.EF2.prohomesolutions.mappers.ProviderMapper;
import com.EF2.prohomesolutions.models.File;
import com.EF2.prohomesolutions.models.Provider;
import com.EF2.prohomesolutions.models.User;
import com.EF2.prohomesolutions.repositories.ProviderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {

    @Autowired
    private ProviderRepo providerRepo;
    @Autowired
    private ProviderMapper providerMapper;
    @Autowired
    private FileService fileService;

    public Optional<Provider> findById(String id) {
        return providerRepo.findById(id);
    }

    public Provider save(Provider provider) {
        return providerRepo.save(provider);
    }

    public Provider update(Provider formProvider, String confirmPassword) throws MyException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Optional<Provider> bdProvider = findById(formProvider.getId());
        if (!bdProvider.isPresent()){
            throw new MyException("User updatin process has failed.");
        }
        Provider updatedProvider = bdProvider.get();
        if (!formProvider.getPassword().equals(confirmPassword)){
            throw new MyException("Passwords does not matched.");
        }
        updatedProvider.setPassword(bCryptPasswordEncoder.encode(formProvider.getPassword()));
        updatedProvider.setName(formProvider.getName());
        updatedProvider.setAlias(formProvider.getAlias());
        updatedProvider.setTel(formProvider.getTel());
        updatedProvider.setProviderType(formProvider.getProviderType());
        updatedProvider.setFeeTypes(formProvider.getFeeTypes());
        updatedProvider.setPrice(formProvider.getPrice());
        return save(updatedProvider);
    }

    public Optional<Provider> findByEmail(String email) {
        return providerRepo.findByEmail(email);
    }

    public List<Provider> getAll() {
        return providerRepo.findAll();
    }

    public void chanteStatus(Provider provider) {
        provider.setEnable(!provider.getEnable());
        providerRepo.save(provider);
    }

    public void delete(Provider provider) {
        providerRepo.delete(provider);
    }

    public UserLoginDTO loadUserDTOByEmail(String email) {
        List<Object[]> provider = providerRepo.loadUserDTOByEmail(email);
        if (!provider.isEmpty()){
            return providerMapper.providerToUserLoginDTO(provider);
        }
        return null;
    }

    public void changeRole(Provider provider, Role role) {
        provider.setRole(role);
        save(provider);
    }

    public List<Provider> getAllFilterByRole(String role) {
        return providerRepo.getAllFilterByRole(Role.valueOf(role));
    }

    public List<Provider> getAllFilterByUserType(String userType) {
        return providerRepo.getAllFilterByUserType(UserType.valueOf(userType));
    }

    public UserLoginDTO loadUserDTOById(String id) {
        List<Object[]> provider = providerRepo.loadUserDTOById(id);
        if (!provider.isEmpty()){
            return providerMapper.providerToUserLoginDTO(provider);
        }
        return null;
    }

    public Provider updateProfile(Provider provider, String name, String alias, String tel,
                                  String country, String providerType, String feeType, String price, MultipartFile file) throws MyException {

        provider.setName(name);
        provider.setAlias(alias);
        provider.setTel(tel);
        provider.setProviderType(ProviderType.valueOf(providerType));
        provider.setFeeTypes(FeeTypes.valueOf(feeType));
        provider.setPrice(Double.valueOf(price));
        provider.setCountry(Country.valueOf(country));
        if (!file.isEmpty()){
            if (provider.getProfilePicture() != null){
                fileService.update(file,provider.getProfilePicture().getId());
            }else{
                File file1 = fileService.saveImage(file);
                provider.setProfilePicture(file1);
            }
        }
        return save(provider);
    }

    public List<Provider> findByProviderType(String type) {
        return providerRepo.findByProviderType(type);
    }

    public List<Provider> findByN (String providerType, String name)throws MyException{
        if(name.isEmpty() || name == null){
            throw new MyException("name can't be null or empty");
        }
        return providerRepo.findByN(providerType, name);
    }

    public List<Provider> findByTypeAndPrice(String providerType, String price1s, String price2s) throws MyException{
        if(price1s == null || price2s == null || price1s.isEmpty() || price2s.isEmpty()){
            throw new MyException("Value can't be null or empty");
        }

        if(price1s.equals("0")|| price2s.equals("0") || price1s.equals("0.0") || price2s.equals("0.0")){
            throw new MyException("Value can't be 0.0");
        }

        double price1 = Double.parseDouble(price1s);
        double price2 = Double.parseDouble(price2s);
        String feeTypes = "PER_HOUER";
        return providerRepo.findByTypeAndPrice(providerType, feeTypes, price1,price2);
    }

    public List<Provider> findByName(String name) {
        return providerRepo.findByName(name);
    }

    public List<Provider> findByTel(String tel) {
        return providerRepo.findByTel(tel);
    }
}
