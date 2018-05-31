package com.wilson.blockchain;

import com.google.gson.Gson;

import java.security.PrivateKey;

/**
 * Created on 5/31/18.
 */
public final class TransactionUtils {
    private TransactionUtils() {
    }

    private static final int DEFAULT_SEQUENCE = 0;

    // This Calculates the transaction hash (which will be used as its Id)
    private static String calulateHash(Transaction transaction) {
        Transaction.sequence++; // increase the sequence to avoid 2 identical transactions
        // having the same hash
        String hashData = generateData(transaction, Transaction.sequence);
        return BlockUtils.applySha256(hashData);
    }

    // Signs all the data we dont wish to be tampered with.
    public static byte[] generateSignature(Transaction transaction, PrivateKey privateKey) {
        String data = generateData(transaction, DEFAULT_SEQUENCE);
        return WalletUtils.applyECDSASig(privateKey, data);
    }

    // Verifies the data we signed hasnt been tampered with
    public static boolean verifySignature(Transaction transaction) {
        String data = generateData(transaction, DEFAULT_SEQUENCE);
        return WalletUtils.verifyECDSASig(transaction.getSender(), data, transaction.getSignature());
    }

    private static String generateData(Transaction transaction, int sequence) {
        Data data = new Data(
                WalletUtils.getStringFromKey(transaction.getSender()),
                WalletUtils.getStringFromKey(transaction.getRecipient()),
                Float.toString(transaction.getValue()),
                Integer.toString(sequence)
        );
        return new Gson().toJson(data);
    }
}
