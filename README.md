# 🌐 Network Packet Sniffer  

A **powerful and intuitive Network Packet Sniffer** built with **Java and Swing**.  
This application captures, analyzes, and displays **network traffic in real-time**.  
Developed as a comprehensive project for the **Object-Oriented Programming (OOP) course** at **Vishwakarma Institute of Technology, Pune**.  

---

## ✨ Key Features
- 🖥️ **Live Packet Capture** – Select a network interface and watch packets roll in live.  
- 📊 **Detailed Analysis** – View packet details including source/destination IP, protocol, and length.  
- 🖱️ **Interactive UI** – A user-friendly Swing GUI with a sortable table and a detailed packet view.  
- 💾 **Save & Export** – Save captured sessions to a `.csv` file for later analysis.  
- 🏃 **Multithreaded Performance** – Dedicated thread for packet sniffing ensures smooth and responsive UI.  

---

## 🎓 OOP Syllabus Mapping  

This project is meticulously designed to apply concepts from every **unit of the OOP in Java syllabus (A.Y. 2025-26).**

| Unit | Syllabus Topic | Project Implementation (Code File) |
|------|----------------|-------------------------------------|
| **1** | OOP & Java Basics | `Main.java` (main method), `PacketInfo.java` (Class, Objects, Encapsulation), `PacketParser.java` (Static Methods), Constructors used in all classes |
| **2** | Arrays, Strings, Methods | `PacketParser.java` (String manipulation for IPs), `SnifferEngine.java` (methods for capturing data), `SnifferUI.java` (Arrays for JComboBox) |
| **3** | Inheritance & Polymorphism | Polymorphism from **Pcap4j library** – handling `TcpPacket`, `UdpPacket` via generic `Packet` interface |
| **4** | Abstraction & Inner Types | `PacketListener` in `SnifferEngine.java` implemented as **lambda** (modern abstraction of anonymous inner class) |
| **5** | Exception Handling, Collections & Threads | `SnifferEngine.java` (implements Runnable, try-catch), `SnifferUI.java` (starts new Thread), `ArrayList` to store packets |
| **6** | File Handling, GUI & Java 8 Features | `SnifferUI.java` (Swing GUI, File I/O with `FileWriter`), Lambda for button listeners, **Stream API** for saving data |

---

## 🛠️ Technology Stack  

- **Core Language:** Java  
- **GUI Framework:** Java Swing  
- **Packet Capture Library:** [Pcap4j](https://www.pcap4j.org/)  
- **Build & Dependency Management:** Apache Maven  

---

## 🚀 Getting Started  

Follow these instructions to set up and run the project locally.  

### 🔧 Prerequisites  
- **Java Development Kit (JDK) 8 or higher**  
  - Ensure your `JAVA_HOME` environment variable is set.  
- **IntelliJ IDEA** (Community or Ultimate edition).  
- **Npcap (Windows users only)**  
  - [Download here](https://nmap.org/npcap/)  
  - ✅ Select **“Install Npcap in WinPcap API-compatible Mode”** during installation.  

### 📥 Installation & Setup  

```bash
# Clone the repository
git clone https://github.com/your-username/NetworkPacketSniffer.git
cd NetworkPacketSniffer
