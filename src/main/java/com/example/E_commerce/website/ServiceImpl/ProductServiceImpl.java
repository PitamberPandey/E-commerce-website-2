package com.example.E_commerce.website.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


import com.example.E_commerce.website.Repository.ProductRepo;
import com.example.E_commerce.website.Service.ProductService;
import com.example.E_commerce.website.model.Product;
@Service
public class ProductServiceImpl  implements ProductService{
    @Autowired
    private ProductRepo productRepo;

    @Override
    public Product saveProduct(Product product) {
         return productRepo.save(product);

        
    }

    @Override
    public List<Product> getallProduct() {
        
        return productRepo.findAll();
    }

    @Override
    public Boolean deleteProduct(int id) {
       Product product= productRepo.findById(id).orElse(null);
  if(!ObjectUtils.isEmpty(product)){
    productRepo.delete(product);
    return true;

  }
  return false;

    }

    @Override
    public Product getProductById(Integer id) {
       Product uProduct=productRepo.findById(id).orElse(null);
       return uProduct;
      
    }

    @Override
    public List<Product> getAllActiveProduct(String category) {
      List<Product> products=null;
      if(ObjectUtils.isEmpty(category)){
        products= productRepo.findByIsActiveFalse();
      } else{
        products=productRepo.findByCategory(category);
      }
     
       return products;
    }

   

    
    

    

}
