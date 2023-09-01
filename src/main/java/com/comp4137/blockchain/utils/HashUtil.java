package com.comp4137.blockchain.utils;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class HashUtil {

    /**
     * Generate SHA-256 hash of a string.
     *
     * @param   data   the string to generate SHA-256 hash
     * @return         a SHA-256 hash
     */
    public static String getHashForStr(String data) {
        try {
            MessageDigest algo = MessageDigest.getInstance("SHA-256");
            algo.update(data.getBytes());
            byte[] hashOfData = algo.digest();
            return byteToHex(hashOfData);

        }catch (NoSuchAlgorithmException ex) {
            return "";
        }
    }


    /**
     * Generate SHA-256 hash of a file.
     *
     * @param   filePath   	the File to generate SHA-256 hash
     * @return         		a SHA-256 hash
     */
    public static String getHashForFile(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            //read file in multiple times to save memory
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            fis.close();

            byte[] hashOfData  = md.digest();
            return byteToHex(hashOfData);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * PoW Demo
     * @param diff number of Prefix0
     * @param str string
     * @return satisfied hash value
     */
    public static String powDemo(int diff, String str){
        String prefix0 = getPrefix0(diff);
        System.out.println("prefix0: " + prefix0);
        int nonce = 0;

        String hash = getHashForStr(str);
        while(true){
            assert prefix0 != null;
            if(hash.startsWith(prefix0)){
                System.out.println("Find target!");
                System.out.println("hash: " + hash);
                System.out.println("nonce: " + nonce);
                return hash;
            }else {
                nonce++;
                hash = getHashForStr(str + nonce);
            }
        }
    }

    public static String getPrefix0(int diff){
        if(diff <= 0){
            return null;
        }
        return String.format("%0"+diff+"d", 0);
    }



    /**
     * Converts a byte array into a hexadecimal string.
     *
     * @param   array       the byte array to convert
     * @return              a length*2 character string encoding the byte array
     */
    public static String byteToHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }

}
