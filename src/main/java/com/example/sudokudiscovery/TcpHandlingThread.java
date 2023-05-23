package com.example.sudokudiscovery;

import org.javatuples.Triplet;

import java.io.*;
import java.net.DatagramPacket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;

public class TcpHandlingThread implements Runnable{
    Socket client;
    BufferedReader in;
    BufferedOutputStream out;
    Map<UUID, Triplet<String,UUID,DatagramPacket>> clientmap;
    UUID uuid;
    String otherClientIP;
    String otherClientPort;
    TcpHandlingThread(Socket client, BufferedOutputStream out, BufferedReader in,Map<UUID,Triplet<String,UUID,DatagramPacket>> clientmap) {
        this.client = client;
        this.out = out;
        this.in = in;
        this.clientmap = clientmap;
    }
    private byte[] uuidToBytes(UUID uuid) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        return byteBuffer.array();
    }

    @Override
    public void run() {
        uuid = UUID.randomUUID();
        try {
            out.write(uuid.toString().getBytes());
            out.write('\n');
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while(client.isConnected()) {
            if(clientmap.get(uuid) != null) {
                Triplet<String,UUID,DatagramPacket> identificationPacket = clientmap.get(uuid);
                for (Triplet<String,UUID,DatagramPacket> idpacket: clientmap.values()) {
                    if (idpacket.contains(identificationPacket.getValue0())) {
                        if(!idpacket.contains(uuid)) {
                            otherClientIP = idpacket.getValue2().getAddress().getHostAddress();
                            otherClientPort = String.valueOf(idpacket.getValue2().getPort());
                            String otherClientInfo = otherClientIP + "&&" + otherClientPort;
                            try {
                                out.write(otherClientInfo.getBytes());
                                out.write('\n');
                                out.flush();
                            } catch (IOException e) {
                                throw new RuntimeException();
                            }
                        }
                    }
                }
            }
        }
        clientmap.remove(uuid);
    }
}
