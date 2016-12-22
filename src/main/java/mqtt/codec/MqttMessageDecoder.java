package mqtt.codec;

import io.netty.buffer.ChannelBuffer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.frame.FrameDecoder;
import java.io.ByteArrayInputStream;
import mqtt.message.MessageInputStream;

public class MqttMessageDecoder extends FrameDecoder {

    @Override
    protected Object decode(ChannelHandlerContext chc, Channel chnl, ChannelBuffer cb) throws Exception {
        if (cb.readableBytes() < 2) {
            return null;
        }

        cb.markReaderIndex();
        cb.readByte();//read away header
        int msgLength = 0;
        int multiplier = 1;
        int digit;
        int lengthSize = 0;

        do {
            lengthSize++;
            digit = cb.readByte();
            msgLength += (digit & 0x7F) * multiplier;
            multiplier *= 128;
            if ((digit & 0x80) > 0 && !cb.readable()) {
                cb.resetReaderIndex();
                return null;
            }
        } while ((digit & 0x80) > 0);

        if (cb.readableBytes() < msgLength) {
            cb.resetReaderIndex();
            return null;
        }

        byte[] data = new byte[1 + lengthSize + msgLength];
        cb.resetReaderIndex();
        cb.readBytes(data);

        MessageInputStream mis = new MessageInputStream(new ByteArrayInputStream(data));
        return mis.readMessage();
    }

}
