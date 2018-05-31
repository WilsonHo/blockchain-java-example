package com.wilson.blockchain;

import java.security.PublicKey;

/**
 * Created on 5/31/18.
 */
public class Transaction {
    private String transactionId; // this is also the hash of the transaction.
    private PublicKey sender; // senders address/public key.
    private PublicKey recipient; // Recipients address/public key.
    private float value;
    private byte[] signature; // this is to prevent anybody else from spending funds in our wallet.

//    private ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
//    private ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    public static int sequence = 0; // a rough count of how many transactions have been generated.

    public Transaction(PublicKey from, PublicKey to, float value) {//, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
//        this.inputs = inputs;
    }

    public String getTransactionId() {
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

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
}
