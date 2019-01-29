package com.icw.rds.validation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.Scanner;

public class EmailVerifierApi {
    public static void main(String[] args) throws Exception {
        emailValidationTest();
        //phoneValidationTest();
    }

    static void phoneValidationTest() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        System.out.printf("Enter Email: ");
        String email = new Scanner(System.in).next();
        email = URLEncoder.encode(email, "UTF-8");
        String fooResourceUrl
                = "https://emailverifierapi.com/v2/?apiKey=fU0cPYEjnw5NTybCK6Zx4HGtRJVAaqoD&email="+email;
        ResponseEntity<String> response
                = restTemplate.getForEntity(fooResourceUrl, String.class);

        System.out.println(response);
    }

    static void emailValidationTest() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        System.out.printf("Enter Email: ");
        String email = new Scanner(System.in).next();
        //email = URLEncoder.encode(email, "UTF-8");
        String fooResourceUrl
                = "https://emailverifierapi.com/v2/?apiKey=fU0cPYEjnw5NTybCK6Zx4HGtRJVAaqoD&email="+email;
        ResponseEntity<String> response
                = restTemplate.getForEntity(fooResourceUrl, String.class);

        System.out.println(response);
    }
}
