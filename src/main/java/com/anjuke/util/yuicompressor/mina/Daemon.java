package com.anjuke.util.yuicompressor.mina;

import java.net.InetSocketAddress;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class Daemon {
    public static void main(String[] args) throws Exception {

        if (args.length <= 0) {
            System.out.println("Usage: listen_port");
            return;
        }
        int port = Integer.parseInt(args[0]);
        NioSocketAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("protocal", new ProtocolCodecFilter(new CompressCodecFactory()));
        acceptor.setHandler(new CompressHandler());
        acceptor.setDefaultLocalAddress(new InetSocketAddress(port));
        acceptor.bind();
    }
}
