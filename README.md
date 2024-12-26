# Distributed Blockchain Simulator

This repository contains Project 3 for the course 95-702 Distributed Systems for Information Systems Management at CMU Heinz College. The project focuses on building and extending blockchain functionalities with tamper-evident designs, distributed systems, and digital signatures.

---

## Project Description

The project is divided into three main tasks:

1. **Task 0: Standalone Blockchain Simulator**
   - Develop a standalone blockchain simulator.
   - Features include:
     - Viewing blockchain status.
     - Adding transactions to the blockchain.
     - Verifying the integrity of the blockchain.
     - Repairing corrupted blockchain data.
   - Implements a simple transaction mechanism using "DSCoin".

2. **Task 1: Distributed Blockchain with Client-Server Architecture**
   - Extends the blockchain simulator to a distributed system using TCP sockets and JSON messages.
   - Features include:
     - A client program to send requests.
     - A server that maintains the blockchain and processes client requests.
   - Encapsulates communication using JSON-based `RequestMessage` and `ResponseMessage` classes.

3. **Task 2: RSA Digital Signatures**
   - Introduces client authentication and message signing using RSA signatures.
   - Features include:
     - Clients generating RSA public/private keys for signing requests.
     - The server verifies requests using the client’s public key and signature.
     - Each client request is signed, ensuring authenticity and integrity.

---

## Prerequisites

- **Java Development Kit (JDK)**
- **IntelliJ IDEA** or another IDE with Maven support.
- **Gson Library** for JSON parsing.

---

## Setup and Execution

### Step 1: Install Dependencies
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/Distributed-Blockchain-Simulator.git
Import the project into your IDE.
Set up Maven dependencies for Gson:
Add the following dependency to pom.xml:
xml
Copy code
<dependencies>
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.9.0</version>
    </dependency>
</dependencies>

### Step 2: Task Execution
Task 0: Standalone Blockchain Simulator

Run Main.java to simulate the blockchain.
Follow the menu-driven interaction in the console.
Task 1: Client-Server Blockchain

Start the server using ServerTCP.java.
Run the client program ClientTCP.java to interact with the blockchain server.
Task 2: RSA Digital Signatures

Start the server using VerifyingServerTCP.java.
Run the client program SigningClientTCP.java to generate keys and sign requests.
