package mqtt.codec;

import io.netty.buffer.ChannelBuffer;
import io.netty.buffer.ChannelBuffers;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.oneone.OneToOneEncoder;
import mqtt.message.Message;

public class MqttMessageEncoder extends OneToOneEncoder {

    @Override
    protected Object encode(ChannelHandlerContext chc, Channel chnl, Object o) throws Exception {
        if (!(o instanceof Message)) {
            return null;
        }

        ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
        buf.writeBytes(((Message) o).toBytes());
        return buf;
    }
}
