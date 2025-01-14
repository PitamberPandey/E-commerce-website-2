package com.example.E_commerce.website.Contoller;

import com.example.E_commerce.website.Service.CategporyService; // Fixed typo in service name
import com.example.E_commerce.website.Service.ProductService;
import com.example.E_commerce.website.model.Category;
import com.example.E_commerce.website.model.Product;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategporyService categoryService;
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/loadAddProduct")
    public String loadAddProduct(Model model) {
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);
        return "admin/add_product";
    }

    @GetMapping("/category")
    public String category(Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        return "admin/category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, 
                               @RequestParam("file") MultipartFile file, 
                               HttpSession session) {
        String imageName = handleFileUpload(file, session);
        category.setImageName(imageName);

        boolean existsCategory = categoryService.exitsByname(category.getName());
        if (existsCategory) {
            session.setAttribute("errorMsg", "Category name already exists");
            return "redirect:/admin/category";
        }

        Category savedCategory = categoryService.saveCategory(category);
        if (savedCategory == null) {
            session.setAttribute("errorMsg", "Not saved! Internal server error");
            return "redirect:/admin/category";
        }

        session.setAttribute("successMsg", "Category saved successfully");
        return "redirect:/admin/category";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session) {
        boolean isDeleted = categoryService.deleteCategory(id);
        if (isDeleted) {
            session.setAttribute("successMsg", "Category deleted successfully.");
        } else {
            session.setAttribute("errorMsg", "Category not found.");
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable int id, Model model) {
        Category category = categoryService.getCategoryById(id);
        if (category == null) {
            model.addAttribute("errorMsg", "Category not found.");
            return "redirect:/admin/category";
        }
        model.addAttribute("category", category);
        return "admin/editCategory";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category, 
                                 @RequestParam("file") MultipartFile file, 
                                 HttpSession session) {
        // Retrieve the existing category by ID
        Category existingCategory = categoryService.getCategoryById(category.getId());
        if (existingCategory == null) {
            session.setAttribute("errorMsg", "Category not found");
            return "redirect:/admin/loadEditCategory/" + category.getId();
        }

        // Handle the file upload and update image name if new file is uploaded
        String imageName = file != null && !file.isEmpty() ? handleFileUpload(file, session) : existingCategory.getImageName();
      
        existingCategory.setName(category.getName());
        existingCategory.setImageName(imageName);

        // Save the updated category
        Category updatedCategory = categoryService.saveCategory(existingCategory);
        if (updatedCategory != null) {
            session.setAttribute("successMsg", "Category updated successfully");
        } else {
            session.setAttribute("errorMsg", "Something went wrong on the server");
        }

        return "redirect:/admin/loadEditCategory/" + category.getId();
    }

    @PostMapping("/saveProduct")   
    public String saveProduct(@ModelAttribute Product product, 
                              @RequestParam("file") MultipartFile image, 
                              HttpSession session) {
        // Handle image upload
        String imageName = handleFileUpload(image, session);
        product.setDiscount(0);
        product.setCategory(product.getCategory());
        product.setDiscountAmount(product.getPrice());
        product.setImage(imageName);
        
       
        // Save the product
        Product savedProduct = productService.saveProduct(product);
       
        if (savedProduct != null) {
            session.setAttribute("successMsg", "Product saved successfully");
        } else {
            session.setAttribute("errorMsg", "Something went wrong on the server");
        }

        return "redirect:/admin/loadAddProduct"; // Ensure this points to the correct redirect path
    }

    // Method to update a product
    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product,
                                @RequestParam("file") MultipartFile file,
                                HttpSession session) {
        // Retrieve existing product by ID
        Product existingProduct = productService.getProductById(product.getId());
        
        if (existingProduct == null) {
            session.setAttribute("errorMsg", "Product not found");
            return "redirect:/admin/loadEditProduct/" + product.getId();
        }
    
        // Handle the file upload and update image name if a new file is uploaded
        String imageName = (file != null && !file.isEmpty()) ? handleFileUpload(file, session) : existingProduct.getImage();
        
        // Update product fields
        existingProduct.setTitle(product.getTitle());
        existingProduct.setStock(product.getStock());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setImage(imageName);
        existingProduct.setDiscount(product.getDiscount());
        existingProduct.setIsActive(product.getIsActive());
    
        // Validate discount
        if (product.getDiscount() < 0 || product.getDiscount() > 100) {
            session.setAttribute("errorMsg", "Invalid discount");
            return "redirect:/admin/loadEditProduct/" + product.getId();
        }
    
        // Calculate and set the discounted price
        double discount = product.getPrice() * (product.getDiscount() / 100.0);
        double discountedPrice = product.getPrice() - discount;
        existingProduct.setDiscountAmount   (discountedPrice);
    
        // Save the updated product
        if (productService.saveProduct(existingProduct) != null) {
            session.setAttribute("successMsg", "Product updated successfully");
        } else {
            session.setAttribute("errorMsg", "Something went wrong on the server");
        }
    
        return "redirect:/admin/loadEditProduct/" + product.getId();
    }
    private String handleFileUpload(MultipartFile file, HttpSession session) {
        if (file == null || file.isEmpty()) {
            return "default.png"; // Return a default image if no file is uploaded
        }
    
        File uploadDir = new File("src/main/resources/static/image/Image");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // Create directories if they do not exist
        }
    
        // Generate a unique file name using the current timestamp
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir.getAbsolutePath() + File.separator + fileName);
    
        try {
            // Copy the file to the upload directory
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return fileName; // Return the name of the uploaded file
        } catch (IOException e) {
            session.setAttribute("errorMsg", "File upload failed: " + e.getMessage());
            return "default.png"; // Return a default image if upload fails
        }
    }
    

    @RequestMapping("/Products")
    public String products(Model model){
        model.addAttribute("products", productService.getallProduct());

        return "admin/Products";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id, HttpSession session){
        Boolean deleteproduct = productService.deleteProduct(id);
        if (deleteproduct) {
            session.setAttribute("successMsg", "Product deleted successfully");
        } else {
            session.setAttribute("errorMsg", "Something went wrong on the server");
        }
        return "redirect:/admin/Products";
    }

    @GetMapping("/loadEditProduct/{id}")
    public String editProduct(@PathVariable int id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        
        model.addAttribute("categories", categoryService.getAllCategory());
        return "admin/editProduct";
    }

}