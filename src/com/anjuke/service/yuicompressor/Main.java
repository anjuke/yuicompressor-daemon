package com.anjuke.service.yuicompressor;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length <= 0) {
            System.out.println("Usage: listen_port");
            return;
        }
        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket = serverSocket.accept();
            Thread thread = new Thread(new Compressor(socket));
            thread.start();
        }
    }
}
