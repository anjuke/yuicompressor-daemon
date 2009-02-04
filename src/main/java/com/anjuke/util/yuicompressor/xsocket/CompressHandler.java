package com.anjuke.util.yuicompressor.xsocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import org.xsocket.Execution;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.INonBlockingConnection;

import com.anjuke.util.yuicompressor.Compressor;

public class CompressHandler implements IDataHandler {
    private static final int MAX_LENGTH = 1024 * 1024 * 2;

    @Execution(Execution.NONTHREADED)
    public boolean onData(INonBlockingConnection connection) throws IOException {
        ByteBuffer[] buffers = connection.readByteBufferByDelimiter("\n\0\n", MAX_LENGTH);

        ByteBufferArrayInputStream in = new ByteBufferArrayInputStream(buffers);
        ByteBufferArrayOutputStream out = new ByteBufferArrayOutputStream();

        Compressor compressor = new Compressor();
        compressor.setInputStream(in);
        compressor.setOutputStream(out);
        compressor.compress();

        connection.write(out.getByteBufferArray());

        connection.close();
        return true;
    }

    private static class ByteBufferArrayInputStream extends InputStream {
        public ByteBufferArrayInputStream(ByteBuffer[] buffers) {
            _buffers = buffers;
            _index = 0;
        }

        private final ByteBuffer[] _buffers;
        private int _index;

        @Override
        public int read() throws IOException {
            while (_index < _buffers.length) {
                ByteBuffer buffer = _buffers[_index];
                if (buffer.remaining() > 0) {
                    return buffer.get();
                }
                _index++;
            }
            return -1;
        }
    }

    private static class ByteBufferArrayOutputStream extends OutputStream {
        public ByteBufferArrayOutputStream() {
            _buffers = new LinkedList <ByteBuffer>();
            newBuffer();
        }

        private final List<ByteBuffer> _buffers;
        private ByteBuffer _buffer;

        @Override
        public void write(int b) throws IOException {
            if (_buffer.position() >= _buffer.capacity()) {
                newBuffer();
            }
            _buffer.put((byte) b);
        }

        private void newBuffer() {
            _buffer = ByteBuffer.allocate(8192);
            _buffers.add(_buffer);
        }

        public ByteBuffer[] getByteBufferArray() {
            ByteBuffer[] array = new ByteBuffer[_buffers.size()];
            int i = 0;
            for (ByteBuffer buffer : _buffers) {
                array[i] = (ByteBuffer) buffer.asReadOnlyBuffer().flip();
                i++;
            }
            return array;
        }
    }
}
