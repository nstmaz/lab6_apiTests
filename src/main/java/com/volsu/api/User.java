package com.volsu.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
@NoArgsConstructor
public class User {

    Integer id;
    String first_name;
    String last_name;
    String gender;
    String email;
    String dob;
    String address;

    // full usr info
    public User(String firstName, String lastName, String gend, String addr, String dob) {
        this.id = null;
        this.first_name = firstName;
        this.last_name = lastName;
        this.gender = gend;
        this.email = generateEmail();
        this.address = addr;
        this.dob = dob;
    }

    // required fields
    public User(String firstName, String lastName, String gend) {
        this.id = null;
        this.first_name = firstName;
        this.last_name = lastName;
        this.gender = gend;
        this.email = generateEmail();
        this.address = null;
        this.dob = null;
    }

    // full usr info
    public User(String addr, String dob) {
        this.id = null;
        this.first_name = null;
        this.last_name = null;
        this.gender = null;
        this.email = null;
        this.address = addr;
        this.dob = dob;
    }


    public String generateEmail() {
        String randomEmail = generateRandomString(13);

        // add domain and set email
        randomEmail += "@mail.example";
        return randomEmail;
    }

    public static String generateRandomString(int length) {
        Random rnd = new Random();
        String charSet = "0123456789abcdefghijklmnopqrstuvwxyz";
        String randomString = "";

        while (randomString.length() < length) {
            int index = rnd.nextInt(charSet.length());  // choose random symbol from charSet
            randomString += charSet.charAt(index);       // add it to result string
        }

        return randomString;
    }
}
