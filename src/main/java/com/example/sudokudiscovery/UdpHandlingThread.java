package com.example.sudokudiscovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Map;

public class UdpHandlingThread implements Runnable{

    DatagramSocket dgSocket;
    DatagramPacket dgPacket;
    Map<String,String> cMap;
    public UdpHandlingThread(DatagramPacket datagramPacket, Map<String, String> clientmap, DatagramSocket datagramSocket) {
        dgPacket = datagramPacket;
        cMap = clientmap;
        dgSocket = datagramSocket;
    }

    @Override
    public void run() {
        while(true) {
            try {
                dgSocket.receive(dgPacket);
                String Msg = new String(dgPacket.getData());
                cMap.put(dgPacket.getAddress().getHostAddress(),Msg.trim());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
