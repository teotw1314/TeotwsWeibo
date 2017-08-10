package com.skyland.mylibrary.utils;

import android.util.Base64;

import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class HashUtils {
    private static final String AES_TYPE ="AES/ECB/PKCS5Padding";


    public static String md5(String setString){
        String sign = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(setString.getBytes());
            sign = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sign;
    }

    public static String AESEncrypt(String value, String setKey) {
        byte[] encrypt = null;
        try{
            value = URLEncoder.encode(value, "UTF-8");
            Key key = generateKey(setKey);
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypt = cipher.doFinal(value.getBytes());
        }catch(Exception e){
            e.printStackTrace();
        }
        if(encrypt == null){
            return "";
        }else {
            return new String(Base64.encode(encrypt, Base64.DEFAULT));
        }
    }

    /**
     * 解密
     * @param encryptData	密文
     * @return				返回明文
     */
    public static String AESDecrypt(String encryptData, String setKey) {
        byte[] decrypt = null;
        try{
            Key key = generateKey(setKey);
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, key);
            decrypt = cipher.doFinal(Base64.decode(encryptData, Base64.DEFAULT));
            String text = new String(decrypt);
            text = URLDecoder.decode(text, "UTF-8");
            return text;
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }

    private static Key generateKey(String key)throws Exception {
        try{
            return new SecretKeySpec(key.getBytes(), "AES");
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
