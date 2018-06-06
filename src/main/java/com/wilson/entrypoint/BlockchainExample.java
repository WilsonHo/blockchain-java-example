package com.wilson.entrypoint;

import com.google.gson.GsonBuilder;
import com.wilson.blockchain.*;
import com.wilson.blockchain.transaction.Transaction;
import com.wilson.blockchain.transaction.TransactionOutputRecipient;
import com.wilson.blockchain.transaction.TransactionUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

import static com.wilson.blockchain.DataStorage.UTXOs;
import static com.wilson.blockchain.DataStorage.blockchain;

/**
 * Created on 5/31/18.
 */
public class BlockchainExample {

    public static int difficulty = 3;
    public static Wallet walletTom;
    public static Wallet walletBob;
    public static Transaction genesisTransaction;

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        Wallet coinbase = new Wallet();
        walletTom = new Wallet();
        walletBob = new Wallet();

        //create genesis transaction, which sends 100 NoobCoin to walletA:
        genesisTransaction = new Transaction(
                coinbase.getPublicKey(),
                walletTom.getPublicKey(),
                100f, null);

        byte[] genesisTransactionSignature = TransactionUtils.generateSignature(genesisTransaction, coinbase.getPrivateKey());
        genesisTransaction.setSignature(genesisTransactionSignature);

        genesisTransaction.setTransactionId("0");

        genesisTransaction.getOutputTransactions().add(
                new TransactionOutputRecipient(genesisTransaction)
        );

        UTXOs.put(
                genesisTransaction.getOutputTransactions().get(0).getId(),
                genesisTransaction.getOutputTransactions().get(0)
        ); //its important to store our first transaction in the UTXOs list.

        System.out.println("Creating and Mining Genesis block... ");

        Block genericBlock = new Block("0");

        genericBlock.addTransaction(genesisTransaction);
        BlockUtils.addBlock(genericBlock);

        //testing
        Block block1 = new Block(genericBlock.getHash());
        System.out.println("\nWalletA's balance is: " + walletTom.getBalance());
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");

        block1.addTransaction(walletTom.sendFunds(walletBob.getPublicKey(), 40f));
        BlockUtils.addBlock(block1);

        System.out.println("\nWalletA's balance is: " + walletTom.getBalance());
        System.out.println("WalletB's balance is: " + walletBob.getBalance());

        Block block2 = new Block(block1.getHash());
        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        block2.addTransaction(walletBob.sendFunds(walletTom.getPublicKey(), 1000f));
        BlockUtils.addBlock(block2);
        System.out.println("\nWalletA's balance is: " + walletBob.getBalance());
        System.out.println("WalletB's balance is: " + walletTom.getBalance());

        Block block3 = new Block(block2.getHash());
        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        block3.addTransaction(walletTom.sendFunds(walletBob.getPublicKey(), 20));
        System.out.println("\nWalletA's balance is: " + walletBob.getBalance());
        System.out.println("WalletB's balance is: " + walletTom.getBalance());

        BlockUtils.isChainValid(blockchain, difficulty);

    }
}
