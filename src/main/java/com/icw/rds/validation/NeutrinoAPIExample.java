package com.icw.rds.validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class NeutrinoAPIExample {
    private static String USER_ID = "raja_khan";
    private static String API_KEY = "nnJKm6B2A5apFw7yprzIHlOFscL11t681aCVKKe8lGxPF0a4";

    public static void main(String[] args) {
        try {
            HttpPost httpPost = new HttpPost("https://neutrinoapi.com/ip-info");
            List<NameValuePair> postData = new ArrayList<>();
            postData.add(new BasicNameValuePair("user-id", "raja_khan"));
            postData.add(new BasicNameValuePair("api-key", "nnJKm6B2A5apFw7yprzIHlOFscL11t681aCVKKe8lGxPF0a4"));
            postData.add(new BasicNameValuePair("ip", "162.209.104.195"));
            httpPost.setEntity(new UrlEncodedFormEntity(postData));

            HttpResponse response = HttpClients.createDefault().execute(httpPost);
            String jsonStr = EntityUtils.toString(response.getEntity());

            JSONObject json = new JSONObject(jsonStr);
            System.out.println("Country: " + json.getString("country"));
            System.out.println("Country Code: " + json.getString("country-code"));
            System.out.println("Region: " + json.getString("region"));
            System.out.println("City: " + json.getString("city"));
        } catch (IOException | ParseException | JSONException ex) {
            ex.printStackTrace();
        }
    }

    private List<NameValuePair> auth(){
        List<NameValuePair> postData = new ArrayList<>();
        postData.add(new BasicNameValuePair("user-id", USER_ID));
        postData.add(new BasicNameValuePair("api-key", API_KEY));
        postData.add(new BasicNameValuePair("ip", "162.209.104.195"));
        httpPost.setEntity(new UrlEncodedFormEntity(postData));

    }

    public static GeoInfo getGeoInfo() {
        try {
            HttpPost httpPost = new HttpPost("https://neutrinoapi.com/ip-info");
            List<NameValuePair> postData = new ArrayList<>();
            postData.add(new BasicNameValuePair("user-id", "raja_khan"));
            postData.add(new BasicNameValuePair("api-key", "nnJKm6B2A5apFw7yprzIHlOFscL11t681aCVKKe8lGxPF0a4"));
            postData.add(new BasicNameValuePair("ip", "162.209.104.195"));
            httpPost.setEntity(new UrlEncodedFormEntity(postData));

            HttpResponse response = HttpClients.createDefault().execute(httpPost);
            String jsonStr = EntityUtils.toString(response.getEntity());

            JSONObject json = new JSONObject(jsonStr);
            System.out.println("Country: " + json.getString("country"));
            System.out.println("Country Code: " + json.getString("country-code"));
            System.out.println("Region: " + json.getString("region"));
            System.out.println("City: " + json.getString("city"));

            return new GeoInfo(json.getString("country"), json.getString("country-code"),
                    json.getString("region"), json.getString("city"));
        } catch (IOException | ParseException | JSONException ex) {
            ex.printStackTrace();
        }

        return null;
    }

}

class GeoInfo {
    private String country;
    private String countryCode;
    private String region;
    private String city;

    public GeoInfo(String country, String countryCode, String region, String city) {
        this.country = country;
        this.countryCode = countryCode;
        this.region = region;
        this.city = city;
    }

    public String getCountry() {
        return country;
    }


    public String getCountryCode() {
        return countryCode;
    }

    public String getRegion() {
        return region;
    }

    public String getCity() {
        return city;
    }

}