package com.king.demo.N13_telnet.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TelnetClient {
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_HOST = "localhost";
    private static final boolean SSL = System.getProperty("ssl") != null;

    public static void main(String[] args) throws Exception {
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
        } else {
            sslCtx = null;
        }
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new TelnetClientInitializer(sslCtx));
            Channel ch = bootstrap.connect(SERVER_HOST, SERVER_PORT).sync().channel();

            ChannelFuture lastWriteFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (; ; ) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }

                lastWriteFuture = ch.writeAndFlush(line + "\r\n");

                if (line.toLowerCase().equals("exit")) {
                    ch.closeFuture().sync();
                    break;
                }
            }
            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
