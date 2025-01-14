package com.example.E_commerce.website.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.E_commerce.website.model.UserDts;

public interface UserRepository  extends JpaRepository<UserDts,Integer>{

}
