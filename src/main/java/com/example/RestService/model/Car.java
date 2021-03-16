package com.example.RestService.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity

public class Car {

    @Id
    @Getter @Setter
    private String carNum;
    @Getter @Setter
    private int userId;
    @Getter @Setter
    private Boolean active;
    @Getter @Setter
    private Boolean approvedStatus;
    @Getter @Setter
    private Timestamp carAddedTime;
    @Getter @Setter
    private Timestamp carApprovedTime;


}
