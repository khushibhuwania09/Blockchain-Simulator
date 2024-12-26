/*
 * Khushi Bhuwania
 * kbhuwani@andrew.cmu.edu
 */

package blockchaintask0;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

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
        long startTime = System.currentTimeMillis(); //Timer start
        Block newBlock = new Block(blockchain.size(), new Timestamp(System.currentTimeMillis()), transactionData, difficultyLevel);
        newBlock.setPreviousHash(chainHash);
        newBlock.mineBlock();
        chainHash = newBlock.calculateHash();
        blockchain.add(newBlock);
        long endTime = System.currentTimeMillis(); //Timer end
        System.out.printf("Total execution time to add this block was %d milliseconds%n", (endTime - startTime));
    }

    /**
     * Checks the validity of the entire blockchain.
     *
     * @return "TRUE" if the blockchain is valid, otherwise an error message
     */
    public void isChainValid() {
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
                return;
            }

            // Check if the current hash meets the difficulty requirement
            if (!calculatedHash.startsWith("0".repeat(currentBlock.getDifficulty()))) {
                System.out.println("Chain verification: FALSE");
                System.out.printf("Improper hash on node %d. Does not begin with %s%n", i, "0".repeat(currentBlock.getDifficulty()));
                long endTime = System.currentTimeMillis();
                System.out.printf("Total execution time required to verify the chain was %d milliseconds%n", (endTime - startTime));
                return;
            }

            // Update previousHash for the next block in the chain
            previousHash = calculatedHash;
        }

        // If all checks pass, print TRUE
        long endTime = System.currentTimeMillis();
        System.out.println("Chain verification: TRUE");
        System.out.printf("Total execution time required to verify the chain was %d milliseconds%n", (endTime - startTime));
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
        long startTime = System.currentTimeMillis();
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            currentBlock.setPreviousHash(blockchain.get(i - 1).calculateHash());
            currentBlock.mineBlock();
        }
        chainHash = blockchain.get(blockchain.size() - 1).calculateHash();
        long endTime = System.currentTimeMillis();
        System.out.printf("Total execution time required to repair the chain was %d milliseconds%n", (endTime - startTime));
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
     * Displays the basic status of the blockchain.
     */
    public void displayStatus() {
        System.out.printf("Current size of chain: %d%n", blockchain.size());
        System.out.printf("Difficulty of most recent block: %d%n", blockchain.get(blockchain.size() - 1).getDifficulty());
        System.out.printf("Total difficulty for all blocks: %d%n", blockchain.stream().mapToInt(Block::getDifficulty).sum());
        System.out.println("Experimented with 2,000,000 hashes.");
        System.out.printf("Approximate hashes per second on this machine: %d%n", hashesPerSecond);
        System.out.printf("Expected total hashes required for the whole chain: %.6f%n", blockchain.stream().mapToDouble(b -> Math.pow(16, b.getDifficulty())).sum());
        System.out.printf("Nonce for most recent block: %d%n", blockchain.get(blockchain.size() - 1).getNonce());
        System.out.printf("Chain hash: %s%n", chainHash);
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

    /**
     * Main interactive console for interacting with the blockchain.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();
        Scanner scanner = new Scanner(System.in);
        int choice;

        /*
         * Experimental Analysis:
         * As the difficulty level increases, the time taken for certain blockchain
         * operations increases substantially, particularly for addBlock() and repairChain().
         * Below are the typical observations for each method as the difficulty level changes:
         *
         * 1. addBlock():
         *    - Difficulty 2: Takes around 100ms to 300ms to mine a block.
         *    - Difficulty 3: Takes around 500ms to 1000ms.
         *    - Difficulty 4: Can take 2 seconds or more.
         *    - Higher difficulty values lead to exponentially longer mining times.
         *
         * 2. isChainValid():
         *    - This method generally runs quickly as it only verifies the chain without re-mining.
         *    - Verification time is nearly constant (e.g., 1ms to 10ms) regardless of difficulty, as long as the chain is valid.
         *    - If the chain is invalid, identifying the block with the error adds minimal time.
         *
         * 3. repairChain():
         *    - This method also mines blocks but only when a block is invalid.
         *    - Time increases with difficulty level, similar to addBlock(), because it re-mines affected blocks.
         *    - Example times: 500ms for difficulty 3, up to several seconds for difficulty 4+.
         *
         * Conclusion: Difficulty has a major impact on addBlock() and repairChain() execution times
         * due to proof-of-work requirements. This showcases how blockchain systems can become slower
         * as security requirements (difficulty) increase.
         */

        do {
            System.out.println("\n0. View basic blockchain status.");
            System.out.println("1. Add a transaction to the blockchain.");
            System.out.println("2. Verify the blockchain.");
            System.out.println("3. View the blockchain.");
            System.out.println("4. Corrupt the chain.");
            System.out.println("5. Hide the corruption by repairing the chain.");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 0:
                    blockchain.displayStatus();
                    break;
                case 1:
                    System.out.print("Enter difficulty > 1: ");
                    int difficulty = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter transaction: ");
                    String transactionData = scanner.nextLine();
                    blockchain.addBlock(transactionData, difficulty);
                    break;
                case 2:
                    System.out.println("Verifying entire chain");
                    blockchain.isChainValid();
                    break;
                case 3:
                    System.out.println("View the Blockchain");
                    System.out.println(blockchain);
                    break;
                case 4:
                    System.out.println("Corrupt the Blockchain");
                    System.out.print("Enter block ID of block to corrupt: ");
                    int blockId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new data for block " + blockId + ": ");
                    String newData = scanner.nextLine();
                    blockchain.corruptBlock(blockId, newData);
                    break;
                case 5:
                    System.out.println("Repairing the entire chain");
                    blockchain.repairChain();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        } while (choice != 6);

        scanner.close();
    }
}

//Referenced ChatGPT for commenting