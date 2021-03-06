package com.cscc01.chatbot.backend.servlet.security;


import com.cscc01.chatbot.backend.model.GetRoleRequest;
import com.cscc01.chatbot.backend.model.Role;
import com.cscc01.chatbot.backend.model.User;
import com.cscc01.chatbot.backend.sql.repositories.UserRepository;
import com.cscc01.chatbot.backend.model.SignUpDto;
import com.cscc01.chatbot.backend.usersystem.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/")
public class AuthenticationController {

    @Inject
    private UserService userService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/users/role", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getRole(@RequestBody GetRoleRequest getRoleRequest) {
        Map<String, Object> response = new HashMap<>();
        ResponseEntity responseEntity;

        String username = getRoleRequest.getUsername();
        try {
            User user = userRepository.findByUsername(username);
            response.put("role", user.getRole());
            response.put("username", username);
            responseEntity = ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "username " + username + " does not exist");
            responseEntity = ResponseEntity.badRequest().body(response);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/users/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> userSignUp(@RequestBody SignUpDto signUpReq) {
        return handleUserSignUp(signUpReq, Role.USER);
    }

    @PreAuthorize("hasAuthority(hasRole('ROLE_ADMIN'))")
    @RequestMapping(value = "/users/admins/signup/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> adminSignUp(@RequestBody SignUpDto signUpReq) {
        return handleUserSignUp(signUpReq, Role.ADMIN);
    }

    private ResponseEntity<Map<String, Object>> handleUserSignUp(@RequestBody SignUpDto signUpReq, String role) {
        Map<String, Object> response = new HashMap<>();
        String username = signUpReq.getUsername();
        String password = signUpReq.getPassword();
        if (username.length() < 4 || password.length() < 4) {
            response.put("message", "username or password is too short");
            return ResponseEntity.badRequest().body(response);
        }
        User existedUser = userRepository.findByUsername(username);
        if (existedUser != null) {
            response.put("status", "username " + username + " already existed!!!");
            return ResponseEntity.badRequest().body(response);
        }
        User user = new User();
        user.setEncryptedPassword(passwordEncoder.encode(signUpReq.getPassword()));
        user.setUsername(username);
        user.setRole(role);
        userService.addNewUser(user);
        response.put("message", username + " is now registered!!!");
        return ResponseEntity.ok().body(response);
    }
}
