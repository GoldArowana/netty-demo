package com.king.demo.N14_simple_chat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SimpleChatClient {
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_HOST = "localhost";

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new SimpleChatClientInitializer());
            Channel ch = bootstrap.connect(SERVER_HOST, SERVER_PORT).sync().channel();
            System.out.println("连接成功");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                ch.writeAndFlush(in.readLine() + "\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
