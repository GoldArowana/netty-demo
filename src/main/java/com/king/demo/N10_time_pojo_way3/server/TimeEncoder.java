package com.king.demo.N10_time_pojo_way3.server;


import com.king.demo.N10_time_pojo_way3.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class TimeEncoder extends MessageToByteEncoder<UnixTime> {
    @Override
    protected void encode(ChannelHandlerContext ctx, UnixTime msg, ByteBuf out) throws Exception {
        out.writeInt((int) msg.value());
        System.out.println("向客户端发送的时间: " + msg.toString());
    }
}
