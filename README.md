Distributed Blockchain Simulator
A lightweight, Java-based project simulating blockchain functionality in a distributed system. This project is designed to explore blockchain concepts, implement secure communication, and detect/repair tampered blockchain data.

Features
Blockchain Simulation: Add, verify, and repair blocks in a blockchain.
Distributed System: Implements client-server communication using TCP sockets.
Secure Communication: RSA-based signing and verification for secure transactions.
Tamper Detection: Automatically detect and repair tampered blockchain blocks.
Extensible Design: Modular code structure for adding new blockchain functionalities.
Installation
Prerequisites
Java JDK 8 or later
Maven (for dependencies)
Gson library (com.google.code.gson:gson:2.9.0)
Setup Steps
Clone the repository:
bash
Copy code
git clone https://github.com/your-username/distributed-blockchain-simulator.git
cd distributed-blockchain-simulator
Add Dependencies: Include Gson in your pom.xml:
xml
Copy code
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.9.0</version>
</dependency>
Build and Run:
For the standalone simulator, run Main.java.
For client-server mode, execute:
Server: ServerTCP.java
Client: ClientTCP.java
For RSA-secured communication, use:
Server: VerifyingServerTCP.java
Client: SigningClientTCP.java
Usage
Blockchain Simulator
Run Main.java to start the simulator.
Use the interactive menu to:
Add transactions to the blockchain.
Verify the integrity of the blockchain.
Repair tampered blocks.
Client-Server Interaction
Start the server by running ServerTCP.java.
Connect a client by executing ClientTCP.java.
Send JSON-based requests to add transactions or retrieve the blockchain status.
RSA-Secured Communication
Run VerifyingServerTCP.java (server).
Use SigningClientTCP.java (client) to:
Generate RSA keys.
Send digitally signed requests.
Validate responses from the server.
Example
Client Request
json
Copy code
{
  "action": "add_transaction",
  "data": "Sample Transaction Data"
}
Server Response
json
Copy code
{
  "status": "success",
  "message": "Transaction added successfully"
}
Project Structure
bash
Copy code
/src
  ├── Blockchain/          # Core blockchain implementation
  ├── Server/              # Server-side logic
  ├── Client/              # Client-side logic
  ├── Utils/               # Utilities (JSON handling, RSA, etc.)
  ├── Main.java            # Entry point for the standalone simulator
  ├── ServerTCP.java       # TCP server implementation
  ├── ClientTCP.java       # TCP client implementation
  ├── VerifyingServerTCP.java  # RSA-secured server
  ├── SigningClientTCP.java    # RSA-secured client
Contributing
We welcome contributions! Follow these steps to contribute:

Fork the repository.
Create a new branch:
bash
Copy code
git checkout -b feature/your-feature-name
Commit your changes:
bash
Copy code
git add .
git commit -m "Add your feature"
Push to your branch:
bash
Copy code
git push origin feature/your-feature-name
Open a Pull Request on the main repository.
