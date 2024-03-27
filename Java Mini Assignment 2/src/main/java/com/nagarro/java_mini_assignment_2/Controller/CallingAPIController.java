package com.nagarro.java_mini_assignment_2.Controller;

import com.nagarro.java_mini_assignment_2.Entity.UserDetails;
import com.nagarro.java_mini_assignment_2.Service.RandomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class CallingAPIController {

    @Autowired
    private RandomUserService userService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${api.nationalize.url}")
    private String nationalizeApiUrl;

    @Value("${api.genderize.url}")
    private String genderizeApiUrl;

    @GetMapping("/getRandomUser")
    public CompletableFuture<UserDetails> getRandomUser() {
        return userService.getRandomUserDetails();
    }

    @GetMapping("/process-user")
    public String processUser() {
        CompletableFuture<UserDetails> userDetailsFuture = userService.getRandomUserDetails();

        try {
            UserDetails userDetails = userDetailsFuture.get();
            String name = userDetails.getName();

            // Call the /gender-and-nationalize endpoint with the fetched name
            String genderAndNationalizeResult = webClientBuilder.build()
                    .get()
                    .uri("/api/gender-and-nationalize?name=" + name)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Log the result
            System.out.println("Result from /gender-and-nationalize endpoint: " + genderAndNationalizeResult);

            // Continue with verification and saving user data
            String nationality = getNationalityFromResult(genderAndNationalizeResult);
            String gender = getGenderFromResult(genderAndNationalizeResult);

            System.out.println("Nationality: " + nationality);
            System.out.println("Gender: " + gender);

            return "User processed successfully";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing user";
        }
    }

    private String getNationalityFromResult(String result) {
        // Implement logic to extract nationality from the result
        // For example, you can use regular expressions or JSON parsing
        // Return the extracted nationality
        return "IN";
    }

    private String getGenderFromResult(String result) {
        // Implement logic to extract gender from the result
        // For example, you can use regular expressions or JSON parsing
        // Return the extracted gender
        return "male";
    }
}
