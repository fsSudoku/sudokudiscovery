package com.example.sudokudiscovery;


import lombok.extern.log4j.Log4j2;
import org.javatuples.Quartet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log4j2
@Service
public class holePuncher implements CommandLineRunner {
    ServerSocket serverSocket;
    Socket client;
    BufferedReader in;
    BufferedOutputStream out;

    static Map<UUID, Quartet<String,UUID,String,String>> clientmap;


    @Override
    public void run(String... args) throws Exception {
        clientmap = new HashMap<>();
        serverSocket = new ServerSocket(5000);
        log.info("ServerSocket started on Port 5000");

        DatagramSocket datagramSocket = new DatagramSocket(5001);
        log.info("DatagramSocket started on Port 5001");
        DatagramPacket datagramPacket = new DatagramPacket(new byte[1024],1024);
        UdpHandlingThread u = new UdpHandlingThread(datagramPacket,datagramSocket);
        log.info("entering udp Thread");
        new Thread(u).start();
        while(true) {
            client = null;
            try {
                client = serverSocket.accept();
                log.info("got client");
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new BufferedOutputStream(client.getOutputStream());
                log.info("entering tcp thread");
                TcpHandlingThread t = new TcpHandlingThread(client,out,in);
                new Thread(t).start();
            } catch (Exception e){
                client.close();
                e.printStackTrace();
            }
        }
    }
}
