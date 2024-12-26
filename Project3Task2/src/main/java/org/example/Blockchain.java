package org.example;

import java.sql.Timestamp;
import java.util.ArrayList;

/*
 * Khushi Bhuwania
 * kbhuwani@andrew.cmu.edu
 */

/**
 * The Blockchain class represents a simple blockchain structure.
 * It contains methods to add blocks, validate the chain, corrupt blocks, and repair the chain.
 */
public class Blockchain {
    private ArrayList<Block> blockchain; // List to hold blocks in the blockchain
    private String chainHash; // Hash of the most recently added block
    private int hashesPerSecond; // Approximate hash rate in hashes per second

    /**
     * Initializes the blockchain with a genesis block and sets the initial chain hash.
     */
    public Blockchain() {
        blockchain = new ArrayList<>();
        chainHash = "";

        // Create and mine the genesis block (initial block)
        Block genesisBlock = new Block(0, new Timestamp(System.currentTimeMillis()), "Genesis", 2);
        genesisBlock.mineBlock();
        chainHash = genesisBlock.calculateHash();
        blockchain.add(genesisBlock);

        // Calculate the approximate hashes per second on this machine
        computeHashesPerSecond();
    }

    /**
     * Adds a new block to the blockchain.
     *
     * @param transactionData the transaction data to be stored in the block
     * @param difficultyLevel the mining difficulty level for the block
     */
    public void addBlock(String transactionData, int difficultyLevel) {
        long startTime = System.currentTimeMillis();
        Block newBlock = new Block(blockchain.size(), new Timestamp(System.currentTimeMillis()), transactionData, difficultyLevel);
        newBlock.setPreviousHash(chainHash);
        newBlock.mineBlock();
        chainHash = newBlock.calculateHash();
        blockchain.add(newBlock);
        long endTime = System.currentTimeMillis();
        System.out.printf("Total execution time to add this block was %d milliseconds%n", (endTime - startTime));
    }



    /**
     * Checks the validity of the entire blockchain.
     *
     * @return "TRUE" if the blockchain is valid, otherwise an error message
     */
    public String isChainValid() {
        long startTime = System.currentTimeMillis();
        String previousHash = "0";

        for (int i = 0; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            String calculatedHash = currentBlock.calculateHash();

            // Check if the previous hash matches the stored previous hash
            if (!currentBlock.getPreviousHash().equals(previousHash)) {
                System.out.println("Chain verification: FALSE");
                System.out.printf("Improper hash on node %d. Does not match previous hash.%n", i);
                long endTime = System.currentTimeMillis();
                System.out.printf("Total execution time required to verify the chain was %d milliseconds%n", (endTime - startTime));
                return "FALSE"; // Return as a string indicating the chain is invalid
            }
            previousHash = calculatedHash; // Update previous hash to current for the next iteration
        }

        // If no issues found, return "TRUE"
        return "TRUE";
    }

    /**
     * Alters the data in a specific block to simulate corruption.
     *
     * @param index             the index of the block to be corrupted
     * @param newTransactionData the new transaction data for the block
     */
    public void corruptBlock(int index, String newTransactionData) {
        if (index >= 0 && index < blockchain.size()) {
            blockchain.get(index).setData(newTransactionData);
            System.out.printf("Block %d now holds %s%n", index, newTransactionData);
        } else {
            System.out.println("Invalid block index.");
        }
    }

    /**
     * Repairs the blockchain by recalculating proof of work for each block in sequence.
     */
    public void repairChain() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            currentBlock.setPreviousHash(blockchain.get(i - 1).calculateHash());
            currentBlock.mineBlock();
        }
        chainHash = blockchain.get(blockchain.size() - 1).calculateHash();
    }

    /**
     * Estimates the hashes per second by timing a set number of hash calculations.
     */
    private void computeHashesPerSecond() {
        long startTime = System.currentTimeMillis();
        int numHashes = 2000000;
        for (int i = 0; i < numHashes; i++) {
            "test".hashCode();
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        hashesPerSecond = (int) (numHashes / (duration / 1000.0));
    }

    /**
     * Returns the current size of the blockchain.
     *
     * @return the number of blocks in the blockchain.
     */
    public int getSize() {
        return blockchain.size();
    }

    /**
     * Returns the hash rate of the blockchain.
     *
     * @return the hash rate of the blockchain.
     */
    public int getHashRate() {
        return hashesPerSecond;
    }

    /**
     * Returns the chain hash (the hash of the most recent block).
     *
     * @return the chain hash.
     */
    public String getBlockchainHash() {
        return chainHash;
    }

    /**
     * Returns the last block in the blockchain.
     *
     * @return the last Block object in the blockchain, or null if empty.
     */
    public Block getLastBlock() {
        return blockchain.isEmpty() ? null : blockchain.get(blockchain.size() - 1);
    }

    /**
     * Calculates and returns the total difficulty of all blocks in the blockchain.
     *
     * @return the total difficulty.
     */
    public int getTotalDifficulty() {
        return blockchain.stream().mapToInt(Block::getDifficulty).sum();
    }

    /**
     * Calculates and returns the expected total hashes for the entire blockchain.
     *
     * @return the expected total hashes.
     */
    public double getExpectedTotalHashes() {
        return blockchain.stream().mapToDouble(b -> Math.pow(16, b.getDifficulty())).sum();
    }

    /**
     * Provides a JSON-like string representation of the entire blockchain.
     *
     * @return a JSON string showing the blockchain contents
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"ds_chain\" : [");
        for (int i = 0; i < blockchain.size(); i++) {
            builder.append(blockchain.get(i).toString());
            if (i < blockchain.size() - 1) {
                builder.append(",");
            }
        }
        builder.append("], \"chainHash\":\"").append(chainHash).append("\"}");
        return builder.toString();
    }
}
