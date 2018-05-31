package com.wilson.blockchain;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created on 5/31/18.
 */
public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
