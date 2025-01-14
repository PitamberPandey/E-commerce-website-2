package com.example.E_commerce.website.Contoller;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.E_commerce.website.Service.CategporyService;
import com.example.E_commerce.website.Service.CategporyService;
import com.example.E_commerce.website.Service.ProductService;
import com.example.E_commerce.website.Service.UserService;
import com.example.E_commerce.website.model.Category;
import com.example.E_commerce.website.model.Product;
import com.example.E_commerce.website.model.UserDts;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    @Autowired
    private CategporyService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/products")
    public String product(Model m, @RequestParam(value = "category", defaultValue = "") String category) {
        List<Category> categories = categoryService.getAllActiveCategory();
        List<Product> products = productService.getAllActiveProduct(category);
        m.addAttribute("categories", categories);
        m.addAttribute("products", products);
        m.addAttribute("paramValue", category);
        return "product";
    }

    @GetMapping("/product/{id}")
    public String products(@PathVariable int id, Model m) {
        Product product = productService.getProductById(id);
        m.addAttribute("product", product);
        return "ViewProduct";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute UserDts user,
                           @RequestParam("img") MultipartFile file,
                           HttpSession session) {
        
        String imageName = handleFileUpload(file, session);
        user.setProfileImage(imageName);

        // Attempt to save the user
        boolean saveUserSuccess = userService.saveUser(user) != null;
        if (!saveUserSuccess) {
            session.setAttribute("errorMsg", "Not saved! Internal server error");
            return "redirect:/register";
        }

        session.setAttribute("successMsg", "User saved successfully");
        return "redirect:/register";
    }

    private String handleFileUpload(MultipartFile file, HttpSession session) {
        if (file == null || file.isEmpty()) {
            return "default.png";
        }

        File uploadDir = new File("src/main/resources/static/image/Image");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir.getAbsolutePath() + File.separator + fileName);

        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            session.setAttribute("errorMsg", "File upload failed: " + e.getMessage());
            return "default.png";
        }
    }
}
