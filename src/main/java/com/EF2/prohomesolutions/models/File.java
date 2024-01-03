package com.EF2.prohomesolutions.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class File {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    protected String id;
    @Column(nullable = false)
    private String mime;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @Lob @Basic(fetch = FetchType.LAZY)
    private byte[] content;

}
