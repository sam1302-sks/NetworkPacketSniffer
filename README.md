# 🌐 Network Packet Sniffer

![WhatsApp Image 2025-10-27 at 10 45 54_2151b60b](https://github.com/user-attachments/assets/eaa7fdd7-4990-4664-893f-27f2971aeac5)

---

## 📘 Overview

The **Network Packet Sniffer** is a Java-based desktop application designed to **capture, analyze, and display live network traffic** in real time.  
It provides insights into various protocols such as **TCP**, **UDP**, and **ICMP**, helping users understand how data flows across their system.

This project demonstrates the implementation of **Java networking, multithreading, and GUI concepts** — making it both educational and practical.

---

## 🎯 Objectives

- Capture real-time network packets.
- Analyze and categorize packets by protocol.
- Display detailed packet information through a graphical interface.
- Provide filtering options for specific protocols.
- Help students understand how low-level network communication works.

---

## ⚙️ Features

✅ Live packet capturing using **Pcap4J Library**  
✅ Packet categorization into **TCP**, **UDP**, and **ICMP**  
✅ Real-time packet count display  
✅ Multi-threaded background capturing for smooth UI performance  
✅ Search and filter packets dynamically  
✅ Simple, clean **Swing-based GUI**  
✅ Option to **export captured data**

---

## 🧠 Concept Mapping (Java Concepts Used)

| Syllabus Topic | Project Implementation |
|----------------|------------------------|
| **OOP & Java Basics** | `Main.java`, `PacketInfo.java` (Classes, Objects, Encapsulation) |
| **Arrays, Strings, Methods** | `PacketParser.java`, `SnifferEngine.java`, `SnifferUI.java` |
| **Inheritance & Polymorphism** | `TcpPacket`, `UdpPacket` classes (via Pcap4J generic interface) |
| **Abstraction & Inner Types** | Lambda expressions and anonymous inner classes in listeners |
| **Exception Handling, Collections & Threads** | `SnifferEngine.java` (Runnable, try-catch, ArrayList for packets) |
| **File Handling, GUI & Java 8 Features** | Swing GUI, FileWriter for saving, Stream API for processing |

---

## 🧩 Technologies Used

| Category | Technology |
|-----------|-------------|
| Language | **Java (JDK 17+)** |
| GUI | **Java Swing** |
| Library | **Pcap4J** |
| Threading | **Java Threads (Runnable Interface)** |
| IDE | IntelliJ IDEA / Eclipse / VS Code |
| Platform | Windows / Linux |

---

## 🚀 How It Works

1. The application initializes a **network interface** using Pcap4J.
2. It starts a **background thread** to continuously capture live packets.
3. Each packet is **parsed and classified** (TCP / UDP / ICMP).
4. The parsed details are **displayed in the GUI table** in real-time.
5. The user can **filter, pause, or export** the captured data.

---

## 🖥️ User Interface Preview

| Interface | Screenshot |
|------------|-------------|
| **Home / Capture Screen** | ![Capture Screen](#) |
| **Packet Details View** | ![Packet Details](#) |
| **Filtered Results / Logs** | ![Filtered Results](#) |

---

## 🧵 Background Thread Explanation

The packet capture process runs on a **background thread** using the `Runnable` interface.  
This ensures the GUI remains **responsive** while packets are being captured.  
The thread continuously listens for incoming packets and updates the GUI in real time.

---

## 🌐 Protocols Handled

| Protocol | Description |
|-----------|-------------|
| **TCP (Transmission Control Protocol)** | Reliable, connection-oriented (e.g., websites, emails). |
| **UDP (User Datagram Protocol)** | Fast, connectionless (e.g., streaming, games). |
| **ICMP (Internet Control Message Protocol)** | Used for diagnostics (e.g., ping commands). |

---

## 🏗️ Project Structure

📂 NetworkPacketSniffer
┣ 📜 Main.java
┣ 📜 SnifferUI.java
┣ 📜 SnifferEngine.java
┣ 📜 PacketParser.java
┣ 📜 PacketInfo.java
┣ 📜 resources/
┗ 📜 README.md
---

## ⚡ Installation & Setup

1. Clone this repository  
   ```bash
   git clone https://github.com/your-username/Network-Packet-Sniffer.git
Install Java (JDK 17 or above)

Download and add Pcap4J library to your project dependencies

Run Main.java

Start capturing packets 🎯

📈 Future Enhancements
🔍 Add protocol-specific filtering (e.g., HTTP, DNS)

💾 Save session data in .csv format

🌐 Develop web-based dashboard for packet visualization

🧠 Integrate AI for anomaly detection
