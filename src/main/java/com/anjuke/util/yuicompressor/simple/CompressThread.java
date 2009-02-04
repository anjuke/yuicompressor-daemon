package com.anjuke.util.yuicompressor.simple;

import java.net.Socket;

import com.anjuke.util.yuicompressor.Compressor;

public class CompressThread implements Runnable {
    public CompressThread(Socket socket) {
        _socket = socket;
    }

    public void run() {
        Compressor compressor = new Compressor();
        try {
            try {
                compressor.setInputStream(_socket.getInputStream());
                compressor.setOutputStream(_socket.getOutputStream());
            } finally {
                _socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Socket _socket;
}
