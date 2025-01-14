package com.example.E_commerce.website.Repository;

import com.example.E_commerce.website.model.Category;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Boolean existsByName(String name);


    public List<Category> findByIsActiveFalse();

}

