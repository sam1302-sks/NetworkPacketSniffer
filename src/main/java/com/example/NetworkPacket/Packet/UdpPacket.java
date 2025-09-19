package com.example.NetworkPacket.Packet;

public class UdpPacket extends Packet {
    private int srcPort;
    private int destPort;

    public UdpPacket(String srcIP, int srcPort, String destIP, int destPort, String data) {
        super(srcIP, destIP, data);
        this.srcPort = srcPort;
        this.destPort = destPort;
    }

    @Override
    public String getType() { return "UDP"; }

    public int getSrcPort() { return srcPort; }
    public int getDestPort() { return destPort; }

    @Override
    public String getSummary() {
        return String.format("[UDP] %s:%d -> %s:%d : %s", srcIP, srcPort, destIP, destPort, data);
    }
}
