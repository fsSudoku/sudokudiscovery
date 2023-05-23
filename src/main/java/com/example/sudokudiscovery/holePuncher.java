package com.example.sudokudiscovery;


import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class holePuncher implements CommandLineRunner {
    ServerSocket serverSocket;
    Socket client;
    BufferedReader in;
    BufferedOutputStream out;

    Map<UUID,Triplet<String,UUID,DatagramPacket>> clientmap;


    @Override
    public void run(String... args) throws Exception {
        clientmap = new HashMap<>();
        serverSocket = new ServerSocket(5000);
        System.out.println("ServerSocket started on Port 5000");

        DatagramSocket datagramSocket = new DatagramSocket(5001);
        System.out.println("DatagramSocket started on Port 5001");
        DatagramPacket datagramPacket = new DatagramPacket(new byte[1024],1024);
        UdpHandlingThread u = new UdpHandlingThread(datagramPacket,clientmap,datagramSocket);
        new Thread(u).start();
        while(true) {
            client = null;
            try {
                client = serverSocket.accept();
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new BufferedOutputStream(client.getOutputStream());
                TcpHandlingThread t = new TcpHandlingThread(client,out,in,clientmap);
                new Thread(t).start();
            } catch (Exception e){
                client.close();
                e.printStackTrace();
            }
        }
    }
}
