package com.EF2.prohomesolutions.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Customer extends User {

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "customer_fav_list",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "provider_id")
    )
    private List<Provider> favList;
    @OneToOne
    private File profilePicture;
    @OneToMany(mappedBy = "cust")
    private List<Job> jobs;
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "customer_liked_posts",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private List<Post> likedPosts;
    @Column(length = 4000)
    private  String about;
    @OneToMany(mappedBy = "customer")
    private List<Notification> notifications;

}
