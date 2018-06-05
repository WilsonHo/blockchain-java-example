package com.wilson.blockchain;


import com.wilson.blockchain.transaction.Transaction;
import com.wilson.blockchain.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on 5/31/18.
 */
public class Block {
    private long createdAt;
    private String previousHash;
    private String hash;
    private int nonce;
    public String merkleRoot;
    public List<Transaction> transactions = new ArrayList<>(); //our data will be a simple message.

    //Block Constructor.
    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.createdAt = new Date().getTime();
        this.hash = BlockUtils.calculateHash(this); //Making sure we do this after we set the other values.
    }

    public Block setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public Block setNonce(int nonce) {
        this.nonce = nonce;
        return this;
    }

    public List<Transaction> getTransactionData() {
        return transactions;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public int getNonce() {
        return nonce;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

    //Add transactions to this block
    public boolean addTransaction(Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if (transaction == null) return false;
        if ((previousHash != "0")) {
            if ((TransactionUtils.processTransaction(transaction) != true)) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }

}
