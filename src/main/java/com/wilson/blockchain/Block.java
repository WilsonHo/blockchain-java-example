package com.wilson.blockchain;


import java.util.Date;

/**
 * Created on 5/31/18.
 */
public class Block {
    private Data data;
    private long createdAt;
    private String previousHash;
    private String hash;
    private int nonce;

    public Block(Data data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.createdAt = new Date().getTime();
        this.hash = BlockUtils.calculateHash(this);
    }

    public Block setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public Block setNonce(int nonce) {
        this.nonce = nonce;
        return this;
    }

    public Data getData() {
        return data;
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


}
