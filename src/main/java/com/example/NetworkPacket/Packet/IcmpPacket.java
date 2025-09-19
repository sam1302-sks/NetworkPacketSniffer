package com.example.NetworkPacket.Packet;

import com.example.NetworkPacket.Packet.Packet;

public class IcmpPacket extends Packet {
    private int typeCode; // simple numeric code

    public IcmpPacket(String srcIP, String destIP, int typeCode, String data) {
        super(srcIP, destIP, data);
        this.typeCode = typeCode;
    }

    @Override
    public String getType() { return "ICMP"; }

    public int getTypeCode() { return typeCode; }

    @Override
    public String getSummary() {
        return String.format("[ICMP] %s -> %s (type=%d) : %s", srcIP, destIP, typeCode, data);
    }
}
