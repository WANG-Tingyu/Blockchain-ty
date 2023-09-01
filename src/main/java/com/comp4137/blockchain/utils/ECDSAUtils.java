package com.comp4137.blockchain.utils;

//import org.apache.commons.codec.binary.Hex;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class ECDSAUtils {

    private static final String ALGORITHM = "EC";
    //generate KeyPair
    public static KeyPair getKeyPair() throws Exception {
        //Creating KeyPair generator object
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);

        //Initializing the KeyPairGenerator
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        keyPairGen.initialize(256, random);

        return keyPairGen.generateKeyPair();
    }


    //generate signature
    public static String signECDSA(PrivateKey privateKey, String message) {
        String result = "";
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(privateKey);
            signature.update(message.getBytes());

            byte[] sign = signature.sign();

            //System.out.println("ECDSA signature: " + Hex.encodeHexString(sign));
            //return Hex.encodeHexString(sign);
            //System.out.println("ECDSA signature: " + DatatypeConverter.printHexBinary(sign));
            return DatatypeConverter.printHexBinary(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //verify signature
    public static boolean verifyECDSA(PublicKey publicKey, String signed, String message) {
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initVerify(publicKey);
            signature.update(message.getBytes());

            //byte[] hex = Hex.decodeHex(signed);
            byte[] hex = DatatypeConverter.parseHexBinary(signed);
            boolean bool = signature.verify(hex);

            System.out.println("verify：" + bool);
            return bool;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将 公钥/私钥 编码后以 Base64 的格式保存到指定文件
     */
    public static void saveKeyForEncodedBase64(Key key, File keyFile) throws IOException {
        // 获取密钥编码后的格式
        byte[] encBytes = key.getEncoded();

        // 转换为 Base64 文本
        String encBase64 = new BASE64Encoder().encode(encBytes);

        // 保存到文件
        IOUtils.writeFile(encBase64, keyFile);
    }
    public static String getPubKeyStr(PublicKey key){
        byte[] bytes = key.getEncoded();
        String pubKeyStr = new BASE64Encoder().encode(bytes);
        return pubKeyStr;
    }

    /**
     * 根据公钥的 Base64 文本创建公钥对象
     */
    public static PublicKey getPublicKey(String pubKeyBase64) throws Exception {
        // 把 公钥的Base64文本 转换为已编码的 公钥bytes
        byte[] encPubKey = new BASE64Decoder().decodeBuffer(pubKeyBase64);

        // 创建 已编码的公钥规格
        X509EncodedKeySpec encPubKeySpec = new X509EncodedKeySpec(encPubKey);

        // 获取指定算法的密钥工厂, 根据 已编码的公钥规格, 生成公钥对象
        return KeyFactory.getInstance(ALGORITHM).generatePublic(encPubKeySpec);
    }

    /**
     * 根据私钥的 Base64 文本创建私钥对象
     */
    public static PrivateKey getPrivateKey(String priKeyBase64) throws Exception {
        // 把 私钥的Base64文本 转换为已编码的 私钥bytes
        byte[] encPriKey = new BASE64Decoder().decodeBuffer(priKeyBase64);

        // 创建 已编码的私钥规格
        PKCS8EncodedKeySpec encPriKeySpec = new PKCS8EncodedKeySpec(encPriKey);

        // 获取指定算法的密钥工厂, 根据 已编码的私钥规格, 生成私钥对象
        return KeyFactory.getInstance(ALGORITHM).generatePrivate(encPriKeySpec);
    }

}
