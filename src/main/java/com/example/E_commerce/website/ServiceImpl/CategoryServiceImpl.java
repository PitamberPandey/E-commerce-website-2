package com.example.E_commerce.website.ServiceImpl;

import com.example.E_commerce.website.Repository.CategoryRepository;
import com.example.E_commerce.website.Service.CategporyService;
import com.example.E_commerce.website.model.Category;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;



import java.util.List;


@Service
public class CategoryServiceImpl implements CategporyService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }




    @Override
    public boolean exitsByname(String name) {
        return categoryRepository.existsByName(name);
    }



    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public boolean deleteCategory(int id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            this.categoryRepository.delete(category);
            return true;
        } else {
            return false;
        }


    }


    @Override
    public Category getCategoryById(int id) {
         Category category=categoryRepository.findById(id).orElse(null);
         return  category;
    }




    @Override
    public List<Category> getAllActiveCategory() {
       List<Category> categories= categoryRepository.findByIsActiveFalse();
        return categories;
    }




   

    
}

