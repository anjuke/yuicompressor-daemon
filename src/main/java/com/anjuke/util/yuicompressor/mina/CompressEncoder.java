package com.anjuke.util.yuicompressor.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.anjuke.util.yuicompressor.Compressor;

public class CompressEncoder extends ProtocolEncoderAdapter {
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        assert (message instanceof Compressor);
        Compressor compressor = (Compressor) message;
        IoBuffer buffer = IoBuffer.allocate(1024);
        buffer.setAutoExpand(true);

        compressor.setOutputStream(buffer.asOutputStream());
        compressor.compress();

        buffer.flip();
        out.write(buffer);
    }

}
