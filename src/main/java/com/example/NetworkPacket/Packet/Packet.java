package com.example.NetworkPacket.Packet;

import com.example.NetworkPacket.Exception.InvalidPacketException;

public abstract class Packet {
    protected String srcIP;
    protected String destIP;
    protected String data;
    protected long timestamp;

    public Packet(String srcIP, String destIP, String data) {
        if (srcIP == null || destIP == null || data == null) {
            throw new InvalidPacketException("Null field in packet");
        }
        this.srcIP = srcIP;
        this.destIP = destIP;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public abstract String getType();

    public String getSrcIP() { return srcIP; }
    public String getDestIP() { return destIP; }
    public String getData() { return data; }
    public long getTimestamp() { return timestamp; }

    public String getSummary() {
        return String.format("[%s] %s -> %s : %s", getType(), srcIP, destIP, data);
    }

    public void display() {
        System.out.println(getSummary());
    }
}
