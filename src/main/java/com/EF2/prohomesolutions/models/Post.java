package com.EF2.prohomesolutions.models;

import com.EF2.prohomesolutions.enums.ProviderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Post {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    protected String id;
    @Column(nullable = false, length = 500)
    private String description;
//    @Column(nullable = false)
//    private Double price;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date postDate;
    @Column(nullable = false)
    private Boolean visible;
    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
    @ManyToMany(mappedBy = "likedPosts")
    private List<Customer> likes;
    @OneToOne
    private File image;
    private ProviderType providerType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;
        return Objects.equals(getId(), post.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
