package com.anjuke.service.yuicompressor;

import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import com.yahoo.platform.yui.compressor.CssCompressor;

import java.io.Reader;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

public class Compressor implements Runnable {
    public Compressor(Socket socket) {
        _socket = socket;
    }

    public void run() {
        try {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));

                String filename = reader.readLine();
                System.out.println(String.format("Thread:%d, %s", Thread.currentThread().getId(), filename));

                String type;
                if (filename.endsWith(".js")) {
                    type = "js";
                } else if (filename.endsWith(".css")) {
                    type = "css";
                } else {
                    return;
                }

                StringBuilder sb = new StringBuilder();
                while (true) {
                    String line = reader.readLine();
                    if (line == null || "\0".equals(line)) {
                        break;
                    }
                    sb.append(line).append("\n");
                }
                StringReader reader2 = new StringReader(sb.toString());

                if ("js".equalsIgnoreCase(type)) {
                    processJavascript(reader2, writer);
                } else if ("css".equalsIgnoreCase(type)) {
                    processStyle(reader2, writer);
                }

                writer.close();
                reader.close();
            } finally {
                _socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void processJavascript(Reader reader, Writer writer) throws IOException {
        JavaScriptCompressor compressor = new JavaScriptCompressor(reader, new ErrorReporter() {
            public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
                System.out.println(String.format("Warning: %s, %s, %d, %s, %d", message, sourceName, line, lineSource, lineOffset));
            }
            public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
                System.out.println(String.format("Error: %s, %s, %d, %s, %d", message, sourceName, line, lineSource, lineOffset));
            }
            public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
                System.out.println(String.format("Exception: %s, %s, %d, %s, %d", message, sourceName, line, lineSource, lineOffset));
                return new EvaluatorException(message);
            }
        });

        boolean verbose = false;
        int linebreakpos = -1;
        boolean munge = true;
        boolean preserveAllSemiColons = false;
        boolean disableOptimizations = false;
        compressor.compress(writer, linebreakpos, munge, verbose, preserveAllSemiColons, disableOptimizations);
    }

    protected void processStyle(Reader reader, Writer writer) throws IOException {
        CssCompressor compressor = new CssCompressor(reader);

        int linebreakpos = -1;
        compressor.compress(writer, linebreakpos);
    }

    private Socket _socket;
}
