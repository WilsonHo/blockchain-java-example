package com.wilson.blockchain;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

/**
 * Created on 5/31/18.
 */
public final class WalletUtils {
    private WalletUtils() {

    }

    private static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random); // 256 bytes provides an
            // acceptable security level
            return keyGen.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void createWalletKeyPair(Wallet wallet) {
        KeyPair keyPair = generateKeyPair();
        // Set the public and private keys from the keyPair
        wallet.setPrivateKey(keyPair.getPrivate());
        wallet.setPublicKey(keyPair.getPublic());
    }

    // Applies ECDSA Signature and returns the result ( as bytes ).
    public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
        Signature dsa;
        byte[] output = new byte[0];
        try {
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSig = dsa.sign();
            output = realSig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    // Verifies a String signature
    public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
