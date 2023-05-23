package com.example.sudokudiscovery;

import lombok.extern.log4j.Log4j2;
import org.javatuples.Quartet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.example.sudokudiscovery.holePuncher.clientmap;

@Log4j2
public class UdpHandlingThread implements Runnable{

    DatagramSocket dgSocket;
    DatagramPacket dgPacket;

    String msg;
    Set<UUID> uuidSet;
    public UdpHandlingThread(DatagramPacket datagramPacket,DatagramSocket datagramSocket) {
        dgPacket = datagramPacket;

        dgSocket = datagramSocket;
    }

    @Override
    public void run() {
        uuidSet = new HashSet<>();
        while(true) {
            try {
                dgSocket.receive(dgPacket);
                msg = new String(dgPacket.getData());
                String[] splitMsg = msg.split("&&");
                UUID uuid = UUID.fromString(splitMsg[0]);
                String clientIP = dgPacket.getAddress().getHostAddress();
                String clientPort = String.valueOf(dgPacket.getPort());
                if(uuidSet.add(uuid)) {
                    clientmap.put(uuid,new Quartet<>(splitMsg[1],uuid,clientIP,clientPort));
                    log.info("put udp message into map");
                    log.info("written ip is: " + dgPacket.getAddress().getHostAddress());
                    log.info("Map looks like: " + clientmap);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
