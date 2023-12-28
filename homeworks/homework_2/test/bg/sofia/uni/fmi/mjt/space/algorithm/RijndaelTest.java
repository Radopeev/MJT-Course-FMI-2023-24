package bg.sofia.uni.fmi.mjt.space.algorithm;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;


public class RijndaelTest {

    private static Rijndael rijndaelTest;

    @Test
    void encryptionAndDecryption() {
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecretKey secretKey = keyGenerator.generateKey();

            rijndaelTest = new Rijndael(secretKey);

            byte[] original = "Modern java technologies is the best".getBytes();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(original);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            rijndaelTest.encrypt(inputStream,outputStream);

            ByteArrayInputStream encryptedData = new ByteArrayInputStream(outputStream.toByteArray());
            ByteArrayOutputStream decryptedData = new ByteArrayOutputStream();

            rijndaelTest.decrypt(encryptedData,decryptedData);

            byte[] decrypted = decryptedData.toByteArray();
            inputStream.close();
            outputStream.close();
            encryptedData.close();
            decryptedData.close();
            assertArrayEquals(original,decrypted,
                "Result is different than expected");

        } catch (NoSuchAlgorithmException | CipherException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
