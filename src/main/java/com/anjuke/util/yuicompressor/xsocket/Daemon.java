package com.anjuke.util.yuicompressor.xsocket;

import org.xsocket.connection.IServer;
import org.xsocket.connection.Server;

public class Daemon {
    public static void main(String[] args) throws Exception {

        if (args.length <= 0) {
            System.out.println("Usage: listen_port");
            return;
        }
        int port = Integer.parseInt(args[0]);
        IServer server = new Server(port, new CompressHandler());
        server.start();
    }
}
