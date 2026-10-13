package com.example.auth.util;

public class MaskingUtil {

    public static String maskIdentificationNumber(String idNum) {
        if (idNum == null || idNum.isBlank()) {
            return idNum;
        }
        if (idNum.length() <= 4) {
            return "****";
        }
        return "****" + idNum.substring(idNum.length() - 4);
    }

    public static String maskEmail(String email) {
        if (email == null || email.isBlank()) {
            return email;
        }
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            return "***" + (atIndex != -1 ? email.substring(atIndex) : "");
        }
        return email.charAt(0) + "***@" + email.substring(atIndex + 1);
    }

    public static String maskMobileNumber(String mobileNumber) {
        if (mobileNumber == null || mobileNumber.isBlank()) {
            return mobileNumber;
        }
        if (mobileNumber.length() <= 4) {
            return "****";
        }
        return "****" + mobileNumber.substring(mobileNumber.length() - 4);
    }
}
