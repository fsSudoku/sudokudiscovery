package com.example.sudokudiscovery;

import java.io.*;
import java.net.DatagramPacket;
import java.net.Socket;
import java.util.Map;

public class TcpHandlingThread implements Runnable{
    Socket client;
    BufferedReader in;
    BufferedOutputStream out;
    Map<String,String> clientmap;
    TcpHandlingThread(Socket client, BufferedOutputStream out, BufferedReader in,Map<String,String> clientmap) {
        this.client = client;
        this.out = out;
        this.in = in;
        this.clientmap = clientmap;
    }


    @Override
    public void run() {
        String tcpIp = client.getInetAddress().getHostAddress();
        while(client.isConnected()) {
            if(clientmap.get(tcpIp) != null) {

            }
        }
    }
}
