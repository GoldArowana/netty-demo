package com.king.demo.N13_telnet.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;

public class TelnetServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.write("欢迎来到: " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
        String response;
        boolean close = false;
        if (request.isEmpty()) {
            response = "您输入的是`空`\r\n";
        } else if (request.toLowerCase().equals("exit")) {
            response = "bye\r\n";
            close = true;
        } else {
            response = "您输入的是: " + request + "\r\n";
        }

        ChannelFuture future = ctx.write(response);
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
