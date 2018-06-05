package com.wilson.blockchain.transaction;

import com.google.gson.Gson;
import com.wilson.blockchain.BlockUtils;
import com.wilson.blockchain.WalletUtils;

import java.security.PublicKey;
import java.util.UUID;

/**
 * Created on 6/1/18.
 */
public class TransactionOutputRecipient extends TransactionOutput {
    private PublicKey recipient; //also known as the new owner of these coins.

    public TransactionOutputRecipient(Transaction transaction) {
        super(transaction);
        this.recipient = transaction.getRecipient();
        this.value = transaction.getValue();
        TransactionOutputData transactionOutputData = TransactionOutputData.generateTransactionOutputRecipientData(
                WalletUtils.getStringFromKey(recipient),
                Float.toString(value),
                parentTransactionId
        );
        String hashData = new Gson().toJson(transactionOutputData);
        String hashId = BlockUtils.applySha256(hashData);
        this.id = UUID.nameUUIDFromBytes(hashId.getBytes());
    }

    //Check if coin belongs to you
    public boolean isMine(PublicKey publicKey) {
        return (publicKey == recipient);
    }

}
