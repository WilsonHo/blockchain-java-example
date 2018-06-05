package com.wilson.blockchain.transaction;

import com.google.gson.Gson;
import com.wilson.blockchain.BlockUtils;
import com.wilson.blockchain.WalletUtils;

import java.security.PublicKey;

/**
 * Created on 6/1/18.
 */
public class TransactionOutputSender extends TransactionOutput {
    private PublicKey sender;
    private float leftOver; //the amount of coins they own

    public TransactionOutputSender(Transaction transaction, float leftOver) {
        super(transaction);
        this.sender = transaction.getSender();
        this.leftOver = leftOver;
        TransactionOutputData transactionOutputData = TransactionOutputData.generateTransactionOutputSenderData(
                WalletUtils.getStringFromKey(sender),
                Float.toString(leftOver),
                parentTransactionId
        );
        String hashData = new Gson().toJson(transactionOutputData);
        String hashId = BlockUtils.applySha256(hashData);
        this.id = hashId;
    }

    @Override
    public boolean isMine(PublicKey publicKey) {
        return false;
    }
}
