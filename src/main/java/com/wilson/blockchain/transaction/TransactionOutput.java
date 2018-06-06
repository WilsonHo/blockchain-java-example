package com.wilson.blockchain.transaction;

import com.wilson.blockchain.BlockUtils;
import com.wilson.blockchain.WalletUtils;

import java.security.PublicKey;

/**
 * Created on 6/1/18.
 */
public class TransactionOutput {
    private String id;
    private String parentTransactionId; //the id of the transaction this output was created in
    private PublicKey recipient; //also known as the new owner of these coins.
    private float value;

    //Constructor
    public TransactionOutput(PublicKey recipient, float value, String parentTransactionId) {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = BlockUtils.applySha256(WalletUtils.getStringFromKey(recipient) + Float.toString(value) + parentTransactionId);
    }

    //Check if coin belongs to you
    public boolean isMine(PublicKey publicKey) {
        return (publicKey == this.recipient);
    }

    public String getId() {
        return id;
    }

    public float getValue() {
        return value;
    }

    public PublicKey getRecipient() {
        return recipient;
    }
}
