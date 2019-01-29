package com.icw.rds;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rds.AmazonRDSClientBuilder;
import com.amazonaws.services.rds.auth.GetIamAuthTokenRequest;
import com.amazonaws.services.rds.auth.RdsIamAuthTokenGenerator;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;

public class GenerateRDSAuthToken {

    public static void main(String[] args) {

        String region = "us-east-1";
        String hostname = "icwapitrial.cluster-cda6dxb4vzh1.us-east-1.rds.amazonaws.com";
        String port = "3306";
        String username = "icwapiuser2";

        System.out.println(generateAuthToken(region, hostname, port, username));
    }

    static String generateAuthToken(String region, String hostName, String port, String username) {

        RdsIamAuthTokenGenerator generator = RdsIamAuthTokenGenerator.builder()
                .credentials(new DefaultAWSCredentialsProviderChain())
                .region(region)
                .build();

        String authToken = generator.getAuthToken(
                GetIamAuthTokenRequest.builder()
                        .hostname(hostName)
                        .port(Integer.parseInt(port))
                        .userName(username)
                        .build());


        return authToken;
    }

}

