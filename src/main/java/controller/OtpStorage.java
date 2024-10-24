package controller;

public class OtpStorage {
    private static String generatedOtp;
    private static String Useremail;

    public static void storeOtp(String otp) {
        generatedOtp = otp;
    }

    public static String getStoredOtp() {
        return generatedOtp;
    }
    public static void storeEmail(String email){
        Useremail = email;
    }
    public static String getStoredEmail() {
        return Useremail;
    }
}
