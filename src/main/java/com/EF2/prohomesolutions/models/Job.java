package com.EF2.prohomesolutions.models;

import com.EF2.prohomesolutions.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Job {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer cust;
    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider prov;
    @Column(nullable = false)
    private Double totalPrice;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus jobStatus;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date;
    private Integer stars;
    @OneToMany(mappedBy = "job")
    private List<Message> messages;
    @Column(length = 1000)
    private String details;

}
