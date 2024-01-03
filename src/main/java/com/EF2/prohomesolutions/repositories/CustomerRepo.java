package com.EF2.prohomesolutions.repositories;

import com.EF2.prohomesolutions.enums.Role;
import com.EF2.prohomesolutions.enums.UserType;
import com.EF2.prohomesolutions.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, String> {

    Optional<Customer> findByEmail(String email);

    @Query(value = "SELECT c.id, c.email, c.password, c.role, c.user_type, c.enable FROM customer c WHERE c.email like %?1%", nativeQuery = true)
    List<Object[]> loadUserDTOByEmail(String email);

    @Query(value = "SELECT c.id, c.email, c.password, c.role, c.user_type, c.enable FROM customer c WHERE c.id = ?1", nativeQuery = true)
    List<Object[]> loadUserDTOById(String id);

    @Query("SELECT c FROM Customer c WHERE c.role = :role")
    List<Customer> getAllFilterByRole(Role role);

    @Query("SELECT c FROM Customer c WHERE c.userType = :userType")
    List<Customer> getAllFilterByUserType(UserType userType);

    @Query(value = "SELECT * FROM customer c WHERE c.name LIKE %?1%", nativeQuery = true)
    List<Customer> findByName(String name);

    @Query(value = "SELECT * FROM customer c WHERE c.tel LIKE %?1%", nativeQuery = true)
    List<Customer> findByTel(String tel);

}
