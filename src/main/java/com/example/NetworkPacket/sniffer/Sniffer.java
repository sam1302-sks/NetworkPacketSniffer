package com.example.NetworkPacket.sniffer;

import com.example.NetworkPacket.Filter.PacketFilter;

import java.util.*;
import java.util.stream.Collectors;

public class Sniffer {
    private final List<Packet> packets = Collections.synchronizedList(new ArrayList<>());

    public void capture(Packet p) {
        packets.add(p);
        System.out.println("Captured: " + p.getSummary());
    }

    public List<Packet> getAll() {
        synchronized (packets) {
            return new ArrayList<>(packets);
        }
    }

    public List<Packet> filter(PacketFilter filter) {
        synchronized (packets) {
            return packets.stream().filter(filter::match).collect(Collectors.toList());
        }
    }

    public void clear() {
        synchronized (packets) {
            packets.clear();
        }
    }
}
