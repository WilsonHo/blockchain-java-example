package com.wilson.blockchain.transaction;

import com.google.gson.Gson;
import com.wilson.blockchain.BlockUtils;
import com.wilson.blockchain.Configuration;
import com.wilson.blockchain.DataStorage;
import com.wilson.blockchain.WalletUtils;

import java.security.PrivateKey;
import java.util.ArrayList;

import static com.wilson.blockchain.Configuration.DEFAULT_SEQUENCE;

/**
 * Created on 5/31/18.
 */
public final class TransactionUtils {
    private TransactionUtils() {
    }


    // This Calculates the transaction hash (which will be used as its Id)
    private static String calculateHash(Transaction transaction) {
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
        TransactionData transactionData = new TransactionData(
                WalletUtils.getStringFromKey(transaction.getSender()),
                WalletUtils.getStringFromKey(transaction.getRecipient()),
                Float.toString(transaction.getValue()),
                Integer.toString(sequence)
        );
        return new Gson().toJson(transactionData);
    }

    // Returns true if new transaction could be created.
    public static boolean processTransaction(Transaction transaction) {

        if (verifySignature(transaction) == false) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }

        // gather transaction inputs (Make sure they are unspent):
        for (TransactionInput input : transaction.getInputTransactions()) {
            input.setUtxo(DataStorage.UTXOs.get(input.getTransactionOutputId()));
        }

        // check if transaction is valid:
        float inputValue = getInputsValue(transaction);
        if (inputValue < Configuration.minimumTransaction) {
            System.out.println("#Transaction Inputs to small: " + inputValue);
            return false;
        }

        // generate transaction outputs:
        float leftOver = inputValue - transaction.getValue(); // get value of inputs then the left over change:
        transaction.setTransactionId(calculateHash(transaction));


        transaction.getOutputTransactions().add(
                new TransactionOutput(transaction.getRecipient(),
                        transaction.getValue(),
                        transaction.getTransactionId())
        ); // send value to recipient

        transaction.getOutputTransactions().add(
                new TransactionOutput(transaction.getSender(),
                        leftOver,
                        transaction.getTransactionId())); // send the left over 'change' back to sender

        // add outputs to Unspent list
        for (TransactionOutput output : transaction.getOutputTransactions()) {
            DataStorage.UTXOs.put(output.getId(), output);
        }

        // remove transaction inputs from UTXO lists as spent:
        for (TransactionInput input : transaction.getInputTransactions()) {
            if (input.getUtxo() == null)
                continue; // if Transaction can't be found skip it
            DataStorage.UTXOs.remove(input.getUtxo().getId());
        }

        return true;
    }

    // returns sum of inputs(UTXOs) values
    public static float getInputsValue(Transaction transaction) {
        float total = 0;
        for (TransactionInput input : transaction.getInputTransactions()) {
            if (input.getUtxo() == null)
                continue; // if Transaction can't be found skip it
            total += input.getUtxo().getValue();
        }
        return total;
    }

    //     returns sum of outputs:
    public static float getOutputsValue(Transaction transaction) {
        float total = 0;
        for (TransactionOutput output : transaction.getOutputTransactions()) {
            total += output.getValue();
        }
        return total;
    }

    public static String getMerkleRoot(ArrayList<Transaction> transactions) {
        int count = transactions.size();
        ArrayList<String> previousTreeLayer = new ArrayList<>();
        for (Transaction transaction : transactions) {
            previousTreeLayer.add(transaction.getTransactionId());
        }
        ArrayList<String> treeLayer = previousTreeLayer;
        while (count > 1) {
            treeLayer = new ArrayList<>();
            for (int i = 1; i < previousTreeLayer.size(); i++) {
                String hashData = BlockUtils.applySha256(previousTreeLayer.get(i - 1).toString() + previousTreeLayer.get(i));
                treeLayer.add(hashData);
            }
            count = treeLayer.size();
            previousTreeLayer = treeLayer;
        }
        return (treeLayer.size() == 1) ? treeLayer.get(0).toString() : "";
    }
}
