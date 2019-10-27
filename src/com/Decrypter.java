package com;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class Decrypter {
    private static final String initVector = "encryptionIntVec";

    public static File decrypt(String filePath, String secretKey, String privateKey) throws Exception {
        byte[] encryptedSecretKey = Base64.getDecoder().decode(secretKey);
        byte[] decSecKey = decryptSecretKey(encryptedSecretKey,privateKey);


        File encryptedFile = new File(filePath);
        byte[] decFileBytes = decryptFileData(encryptedFile,decSecKey);
        Path decryptedFile = Files.write(new File(filePath).toPath(), decFileBytes);
        return decryptedFile.toFile();
    }

    public static byte[] decryptSecretKey(byte[] encryptedSecretKey, String privateKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        byte[] privateBytes = Base64.getDecoder().decode(privateKeyStr);
        PKCS8EncodedKeySpec keySpec1 = new PKCS8EncodedKeySpec(privateBytes);
        KeyFactory keyFactoryPr = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactoryPr.generatePrivate(keySpec1);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.PRIVATE_KEY, privateKey);
        byte[] decryptedKeyBytes = cipher.doFinal(encryptedSecretKey);

        return decryptedKeyBytes;
    }

    public static  byte[] decryptFileData(File file, byte[] secretKeyBytes) throws Exception {
        byte[] decryptedFileBytes = null;
        try {
            byte [] fileByteArr=Files.readAllBytes(file.toPath());

            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(secretKeyBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            decryptedFileBytes = cipher.doFinal(fileByteArr);

        } catch (IOException ex) {
            throw new Exception("Errorencrypting/decryptingfile"+ex.getMessage(),ex);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return decryptedFileBytes;
    }
}
