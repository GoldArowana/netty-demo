package com.king.demo.N01_discard.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 与ChannelInboundHandlerAdapter的
 * 区别就是, SimpleChannelInboundHandler在接收到数据后
 * 会自动release掉数据占用的Bytebuffer资源
 * (自动调用Bytebuffer.release())。
 * 而为何服务器端不能用呢，
 * 因为我们想让服务器把客户端请求的数据发送回去，
 * 而服务器端有可能在channelRead方法返回前
 * 还没有写完数据，因此不能让它自动release。
 */
public class DiscardClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("I am discard client !", CharsetUtil.UTF_8));
        System.out.println("发送成功");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("this client can't support read...");
    }
}
