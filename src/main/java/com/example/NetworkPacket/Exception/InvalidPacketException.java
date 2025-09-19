package com.example.NetworkPacket.Exception;

public class InvalidPacketException extends RuntimeException {
    public InvalidPacketException(String message) {
        super(message);
    }
}
