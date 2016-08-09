package com.codesanook.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class HashUtils {

    private  static Log log = LogFactory.getLog(HashUtils.class);

    public static String md5Hex(String message){
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(message.getBytes("UTF-8"));

            //converting byte array to Hexadecimal String
            StringBuilder sb = new StringBuilder(2*hash.length);
            for(byte b : hash){
                sb.append(String.format("%02x", b&0xff));
            }

            digest = sb.toString();

        } catch (UnsupportedEncodingException ex) {
            log.error(ex);
        } catch (NoSuchAlgorithmException ex) {
            log.error(ex);
        }
        return digest;
    }

    public static String generatePassword(int resetPasswordLength)
    {
        // Create an array of characters to user for password reset.
        // Exclude confusing or ambiguous characters such as 1 0 l o i
        String[] characters = new String[] { "2", "3", "4", "5", "6", "7", "8",
                "9", "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "m", "n",
                "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

        StringBuffer newPassword = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < resetPasswordLength; i++)
        {
            newPassword.append(characters[rnd.nextInt(characters.length)]);
        }
        return newPassword.toString();
    }

}
