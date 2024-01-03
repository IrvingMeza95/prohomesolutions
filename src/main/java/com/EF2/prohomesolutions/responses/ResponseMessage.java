package com.EF2.prohomesolutions.responses;

import com.EF2.prohomesolutions.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseMessage {
    private ApplicationStatus applicationStatus;
    private String message;
}