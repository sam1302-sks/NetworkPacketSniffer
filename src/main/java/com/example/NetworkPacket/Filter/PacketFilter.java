package com.example.NetworkPacket.Filter;

import com.example.NetworkPacket.sniffer.Packet;

public interface PacketFilter {
    boolean match(Packet p);
}
