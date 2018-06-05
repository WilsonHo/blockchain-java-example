package com.wilson.blockchain.transaction;

import java.security.PublicKey;

/**
 * Created on 6/1/18.
 */
public abstract class TransactionOutput {
    protected String id;
    protected String parentTransactionId; //the id of the transaction this output was created in
    protected float value;


//    //Constructor
//    public TransactionOutput(PublicKey recipient, float value, UUID parentTransactionId) {
//        this.recipient = recipient;
//        this.value = value;
//        this.parentTransactionId = parentTransactionId;
//
//        TransactionOutputData transactionOutputData = new TransactionOutputData(
//                WalletUtils.getStringFromKey(recipient),
//                Float.toString(value),
//                parentTransactionId
//        );
//        String hashData = new Gson().toJson(transactionOutputData);
//        String hashId = BlockUtils.applySha256(hashData);
//        this.id = UUID.nameUUIDFromBytes(hashId.getBytes());
//    }

    public TransactionOutput(Transaction transaction) {
        this.parentTransactionId = transaction.getTransactionId();
    }

    public float getValue() {
        return value;
    }

    public String getId() {
        return id;
    }

    public abstract boolean isMine(PublicKey publicKey);

}
