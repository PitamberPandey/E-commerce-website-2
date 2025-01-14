package com.example.E_commerce.website.Service;

import java.util.List;

import com.example.E_commerce.website.model.Product;

public interface ProductService {

    

    public Product saveProduct(Product product);
    public List<Product> getallProduct();
    public Boolean deleteProduct(int id);
    public Product getProductById(Integer id);
    public List<Product> getAllActiveProduct( String category);
    

}
