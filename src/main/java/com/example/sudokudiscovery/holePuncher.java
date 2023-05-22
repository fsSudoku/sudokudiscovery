package com.example.sudokudiscovery;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Service
public class holePuncher implements CommandLineRunner {
    ServerSocket serverSocket;
    Socket client;
    BufferedReader in;
    BufferedOutputStream out;

    Map<String,String> clientmap;

    @Override
    public void run(String... args) throws Exception {
        clientmap = new HashMap<>();
        serverSocket = new ServerSocket(5000);
        System.out.println("ServerSocket started on Port 5000");
        client = serverSocket.accept();
        System.out.println("RemoteSocketAddress: " + client.getRemoteSocketAddress());
        client.close();
//        DatagramSocket datagramSocket = new DatagramSocket(5001);
//        System.out.println("DatagramSocket started on Port 5001");
//        DatagramPacket datagramPacket = new DatagramPacket(new byte[1024],1024);
//        UdpHandlingThread u = new UdpHandlingThread(datagramPacket,clientmap,datagramSocket);
//        new Thread(u).start();
//        while(true) {
//            client = null;
//            try {
//                client = serverSocket.accept();
//                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
//                out = new BufferedOutputStream(client.getOutputStream());
//                TcpHandlingThread t = new TcpHandlingThread(client,out,in,clientmap);
//                new Thread(t).start();
//            } catch (Exception e){
//                client.close();
//                e.printStackTrace();
//            }
//        }
    }
}
