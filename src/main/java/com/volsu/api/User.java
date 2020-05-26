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

    public User(String firstName, String lastName, String gend, String mail) {
        this.id = null;
        this.first_name = firstName;
        this.last_name = lastName;
        this.gender = gend;
        this.email = mail;
    }

    public User(String firstName, String lastName, String gend) {
        this.id = null;
        this.first_name = firstName;
        this.last_name = lastName;
        this.gender = gend;
        this.email = generateEmail();
    }

    public String generateEmail() {
            String charSet = "0123456789abcdefghijklmnopqrstuvwxyz!#$%&'*+-/=?^_`{|}~";
            String randomEmail = "";
            Random rnd = new Random();

            //generate random string of 13 symbols
            while (randomEmail.length() < 13) {
                int index = rnd.nextInt(charSet.length());  // choose random symbol from charSet
                randomEmail += charSet.charAt(index);       // add it to result string
            }

            // add domain and set email
            randomEmail += "@mail.example";
            return randomEmail;
    }
}
