package com.mqtt.mqtt;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPipelineFactory;
import io.netty.channel.Channels;
import io.netty.channel.ServerChannelFactory;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.nio.NioServerSocketChannelFactory;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import mqtt.codec.MqttMessageDecoder;
import mqtt.codec.MqttMessageEncoder;
import mqtt.codec.MqttMessageHandler;

public class MqttBroker {

    private ServerChannelFactory server;
    private ChannelGroup channelGroup;

    public MqttBroker() {
        this.server = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        this.channelGroup = new DefaultChannelGroup(this + "-channelGroup");

        ChannelPipelineFactory pipelineFactory = new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("encoder", new MqttMessageEncoder());
                pipeline.addLast("decoder", new MqttMessageDecoder());
                pipeline.addLast("handler", new MqttMessageHandler(channelGroup));
                return pipeline;
            }
        };

        ServerBootstrap bootstrap = new ServerBootstrap(this.server);
        bootstrap.setOption("reuseAddress", true);
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.setPipelineFactory(pipelineFactory);

        Channel channel = bootstrap.bind(new InetSocketAddress(1883));
        if (!channel.isBound()) {
            this.stop();
        }

        this.channelGroup.add(channel);
    }

    private void stop() {
        if (this.channelGroup != null) {
            this.channelGroup.close();
        }
        if (this.server != null) {
            this.server.releaseExternalResources();
        }
    }
}
