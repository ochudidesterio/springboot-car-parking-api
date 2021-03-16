package com.example.RestService.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
public class UserRequest {
    @Getter
    private String userId;
    @Getter
    private String name;
    @Getter
    private String surname;
    @Getter
    private String phone;

    @Getter
    private String carNum;
    @Getter
    private Boolean approvedStatus;
    @Getter
    private Timestamp carAddedTime;
    @Getter
    private Timestamp carApprovedTime;


}
