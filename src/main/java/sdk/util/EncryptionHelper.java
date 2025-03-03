package sdk.util;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class EncryptionHelper {

    private final static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKMbmkHAJQmplfJYfb+BXlLbr0YNp1JduxcF2cgC7KgmVGyrc0ki9OOh2e4nHUQCPkj/WMax3ESqbfXkqiglD2pjn3GdlZq9y8UKp7ULfICbYo50oRDkAN80Q1Rkzp/vvBEytwU0mKUVKflNINvaG1qmqOxFXl+67cTGYsp3WrQwIDAQAB";
    private final static String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIoxuaQcAlCamV8lh9v4FeUtuvRg2nUl27FwXZyALsqCZUbKtzSSL046HZ7icdRAI+SP9YxrHcRKpt9eSqKCUPamOfcZ2Vmr3LxQqntQt8gJtijnShEOQA3zRDVGTOn++8ETK3BTSYpRUp+U0g29obWqao7EVeX7rtxMZiyndatDAgMBAAECgYAhAoC0BSa+ZzdkhVv+/LMKM9K5BhuYqQrhxxPtSqcio2QatVhuu86F8pbZuYB0MUABnCuMt5d8KFLU/zp3uAYrjsj6FtZ6F4kw/BdQxrojWvfshDV1HwRTn8J2FBBqrfNEIUSTddz6U887seUR7sy+WIEG0iVsBd5B/fATZXZnrQJBAIxLCvtAp4QRaNoejQ/j3PDkTlHb0uJIj/qH90ignbJaAxmxcbT3DyfvogVxuOHtUSuz0KvbVbVkjH7IigRoP9UCQQD8K4dxzNKZjuzocwsbjWC3DmKbWdQpvERVaDbaNAROj/cFl0E5yMCAnP6z43QivWBQDWJgVJa+49ZQB+R3PuK3AkAqnum5Jw09PCTEjx9/wlPvAw0gJU5N3ImRLRldAR6nfl0Owy+uMJh0I4LvcHsgd/5+7mPosfu53dAo/Deh2yH5AkBPZADzghG3yLirgjS4Cu2tFDlVQZW7Qgude/7w+bl1ZnvV5cXiRGLe2w6RYgXZPhgJe/1B6L/A/4gkkCeMp17HAkAPaoM/4eEKafaKoQ5zFEznRzqZ7hf9cOFbiD7zYo2y1aAgX+bxSK0u0wu/tHLy3Ia6P5nU1kPydtkk8DNE/gdq";


    public static String encryptRSA(String rawText) throws Exception {
        PublicKey publicKey = getPublicKeyFromString(EncryptionHelper.publicKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encodeBase64String(cipher.doFinal(rawText.getBytes("UTF-8")));
    }


    public static String decryptRSA(String cipherText) throws Exception {
        PrivateKey privateKey = getPrivateKeyFromString(EncryptionHelper.privateKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.decodeBase64(cipherText)), "UTF-8");
    }

    public static RSAPublicKey getPublicKeyFromString(String key) throws Exception {
        String publicKeyPEM = key;

        // Base64 decode data
        byte[] encoded = Base64.decodeBase64(publicKeyPEM);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
        return pubKey;
    }

    public static PrivateKey getPrivateKeyFromString(String key) throws Exception {
        StringBuilder pkcs8Lines = new StringBuilder();
        BufferedReader rdr = new BufferedReader(new StringReader(privateKey));
        String line;
        while ((line = rdr.readLine()) != null) {
            pkcs8Lines.append(line);
        }
        // Remove any whitespace
        String pkcs8Pem = pkcs8Lines.toString();
        pkcs8Pem = pkcs8Pem.replaceAll("\\s+", "");
        // Base64 decode the result
        byte[] pkcs8EncodedBytes = Base64.decodeBase64(pkcs8Pem);
        // extract the private key
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privKey = kf.generatePrivate(keySpec);
        return privKey;
    }

    public static void main(String[] args) throws Exception {
        String StringEnc = EncryptionHelper.encryptRSA("test");
        System.out.println(" ==========>" + StringEnc);
        System.out.println(" ==========>" + EncryptionHelper.decryptRSA(StringEnc));
    }

}
