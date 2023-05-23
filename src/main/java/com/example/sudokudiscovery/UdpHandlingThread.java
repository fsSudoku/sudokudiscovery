package com.example.sudokudiscovery;

import org.javatuples.Triplet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Map;
import java.util.UUID;

public class UdpHandlingThread implements Runnable{

    DatagramSocket dgSocket;
    DatagramPacket dgPacket;
    Map<UUID, Triplet<String,UUID,DatagramPacket>> cMap;
    String Msg;
    public UdpHandlingThread(DatagramPacket datagramPacket, Map<UUID,Triplet<String,UUID,DatagramPacket>> clientmap, DatagramSocket datagramSocket) {
        dgPacket = datagramPacket;
        cMap = clientmap;
        dgSocket = datagramSocket;
    }

    @Override
    public void run() {
        while(true) {
            try {
                dgSocket.receive(dgPacket);
                Msg = new String(dgPacket.getData());
                String[] splitMsg = Msg.split("&&");
                UUID uuid = UUID.fromString(splitMsg[0]);
                cMap.put(uuid,new Triplet<>(splitMsg[1],uuid,dgPacket));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
