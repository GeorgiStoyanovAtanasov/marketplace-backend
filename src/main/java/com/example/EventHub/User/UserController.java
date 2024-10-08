package com.example.EventHub.User;

import com.example.EventHub.EventType.EventType;
import com.example.EventHub.JWT.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

//@GetMapping("/users")
@RestController
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private JwtService jwtService;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, UserMapper userMapper, JwtService jwtService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    @GetMapping("/users/me")
    public UserDTO authenticatedUser() {
        User user;
        UserDTO userDTO;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> foundUser = userRepository.findByEmail(email);
        if (foundUser.isPresent()){
            user = foundUser.get();
            userDTO = userMapper.toDTO(user);
            return userDTO;
        }
        else {
            throw new NoSuchElementException("No such user is found!");
        }
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/roles")
    public ResponseEntity<List<String>> getRoles(String token){
        return ResponseEntity.ok(jwtService.extractRoles(token));
    }
    @GetMapping("/users/email")
    public ResponseEntity<String> getEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok(email);
    }
    @GetMapping("/users/findByEmail")
    public boolean ifEmailExist(@RequestParam String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            return true;
        }else{
            return false;
        }
    }


}
