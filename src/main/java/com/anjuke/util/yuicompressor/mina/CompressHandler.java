package com.anjuke.util.yuicompressor.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class CompressHandler extends IoHandlerAdapter {
    public void messageReceived(IoSession session, Object message) throws Exception {
        session.write(message);
        session.close(false);
    }
}
