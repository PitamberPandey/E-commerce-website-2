package com.example.E_commerce.website.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.E_commerce.website.Repository.UserRepository;
import com.example.E_commerce.website.Service.UserService;
import com.example.E_commerce.website.model.UserDts;

@Service
public class UserServiceImpl implements UserService  {
@Autowired
    private UserRepository userRepository;

    @Override
    public UserDts saveUser(UserDts user) {
         UserDts saveUser=userRepository.save(user);
         return saveUser;

    }

}
