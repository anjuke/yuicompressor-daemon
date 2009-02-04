package com.anjuke.util.yuicompressor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

public class Compressor {
    public void setInputStream(InputStream value) {
        _in = value;
    }

    public void setOutputStream(OutputStream value) {
        _out = value;
    }

    public void compress() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(_in));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(_out));

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

            if ("js".equalsIgnoreCase(type)) {
                processJavascript(reader, writer);
            } else if ("css".equalsIgnoreCase(type)) {
                processStyle(reader, writer);
            }

            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void processJavascript(Reader reader, Writer writer) throws IOException {
        JavaScriptCompressor compressor = new JavaScriptCompressor(reader, new ErrorReporter() {
            public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
                System.out.println(String.format("Warning: %s, %s, %d, %s, %d", message, sourceName, line, lineSource,
                        lineOffset));
            }

            public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
                System.out.println(String.format("Error: %s, %s, %d, %s, %d", message, sourceName, line, lineSource,
                        lineOffset));
            }

            public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource,
                    int lineOffset) {
                System.out.println(String.format("Exception: %s, %s, %d, %s, %d", message, sourceName, line,
                        lineSource, lineOffset));
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

    private InputStream _in;
    private OutputStream _out;
}
