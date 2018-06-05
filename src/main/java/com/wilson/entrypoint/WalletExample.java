package com.wilson.entrypoint;

import com.wilson.blockchain.transaction.Transaction;
import com.wilson.blockchain.transaction.TransactionUtils;
import com.wilson.blockchain.Wallet;
import com.wilson.blockchain.WalletUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/**
 * Created on 5/31/18.
 */
public class WalletExample {
    public static Wallet walletTom;
    public static Wallet walletBob;

    public static void main(String[] args) {
        // Setup Bouncey castle as a Security Provider
        Security.addProvider(new BouncyCastleProvider());

        walletTom = new Wallet();
        walletBob = new Wallet();

        // Test public and private keys
        System.out.println("Private and public keys:");
        System.out.println(WalletUtils.getStringFromKey(walletTom.getPrivateKey()));
        System.out.println(WalletUtils.getStringFromKey(walletTom.getPublicKey()));
        // Create a test transaction from WalletA to walletB
        Transaction transaction = new Transaction(
                walletTom.getPublicKey(),
                walletBob.getPublicKey(),
                5,
                null);

        byte[] signature = TransactionUtils.generateSignature(transaction, walletTom.getPrivateKey());
        transaction.setSignature(signature);

        // Verify the signature works and verify it from the public key
        System.out.println("Is signature verified");
        System.out.println(TransactionUtils.verifySignature(transaction));
    }

}
