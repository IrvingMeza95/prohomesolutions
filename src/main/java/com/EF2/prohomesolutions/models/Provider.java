package com.EF2.prohomesolutions.models;

import com.EF2.prohomesolutions.enums.FeeTypes;
import com.EF2.prohomesolutions.enums.ProviderType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Provider extends  User {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProviderType providerType;
    @ManyToMany(mappedBy = "favList")
    private List<Customer> followers;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeeTypes feeTypes;
    private Double price;
    @OneToMany(mappedBy = "provider")
    private List<Post> posts;
    @OneToOne
    private File profilePicture;
    @OneToMany(mappedBy = "prov")
    private List<Job> jobs;
    @OneToMany(mappedBy = "provider")
    private List<Notification> notifications;

}
