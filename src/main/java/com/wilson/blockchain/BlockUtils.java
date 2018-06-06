package com.wilson.blockchain;

import com.wilson.blockchain.transaction.Transaction;
import com.wilson.blockchain.transaction.TransactionInput;
import com.wilson.blockchain.transaction.TransactionOutput;
import com.wilson.blockchain.transaction.TransactionUtils;

import java.security.MessageDigest;
import java.util.HashMap;

import static com.wilson.blockchain.DataStorage.blockchain;

/**
 * Created on 5/31/18.
 */
public final class BlockUtils {
    private BlockUtils() {
    }

    // Applies Sha256 to a string and returns the result.
    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Applies sha256 to our input,
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String calculateHash(Block block) {
        String inputData = block.getPreviousHash()
                + Long.toString(block.getCreatedAt())
                + Integer.toString(block.getNonce())
                + block.getTransactionData()
                + block.getMerkleRoot();
        return BlockUtils.applySha256(inputData);
    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[Configuration.difficulty]).replace('\0', '0');

        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<>(); //a temporary working list of unspent transactions at a given block state.
        tempUTXOs.put(DataStorage.genesisTransaction.getOutputTransactions().get(0).getId(),
                DataStorage.genesisTransaction.getOutputTransactions().get(0));

        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            // compare registered hash and calculated hash:
            if (!currentBlock.getHash().equals(calculateHash(currentBlock))) {
                System.out.println("Current Hashes not equal");
                return false;
            }
            // compare previous hash and registered previous hash
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("Previous Hashes not equal");
                return false;
            }

            //check if hash is solved
            if (!currentBlock.getHash().substring(0, Configuration.difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }

            //loop thru blockchains transactions:
            TransactionOutput tempOutput;
            for (int t = 0; t < currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if (!TransactionUtils.verifySignature(currentTransaction)) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }
                if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
                    return false;
                }

                for (TransactionInput input : currentTransaction.getInputTransactions()) {
                    tempOutput = tempUTXOs.get(input.getTransactionOutputId());

                    if (tempOutput == null) {
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }

                    if (input.getUtxo().getValue() != tempOutput.getValue()) {
                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.getTransactionOutputId());
                }

                for (TransactionOutput output : currentTransaction.getOutputTransactions()) {
                    tempUTXOs.put(output.getId(), output);
                }

                if (currentTransaction.getOutputTransactions().get(0).getRecipient() != currentTransaction.getRecipient()) {
                    System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
                    return false;
                }
                if (currentTransaction.getOutputTransactions().get(1).getRecipient() != currentTransaction.getSender()) {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }
            }

        }
        System.out.println("Blockchain is valid");
        return true;
    }

    public static void mineBlock(Block block, int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
        while (!block.getHash().substring(0, difficulty).equals(target)) {
            int nonce = block.getNonce();
            block = block.setNonce(++nonce)
                    .setHash(calculateHash(block));
        }
        System.out.println("Block Mined!!! : " + block.getHash());
    }

    public static void addBlock(Block newBlock) {
        mineBlock(newBlock, Configuration.difficulty);
        blockchain.add(newBlock);
    }

}
