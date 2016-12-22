package mqtt.codec;

import mqtt.message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelStateEvent;
import io.netty.channel.MessageEvent;
import io.netty.channel.SimpleChannelHandler;
import io.netty.channel.group.ChannelGroup;
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
                    ConnAckMessage connAckMessage = (ConnAckMessage) msg;
                    log.debug("ConnAckMessage:[{}] {}", e.getChannel().getRemoteAddress(), connAckMessage.toString());
                    break;
                case CONNECT:
                    ConnectMessage connectMessage = (ConnectMessage) e.getMessage();
                    log.debug("ConnectMessage:[{}] {} ", e.getChannel().getRemoteAddress(), connectMessage.toString());
                    e.getChannel().write(new ConnAckMessage(ConnAckMessage.ConnectionStatus.ACCEPTED));
                    break;
                case DISCONNECT:
                    DisconnectMessage disconnectMessage = (DisconnectMessage) msg;
                    log.debug("DisconnectMessage:[{}] {}", e.getChannel().getRemoteAddress(), disconnectMessage.toString());
                    break;
                case PINGREQ:
                    PingReqMessage pingReqMessage = (PingReqMessage) msg;
                    log.debug("PingReqMessage:[{}] {}", e.getChannel().getRemoteAddress(), pingReqMessage.toString());
                    e.getChannel().write(new PingRespMessage());
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
                    PublishMessage publishMessage = (PublishMessage) msg;
                    log.debug("PublishMessage:[{}] {}", e.getChannel().getRemoteAddress(), publishMessage.toString());
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
                    SubscribeMessage subscribeMessage = (SubscribeMessage) msg;
                    log.debug("SubscribeMessage:[{}] {}", e.getChannel().getRemoteAddress(), subscribeMessage.toString());
                    e.getChannel().write(new SubAckMessage());
                    break;
                case UNSUBACK:
                    log.debug("UnSubAck Message..");
                    break;
                case UNSUBSCRIBE:
                    UnsubscribeMessage unsubscribeMessage = (UnsubscribeMessage) msg;
                    log.debug("UnsubscribeMessage:[{}] {}", e.getChannel().getRemoteAddress(), unsubscribeMessage.toString());
                    e.getChannel().write(new UnsubAckMessage());
                    break;
                default:
                    log.error("Unkwnown message received : " + msg.getType());
            }
        } else {
            super.messageReceived(ctx, e);
        }
    }
}
