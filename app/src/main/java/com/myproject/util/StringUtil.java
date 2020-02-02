package com.myproject.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static String checkNullString(String string) {

        if(string == null){
            return "";
        }
        else{
            return string;
        }
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toCharArray();

        if (string == null) {
            return "";
        }

        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    public static String capitalizeAllString(String string) {
        char[] chars = string.toUpperCase().toCharArray();

        return String.valueOf(chars);
    }

    public static String capitalizeFirstString(String string) {
        if(checkNullString(string).isEmpty()){
            return "";
        }

        try {
            char[] chars = string.toCharArray();

            for (int i = 0; i < chars.length; i++) {
                if (i == 0) {
                    chars[i] = Character.toUpperCase(chars[i]);
                }
            }
            return String.valueOf(chars);
        } catch (Exception e) {
            return "";
        }
    }


    public static String capitalizeStringStyle(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        boolean found2 = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found) {
                if (chars[i] == '<') {
                    found2 = true;
                }

                if (found2 == true) {
                    if (chars[i] == '>') {
                        found2 = false;
                        if (i + 1 < chars.length) {
                            chars[i + 1] = Character.toUpperCase(chars[i + 1]);
                        }
                        found = true;
                    }
                } else {
                    chars[i] = Character.toUpperCase(chars[i]);
                    found = true;
                }


            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    public static String capitalizeHTML(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    public static boolean isEmailFormatCorrect(String email){
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(email);

        if(!m.matches()){
            return false;
        }
        return true;
    }

    public static boolean isAlphaNumeric(String s){
        String pattern= "^[a-zA-Z0-9]*$";
        if(s.matches(pattern)){
            return true;
        }
        return false;
    }

    public static boolean isAlpha(String s){
        String pattern= "^[a-zA-Z]+$";
        if(s.matches(pattern)){
            return true;
        }
        return false;
    }

    public static boolean isNumeric(String s){
        String pattern= "^.*[0-9].*$";
        if(s.matches(pattern)){
            return true;
        }
        return false;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static String toCurrDigitGrouping(String number){
        BigDecimal dec = new BigDecimal(number);
        NumberFormat formatter = new DecimalFormat("#,###.##");
        return "IDR " + formatter.format(dec);
    }
}

