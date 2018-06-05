package com.wilson.blockchain;

import com.wilson.blockchain.transaction.Transaction;
import com.wilson.blockchain.transaction.TransactionInput;
import com.wilson.blockchain.transaction.TransactionOutput;
import com.wilson.blockchain.transaction.TransactionUtils;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created on 5/31/18.
 */
public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    private HashMap<String, TransactionOutput> UTXOs = new HashMap<>(); //only UTXOs owned by this wallet.

    public Wallet() {
        KeyPair keyPair = WalletUtils.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public float getBalance() {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : DataStorage.UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            if (UTXO.isMine(publicKey)) { // if output belongs to me ( if coins
                // belong to me )
                UTXOs.put(UTXO.getId(), UTXO); // add it to our list of unspent
                // transactions.
                total += UTXO.getValue();
            }
        }
        return total;
    }

    // Generates and returns a new transaction from this wallet.
    public Transaction sendFunds(PublicKey recipient, float value) {
        if (getBalance() < value) { // gather balance and check funds.
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }
        // create array list of inputs
        ArrayList<TransactionInput> inputs = new ArrayList<>();

        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            total += UTXO.getValue();
            inputs.add(new TransactionInput(UTXO.getId()));
            if (total > value)
                break;
        }

        Transaction newTransaction = new Transaction(publicKey, recipient, value, inputs);
        newTransaction.setSignature(TransactionUtils.generateSignature(newTransaction, privateKey));

        for (TransactionInput input : inputs) {
            UTXOs.remove(input.getTransactionOutputId());
        }
        return newTransaction;
    }
}
