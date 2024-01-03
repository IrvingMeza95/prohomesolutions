package com.EF2.prohomesolutions.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseFile {

    private String name;
    private String url;
    private String type;
    private Integer size;

}
