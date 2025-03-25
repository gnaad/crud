package com.gnaad.student.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil {
    private static final String AES_GCM_PADDING = "AES/GCM/NoPadding";
    private static final int IV_SIZE = 12;

    public static String encrypt(String data, String secretKey) throws Exception {
        System.out.println("Executing encrypt");
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);

        SecretKey key = getSecretKeyFromString(secretKey);

        Cipher cipher = Cipher.getInstance(AES_GCM_PADDING);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(data.getBytes((StandardCharsets.UTF_8)));

        byte[] encryptedData = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, encryptedData, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, encryptedData, iv.length, encryptedBytes.length);
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public static String decrypt(String encryptedData, String secretKey) throws Exception {
        System.out.println("Executing decrypt");
        byte[] decodedData = Base64.getDecoder().decode(encryptedData.trim());

        SecretKey key = getSecretKeyFromString(secretKey);

        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(decodedData, 0, iv, 0, iv.length);
        byte[] encryptedText = new byte[decodedData.length - IV_SIZE];
        System.arraycopy(decodedData, IV_SIZE, encryptedText, 0, encryptedText.length);

        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
        Cipher cipher = Cipher.getInstance(AES_GCM_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);

        byte[] decryptedData = cipher.doFinal(encryptedText);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    public static String generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    private static SecretKey getSecretKeyFromString(String secretKey) {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(decodedKey, "AES");
    }
}
