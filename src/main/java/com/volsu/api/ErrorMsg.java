package com.volsu.api;
import lombok.Getter;

@Getter
public class ErrorMsg {
    private static String err401 = "Authentication failed.";
    private static String err422 = "Data validation failed. Please check the response body for detailed error messages.";
    private static String err404 = "The requested resource does not exist.";
    private static String err405 = "Method not allowed. Please check the Allow header for the allowed HTTP methods.";

    public static String getErr401Msg() {
        return err401;
    }
    public static String getErr422Msg() {
        return err422;
    }
    public static String getErr404Msg() {
        return err404;
    }
    public static String getErr405Msg() {
        return err405;
    }
}
