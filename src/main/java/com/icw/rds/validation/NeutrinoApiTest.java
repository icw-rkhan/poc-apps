package com.icw.rds.validation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.Scanner;

public class NeutrinoApiTest {
    String userName = "raja_khan";
    static String API_KEY = "nnJKm6B2A5apFw7yprzIHlOFscL11t681aCVKKe8lGxPF0a4";

    public static void main(String[] args) throws Exception {
        phoneValidationTest();
    }

    static void phoneValidationTest() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        System.out.printf("Enter Phone: ");
        String email = new Scanner(System.in).next();
        String fooResourceUrl
                = "https://emailverifierapi.com/v2/?apiKey=fU0cPYEjnw5NTybCK6Zx4HGtRJVAaqoD&email="+email;
        ResponseEntity<String> response
                = restTemplate.getForEntity(fooResourceUrl, String.class);

        System.out.println(response);
    }
}
