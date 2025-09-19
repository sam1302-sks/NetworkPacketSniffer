package com.example.NetworkPacket.Packet;

import java.util.Random;

public class PacketGenerator implements Runnable {
    private final Sniffer sniffer;
    private final Random rnd = new Random();
    private volatile boolean running = true;
    private final int delayMs;

    public PacketGenerator(Sniffer sniffer, int delayMs) {
        this.sniffer = sniffer;
        this.delayMs = delayMs;
    }

    public void stop() { running = false; }

    private String randomIp() {
        return String.format("%d.%d.%d.%d", rnd.nextInt(223) + 1, rnd.nextInt(254) + 1, rnd.nextInt(254) + 1, rnd.nextInt(254) + 1);
    }

    private String randomData() {
        int len = rnd.nextInt(10) + 3;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) sb.append((char)('a' + rnd.nextInt(26)));
        return sb.toString();
    }

    @Override
    public void run() {
        while (running) {
            try {
                int typ = rnd.nextInt(3);
                Packet p;
                switch (typ) {
                    case 0:
                        p = new TcpPacket(randomIp(), rnd.nextInt(65535-1024)+1024, randomIp(), rnd.nextInt(65535-1024)+1024, randomData());
                        break;
                    case 1:
                        p = new UdpPacket(randomIp(), rnd.nextInt(65535-1024)+1024, randomIp(), rnd.nextInt(65535-1024)+1024, randomData());
                        break;
                    default:
                        p = new IcmpPacket(randomIp(), randomIp(), rnd.nextInt(16), randomData());
                        break;
                }
                sniffer.capture(p);
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (InvalidPacketException ex) {
                System.err.println("Invalid packet generated: " + ex.getMessage());
            }
        }
    }
}
