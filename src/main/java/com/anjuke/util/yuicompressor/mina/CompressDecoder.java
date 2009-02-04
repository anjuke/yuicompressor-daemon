package com.anjuke.util.yuicompressor.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.anjuke.util.yuicompressor.Compressor;

public class CompressDecoder extends CumulativeProtocolDecoder {
    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        int len = in.remaining();
        if (len >= 3 && '\n' == in.get(len - 3) && '\0' == in.get(len - 2) && '\n' == in.get(len - 1)) {
            Compressor compressor = new Compressor();
            compressor.setInputStream(in.asInputStream());
            out.write(compressor);
            return true;
        }
        return false;
    }
}
