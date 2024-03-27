package com.nagarro.java_mini_assignment_2.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.java_mini_assignment_2.Entity.UserRequest;
import com.nagarro.java_mini_assignment_2.Entity.UserDetailsResponse;
import com.nagarro.java_mini_assignment_2.Response.FinalApiResponse;
import com.nagarro.java_mini_assignment_2.Service.RandomUserService;
import com.nagarro.java_mini_assignment_2.Service.UserService;
import com.nagarro.java_mini_assignment_2.Exception.*;

@RestController


public class CallingAPIController {

    @Autowired
    public  RandomUserService randomUserApi;

    @Autowired
    public UserService secondApiService;

    @PostMapping("/users")
    public ResponseEntity<?> createUsers(@RequestBody UserRequest size) {
        try {
            int requestSize = size.getSize();
            List<UserDetailsResponse> createdUsers = randomUserApi.createUsers(requestSize);
            return ResponseEntity.ok(createdUsers);
        } catch (Exception e) {
            ErrorMessage error = new ErrorMessage("page not found, User creation failed", 404);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(
            @RequestParam String sortType,
            @RequestParam String sortOrder,
            @RequestParam int limit,
            @RequestParam int offset) {
        try {
            FinalApiResponse result = secondApiService.getSortedUsers(sortType, sortOrder, limit, offset);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            ErrorMessage error = new ErrorMessage("page not found", 404);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }



}
