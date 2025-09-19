package com.example.NetworkPacket.Filter;

import com.example.NetworkPacket.Packet.Packet;

public interface PacketFilter {
    boolean match(Packet p);
}
