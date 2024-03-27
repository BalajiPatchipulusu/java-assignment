package com.nagarro.java_mini_assignment_2.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.java_mini_assignment_2.Entity.UserDetails;
import com.nagarro.java_mini_assignment_2.Entity.UserDetailsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
public class RandomUserService {

    private static final Logger logger = LoggerFactory.getLogger(RandomUserService.class);


    private final WebClient webClient;

    public RandomUserService()
    {
        this.webClient = this.webClient();
    }


    public WebClient webClient() {
        return WebClient.builder().baseUrl("https://randomuser.me/api/").build();
    }

    public CompletableFuture<UserDetails> getRandomUserDetails() {

        ObjectMapper mapper = new ObjectMapper();

        String response = webClient.get().retrieve().bodyToMono(String.class).block();

        try{

            String name = mapper.readTree(response).get("results").get(0).get("name").get("first").asText();
            String nationality = mapper.readTree(response).get("results").get(0).get("nat").asText();
            String gender = mapper.readTree(response).get("results").get(0).get("gender").asText();
            String age = mapper.readTree(response).get("results").get(0).get("dob").get("age").asText();
            System.out.println("Name : " + name);
            UserDetails user = new UserDetails();
            user.setName(name);
            user.setGender(gender);
            user.setNationality(nationality);
            user.setAge(Integer.parseInt(age));
            return CompletableFuture.supplyAsync(()->{
                return user;
            });
        }
        catch (JsonProcessingException exception)
        {
            System.out.println("cannot parse");
        }

        return null;




    }

    public CompletableFuture<String> getNationality(String name) {
        return CompletableFuture.supplyAsync(() -> {
            String apiUrl = "https://api.nationalize.io/?name=" + name;
            return webClient.get()
                    .uri(apiUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        });
    }

    public CompletableFuture<String> getGender(String name) {
        return CompletableFuture.supplyAsync(() -> {
            String apiUrl = "https://api.genderize.io/?name=" + name;
            return webClient.get()
                    .uri(apiUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        });
    }

    public String verifyUser(String name, String nationality, String gender) {
        String expectedNationality = "IN"; // Modify as needed
        String expectedGender = "male"; // Modify as needed

        if (nationality.equals(expectedNationality) && gender.equals(expectedGender)) {
            return "VERIFIED";
        } else {
            return "TO_BE_VERIFIED";
        }
    }

    public void saveUserData(String name, String nationality, String gender, String verificationStatus) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        logger.info("Saving user data to the database...");
        logger.info("Name: {}", name);
        logger.info("Nationality: {}", nationality);
        logger.info("Gender: {}", gender);
        logger.info("Verification Status: {}", verificationStatus);
        logger.info("Date Created: {}", currentDateTime);
        logger.info("Date Modified: {}", currentDateTime);
    }
}
