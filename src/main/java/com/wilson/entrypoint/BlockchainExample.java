package com.wilson.entrypoint;

import com.wilson.blockchain.Block;
import com.wilson.blockchain.BlockUtils;
import com.wilson.blockchain.DataStorage;
import com.wilson.blockchain.Wallet;
import com.wilson.blockchain.transaction.Transaction;
import com.wilson.blockchain.transaction.TransactionOutput;
import com.wilson.blockchain.transaction.TransactionUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/**
 * Created on 5/31/18.
 */
public class BlockchainExample {

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
                100f,
                null);

        byte[] genesisTransactionSignature = TransactionUtils.generateSignature(genesisTransaction, coinbase.getPrivateKey());
        genesisTransaction.setSignature(genesisTransactionSignature);
        genesisTransaction.setTransactionId("0");


        TransactionOutput transactionOutput = new TransactionOutput(genesisTransaction.getRecipient(), genesisTransaction.getValue(), genesisTransaction.getTransactionId());
        genesisTransaction.getOutputTransactions().add(transactionOutput);

        DataStorage.UTXOs.put(
                genesisTransaction.getOutputTransactions().get(0).getId(),
                genesisTransaction.getOutputTransactions().get(0)
        ); //its important to store our first transaction in the UTXOs list.
        System.out.println("Creating and Mining Genesis block... ");

        Block genericBlock = new Block("0");

        genericBlock.addTransaction(genesisTransaction);
        BlockUtils.addBlock(genericBlock);

        System.out.println("walletTom's balance is: " + walletTom.getBalance());

        //testing
        Block block1 = new Block(genericBlock.getHash());

        System.out.println("\nwalletTom is Attempting to send funds (40) to WalletB...");

        block1.addTransaction(walletTom.sendFunds(walletBob.getPublicKey(), 40f));
        BlockUtils.addBlock(block1);

        System.out.println("\nwalletTom's balance is: " + walletTom.getBalance());
        System.out.println("walletBob's balance is: " + walletBob.getBalance());

        Block block2 = new Block(block1.getHash());
        System.out.println("\nwalletTom Attempting to send more funds (1000) than it has...");
        block2.addTransaction(walletBob.sendFunds(walletTom.getPublicKey(), 1000f));
        BlockUtils.addBlock(block2);
        System.out.println("\nwalletTom's balance is: " + walletTom.getBalance());
        System.out.println("walletBob's balance is: " + walletBob.getBalance());

        Block block3 = new Block(block2.getHash());
        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        block3.addTransaction(walletBob.sendFunds(walletTom.getPublicKey(), 20));
        BlockUtils.addBlock(block3);
        System.out.println("\nwalletTom's balance is: " + walletTom.getBalance());
        System.out.println("walletBob's balance is: " + walletBob.getBalance());
        BlockUtils.isChainValid();

        Block block4 = new Block(block3.getHash());
        block4.addTransaction(walletBob.sendFunds(walletTom.getPublicKey(), 10));
        BlockUtils.addBlock(block4);
        System.out.println("\nwalletTom's balance is: " + walletTom.getBalance());
        System.out.println("walletBob's balance is: " + walletBob.getBalance());

    }
}
