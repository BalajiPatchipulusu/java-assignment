package com.nagarro.java_mini_assignment_2.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
@RequestMapping("/api")
public class GenderAndNationalize {

    @Value("${api.nationalize.url}")
    private String nationalizeApiUrl;

    @Value("${api.genderize.url}")
    private String genderizeApiUrl;

    private final WebClient webClient;

    public GenderAndNationalize(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://randomuser.me/api/").build();
    }

    @GetMapping("/gender-and-nationalize")
    @ResponseBody
    public String getGenderAndNationalize() {
        // Fetch the random user details from the RandomUser API
        JsonNode randomUserApiResponse = webClient.get()
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        // Extract relevant details from RandomUser API response
        String name = randomUserApiResponse.at("/results/0/name/first").asText();
        String nationalityFromRandomUser = randomUserApiResponse.at("/results/0/nat").asText();
        String genderFromRandomUser = randomUserApiResponse.at("/results/0/gender").asText();

        // Log the fetched details
        System.out.println("Fetched details from RandomUser API: " +
                "Name: " + name + ", Nationality: " + nationalityFromRandomUser + ", Gender: " + genderFromRandomUser);

        // Execute API requests in parallel using WebClient
        JsonNode nationalizeApiResponse = executeApi(nationalizeApiUrl + name);
        JsonNode genderizeApiResponse = executeApi(genderizeApiUrl + name);

        // Logging for debugging
        System.out.println("Nationalize API Response: " + nationalizeApiResponse);
        System.out.println("Genderize API Response: " + genderizeApiResponse);

        // Process and return the results
        return processApiResponse("Nationalize", nationalityFromRandomUser, nationalizeApiResponse) +
                processApiResponse("Genderize", genderFromRandomUser, genderizeApiResponse);
    }

    private JsonNode executeApi(String apiUrl) {
        return webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    private String processApiResponse(String apiName, String valueFromRandomUser, JsonNode response) {
        if (response != null) {
            StringBuilder result = new StringBuilder(apiName + " API Response:\n");

            // Extract and append specific fields
            if (response.has("country_id") && response.has("probability")) {
                String countryId = response.get("country_id").asText();
                double probability = response.get("probability").asDouble();

                // Verify the user based on the criteria
                if (countryId.equals(valueFromRandomUser) && probability > 0.5) {
                    result.append("Verification Status: VERIFIED\n");
                } else {
                    result.append("Verification Status: TO_BE_VERIFIED\n");
                }
            } else if (response.has("gender")) {
                String gender = response.get("gender").asText();

                // Verify the user based on the criteria
                if (gender.equals(valueFromRandomUser)) {
                    result.append("Verification Status: VERIFIED\n");
                } else {
                    result.append("Verification Status: TO_BE_VERIFIED\n");
                }
            }

            return result.toString();
        } else {
            return apiName + " API failed\n";
        }
    }
}
