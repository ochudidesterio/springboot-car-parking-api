package com.example.RestService.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@NoArgsConstructor
public class Person {

    @Id
    @Getter @Setter
    private int userId;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String surname;
    @Getter @Setter
    private String phone;


}
