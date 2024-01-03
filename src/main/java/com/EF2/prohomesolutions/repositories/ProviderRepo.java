package com.EF2.prohomesolutions.repositories;

import com.EF2.prohomesolutions.enums.Role;
import com.EF2.prohomesolutions.enums.UserType;
import com.EF2.prohomesolutions.models.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepo extends JpaRepository<Provider, String> {

    Optional<Provider> findByEmail(String email);

    @Query(value = "SELECT p.id, p.email, p.password, p.role, p.user_type, p.enable FROM provider p WHERE p.email like %?1%", nativeQuery = true)
    List<Object[]> loadUserDTOByEmail(String email);

    @Query(value = "SELECT p.id, p.email, p.password, p.role, p.user_type, p.enable FROM provider p WHERE p.id = ?1", nativeQuery = true)
    List<Object[]> loadUserDTOById(String id);

    @Query("SELECT p FROM Provider  p WHERE p.role = :role")
    List<Provider> getAllFilterByRole(Role role);

    @Query("SELECT p FROM Provider  p WHERE p.userType = :userType")
    List<Provider> getAllFilterByUserType(UserType userType);

    @Query(
            value="SELECT * FROM provider p WHERE p.provider_type Like ?1",
            nativeQuery = true
        )
    List<Provider> findByProviderType(String providerType);

    @Query(
            value="SELECT * FROM Provider p WHERE p.provider_type = ?1 AND P.fee_Types = ?2 AND p.price BETWEEN ?3 AND ?4",
            nativeQuery = true
    )
    List<Provider> findByTypeAndPrice(String providerType, String feeTypes, double price1, double price2);

    @Query(
            value="SELECT * FROM provider p WHERE p.provider_type = ?1 AND p.name LIKE %?2%",
            nativeQuery = true
    )
    List<Provider> findByN(String providerType, String name);

    @Query(value = "SELECT * FROM provider p WHERE p.name LIKE %?1%", nativeQuery = true)
    List<Provider> findByName(String name);

    @Query(value = "SELECT * FROM provider p WHERE p.tel LIKE %?1%", nativeQuery = true)
    List<Provider> findByTel(String tel);

}
