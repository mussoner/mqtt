package mqtt.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelStateEvent;
import io.netty.channel.MessageEvent;
import io.netty.channel.SimpleChannelHandler;
import io.netty.channel.group.ChannelGroup;
import mqtt.message.ConnAckMessage;
import mqtt.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttMessageHandler extends SimpleChannelHandler {

    private static final Logger log = LoggerFactory.getLogger(MqttMessageHandler.class);

    private ChannelGroup channelGroup;

    public MqttMessageHandler(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        this.channelGroup.add(e.getChannel());
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (e.getMessage() instanceof Message) {
            Message msg = (Message) e.getMessage();

            switch (msg.getType()) {
                case CONNACK:
                    log.debug("ConnAck Message..");
                    break;
                case CONNECT:
                    log.debug("Connect Message..");
                    e.getChannel().write(new ConnAckMessage(ConnAckMessage.ConnectionStatus.ACCEPTED));
                    break;
                case DISCONNECT:
                    log.debug("Disconnect Message..");
                    break;
                case PINGREQ:
                    log.debug("PingReq Message..");
                    break;
                case PINGRESP:
                    log.debug("PingResp Message..");
                    break;
                case PUBACK:
                    log.debug("PubAck Message..");
                    break;
                case PUBCOMP:
                    log.debug("PubComp Message..");
                    break;
                case PUBLISH:
                    log.debug("Publish Message..");
                    break;
                case PUBREC:
                    log.debug("PubRec Message..");
                    break;
                case PUBREL:
                    log.debug("PubRel Message..");
                    break;
                case SUBACK:
                    log.debug("SubAck Message..");
                    break;
                case SUBSCRIBE:
                    log.debug("Subscribe Message..");
                    break;
                case UNSUBACK:
                    log.debug("UnSubAck Message..");
                    break;
                case UNSUBSCRIBE:
                    log.debug("Unsubscribe Message..");
                    break;
                default:
                    log.error("Unkwnown message received : " + msg.getType());
            }
        } else {
            super.messageReceived(ctx, e);
        }
    }
}
