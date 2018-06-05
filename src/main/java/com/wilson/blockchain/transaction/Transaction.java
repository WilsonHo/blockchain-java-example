package com.wilson.blockchain.transaction;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created on 5/31/18.
 */
public class Transaction {
    private UUID transactionId; // this is also the hash of the transaction.
    private PublicKey sender; // senders address/public key.
    private PublicKey recipient; // Recipients address/public key.
    private float value;
    private byte[] signature; // this is to prevent anybody else from spending funds in our wallet.

    private ArrayList<TransactionInput> inputTransactions = new ArrayList<>();
    private ArrayList<TransactionOutput> outputTransactions = new ArrayList<>();

    public static int sequence = 0; // a rough count of how many transactions have been generated.

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputTransactions = inputs;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public PublicKey getSender() {
        return sender;
    }

    public PublicKey getRecipient() {
        return recipient;
    }

    public float getValue() {
        return value;
    }

    public byte[] getSignature() {
        return signature;
    }

    public ArrayList<TransactionInput> getInputTransactions() {
        return inputTransactions;
    }

    public ArrayList<TransactionOutput> getOutputTransactions() {
        return outputTransactions;
    }

    public void setTransactionId(String hashId) {
        this.transactionId = UUID.nameUUIDFromBytes(hashId.getBytes());
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

//    // returns sum of inputs(UTXOs) values
//    public float getInputsValue() {
//        float total = 0;
//        for (TransactionInput i : inputTransactions) {
//            if (i.getUtxo() == null)
//                continue; // if Transaction can't be found skip it
//            total += i.getUtxo().value;
//        }
//        return total;
//    }
//
//    // returns sum of outputs:
//    public float getOutputsValue() {
//        float total = 0;
//        for (TransactionOutput o : outputTransactions) {
//            total += o.value;
//        }
//        return total;
//    }
}
