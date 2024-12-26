/*
 * Khushi Bhuwania
 * kbhuwani@andrew.cmu.edu
 */
package org.example;

import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Scanner;

/**
 * ServerTCP handles client requests over TCP sockets, manages the blockchain on the server,
 * and processes various blockchain operations.
 */
public class ServerTCP {
    public static void main(String[] args) {
        try {
            // Set up server socket to listen on port 7777
            int port = 7777;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Blockchain Server Running");

            // Initialize blockchain with a genesis block
            Blockchain blockchain = new Blockchain();

            // Continuously listen for incoming client connections
            while (true) {
                Socket clientSocket = serverSocket.accept();
                try (Scanner inputScanner = new Scanner(clientSocket.getInputStream());
                     PrintWriter outputWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())))) {

                    Gson gson = new Gson();

                    while (inputScanner.hasNextLine()) {
                        // Process each incoming client request
                        String requestJson = inputScanner.nextLine();
                        RequestMessage request = gson.fromJson(requestJson, RequestMessage.class);
                        String responseJson = processRequest(request, blockchain, gson);
                        outputWriter.println(responseJson);
                        outputWriter.flush();

                        // Log request and response details for debugging
                        System.out.println("\nWe have a visitor");
                        System.out.println("THE JSON REQUEST MESSAGE IS: " + requestJson);
                        System.out.println("THE JSON RESPONSE MESSAGE IS: " + responseJson);
                        System.out.println("Number of Blocks on Chain: " + blockchain.getSize());
                    }
                } catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                } finally {
                    System.out.println("Client disconnected");
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    /**
     * Processes incoming requests and generates appropriate responses.
     *
     * @param request    The request message received from the client.
     * @param blockchain The blockchain instance to perform operations on.
     * @param gson       Gson instance for JSON processing.
     * @return JSON-formatted response message.
     */
    private static String processRequest(RequestMessage request, Blockchain blockchain, Gson gson) {
        long startTime, endTime;
        String responseJson;

        switch (request.getAction()) {
            case "getStatus": // Retrieve blockchain status
                startTime = System.currentTimeMillis();
                endTime = System.currentTimeMillis();
                responseJson = gson.toJson(new ResponseMessage(
                        "success", "Viewing status", blockchain.getBlockchainHash(), blockchain.getSize(),
                        blockchain.getLastBlock().getDifficulty(), blockchain.getTotalDifficulty(),
                        blockchain.getHashRate(), blockchain.getExpectedTotalHashes(),
                        blockchain.getLastBlock().getNonce(), startTime, endTime));
                break;

            case "addBlock": // Add a new block to the blockchain
                startTime = System.currentTimeMillis();
                Block newBlock = new Block(blockchain.getSize(), new Timestamp(System.currentTimeMillis()), request.getTransactionData(), request.getDifficultyOrIndex());
                blockchain.addBlock(request.getTransactionData(), request.getDifficultyOrIndex());
                endTime = System.currentTimeMillis();
                responseJson = gson.toJson(new ResponseMessage(
                        "success", "Block added", blockchain.getBlockchainHash(), blockchain.getSize(),
                        blockchain.getLastBlock().getDifficulty(), blockchain.getTotalDifficulty(),
                        blockchain.getHashRate(), blockchain.getExpectedTotalHashes(),
                        blockchain.getLastBlock().getNonce(), startTime, endTime));
                break;

            case "verifyChain": // Verify the blockchain's integrity
                startTime = System.currentTimeMillis();
                String validationResult = blockchain.isChainValid();
                endTime = System.currentTimeMillis();
                responseJson = gson.toJson(new ResponseMessage(
                        "success", validationResult, blockchain.getBlockchainHash(), blockchain.getSize(),
                        blockchain.getLastBlock().getDifficulty(), blockchain.getTotalDifficulty(),
                        blockchain.getHashRate(), blockchain.getExpectedTotalHashes(),
                        blockchain.getLastBlock().getNonce(), startTime, endTime));
                break;

            case "viewChain": // View the entire blockchain
                startTime = System.currentTimeMillis();
                endTime = System.currentTimeMillis();
                responseJson = gson.toJson(new ResponseMessage(
                        "success", "Viewing blockchain", blockchain.toString(), blockchain.getSize(),
                        blockchain.getLastBlock().getDifficulty(), blockchain.getTotalDifficulty(),
                        blockchain.getHashRate(), blockchain.getExpectedTotalHashes(),
                        blockchain.getLastBlock().getNonce(), startTime, endTime));
                break;

            case "corruptBlock": // Corrupt a specific block in the blockchain
                startTime = System.currentTimeMillis();
                int blockIndex = request.getDifficultyOrIndex();
                if (blockIndex >= 0 && blockIndex < blockchain.getSize()) {
                    blockchain.corruptBlock(blockIndex, request.getTransactionData());
                    endTime = System.currentTimeMillis();
                    responseJson = gson.toJson(new ResponseMessage(
                            "success", "Block " + blockIndex + " corrupted with new data",
                            blockchain.getBlockchainHash(), blockchain.getSize(),
                            blockchain.getLastBlock().getDifficulty(), blockchain.getTotalDifficulty(),
                            blockchain.getHashRate(), blockchain.getExpectedTotalHashes(),
                            blockchain.getLastBlock().getNonce(), startTime, endTime));
                } else {
                    responseJson = gson.toJson(new ResponseMessage(
                            "error", "Invalid block index", null, blockchain.getSize(),
                            0, blockchain.getTotalDifficulty(), 0, 0, null, 0L, 0L));
                }
                break;

            case "repairChain": // Repair the blockchain by recalculating hashes
                startTime = System.currentTimeMillis();
                blockchain.repairChain();
                endTime = System.currentTimeMillis();
                responseJson = gson.toJson(new ResponseMessage(
                        "success", "Blockchain repaired", blockchain.getBlockchainHash(), blockchain.getSize(),
                        blockchain.getLastBlock().getDifficulty(), blockchain.getTotalDifficulty(),
                        blockchain.getHashRate(), blockchain.getExpectedTotalHashes(),
                        blockchain.getLastBlock().getNonce(), startTime, endTime));
                break;

            default: // Invalid action
                responseJson = gson.toJson(new ResponseMessage(
                        "error", "Invalid action", null, -1, 0, 0, 0, 0, null, 0L, 0L));
                break;
        }
        return responseJson;
    }
}
