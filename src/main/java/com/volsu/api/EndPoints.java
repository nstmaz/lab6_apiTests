package com.volsu.api;

public class EndPoints {
    public static final String token = "EzzdcuQjiW_K26sXYeQm0aFBLybk_25M__yL";

    public static final String users = "/public-api/users";
    public static final String usersFirstName = "/public-api/users?first_name=";
    public static final String usersID = "/public-api/users/";
    public static final String usersEmail = "/public-api/users?email=";

    public static String usersWithFirstName(String firstName) {
        return usersFirstName+firstName;
    }

    public static String usersWithID(int Id) {
        return usersID+Integer.toString(Id);
    }

    public static String usersWithEmail(String mail) {
        return usersEmail+mail;
    }

}
