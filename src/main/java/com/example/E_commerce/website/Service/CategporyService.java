package com.example.E_commerce.website.Service;

import com.example.E_commerce.website.model.Category;
import com.example.E_commerce.website.model.Product;

import org.springframework.stereotype.Service;

import java.util.List;

public interface CategporyService {
    public Category saveCategory(Category category);

    public boolean exitsByname(String name);
    public List<Category> getAllCategory();
    public boolean deleteCategory(int id);
    public Category getCategoryById(int id);
    public List<Category> getAllActiveCategory();
   



}
