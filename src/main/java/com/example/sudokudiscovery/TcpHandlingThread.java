package com.example.sudokudiscovery;

import lombok.extern.log4j.Log4j2;
import org.javatuples.Quartet;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

import static com.example.sudokudiscovery.holePuncher.clientmap;

@Log4j2
public class TcpHandlingThread implements Runnable{
    Socket client;
    BufferedReader in;
    BufferedOutputStream out;

    UUID uuid;
    String otherClientIP;
    String otherClientPort;
    TcpHandlingThread(Socket client, BufferedOutputStream out, BufferedReader in) {
        this.client = client;
        this.out = out;
        this.in = in;
    }

    @Override
    public void run() {
        uuid = UUID.randomUUID();
        try {
            out.write(uuid.toString().getBytes());
            out.write('\n');
            out.flush();
            log.info("wrote uuid to client");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while(!client.isClosed()) {
            if(clientmap.get(uuid) != null) {
                Quartet<String,UUID,String,String> identificationPacket = clientmap.get(uuid);
                for (Quartet<String,UUID,String,String> idPacket: clientmap.values()) {
                    if (idPacket.contains(identificationPacket.getValue0())) {
                        if(!idPacket.contains(uuid)) {
                            log.info("found counterpart udp message");
                            otherClientIP = idPacket.getValue2();
                            otherClientPort = idPacket.getValue3();
                            String otherClientInfo = otherClientIP + "&&" + otherClientPort + "&&" + idPacket.getValue1();
                            try {
                                out.write(otherClientInfo.getBytes());
                                out.write('\n');
                                out.flush();
                                log.info("sent counterpart info back");
                                client.close();
                            } catch (IOException e) {
                                throw new RuntimeException();
                            }
                        }
                    }
                }
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        clientmap.remove(uuid);
        log.info("removed from map");
        Thread.currentThread().interrupt();
    }
}
