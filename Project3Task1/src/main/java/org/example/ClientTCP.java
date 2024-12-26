/*
 * Khushi Bhuwania
 * kbhuwani@andrew.cmu.edu
 */
package org.example;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

/**
 * ClientTCP serves as a client to interact with the blockchain server over TCP sockets.
 * It sends JSON-formatted requests for blockchain operations, such as adding a block,
 * verifying the chain, viewing the blockchain, and repairing the chain.
 */
public class ClientTCP {
    public static void main(String args[]) {
        Socket clientSocket = null;
        try {
            // Connect to the server socket running on localhost:7777
            int serverPort = 7777;
            clientSocket = new Socket("localhost", serverPort);

            // Set up input and output streams for communication with the server
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

            // Set up Gson for JSON serialization/deserialization
            Gson gson = new Gson();
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
            int choice;
            int blockId = 0; // Declare blockId for corrupting specific blocks

            do {
                // Display menu options
                System.out.println("\n0. View basic blockchain status.");
                System.out.println("1. Add a transaction to the blockchain.");
                System.out.println("2. Verify the blockchain.");
                System.out.println("3. View the blockchain.");
                System.out.println("4. Corrupt the chain.");
                System.out.println("5. Hide the corruption by repairing the chain.");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                String choiceInput = typed.readLine().trim();
                choice = Integer.parseInt(choiceInput);

                RequestMessage requestMessage = null;
                switch (choice) {
                    case 0: // Retrieve blockchain status
                        requestMessage = new RequestMessage("getStatus", null, 0);
                        break;
                    case 1: // Add a new block to the blockchain
                        System.out.print("Enter difficulty > 1: ");
                        int difficulty = Integer.parseInt(typed.readLine());
                        System.out.print("Enter transaction: ");
                        String transaction = typed.readLine();
                        requestMessage = new RequestMessage("addBlock", transaction, difficulty);
                        break;
                    case 2: // Verify the integrity of the blockchain
                        requestMessage = new RequestMessage("verifyChain", null, 0);
                        break;
                    case 3: // View the entire blockchain
                        requestMessage = new RequestMessage("viewChain", null, 0);
                        break;
                    case 4: // Corrupt a block in the blockchain
                        System.out.println("Corrupt the Blockchain");
                        System.out.print("Enter block ID of block to corrupt: ");
                        blockId = Integer.parseInt(typed.readLine()); // Initialize blockId here
                        System.out.print("Enter new data for block " + blockId + ": ");
                        String newData = typed.readLine();
                        requestMessage = new RequestMessage("corruptBlock", newData, blockId);
                        break;
                    case 5: // Repair the blockchain
                        requestMessage = new RequestMessage("repairChain", null, 0);
                        break;
                    case 6: // Exit
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }

                // Send the request to the server and process the response
                if (requestMessage != null) {
                    String jsonRequest = gson.toJson(requestMessage);
                    out.println(jsonRequest);
                    out.flush();
                    String jsonResponse = in.readLine();
                    ResponseMessage responseMessage = gson.fromJson(jsonResponse, ResponseMessage.class);

                    switch (choice) {
                        case 0: // Display blockchain status
                            System.out.println("Current size of chain: " + responseMessage.getChainSize());
                            System.out.println("Difficulty of most recent block: " + responseMessage.getDifficulty());
                            System.out.println("Total difficulty for all blocks: " + responseMessage.getTotalDifficulty());
                            System.out.println("Experimented with 2,000,000 hashes.");
                            System.out.println("Approximate hashes per second on this machine: " + responseMessage.getHashesPerSecond());
                            System.out.println("Expected total hashes required for the whole chain: " + responseMessage.getTotalExpectedHashes());
                            System.out.println("Nonce for most recent block: " + responseMessage.getNonce());
                            System.out.println("Chain hash: " + responseMessage.getChainHash());
                            break;
                        case 1: // Display block creation time
                            long blockCreationTime = responseMessage.getEndTime() - responseMessage.getStartTime();
                            System.out.println("Total execution time to add this block was " + blockCreationTime + " milliseconds");
                            break;
                        case 2: // Display chain verification result
                            long verificationTime = responseMessage.getEndTime() - responseMessage.getStartTime();
                            System.out.println("Verifying entire chain");
                            System.out.println("Chain verification: " + responseMessage.getMessage());
                            System.out.println("Total execution time required to verify the chain was " + verificationTime + " milliseconds");
                            break;
                        case 3: // Display the blockchain
                            System.out.println("View the Blockchain");
                            System.out.println(responseMessage.getChainHash());
                            break;
                        case 4: // Display confirmation of block corruption
                            System.out.println("Block " + blockId + " now holds " + requestMessage.getTransactionData());
                            break;
                        case 5: // Display chain repair time
                            long repairTime = responseMessage.getEndTime() - responseMessage.getStartTime();
                            System.out.println("Repairing the entire chain");
                            System.out.println("Total execution time required to repair the chain was " + repairTime + " milliseconds");
                            break;
                        case 6: // Exit
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
                    }
                }
            } while (choice != 6);
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                // Ignore exception on close
            }
        }
    }
}
