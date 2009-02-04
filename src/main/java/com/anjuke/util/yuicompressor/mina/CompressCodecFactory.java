package com.anjuke.util.yuicompressor.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class CompressCodecFactory implements ProtocolCodecFactory {

    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return new CompressDecoder();
    }

    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return new CompressEncoder();
    }

}
