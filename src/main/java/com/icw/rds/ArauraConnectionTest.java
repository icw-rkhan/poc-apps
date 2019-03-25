package com.icw.rds;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

import java.sql.Connection;
import java.sql.DriverManager;

public class ArauraConnectionTest {

    public static void main(String[] args) {
        auth();
        dbConnectTest();
    }

    static void dbConnectTest1(){
        String hostname = "icwapitrial.cluster-cda6dxb4vzh1.us-east-1.rds.amazonaws.com";
        String databaseName = "icwapitrial";
        String username= "icwapiuser-raja";
        String password= "khanICW333";

        String jdbc_url = "jdbc:mysql://" + hostname + ":3306/" + databaseName;

        DriverManager.setLoginTimeout(60);
        // Create a connection using the JDBC driver
        try (Connection conn = DriverManager.getConnection(jdbc_url, username, password)) {

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    static void dbConnectTest(){
        String hostname = "test.cluster-cda6dxb4vzh1.us-east-1.rds.amazonaws.com";
        String databaseName = "test";
        String username= "test";
        String password= "test1234";

        String jdbc_url = "jdbc:mysql://" + hostname + ":3306/" + databaseName;

        DriverManager.setLoginTimeout(60);
        // Create a connection using the JDBC driver
        try (Connection conn = DriverManager.getConnection(jdbc_url, username, password)) {

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    static void auth() {
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();

        try {
            System.out.printf(">AccessKeyId:%s\n>SecretKey:%s\n",
                    credentialsProvider.getCredentials().getAWSAccessKeyId(),
                    credentialsProvider.getCredentials().getAWSSecretKey());
            credentialsProvider.getCredentials();
            System.out.println("AWS Authentication - OK");
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",
                    e);
        }
    }
}
