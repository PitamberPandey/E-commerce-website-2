package com.example.E_commerce.website.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter



public class UserDts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)


    private Integer id;
    private String name;
    private String mobileNumber;
    private String email;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String password;
    private String profileImage;



}